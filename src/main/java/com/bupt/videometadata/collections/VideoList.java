package com.bupt.videometadata.collections;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Che Jin <jotline@github>
 */
public class VideoList implements Serializable {
    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeObject(videoFileDatas);
    }

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream s)
            throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        reinitialize();
        videoFileDatas = (ArrayList<VideoFileData>) s.readObject();
    }

    private void reinitialize() {
        videoFileDatas = null;
    }

    private ArrayList<VideoFileData> videoFileDatas = new ArrayList<>();

    //给起始时间和结束时间返回包含的文件，当然有可能是半中间，这个方法先不考虑这个问。
    public List<VideoFileData> findFiles(Long start, Long end) {
        //分别查start和end在哪个文件中，返回其中的所有
        int startIndes = find(start);
        int endIndes = find(end);
        return videoFileDatas.subList(startIndes, endIndes+1);
    }

    public ArrayList<VideoFileData> getVideoFileDatas() {
        return videoFileDatas;
    }

    public void addVideo(VideoFileData fileData) {
        videoFileDatas.add(fileData);
    }

    //给一个时间，返回它在哪个文件中
    private int find(Long time) {
        //因为天然是排好序的，所以用二分查找吧。
        int start = 0;
        int end = videoFileDatas.size() - 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            if (time >= videoFileDatas.get(mid).getStarttime() && time <= videoFileDatas.get(mid).getEndtime()) {
                return mid;
            }
            if (time > videoFileDatas.get(mid).getEndtime()) {
                start = mid + 1;
            }
            if (time < videoFileDatas.get(mid).getStarttime()) {
                end = mid;
            }
        }
        return -1;
    }

    public VideoFileData findFile(Long time) {
        int x = find(time);
        return x == -1 ? null : videoFileDatas.get(x);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        videoFileDatas.stream().forEach(item ->
                result.append("     " + item.toString() + "\n"));
        return result.toString();
    }

}
