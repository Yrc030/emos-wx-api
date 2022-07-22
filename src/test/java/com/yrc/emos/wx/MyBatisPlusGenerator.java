package com.yrc.emos.wx;


/**
 * Description:
 * User: joker
 * Date: 2022-05-12-22:23
 * Time: 22:23
 */
public class MyBatisPlusGenerator {

    /*使用前请导入:
        <!--mybatis-plus代码生成器-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
            <version>3.5.1</version>
        </dependency>
        <!--mybatis-plus默认模板引擎-->
        <dependency>
            <groupId>org.apache.velocity</groupId>
            <artifactId>velocity-engine-core</artifactId>
            <version>2.3</version>
        </dependency>






    public static void main(String[] args) {
        AutoGenerator autoGenerator = new AutoGenerator(
                // 数据库配置
                new DataSourceConfig.
                        Builder("jdbc:mysql://192.168.100.4:3306/emos?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai",
                        "root",
                        "1234")
                        .dbQuery(new MySqlQuery())
                        .schema("emos")
                        .typeConvert(new MySqlTypeConvert())
                        .keyWordsHandler(new MySqlKeyWordsHandler())
                        .build()
        );


        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig.Builder()
                // 配置工程的路径
                .outputDir(System.getProperty("user.dir") + "/src/main/java")
                // 配置作者名
                .author("joker")
                // 创建完毕后不自动打开文件夹
                .disableOpenDir()
                // 开启 swagger 模式
                .enableSwagger()
                .build();
        autoGenerator.global(globalConfig);

        // 包配置
        PackageConfig packageConfig = new PackageConfig.Builder()
                .parent("com.yrc.emos.wx")
                .entity("entity")
                .service("service")
                .serviceImpl("service.impl")
                .controller("controller")
                .mapper("mapper")
                .xml("mapper.xml")
                .build();
        autoGenerator.packageInfo(packageConfig);

        // 策略配置
        StrategyConfig.Builder strategyConfig = new StrategyConfig.Builder()
                // 过滤表的前缀（防止将前缀作为实体类名的一部分）
                .addTablePrefix("tb_")
                // 不创建 qrtz_% 的表
                .notLikeTable(new LikeTable("qrtz_", SqlLike.RIGHT));

        // 实体策略配置
        strategyConfig.entityBuilder()
                // 开启lombok模型
                .enableLombok()
                // 设置数据库表映射到实体的命名策略: 下划线命名 -> 驼峰命名
                .naming(NamingStrategy.underline_to_camel)
                // 设置数据库表字段映射到实体的命名策: 下划线命名 -> 驼峰命名
                .columnNaming(NamingStrategy.underline_to_camel)
                // 	添加表字段填充
                .addTableFills(new Column("create_time", FieldFill.INSERT))
                // 设置主键类型
                .idType(IdType.AUTO)
                // 格式化实体类名称
                .formatFileName("%sEntity")
                .build();

        // Controller 策略配置
        strategyConfig.controllerBuilder()
                // 开启驼峰命名转下划线命名
                .enableHyphenStyle()
                // 设置Controller名称
                .formatFileName("%sController")
                .build();

        // Service 策略配置
        strategyConfig.serviceBuilder()
                // 设置 service 的父接口
                .superServiceClass(IService.class)
                // 设置 service实现类 的父类
                .superServiceImplClass(ServiceImpl.class)
                // 设置service接口名称
                .formatServiceFileName("%sService")
                // 设置service实现类名称
                .formatServiceImplFileName("%sServiceImpl")
                .build();


        // Mapper 策略配置
        strategyConfig.mapperBuilder()
                // 设置Mapper接口父类
                .superClass(BaseMapper.class)
                // 开启 @Mapper 注解
                .enableMapperAnnotation()
                // 自动生成 BaseResultMap
                .enableBaseResultMap()
                // 启用BaseColumnList
                .enableBaseColumnList()
                // 设置Mapper接口名称
                .formatMapperFileName("%sMapper")
                // 设置XML文件名称
                .formatXmlFileName("%sMapper")
                .build();


        autoGenerator.strategy(strategyConfig.build());

        // 执行代码生成器
        autoGenerator.execute();
    }*/
}
