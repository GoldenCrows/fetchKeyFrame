package com.bupt.videometadata.collections.value;

import com.bupt.videometadata.collections.VideoMetaDataType;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Che Jin <jotline@github>
 */
//imgarray类型的type的值类型
public class VideoMetaDataEnum extends VideoMetaDataValue implements Serializable {
    private Set<String> set = new HashSet<String>();

    public VideoMetaDataEnum(Set<String> set) {
        this.set = set;
    }

    static {
        metaDataType = VideoMetaDataType.ENUM;
    }

    protected void writeObject(java.io.ObjectOutputStream s) throws IOException {
        super.writeObject(s);
        s.defaultWriteObject();
        s.writeObject(set);
    }

    @SuppressWarnings("unchecked")
    protected void readObject(java.io.ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        super.readObject(s);
        s.defaultReadObject();
        reinitialize();
        set = (Set<String>) s.readObject();
    }

    private void reinitialize() {
        set = null;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(super.toString());
        set.stream().forEach(item ->
                result.append(item + "  "));
        result.append("\n");
        return result.toString();
    }

}
