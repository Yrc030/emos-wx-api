package com.yrc.emos.wx.config.xss;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (StrUtil.isNotBlank(value)) {
            value = HtmlUtil.filter(value);
        }
        return value;
    }


    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (ArrayUtil.isNotEmpty(values)) {
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                if (StrUtil.isNotBlank(value)) {
                    value = HtmlUtil.filter(value);
                }
                values[i] = value;
            }
        }
        return values;
    }


    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> original = super.getParameterMap();
        if (CollectionUtil.isEmpty(original)) {
            return original;
        }
        Map<String, String[]> res = new LinkedHashMap<>();
        original.forEach((key, values) -> {
            if (ArrayUtil.isNotEmpty(values)) {
                for (int i = 0; i < values.length; i++) {
                    String value = values[i];
                    if (StrUtil.isNotBlank(value)) {
                        value = HtmlUtil.filter(value);
                    }
                    values[i] = value;
                }
            }
            res.put(key, values);
        });
        return res;
    }
    //
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (StrUtil.isNotBlank(value)) {
            value = HtmlUtil.filter(value);
        }
        return value;
    }


    @Override
    public ServletInputStream getInputStream() {
        ServletInputStream in = null;
        InputStreamReader ir = null;
        BufferedReader br = null;
        try {
            // 读取输入流
            in = super.getInputStream();
            ir = new InputStreamReader(in, StandardCharsets.UTF_8);
            br = new BufferedReader(ir);
            StringBuilder body = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                body.append(line);
            }

            if (body.length() == 0) {
                return in;
            }
            // 过滤 xss 脚本攻击

            Map<String, Object> original = JSONUtil.parseObj(body.toString());
            Map<String, Object> filterMap = new LinkedHashMap<>();
            if (CollectionUtil.isNotEmpty(original)) {
                original.forEach((key, value) -> {
                    if (String.class.isAssignableFrom(value.getClass())) {
                        value = HtmlUtil.filter(value.toString());
                    }
                    if (String[].class.isAssignableFrom(value.getClass())) {
                        value = Arrays.stream(((String[]) value)).map(HtmlUtil::filter).toArray();
                    }
                    filterMap.put(key, value);
                });
            }

            // 转为 ServletInputStream
            String json = JSONUtil.toJsonStr(filterMap);
            ByteArrayInputStream bais = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

            return new ServletInputStream() {
                @Override
                public int read() throws IOException {
                    return bais.read();
                }

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭流
            close(br);
            close(ir);
            close(in);
        }
    }


    private void close(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
