package com.bupt.videometadata.collections.value;

import com.bupt.videometadata.collections.VideoMetaDataType;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Che Jin <jotline@github>
 */
//imgarray类型的type的值类型
@Data
public class VideoMetaDataImg extends VideoMetaDataValue implements Comparable, Serializable {

    static {
        metaDataType = VideoMetaDataType.IMG;
    }

    protected void writeObject(java.io.ObjectOutputStream s) throws IOException {
        super.writeObject(s);
        s.defaultWriteObject();
        s.writeLong(frame);
        s.writeUTF(path);
        for (int i = 1; i <= coodinates.length; i++)
            for (int j = 1; j <= coodinates[0].length; j++)
                s.writeInt(coodinates[i - 1][j - 1]);
    }

    @SuppressWarnings("unchecked")
    protected void readObject(java.io.ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        super.readObject(s);
        s.defaultReadObject();
        reinitialize();
        frame = s.readLong();
        path = s.readUTF();
        for (int i = 1; i <= coodinates.length; i++)
            for (int j = 1; j <= coodinates[0].length; j++)
                coodinates[i - 1][j - 1] = s.readInt();
    }

    private void reinitialize() {
        frame = 0;
        coodinates = new int[4][2];
        path = null;
    }

    //表示视频的第几帧
    private long frame;
    //坐标点集合，四个点矩阵
    private int[][] coodinates = new int[4][2];

    private String path;

    public int compareTo(Object o) {
        if (this.getFrame() - ((VideoMetaDataImg) o).getFrame() == 0) return 0;
        return this.getFrame() - ((VideoMetaDataImg) o).getFrame() > 0 ? 1 : -1;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(super.toString());
        result.append("frame="+frame+"\n");
        result.append("path="+path+"\n");
        return result.toString();
    }
}
