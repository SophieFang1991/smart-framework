package org.smart4j.framework.helper;

import org.smart4j.framework.ConfigConstant;
import org.smart4j.framework.util.PropsUtil;

import java.util.Properties;

/**
 * 读取配置文件
 * */
public final class ConfigHelper {
    private static final Properties CONFIG_PROPS= PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);
    /**
     * 获取jdbc 驱动
     * */
    public static String getJdbcDriver(){
        return CONFIG_PROPS.getProperty(ConfigConstant.JDBC_DRIVER,"");
    }
    /**
     * 获取jdbc url
     * */
    public static String getJdbcUrl(){
        return  CONFIG_PROPS.getProperty(ConfigConstant.JDBC_URL,"");
    }
    /**
     * 获取jdbc 用户名
     * */
    public static String getJdbcUserName(){
        return  CONFIG_PROPS.getProperty(ConfigConstant.JDBC_USERNAME, "");
    }
    /**
     * 获取jdbc password
     * */
    public  static String getJdbcPassword(){
        return  CONFIG_PROPS.getProperty(ConfigConstant.JDBC_PASSWORD,"");
    }
    /**
     * 获取应用基础包名
     * */
    public static String getAppBasePackage(){
        return CONFIG_PROPS.getProperty(ConfigConstant.APP_BASE_PACKAGE,"");
    }
    /**
     * 获取应用jsp路径
     * */
    public static String getAppJspPath(){
        return  CONFIG_PROPS.getProperty(ConfigConstant.APP_JSP_PATH,"/WEB-INF/view/");
    }
    /**
     * 获取静态资源文件路径
     * */
    public static String getAppAssetPath(){
        return  CONFIG_PROPS.getProperty(ConfigConstant.APP_ASSET_PATH,"/asset/");
    }

}
