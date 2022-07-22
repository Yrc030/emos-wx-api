package com.yrc.emos.wx.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrc.emos.wx.constants.UserStatusEnum;
import com.yrc.emos.wx.exception.EmosException;
import com.yrc.emos.wx.mapper.*;
import com.yrc.emos.wx.pojo.entity.MessageEntity;
import com.yrc.emos.wx.pojo.entity.UserEntity;
import com.yrc.emos.wx.pojo.form.RegisterForm;
import com.yrc.emos.wx.pojo.form.UserSummaryForm;
import com.yrc.emos.wx.pojo.result.DeptWithMembersResult;
import com.yrc.emos.wx.pojo.result.GetContactsListResult;
import com.yrc.emos.wx.pojo.result.MemberResult;
import com.yrc.emos.wx.pojo.result.UsernameAndPhotoResult;
import com.yrc.emos.wx.service.UserService;
import com.yrc.emos.wx.task.ActiveCodeTask;
import com.yrc.emos.wx.task.MessageTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.yrc.emos.wx.constants.EmosConstants.ACTIVE_CODE_PREFIX;
import static com.yrc.emos.wx.constants.EmosConstants.Root;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author joker
 * @since 2022-05-12
 */
@Service
@Slf4j
@Scope("prototype")
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Value("${wx.app-id}")
    private String appId;

    @Value("${wx.app-secret}")
    private String appSecret;

    @Autowired
    private UserMapper userDao;

    @Autowired
    private DeptMapper deptDao;

    @Autowired
    private CheckinMapper checkinDao;

    @Autowired
    private MessageRefDao messageRefDao;

    @Autowired
    private FaceModelMapper faceModelDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MessageTask messageTask;


    @Autowired
    private ActiveCodeTask activeCodeTask;

    /**
     * 注册业务
     * 1. 注册用户
     * 2. 创建 token ，并缓存
     * 3. 获取用户权限
     * 4. 返回 token 和 用户权限
     */
    @Override
    @Transactional
    public Integer register(RegisterForm form) {
        Integer userId = null;
        UserEntity user = new UserEntity();
        String registerCode = form.getRegisterCode();
        String key = ACTIVE_CODE_PREFIX + registerCode;

        // 判断注册码是否为超级管理员注册码
        if (registerCode.equals(Root.ROOT_REGISTER_CODE)) {
            // 如果是，判断是否存在超级管理员
            if (hasRootUser()) {
                // 如果存在则抛出异常
                throw new EmosException("注册失败，已存在超级管理员账户");
            }
            // 否则，注册超级管理员
            user.setNickname(form.getNickname());
            user.setPhoto(form.getPhoto());
            user.setOpenId(getOpenId(form.getCode()));
            user.setRoot(true);
            user.setRole(Root.ROLE);
            user.setName(Root.NAME);
            user.setStatus(UserStatusEnum.ON_JOB.getCode());
            userDao.insert(user);
            userId = user.getId();
        } else if (BooleanUtil.isFalse(redisTemplate.hasKey(key))) {
            throw new EmosException("注册码错误");
        } else {
            // 注册普通用户
            userId = Integer.parseInt(redisTemplate.opsForValue().get(key));
            user.setId(userId);
            user.setNickname(form.getNickname());
            user.setPhoto(form.getPhoto());
            user.setOpenId(getOpenId(form.getCode()));
            userDao.update(user, new QueryWrapper<UserEntity>().eq("id", user.getId()));
        }
        // 发送注册成功消息
        String msg = "恭喜您成功注册，请及时更新您的个人信息";
        sendSystemMsg(userId, msg);
        return userId;
    }

    /**
     * 发送系统消息
     */
    private void sendSystemMsg(Integer userId, String msg) {
        MessageEntity message = new MessageEntity();
        message.setSenderId(0);
        message.setSenderName("系统消息");
        message.setSenderPhoto("https://emos-static.oss-cn-hangzhou.aliyuncs.com/img/System.jpg");
        message.setSendTime(new Date());
        message.setMsg(msg);
        messageTask.sendMsgAsync("message." + userId, message);
    }

    /**
     * 获取指定用户的权限信息
     */
    @Override
    public Set<String> getUserPermissions(Integer userId) {
        return this.baseMapper.selectUserPermissions(userId);
    }


    /**
     * 用户登录
     *
     * @param code  临时授权凭证
     * @param token
     * @return
     */
    @Override
    public Integer login(String code, String token) {
        // 获取 openId
        String openId = getOpenId(code);
        // 到数据库中查询
        Integer id = userDao.selectIdByOpenId(openId);
        if (id == null) {
            throw new EmosException("账户不存在");
        }
        return id;
    }


    /**
     * 判断数据库中是否存在 Root 用户
     */
    private boolean hasRootUser() {
        return userDao.exists(new QueryWrapper<UserEntity>().eq("root", 1));
    }


    /**
     * 保存 token 到缓存中
     * key: token 字符串
     * value: userId
     */
    public void saveCacheToken(String token, Integer userId, Integer cacheExpire) {
        redisTemplate.opsForValue().set(token, userId.toString(), cacheExpire, TimeUnit.DAYS);
    }


    /**
     * 获取微信用户的 OpenId
     */
    private String getOpenId(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        HashMap<String, Object> map = new HashMap<>();
        map.put("appid", this.appId);
        map.put("secret", this.appSecret);
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String response = HttpUtil.post(url, map);
        String openid = JSONUtil.parseObj(response).getStr("openid");
        if (StrUtil.isBlank(openid)) {
            throw new RuntimeException("临时登录凭证错误");
        }
        return openid;
    }


    @Override
    public DateTime getUserHiredate(Integer userId) {
        return DateUtil.parse(userDao.selectUserHiredate(userId));
    }

    @Override
    public UserSummaryForm getUserSummary(Integer userId) {
        return userDao.selectUserSummary(userId);
    }

    @Override
    public List<DeptWithMembersResult> searchMembersByKeyword(String keyword) {
        // 查询部门信息
        List<DeptWithMembersResult> result = deptDao.selectWithMembersCount(keyword);
        if (CollectionUtil.isEmpty(result)) {
            return result;
        }
        // 建立映射关系 deptId -> deptWithMembersResult
        Map<Integer, DeptWithMembersResult> map = result.stream().collect(Collectors.toMap(DeptWithMembersResult::getId, r -> r));

        // 查询成员信息
        List<MemberResult> members = userDao.selectMembersByKeyword(keyword);

        // 分组
        for (MemberResult m : members) {
            DeptWithMembersResult r = map.get(m.getDeptId());
            if (r == null) {
                continue;
            }
            List<MemberResult> list = r.getMembers();
            if (CollectionUtil.isEmpty(list)) {
                list = new ArrayList<>();
                r.setMembers(list);
            }
            list.add(m);
        }

        return result;
    }

    @Override
    public List<MemberResult> searchMembersByIds(List<Integer> ids) {
        List<UserEntity> ets = this.list(new QueryWrapper<UserEntity>()
                .select("id", "photo", "name")
                .in("id", ids)
                .orderBy(true, true, "name"));
        List<MemberResult> result = new ArrayList<>();
        ets.forEach(et -> {
            MemberResult mb = new MemberResult();
            mb.setUserId(et.getId());
            mb.setName(et.getName());
            mb.setPhoto(et.getPhoto());
            result.add(mb);
        });
        return result;
    }

    @Override
    public List<UsernameAndPhotoResult> getUserNameAndPhoto(List<Integer> ids) {
        List<UserEntity> users = this.list(new QueryWrapper<UserEntity>().select("id", "name", "photo").eq("status", 1).in("id", ids));
        return users.stream().map(u -> {
            UsernameAndPhotoResult r = new UsernameAndPhotoResult();
            BeanUtil.copyProperties(u, r);
            return r;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveUser(UserEntity userEntity) {
        // 保存至数据库
        if (!save(userEntity)) {
            throw new EmosException("创建员工失败");
        }
        Integer userId = userEntity.getId();
        // 发送验证码
        activeCodeTask.sendActiveCodeAsync(userId, userEntity.getEmail());
    }

    @Override
    public boolean hasGM() {
        return userDao.selectGMId() != null;
    }

    @Override
    public boolean hasManager(Integer deptId) {
        return userDao.selectManagerIdInDept(deptId) != null;
    }

    @Override
    public UserEntity getUserById(Integer userId) {
        String[] columns = {
                "name",
                "sex",
                "tel",
                "email",
                "hiredate",
                "role",
                "dept_id",
                "status"
        };
        return userDao.selectOne(new QueryWrapper<UserEntity>().select(columns).eq("id", userId));
    }

    @Override
    @Transactional
    public void updateUser(UserEntity userEntity) {
        // 更新用户信息
        int count = userDao.update(userEntity, new QueryWrapper<UserEntity>().eq("id", userEntity.getId()));
        if (count != 1) {
            throw new EmosException("修改员工信息失败");
        }
        String msg = "个人信息修改成功，如果不是本人操作请联系管理员";
        sendSystemMsg(userEntity.getId(), msg);
    }

    @Override
    @Transactional
    public void deleteUserById(Integer userId) {
        int count = userDao.deleteById(userId);
        if (count != 1) {
            throw new EmosException("删除用户失败");
        }
        deleteRelationAsync(userId);
    }

    @Override
    public GetContactsListResult getContactList() {

        GetContactsListResult result = new GetContactsListResult();
        result.setContactListMap(new LinkedHashMap<>());
        LinkedHashMap<String, List<GetContactsListResult.ContactResult>> contactListMap = result.getContactListMap();

        List<GetContactsListResult.ContactResult> contactList = userDao.selectContactList();
        contactList.forEach( contact -> {
            String firstLetter = (PinyinUtil.getPinyin(contact.getName()).charAt(0) + "").toUpperCase();
            List<GetContactsListResult.ContactResult> list = contactListMap.get(firstLetter);
            if(list == null) {
                list = new ArrayList<>();
            }
            list.add(contact);
            contactListMap.put(firstLetter, list);
        });
        return result;
    }

    @Async("asyncTaskExecutor")
    protected void deleteRelationAsync(Integer userId) {
        // 删除用户纪录
        checkinDao.deleteAllByUserId(userId);
        // 删除消息引用关系
        messageRefDao.deleteAllByUserId(userId);
        // 删除人脸模型
        faceModelDao.deleteByUserId(userId);
        // 删除消息队列
        messageTask.deleteQueue(userId);
    }

    /**
     * 系统是否存在ROOT用户，并且不是指定ID的用户
     */
    @Override
    public boolean hasNotSpecifiedUserRoot(Integer userId) {
        UserEntity userRoot = userDao.selectOne(new QueryWrapper<UserEntity>()
                .ne("id", userId)
                .eq("root", 1));
        return userRoot != null;
    }

    /**
     * 系统是否存在总经理，并且不是指定ID的用户
     */
    @Override
    public boolean hasNotSpecifiedUserGM(Integer userId) {
        return !userId.equals(userDao.selectGMId());
    }

    /**
     * 部门是否存在经理，并且不是指定ID的用户
     */
    @Override
    public boolean hasManagerAndNotSpecifiedUser(Integer userId, Integer deptId) {
        return !userId.equals(userDao.selectManagerIdInDept(deptId));
    }


}
