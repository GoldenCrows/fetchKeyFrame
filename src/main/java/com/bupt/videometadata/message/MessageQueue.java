package com.bupt.videometadata.message;

import com.bupt.videometadata.MetaDataManager;
import com.bupt.videometadata.collections.VideoMetaDataType;
import com.bupt.videometadata.redis.MyJedisPubSub;
import com.google.protobuf.ByteString;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.bupt.videometadata.proto.Message.*;

/**
 * @author Che Jin <jotline@github>
 */
public class MessageQueue {
    //需要有一个消息队列，来动态更新结构，需要收听的信息有：
    //当视频存入HDFS的时候要把起始时间和终止时间传递过来。
    //当给摄像头增加结果类型的时候
    //当生成结果的时候。所以需要定义三种消息格式，
    private static final ExecutorService THREADPOOL = Executors.newFixedThreadPool(3);

    private static final JedisPool JEDIS_POOL = new JedisPool("127.0.0.1",6379);


    public static MSG getStoreVideoMessage(String geohash, Long starttime, Long endtime) {
        MSG.Builder msgBuilder= MSG.newBuilder();
        StoreVideo.Builder builder = StoreVideo.newBuilder();
        builder.setGeohash(geohash);
        builder.setStarttime(starttime);
        builder.setEndtime(endtime);
        msgBuilder.setStoreVideo(builder);
        msgBuilder.setType(MSGTYPE.Store_Video);
        return msgBuilder.build();
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

    public static MSG getAddImgMessage(String geohash,
                                          Long starttime,
                                          Long endtime,
                                          ImgData img,
                                          String name) {
        MSG.Builder msgBuilder= MSG.newBuilder();
        AddImg.Builder builder = AddImg.newBuilder();
        builder.setGeohash(geohash);
        builder.setStarttime(starttime);
        builder.setEndtime(endtime);
        builder.setImg(img);
        builder.setName(ByteString.copyFromUtf8(name));
        msgBuilder.setAddImg(builder);
        msgBuilder.setType(MSGTYPE.Add_Img);
        return msgBuilder.build();
    }

    public static MSG getAddVideoMessage(String geohash,
                                              Long starttime,
                                              Long endtime,
                                              VideoData video,
                                              String name) {
        MSG.Builder msgBuilder= MSG.newBuilder();
        AddVideo.Builder builder = AddVideo.newBuilder();
        builder.setGeohash(geohash);
        builder.setStarttime(starttime);
        builder.setEndtime(endtime);
        builder.setVideo(video);
        builder.setName(ByteString.copyFromUtf8(name));
        msgBuilder.setAddVideo(builder);
        msgBuilder.setType(MSGTYPE.Add_Video);
        return msgBuilder.build();
    }

    public static MSG getAddVideoArrayMessage(String geohash,
                                                        Long starttime,
                                                        Long endtime,
                                                        List<VideoData> videos,
                                                        String name) {
        MSG.Builder msgBuilder= MSG.newBuilder();
        AddVideoArray.Builder builder = AddVideoArray.newBuilder();
        builder.setGeohash(geohash);
        builder.setStarttime(starttime);
        builder.setEndtime(endtime);
        for (int i = 1; i <= videos.size(); i++)
            builder.setVideo(i - 1, videos.get(i - 1));
        builder.setName(ByteString.copyFromUtf8(name));
        msgBuilder.setType(MSGTYPE.Add_VideoArray);
        msgBuilder.setAddVideoArray(builder);
        return msgBuilder.build();
    }

    public static MSG getAddImgArrayMessage(String geohash,
                                                    Long starttime,
                                                    Long endtime,
                                                    List<ImgData> imgs,
                                                    String name) {
        MSG.Builder msgBuilder= MSG.newBuilder();
        AddImgArray.Builder builder = AddImgArray.newBuilder();
        builder.setGeohash(geohash);
        builder.setStarttime(starttime);
        builder.setEndtime(endtime);
        for (int i = 1; i <= imgs.size(); i++)
            builder.setImg(i - 1, imgs.get(i - 1));
        builder.setName(ByteString.copyFromUtf8(name));
        msgBuilder.setAddImgArray(builder);
        msgBuilder.setType(MSGTYPE.Add_ImgArray);
        return msgBuilder.build();
    }

    public static MSG getAddEnumMessage(String geohash,
                                            Long starttime,
                                            Long endtime,
                                            String item,
                                            String name) {
        MSG.Builder msgBuilder= MSG.newBuilder();
        AddEnum.Builder builder = AddEnum.newBuilder();
        builder.setGeohash(geohash);
        builder.setStarttime(starttime);
        builder.setEndtime(endtime);
        builder.setEnumItem(ByteString.copyFromUtf8(item));
        builder.setName(ByteString.copyFromUtf8(name));
        msgBuilder.setType(MSGTYPE.Add_Enum);
        msgBuilder.setAddEnum(builder);
        return msgBuilder.build();
    }

    public static MSG getAddNumMessage(String geohash,
                                          Long starttime,
                                          Long endtime,
                                          Double num,
                                          String name) {
        MSG.Builder msgBuilder= MSG.newBuilder();
        AddNum.Builder builder = AddNum.newBuilder();
        builder.setGeohash(geohash);
        builder.setStarttime(starttime);
        builder.setEndtime(endtime);
        builder.setNum(num);
        builder.setName(ByteString.copyFromUtf8(name));
        msgBuilder.setType(MSGTYPE.Add_Num);
        msgBuilder.setAddNum(builder);
        return msgBuilder.build();
    }

    public static MSG getAddNumMessage(String geohash,
                                           Long starttime,
                                           Long endtime,
                                           String text,
                                           String name) {
        MSG.Builder msgBuilder= MSG.newBuilder();
        AddText.Builder builder = AddText.newBuilder();
        builder.setGeohash(geohash);
        builder.setStarttime(starttime);
        builder.setEndtime(endtime);
        builder.setText(ByteString.copyFromUtf8(text));
        builder.setName(ByteString.copyFromUtf8(name));
        msgBuilder.setType(MSGTYPE.Add_Text);
        msgBuilder.setAddText(builder);
        return msgBuilder.build();
    }


    public static MSG getAddMetaDataTypeMessage(String name, VideoMetaDataType type, List<String> items) {
        MSG.Builder msgBuilder= MSG.newBuilder();
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
        msgBuilder.setAddMetaDataType(builder);
        msgBuilder.setType(MSGTYPE.Add_MetaDataType);
        return msgBuilder.build();
    }

    public static void sendMessage(byte[] str, String channel) {
        //execute无返回值，submit有返回值
        THREADPOOL.execute(new Runnable() {
            public void run() {
                try (Jedis jedis = JEDIS_POOL.getResource()) {
                    jedis.publish(channel.getBytes(), str);
                }
            }
        });
    }

    public static void reciveMessage(String channel, MetaDataManager manager) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try (Jedis jedis = JEDIS_POOL.getResource()) {
                        jedis.subscribe(new MyJedisPubSub(manager), channel.getBytes());
                    }
                }
            }
        }).start();


    }


}