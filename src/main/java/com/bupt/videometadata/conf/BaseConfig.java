package com.bupt.videometadata.conf;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public abstract class BaseConfig implements Serializable {

    private static final long serialVersionUID = 8042355387234211583L;
    public static HashMap<String, String> propMap = new HashMap<String, String>();
    public static String EMPTY_STR = "";
    private static String configFile = "classpath*:metadata.properties";
    private static List<String> configFiles = null;

    static {
        //加载默认配置
        loadConfigFile();
    }

    //加载单配置文件
    private static boolean loadConfigFile() {
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(configFile);
            if (resources == null) {
                return false;
            }

            Properties p = new Properties();
            for (Resource r : resources) {
                if (r != null) {
                    p.load(r.getInputStream());
                    mergeProperties(p);
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return true;
    }

    //加载多配置文件
    private static boolean loadConfigFiles() {
        if (configFiles == null) {
            return false;
        }

        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            List<Resource> resources = new ArrayList<Resource>();
            for (int idx = 0; idx < configFiles.size(); idx++) {
                Resource[] tempResoureArr = resolver.getResources(configFiles.get(idx));
                if (tempResoureArr != null) {
                    for (int i = 0; i < tempResoureArr.length; i++) {
                        if (tempResoureArr[i] != null) {
                            resources.add(tempResoureArr[i]);
                        }
                    }
                }
            }
            Properties p = new Properties();
            for (Resource r : resources) {
                if (r != null) {
                    p.load(r.getInputStream());
                    mergeProperties(p);
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return true;
    }

    //合并配置
    private static void mergeProperties(Properties p) {
        Set<Map.Entry<Object, Object>> entrySet = p.entrySet();
        for (Map.Entry<Object, Object> entry : entrySet) {
            if (entry.getKey() != null) {
                propMap.put((String) entry.getKey(), (String) entry.getValue());
            }
        }
    }

    /**
     * 获取配置
     *
     * @param key
     * @return value
     */
    public static String get(String key) {
        String v = propMap.get(key);
        if (v != null) {
            return v.trim();
        }
        return null;
    }

    /**
     * 获取配置
     *
     * @param key
     * @return value
     */
    public static String get(String key, String defaultValue) {
        String v = propMap.get(key);
        if (v == null || EMPTY_STR.equals(v.trim())) {
            return defaultValue;
        }
        return v.trim();
    }

    /**
     * 获取配置
     *
     * @return HashMap<String,String>
     */
    public static HashMap<String, String> getMap() {
        return propMap;
    }


}