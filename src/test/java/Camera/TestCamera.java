package Camera;

import com.bupt.videometadata.Camera;
import com.bupt.videometadata.collections.VideoFileData;
import com.bupt.videometadata.collections.VideoMetaDataType;
import com.bupt.videometadata.util.DateUtil;
import org.junit.Test;

import java.util.BitSet;
import java.util.Date;
import java.util.Map;

/**
 * @author Che Jin <jotline@github>
 */
public class TestCamera {
    public static Camera c = new Camera(30);

    static {
        try {
            c.storeVideo(DateUtil.transfer("2015-1-1 0:0:0"), DateUtil.transfer("2015-1-2 0:0:0"));
            c.storeVideo(DateUtil.transfer("2015-1-13 0:0:0"), DateUtil.transfer("2015-2-2 0:0:0"));
            c.storeVideo(DateUtil.transfer("2016-1-13 0:0:0"), DateUtil.transfer("2016-2-2 0:0:0"));
            c.addMetaDataType("黄色", VideoMetaDataType.IMG_ARRAY);
            c.addMetaDataType("绿色", VideoMetaDataType.IMG_ARRAY);
            c.addMetaDataType("热度", VideoMetaDataType.NUM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testStoreVideo() throws Exception {
        c.getVideoList().getVideoFileDatas().forEach(item ->
                System.out.println(item.getStarttime() + " " + item.getEndtime()));
    }

    @Test
    public void testFindFile() throws Exception {
        VideoFileData re = c.findFile(DateUtil.transfer("2015-1-1 11:23:12"));
        System.out.println(DateUtil.transfer(re.getStarttime()) + " " + DateUtil.transfer(re.getEndtime()));
    }

    @Test
    public void testToString(){
        System.out.println(c);
    }

    @Test
    public void testPushMetaData() {

    }

    @Test
    public void testAddMetaDataType() {

        Map<String, VideoMetaDataType> map = c.listMetaDataType();
        for (String str : map.keySet()) {
            System.out.println(str + " " + map.get(str));
        }
    }

    @Test
    public void testTime() {
        Date d = new Date();
        System.out.println(d.getTime());
        BitSet set = new BitSet(30000000);
        set.set(28912912);
        System.out.println(set.nextSetBit(0));
        System.out.println(new Date().getTime());
    }


}
