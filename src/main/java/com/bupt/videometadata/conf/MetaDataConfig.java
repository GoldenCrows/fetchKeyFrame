package com.bupt.videometadata.conf;

import java.io.Serializable;

/**
 * @author Che Jin <chejin@wandoujia.com>
 */
public class MetaDataConfig extends BaseConfig implements Serializable {


    public static String REDIS_URL = "127.0.0.1";

    public static Integer REDIS_PORT = 6379;

    public static String REDIS_RECIVE_MESSAGE = "metatdata";

    public static String REDIS_METADATAMANAGER = "MetaDataManager";

    public static String REDIS_COMMEND_QUEUE = "commend_queue";


    public static void init() {
        try {
            REDIS_PORT = Integer.parseInt(get("REDIS_PORT", REDIS_PORT + ""));
            REDIS_URL = get("REDIS_URL", REDIS_URL + "");
            REDIS_RECIVE_MESSAGE = get("REDIS_RECIVE_MESSAGE", REDIS_RECIVE_MESSAGE + "");
            REDIS_METADATAMANAGER = get("REDIS_METADATAMANAGER", REDIS_METADATAMANAGER + "");
            REDIS_COMMEND_QUEUE=get("REDIS_COMMEND_QUEUE",REDIS_COMMEND_QUEUE);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
