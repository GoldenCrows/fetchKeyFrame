package com.bupt.videometadata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Che Jin <jotline@github>
 */
public class MetaDataManager implements Serializable{
    //摄像头表,key是geoHash，value是一个类
    private Map <String,Camera> cameraMap;
    public MetaDataManager(int num){
        cameraMap=new HashMap<String,Camera>(num);
    }
    public Camera removeCamera(String geoHash){
        return cameraMap.remove(geoHash);
    }
    public Camera addCamera(String geoHash,Camera camera){
        return cameraMap.put(geoHash,camera);
    }
}
