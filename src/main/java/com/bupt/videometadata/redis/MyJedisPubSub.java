package com.bupt.videometadata.redis;

import com.bupt.videometadata.Camera;
import com.bupt.videometadata.MetaDataManager;
import com.bupt.videometadata.collections.VideoFileData;
import com.bupt.videometadata.collections.VideoMetaData;
import com.bupt.videometadata.collections.VideoMetaDataType;
import com.bupt.videometadata.collections.value.*;
import com.google.common.util.concurrent.AtomicDouble;
import redis.clients.jedis.BinaryJedisPubSub;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.bupt.videometadata.proto.Message.*;

/**
 * @author Che Jin <jotline@github>
 */
public class MyJedisPubSub extends BinaryJedisPubSub {
    private MetaDataManager manager;

    public MyJedisPubSub(MetaDataManager manager) {
        //因为要解析受到的proto然后添加到内存中啦
        this.manager = manager;
    }

    @Override
    public void onUnsubscribe(byte[] channel, int number) {
        System.out.println("channel: " + channel);
        System.out.println("number :" + number);
    }

    @Override
    public void onSubscribe(byte[] channel, int number) {
        System.out.println("channel: " + channel);
        System.out.println("number :" + number);
    }

    @Override
    public void onPUnsubscribe(byte[] arg0, int arg1) {
    }

    @Override
    public void onPSubscribe(byte[] arg0, int arg1) {
    }

    @Override
    public void onPMessage(byte[] arg0, byte[] arg1, byte[] arg2) {
    }

