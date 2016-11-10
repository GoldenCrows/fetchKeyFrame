package com.bupt.videometadata.collections.value;

import com.bupt.videometadata.collections.VideoMetaDataType;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Che Jin <jotline@github>
 */
@Data
public class VideoMetaDataVideo extends  VideoMetaDataValue implements Comparable,Serializable{

    protected void writeObject(java.io.ObjectOutputStream s) throws IOException {
        super.writeObject(s);
        s.defaultWriteObject();
        s.writeLong(startframe);
        s.writeLong(endframe);
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
        startframe = s.readLong();
        endframe = s.readLong();
        path = s.readUTF();
        for (int i = 1; i <= coodinates.length; i++)
            for (int j = 1; j <= coodinates[0].length; j++)
                coodinates[i - 1][j - 1] = s.readInt();
    }

    private void reinitialize() {
        startframe = 0;
        endframe=0;
        coodinates = new int[4][2];
        path = null;
    }

    static {
        metaDataType = VideoMetaDataType.VIDEO;
    }
    //表示视频的第几帧
    private long startframe;

    private long endframe;
    //坐标点集合，四个点矩阵
    private int[][] coodinates;

    private String path;

    public int compareTo(Object o) {
        if (this.getStartframe() - ((VideoMetaDataVideo) o).getStartframe() == 0) return 0;
        return this.getStartframe() - ((VideoMetaDataVideo) o).getStartframe() > 0 ? 1 : -1;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(super.toString());
        result.append("startframe="+startframe+"\n");
        result.append("endframe="+endframe+"\n");
        result.append("path="+path+"\n");
        return result.toString();
    }
}
