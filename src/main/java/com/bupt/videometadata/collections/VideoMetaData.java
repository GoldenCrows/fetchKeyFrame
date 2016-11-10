package com.bupt.videometadata.collections;

import com.bupt.videometadata.collections.value.VideoMetaDataValue;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Che Jin <jotline@github>
 */
@Data
public class VideoMetaData implements Serializable {

    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeUTF(name);
        s.writeUTF(type.name());
        s.writeObject(value);
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        reinitialize();
        name = s.readUTF();
        String typestr = s.readUTF();
        for (VideoMetaDataType aa : VideoMetaDataType.values()) {
            if (aa.name().equals(typestr)) {
                type = aa;
                break;
            }
        }
        value = (VideoMetaDataValue) s.readObject();
    }

    private void reinitialize() {
        name = null;
        type = null;
        value = null;
    }

    //这个元数据的名字
    String name;
    //类型
    VideoMetaDataType type;

    VideoMetaDataValue value;

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("name=" + name + "\n");
        result.append("type=" + type.name() + "\n");
        result.append("value=" + value.toString());

        return result.toString();
    }

}
