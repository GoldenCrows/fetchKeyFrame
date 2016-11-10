package com.bupt.videometadata.collections.value;

import com.bupt.videometadata.collections.VideoMetaDataType;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Che Jin <jotline@github>
 */
//imgarray类型的type的值类型
@Data
public class VideoMetaDataNum extends VideoMetaDataValue implements Serializable{

    protected void writeObject(java.io.ObjectOutputStream s) throws IOException {
        super.writeObject(s);
        s.defaultWriteObject();
        s.writeDouble(num.get());
    }

    @SuppressWarnings("unchecked")
    protected void readObject(java.io.ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        super.readObject(s);
        s.defaultReadObject();
        reinitialize();
        num=new AtomicDouble(s.readDouble());
    }

    private void reinitialize() {
        num=null;
    }

    static{
        metaDataType= VideoMetaDataType.NUM;
    }
    AtomicDouble num;

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(super.toString());
        result.append("num="+num.doubleValue());
        return result.toString();
    }
}
