package com.bupt.videometadata;

import com.bupt.videometadata.collections.BitMap;
import com.bupt.videometadata.collections.VideoList;
import com.bupt.videometadata.collections.VideoMetaDataType;
import lombok.Data;

import java.io.IOException;
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

    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeObject(cameraMap);
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        reinitialize();
        cameraMap=(Map<String, Camera>)s.readObject();
    }

    private void reinitialize() {
        cameraMap=null;
    }

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
