package com.bupt.videometadata.collections;

import java.io.Serializable;

/**
 * @author Che Jin <jotline@github>
 */
//这个类用来存储所有的元数据类型
public enum VideoMetaDataType implements Serializable{
    IMG,        //图片
    IMG_ARRAY,//图片列表
    VIDEO,      //视频
    VIDEO_ARRAY,//视频列表
    NUM,        //数值
    ENUM,       //枚举
    TEXT;       //文字
}