    @Override
    public void onMessage(byte[] channel, byte[] msgstr) {
        try {
            MSG msg = null;
            msg = MSG.parseFrom(msgstr);
            String type = msg.getType().name();
            if (type.equals("Store_Video")) {
                processStore_Video(msg);
            }
            if (type.equals("Add_MetaDataType")) {
                processAdd_MetaDataType(msg);

            }
            if (type.equals("Add_Img")) {
                processAdd_Img(msg);
            }
            if (type.equals("Add_ImgArray")) {
                processAdd_ImgArray(msg);
            }
            if (type.equals("Add_Video")) {
                processAdd_Video(msg);
            }
            if (type.equals("Add_VideoArray")) {
                processAdd_VideoArray(msg);
            }
            if (type.equals("Add_Num")) {
                processAdd_Num(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean processAdd_Num(MSG msg) {
        AddNum addnum = msg.getAddNum();
        if (addnum == null) return false;
        //加一秒肯定在需要的文件中啦，不可能这个视频连一秒都没有吧
        VideoFileData videoFileData = manager.getCamera(addnum.getGeohash()).findFile(addnum.getStarttime() + 1);
        VideoMetaData metaData = new VideoMetaData();
        metaData.setType(VideoMetaDataType.NUM);
        metaData.setName(addnum.getName().toStringUtf8());
        VideoMetaDataNum value = new VideoMetaDataNum();
        value.setNum(new AtomicDouble(addnum.getNum()));
        metaData.setValue(value);
        videoFileData.pushMetaData(addnum.getName().toStringUtf8(), metaData);

        System.out.println(manager.getCamera(addnum.getGeohash()));
        return true;
    }

    private boolean processAdd_VideoArray(MSG msg) {
        AddVideoArray addVideoArray = msg.getAddVideoArray();
        if (addVideoArray == null) return false;
        //加一秒肯定在需要的文件中啦，不可能这个视频连一秒都没有吧
        VideoFileData videoFileData = manager.getCamera(addVideoArray.getGeohash()).findFile(addVideoArray.getStarttime() + 1);
        VideoMetaData metaData = new VideoMetaData();
        metaData.setType(VideoMetaDataType.VIDEO_ARRAY);
        metaData.setName(addVideoArray.getName().toStringUtf8());
        VideoMetaDataVideoArray value = new VideoMetaDataVideoArray();
        value.setArray(transferVideoList(addVideoArray.getVideoList()));
        metaData.setValue(value);
        videoFileData.pushMetaData(addVideoArray.getName().toStringUtf8(), metaData);

        System.out.println(manager.getCamera(addVideoArray.getGeohash()));
        return true;
    }

    private List<VideoMetaDataVideo> transferVideoList(List<VideoData> videoList) {
        return videoList.stream().map(item -> {
            VideoMetaDataVideo metaData = new VideoMetaDataVideo();
            metaData.setPath(item.getPath().toStringUtf8());
            //coodinates的格式应该是x,y以逗号分隔
            List<List<String>> coodinatesList = item.getCoodinatesList().stream().map(itemitem -> {
                return Arrays.asList(itemitem.split(","));
            }).collect(Collectors.toList());
            int[][] coodinates = new int[4][2];
            for (int i = 1; i <= 4; i++) {
                coodinates[i - 1][0] = Integer.parseInt(coodinatesList.get(i - 1).get(0));
                coodinates[i - 1][1] = Integer.parseInt(coodinatesList.get(i - 1).get(1));
            }
            metaData.setCoodinates(coodinates);
            metaData.setStartframe(item.getStartframe());
            metaData.setEndframe(item.getEndframe());
            return metaData;
        }).collect(Collectors.toList());
    }

    private List<VideoMetaDataImg> transferImgList(List<ImgData> imgList) {
        return imgList.stream().map(item -> {
            VideoMetaDataImg metaData = new VideoMetaDataImg();
            metaData.setPath(item.getPath().toStringUtf8());
            //coodinates的格式应该是x,y以逗号分隔
            List<List<String>> coodinatesList = item.getCoodinatesList().stream().map(itemitem -> {
                return Arrays.asList(itemitem.split(","));
            }).collect(Collectors.toList());
            int[][] coodinates = new int[4][2];
            for (int i = 1; i <= 4; i++) {
                coodinates[i - 1][0] = Integer.parseInt(coodinatesList.get(i - 1).get(0));
                coodinates[i - 1][1] = Integer.parseInt(coodinatesList.get(i - 1).get(1));
            }
            metaData.setCoodinates(coodinates);
            metaData.setFrame(item.getFrame());
            return metaData;
        }).collect(Collectors.toList());
    }

    private boolean processAdd_Video(MSG msg) {
        AddVideo addVideo = msg.getAddVideo();
        if (addVideo == null) return false;
        //加一秒肯定在需要的文件中啦，不可能这个视频连一秒都没有吧
        VideoFileData videoFileData = manager.getCamera(addVideo.getGeohash()).findFile(addVideo.getStarttime() + 1);
        VideoMetaData metaData = new VideoMetaData();
        metaData.setType(VideoMetaDataType.VIDEO);
        metaData.setName(addVideo.getName().toStringUtf8());
        //// TODO: 2016/11/12  这里有些臃肿了，可以改一下
        VideoMetaDataVideo value = transferVideoList(Arrays.asList(addVideo.getVideo())).get(0);
        metaData.setValue(value);
        videoFileData.pushMetaData(addVideo.getName().toStringUtf8(), metaData);

        System.out.println(manager.getCamera(addVideo.getGeohash()));
        return true;
    }

    private boolean processAdd_ImgArray(MSG msg) {
        AddImgArray addImgArray = msg.getAddImgArray();
        if (addImgArray == null) return false;
        //加一秒肯定在需要的文件中啦，不可能这个视频连一秒都没有吧
        VideoFileData videoFileData = manager.getCamera(addImgArray.getGeohash()).findFile(addImgArray.getStarttime() + 1);
        VideoMetaData metaData = new VideoMetaData();
        metaData.setType(VideoMetaDataType.VIDEO_ARRAY);
        metaData.setName(addImgArray.getName().toStringUtf8());
        VideoMetaDataImgArray value = new VideoMetaDataImgArray();
        value.setArray(transferImgList(addImgArray.getImgList()));
        metaData.setValue(value);
        videoFileData.pushMetaData(addImgArray.getName().toStringUtf8(), metaData);

        System.out.println(manager.getCamera(addImgArray.getGeohash()));
        return true;
    }

    private boolean processAdd_Img(MSG msg) {
        AddImg addImg = msg.getAddImg();
        if (addImg == null) return false;
        //加一秒肯定在需要的文件中啦，不可能这个视频连一秒都没有吧
        VideoFileData videoFileData = manager.getCamera(addImg.getGeohash()).findFile(addImg.getStarttime() + 1);
        VideoMetaData metaData = new VideoMetaData();
        metaData.setType(VideoMetaDataType.IMG);
        metaData.setName(addImg.getName().toStringUtf8());
        //// TODO: 2016/11/12  这里有些臃肿了，可以改一下
        VideoMetaDataImg value = transferImgList(Arrays.asList(addImg.getImg())).get(0);
        metaData.setValue(value);
        videoFileData.pushMetaData(addImg.getName().toStringUtf8(), metaData);

        System.out.println(manager.getCamera(addImg.getGeohash()));

        return true;
    }

    private boolean processAdd_MetaDataType(MSG msg) {
        AddMetaDataType addMetaDataType = msg.getAddMetaDataType();
        if (addMetaDataType == null) return false;
        Camera camera = manager.getCamera(addMetaDataType.getGeohash());
        VideoMetaDataType type = VideoMetaDataType.IMG;
        if (addMetaDataType.getType().name().equals("Img")) type = VideoMetaDataType.IMG;
        if (addMetaDataType.getType().name().equals("ImgArray")) type = VideoMetaDataType.IMG_ARRAY;
        if (addMetaDataType.getType().name().equals("Video")) type = VideoMetaDataType.VIDEO;
        if (addMetaDataType.getType().name().equals("VideoArray")) type = VideoMetaDataType.VIDEO_ARRAY;
        if (addMetaDataType.getType().name().equals("Num")) type = VideoMetaDataType.NUM;
        if (addMetaDataType.getType().name().equals("Enum")) type = VideoMetaDataType.ENUM;
        if (addMetaDataType.getType().name().equals("Text")) type = VideoMetaDataType.TEXT;
        camera.addMetaDataType(addMetaDataType.getName().toStringUtf8(), type);
        //todo 这里没有考虑到enum的list         addMetaDataType.getEnumItemList();

        System.out.println(manager.getCamera(addMetaDataType.getGeohash()));

        return true;

    }

    private boolean processStore_Video(MSG msg) {
        StoreVideo storeVideo = msg.getStoreVideo();
        if (storeVideo == null) return false;
        Camera camera = manager.getCamera(storeVideo.getGeohash());
        camera.storeVideo(storeVideo.getStarttime(), storeVideo.getEndtime());

        System.out.println(manager.getCamera(storeVideo.getGeohash()));

        return true;
    }

}
