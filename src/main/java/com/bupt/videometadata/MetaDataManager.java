package com.bupt.videometadata;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Che Jin <jotline@github>
 */
@Data
public class MetaDataManager implements Serializable {
    //摄像头表,key是geoHash，value是一个类
    private Map<String, Camera> cameraMap;

    //这里需要初始化，想一下camera持久化在哪里
    //思考一下manager放在哪里
    public MetaDataManager(int num) {
        cameraMap = new HashMap<String, Camera>(num);
    }

    public Camera removeCamera(String geoHash) {
        return cameraMap.remove(geoHash);
    }

    public Camera addCamera(String geoHash, Camera camera) {
        return cameraMap.put(geoHash, camera);
    }

    public Camera getCamera(String geoHash) {
        return cameraMap.get(geoHash);
    }
}
