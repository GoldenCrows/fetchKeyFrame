import com.bupt.videometadata.Camera;
import com.bupt.videometadata.collections.VideoMetaData;
import com.bupt.videometadata.collections.VideoMetaDataType;
import com.bupt.videometadata.collections.value.GenerateType;
import com.bupt.videometadata.collections.value.StorageType;
import com.bupt.videometadata.collections.value.VideoMetaDataImg;
import com.bupt.videometadata.collections.value.VideoMetaDataImgArray;
import com.bupt.videometadata.util.DateUtil;
import org.junit.Test;

import java.io.*;

/**
 * @author Che Jin <jotline@github>
 */
public class TestSerializable {

    public static Camera c = new Camera(30);

    static {
        try {
            c.storeVideo(DateUtil.transfer("2015-1-1 0:0:0"), DateUtil.transfer("2015-1-2 0:0:0"));
            c.storeVideo(DateUtil.transfer("2015-1-13 0:0:0"), DateUtil.transfer("2015-2-2 0:0:0"));
            c.storeVideo(DateUtil.transfer("2016-1-13 0:0:0"), DateUtil.transfer("2016-2-2 0:0:0"));
            c.addMetaDataType("黄色", VideoMetaDataType.IMG_ARRAY);
            c.addMetaDataType("绿色", VideoMetaDataType.IMG_ARRAY);
            c.addMetaDataType("热度", VideoMetaDataType.NUM);
            VideoMetaData image=new VideoMetaData();
            image.setName("黄色");
            image.setType(VideoMetaDataType.IMG_ARRAY);
            VideoMetaDataImgArray videoMetaDataImgArray=new VideoMetaDataImgArray();
            VideoMetaDataImg img=new VideoMetaDataImg();
            img.setFrame(3000);
            img.setPath("3000path");
            VideoMetaDataImg img1=new VideoMetaDataImg();
            img1.setFrame(31231);
            img1.setPath("31231path");
            videoMetaDataImgArray.getArray().add(img1);
            videoMetaDataImgArray.getArray().add(img);
            videoMetaDataImgArray.setGenerateType(GenerateType.BOTH);
            videoMetaDataImgArray.setStorageType(StorageType.PATH);
            image.setValue(videoMetaDataImgArray);
            c.pushMetaData(c.findFile(DateUtil.transfer("2016-1-15 0:0:3")),image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWriteObject() throws Exception {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("Camera.obj"))) {
            objectOutputStream.writeObject(c);
        }
    }

    @Test
    public void testReadObject() throws Exception {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("Camera.obj"))) {
            Camera camera=(Camera)inputStream.readObject();
            System.out.println(camera);
        }
    }
}
