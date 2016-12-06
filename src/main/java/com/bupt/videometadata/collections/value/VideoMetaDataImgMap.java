package com.bupt.videometadata.collections.value;

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
public class VideoMetaDataImgMap extends VideoMetaDataValue implements Serializable {
    static {
        metaDataType = VideoMetaDataType.IMG_MAP;
    }

    private Map<String, VideoMetaDataImgArray> map = new HashMap<>();

    protected void writeObject(java.io.ObjectOutputStream s) throws IOException {
        super.writeObject(s);
        s.defaultWriteObject();
        s.writeObject(map);
    }

    @SuppressWarnings("unchecked")
    protected void readObject(java.io.ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        super.readObject(s);
        s.defaultReadObject();
        reinitialize();
        map = (Map<String, VideoMetaDataImgArray>) s.readObject();
    }

    private void reinitialize() {
        map = null;
    }
}
