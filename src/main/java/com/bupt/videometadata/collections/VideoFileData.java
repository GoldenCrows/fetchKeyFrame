package com.bupt.videometadata.collections;

import lombok.Data;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Che Jin <jotline@github>
 */
@Data
public class VideoFileData implements Comparable, Serializable {

    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeLong(starttime);
        s.writeLong(endtime);
        s.writeObject(videoMetaDatas);
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        reinitialize();
        starttime = s.readLong();
        endtime = s.readLong();
        videoMetaDatas = (Map<String, VideoMetaData>) s.readObject();
    }

    private void reinitialize() {
        starttime = 0l;
        endtime = 0l;
        videoMetaDatas = null;
    }


    public VideoFileData(Long starttime, Long endtime) {
        this.starttime = starttime;
        this.endtime = endtime;
    }

    //表示这个视频文件从什么时候开始和结束，
    private Long starttime;
    private Long endtime;

    //用来存所有的结果数据,黄色是一个metadata ，热度也是一个metadata
    private Map<String, VideoMetaData> videoMetaDatas = new HashMap<String, VideoMetaData>();

    public void pushMetaData(String name, VideoMetaData metaData) {

        videoMetaDatas.put(name, metaData);
    }

    //目前只是比较其实时间，因为理论上都是不会重叠的线段
    public int compareTo(Object o) {
        VideoFileData d = (VideoFileData) o;
        return this.starttime.compareTo(d.starttime);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("     starttime=" + starttime);
        result.append("     endtime=" + endtime);
        videoMetaDatas.keySet().stream().forEach(item -> {
            result.append("     " + videoMetaDatas.get(item).toString() + "\n");
        });
        return result.toString();
    }
}
