package com.bupt.videometadata.collections.value;

import com.bupt.videometadata.collections.VideoMetaDataType;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Che Jin <jotline@github>
 */
@Data
public abstract class VideoMetaDataValue implements Serializable {
    protected static VideoMetaDataType metaDataType;
    protected String name;
    protected GenerateType generateType = GenerateType.FRAME;
    protected StorageType storageType = StorageType.PATH;

    private static final long serialVersionUID = 1412277949381450533l;

    protected void writeObject(java.io.ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeUTF(name);
        s.writeUTF(generateType.name());
        s.writeUTF(storageType.name());
    }

    @SuppressWarnings("unchecked")
    protected void readObject(java.io.ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        reinitialize();
        name = s.readUTF();
        String generateTypestr = s.readUTF();
        String storageTypestr = s.readUTF();
        for (GenerateType g : GenerateType.values()) {
            if (g.name().equals(generateTypestr)) {
                generateType = g;
                break;
            }
        }
        for (StorageType st : StorageType.values()) {
            if (st.name().equals(generateTypestr)) {
                storageType = st;
                break;
            }
        }
    }

    private void reinitialize() {
        name = null;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("name=" + name + "\n");
        result.append("type=" + metaDataType.name() + "\n");
        result.append("generate=" + generateType.name() + "\n");
        result.append("storage=" + storageType.name() + "\n");

        return result.toString();
    }
}
