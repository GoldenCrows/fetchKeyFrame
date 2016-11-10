package com.bupt.videometadata;

import com.bupt.videometadata.collections.*;
import com.bupt.videometadata.collections.value.VideoMetaDataImgArray;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Che Jin <jotline@github>
 */

public class Camera implements Serializable {
    //Camera设计的里面要有一棵B树的,用链表来实现得了，也是logn，链表不行，用数组把ArrayLIST

    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(frameRate);
        s.writeObject(videoList);
        s.writeObject(map);
        s.writeObject(typemap);
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        reinitialize();
        frameRate = s.readInt();
        videoList = (VideoList) s.readObject();
        map = (Map<String, BitMap>) s.readObject();
        typemap = (Map<String, VideoMetaDataType>) s.readObject();
    }

    private void reinitialize() {
        frameRate = 30;
        videoList = null;
        map = null;
        typemap = null;
    }

    //2015-1-1 0:0:0,bitmap的起始时间
    private final static Long STARTDATE = 1420041600l;

    private int frameRate;

    private VideoList videoList;  //需要序列化

    public Camera(int frameRate) {
        this.frameRate = frameRate;
        videoList = new VideoList();
    }

    //还应该有一个bitmap，每次读的时候先命中一下bitmap看看有没有。
    //每个array类型的数据都应该有一个bitmap

    private Map<String, BitMap> map = new HashMap<String, BitMap>();//需要序列化

    //存放这个摄像头输出有哪些type
    private Map<String, VideoMetaDataType> typemap = new HashMap<String, VideoMetaDataType>();

    public VideoFileData findFile(Long time) {
        return videoList.findFile(time);
    }

    public List<VideoFileData> findFiles(Long start, Long end) {
        return videoList.findFiles(start, end);
    }

    public List<VideoMetaData> findMetaDatas(Long start, Long end, String name) {
        Long startindex = start;
        if (typemap.get(name).equals(VideoMetaDataType.IMG_ARRAY)) {
            startindex = map.get(name).findStart(start, end);
            if (startindex < 0) return null;
        }
        return videoList.findFiles(start, end).stream()
                .map(item -> item.getVideoMetaDatas().get(name)).collect(Collectors.toList());

    }

    //存视频接口
    //spark调用这个接口存入hdfs,不需要调用这个接口存入hdfs，而是存入之后传hdfs的path进来，然后通过消息队列传递元信息给每个节点，同时持久化元信息
    public void storeVideo(Long start, Long end) {
        VideoFileData fileData = new VideoFileData(start, end);//这时候这个videofiledata还没有metadata所以为空
        videoList.addVideo(fileData);
    }

    //添加这个视频的输出类型
    public VideoMetaDataType addMetaDataType(String name, VideoMetaDataType type) {
        if (type.equals(VideoMetaDataType.IMG_ARRAY)) {
            //这两种类型需要建立bitmap
            map.put(name, new BitMap(STARTDATE));
        }
        return typemap.put(name, type);
    }

    //列出所有的输出类型
    public Map<String, VideoMetaDataType> listMetaDataType() {
        return typemap;
    }

    public boolean pushMetaData(VideoFileData fileData, VideoMetaData metaData) {
        if (typemap.get(metaData.getName()) == null) {
            //没有事先定义这种输出
            return false;
        }

        if (metaData.getType() == VideoMetaDataType.IMG_ARRAY) {
            ((VideoMetaDataImgArray) metaData.getValue()).getArray().stream().forEach(item ->
                    map.get(metaData.getName()).set(fileData.getStarttime() + item.getFrame() / 30)

            );
        }

        fileData.pushMetaData(metaData.getName(), metaData);
        return true;
    }

    public VideoList getVideoList() {
        return videoList;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("frameRate=" + frameRate + "\n");
        result.append(videoList.toString());
        map.keySet().stream().forEach(item ->
                result.append(item + " = " + map.get(item).toString())
        );
        typemap.keySet().stream().forEach(item ->
                result.append(item + " = " + typemap.get(item).name())
        );
        //TODO almost done


        return result.toString();
    }
}
