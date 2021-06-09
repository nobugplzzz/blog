package com.luqiyu.qiyublogspringboot.util;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * mp自动生成代码类
 *
 * @author: 启誉
 * @create: 2021-05-29
 **/
public class MpAutoGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 需要构建一个 代码自动生成器 对象
        AutoGenerator mpg = new AutoGenerator();

        // 配置策略

        // 1、全局配置
        GlobalConfig gc = new GlobalConfig();
        String peojectPath = System.getProperty("user.dir");
        gc.setOutputDir(peojectPath+"/src/main/java");
        gc.setAuthor("luqiyu");
        gc.setOpen(false);
        gc.setServiceName("%sService"); // 服务接口，去Service的I前缀
//        gc.setIdType(IdType.ASSIGN_ID); // 主键生成策略
        gc.setDateType(DateType.ONLY_DATE);
//        gc.setSwagger2(true);
        gc.setFileOverride(true);// 是否覆盖同名文件，默认是false
//        gc.setActiveRecord(true);// 不需要ActiveRecord特性的请改为false
//        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList


        // 给代码自动生成器注入配置
        mpg.setGlobalConfig(gc);

        // 2、 设置数据源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/qiyu_blog_springboot?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2b8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setDbType(DbType.MYSQL);

        mpg.setDataSource(dsc);

        // 3、包的配置
        PackageConfig pc = new PackageConfig();
//        pc.setModuleName("luqiyu");
        pc.setParent("com.luqiyu.qiyublogspringboot");
        pc.setEntity("entity");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setController("controller");

        mpg.setPackageInfo(pc);

        // 数据库表策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));// 设置要映射的表名
//        strategy.setTablePrefix(new String[] { "user_" });// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);  // 下划线转驼峰命名
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);  // 自动Lombok
        strategy.setLogicDeleteFieldName("deleted");  // 逻辑删除字段

        // 自动填充策略
//        TableFill createTime = new TableFill("create_time", FieldFill.INSERT);
        // 任一列更新会要求手动更新时间，否则update_time为null，而我只要在某些我想更新的列再更新update_time
//        TableFill updateTime = new TableFill("update_time", FieldFill.INSERT_UPDATE);
//        ArrayList<TableFill> tableFills = new ArrayList<>();
//        tableFills.add(createTime);
//        tableFills.add(updateTime);
//        strategy.setTableFillList(tableFills);
        // 乐观锁
        strategy.setVersionFieldName("version");
        // RestController
        strategy.setRestControllerStyle(true);
        mpg.setStrategy(strategy);

        // 执行
        mpg.execute();
    }
}
