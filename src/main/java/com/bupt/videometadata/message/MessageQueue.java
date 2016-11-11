package com.bupt.videometadata.message;

import com.bupt.videometadata.collections.VideoMetaDataType;
import com.google.protobuf.ByteString;

import java.util.List;

import static com.bupt.videometadata.proto.Message.*;

/**
 * @author Che Jin <jotline@github>
 */
public class MessageQueue {
    //需要有一个消息队列，来动态更新结构，需要收听的信息有：
    //当视频存入HDFS的时候要把起始时间和终止时间传递过来。
    //当给摄像头增加结果类型的时候
    //当生成结果的时候。所以需要定义三种消息格式，
    public static StoreVideo getStoreVideoMessage(String geohash, Long starttime, Long endtime) {
        StoreVideo.Builder builder = StoreVideo.newBuilder();
        builder.setGeohash(geohash);
        builder.setStarttime(starttime);
        builder.setEndtime(endtime);
        return builder.build();
    }

    public static ImgData getImgDataMessage(String path, Long frame, String[] coodinates) {
        ImgData.Builder builder = ImgData.newBuilder();
        builder.setPath(ByteString.copyFromUtf8(path));
        builder.setFrame(frame);
        if (coodinates != null)
            for (int i = 1; i <= coodinates.length; i++) {
                builder.setCoodinates(i - 1, coodinates[i - 1]);
            }
        return builder.build();
    }

    public static VideoData getVideoDataMessage(String path,
                                                Long starttime,
                                                Long endtime,
                                                String[] coodinates) {
        VideoData.Builder builder = VideoData.newBuilder();
        builder.setPath(ByteString.copyFromUtf8(path));
        builder.setStartframe(starttime);
        builder.setEndframe(endtime);
        if (coodinates != null)
            for (int i = 1; i <= coodinates.length; i++) {
                builder.setCoodinates(i - 1, coodinates[i - 1]);
            }
        return builder.build();
    }

    public static AddImg getAddImgMessage(String geohash,
                                          Long starttime,
                                          Long endtime,
                                          ImgData img,
                                          String name) {
        AddImg.Builder builder = AddImg.newBuilder();
        builder.setGeohash(geohash);
        builder.setStarttime(starttime);
        builder.setEndtime(endtime);
        builder.setImg(img);
        builder.setName(ByteString.copyFromUtf8(name));
        return builder.build();
    }

    public static AddVideo getAddVideoMessage(String geohash,
                                              Long starttime,
                                              Long endtime,
                                              VideoData video,
                                              String name) {
        AddVideo.Builder builder = AddVideo.newBuilder();
        builder.setGeohash(geohash);
        builder.setStarttime(starttime);
        builder.setEndtime(endtime);
        builder.setVideo(video);
        builder.setName(ByteString.copyFromUtf8(name));
        return builder.build();
    }

    public static AddVideoArray getAddVideoArrayMessage(String geohash,
                                                        Long starttime,
                                                        Long endtime,
                                                        List<VideoData> videos,
                                                        String name) {
        AddVideoArray.Builder builder = AddVideoArray.newBuilder();
        builder.setGeohash(geohash);
        builder.setStarttime(starttime);
        builder.setEndtime(endtime);
        for (int i = 1; i <= videos.size(); i++)
            builder.setVideo(i - 1, videos.get(i - 1));
        builder.setName(ByteString.copyFromUtf8(name));
        return builder.build();
    }

    public static AddImgArray getAddImgArrayMessage(String geohash,
                                                    Long starttime,
                                                    Long endtime,
                                                    List<ImgData> imgs,
                                                    String name) {
        AddImgArray.Builder builder = AddImgArray.newBuilder();
        builder.setGeohash(geohash);
        builder.setStarttime(starttime);
        builder.setEndtime(endtime);
        for (int i = 1; i <= imgs.size(); i++)
            builder.setImg(i - 1, imgs.get(i - 1));
        builder.setName(ByteString.copyFromUtf8(name));
        return builder.build();
    }

    public static AddEnum getAddEnumMessage(String geohash,
                                            Long starttime,
                                            Long endtime,
                                            String item,
                                            String name) {
        AddEnum.Builder builder = AddEnum.newBuilder();
        builder.setGeohash(geohash);
        builder.setStarttime(starttime);
        builder.setEndtime(endtime);
        builder.setEnumItem(ByteString.copyFromUtf8(item));
        builder.setName(ByteString.copyFromUtf8(name));
        return builder.build();
    }

    public static AddNum getAddNumMessage(String geohash,
                                            Long starttime,
                                            Long endtime,
                                            Double num,
                                            String name) {
        AddNum.Builder builder = AddNum.newBuilder();
        builder.setGeohash(geohash);
        builder.setStarttime(starttime);
        builder.setEndtime(endtime);
        builder.setNum(num);
        builder.setName(ByteString.copyFromUtf8(name));
        return builder.build();
    }

    public static AddText getAddNumMessage(String geohash,
                                          Long starttime,
                                          Long endtime,
                                          String text,
                                          String name) {
        AddText.Builder builder = AddText.newBuilder();
        builder.setGeohash(geohash);
        builder.setStarttime(starttime);
        builder.setEndtime(endtime);
        builder.setText(ByteString.copyFromUtf8(text));
        builder.setName(ByteString.copyFromUtf8(name));
        return builder.build();
    }


    public static AddMetaDataType getAddMetaDataTypeMessage(String name, VideoMetaDataType type, List<String> items) {
        AddMetaDataType.Builder builder = AddMetaDataType.newBuilder();
        builder.setName(ByteString.copyFromUtf8(name));
        //todo 这里可以给enum加上value，这样太麻烦
        if (type.name().equals("IMG"))
            builder.setType(METADATATYPE.Img);
        if (type.name().equals("IMGARRAY"))
            builder.setType(METADATATYPE.ImgArray);
        if (type.name().equals("VIDEO"))
            builder.setType(METADATATYPE.Video);
        if (type.name().equals("VIDEOARRAY"))
            builder.setType(METADATATYPE.VideoArray);
        if (type.name().equals("ENUM"))
            builder.setType(METADATATYPE.Enum);
        if (type.name().equals("TEXT"))
            builder.setType(METADATATYPE.Text);
        if (type.name().equals("NUM"))
            builder.setType(METADATATYPE.Num);

        for (int i = 1; i <= items.size(); i++) {
            builder.setEnumItem(i - 1, ByteString.copyFromUtf8(items.get(i - 1)));
        }
        return builder.build();
    }


}
