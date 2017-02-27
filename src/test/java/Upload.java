import com.bupt.videometadata.collections.VideoMetaDataType;
import com.bupt.videometadata.conf.MetaDataConfig;
import com.bupt.videometadata.hbase.HbaseController;
import com.bupt.videometadata.message.MessageQueue;
import com.bupt.videometadata.proto.Message;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.bupt.videometadata.message.MessageQueue.*;

/**
 * @author Che Jin <jotline@github>
 */
public class Upload {

    public static long[][] dates = new long[12][2];

    static {
        dates[0][0] = 1420041600l;
        dates[0][1] = 1422720000l;
        dates[1][0] = 1422720001l;
        dates[1][1] = 1425139200l;
        dates[2][0] = 1425139201l;
        dates[2][1] = 1427817600l;

        dates[3][0] = 1427817601l;
        dates[3][1] = 1430409600l;
        dates[4][0] = 1430409601l;
        dates[4][1] = 1433088000l;
        dates[5][0] = 1433088001l;
        dates[5][1] = 1435680000l;

        dates[6][0] = 1435680001l;
        dates[6][1] = 1438358400l;
        dates[7][0] = 1438358401l;
        dates[7][1] = 1441036800l;
        dates[8][0] = 1441036801l;
        dates[8][1] = 1443628800l;

        dates[9][0] = 1443628801l;
        dates[9][1] = 1446307200l;
        dates[10][0] = 1446307201l;
        dates[10][1] = 1448899200l;
        dates[11][0] = 1448899201l;
        dates[11][1] = 1451577600l;
    }

    @Test
    public void uploadSUV() throws Exception {
        new MessageQueue().sendMessage(getAddMetaDataTypeMessage("1", "小轿车", VideoMetaDataType.IMG_ARRAY, null).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);
        //一月到二月
        new MessageQueue().sendMessage(getStoreVideoMessage("1", 1420041600l, 1422720000l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessage(getStoreVideoMessage("1", 1422720001l, 1425139200l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessage(getStoreVideoMessage("1", 1425139201l, 1427817600l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessage(getStoreVideoMessage("1", 1427817601l, 1430409600l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessage(getStoreVideoMessage("1", 1430409601l, 1433088000l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessage(getStoreVideoMessage("1", 1433088001l, 1435680000l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessage(getStoreVideoMessage("1", 1435680001l, 1438358400l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessage(getStoreVideoMessage("1", 1438358401l, 1441036800l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessage(getStoreVideoMessage("1", 1441036801l, 1443628800l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessage(getStoreVideoMessage("1", 1443628801l, 1446307200l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessage(getStoreVideoMessage("1", 1446307201l, 1448899200l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessage(getStoreVideoMessage("1", 1448899201l, 1451577600l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);


        File file = new File("D:\\十二类\\小轿车");
        int rowkey = 1;
        long zhenhao = 0;
        List<Message.ImgData> imgDataList2 = new ArrayList<>();
        List<Message.ImgData> imgDataList1 = new ArrayList<>();
        long date = new Date().getTime();
        //1.1-2.1
        for (File file1 : file.listFiles()) {
            rowkey++;
            zhenhao += (1425139200l - 1422720001l) * 30 / file.listFiles().length;
            final int rowkeyfinal = rowkey;
            new Thread(() -> {
                try {
                    HbaseController.upload("cjtest2", file1, rowkeyfinal);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            imgDataList2.add(MessageQueue.getImgDataMessage("" + rowkey, zhenhao, null));
        }
        zhenhao = 0;
        for (File file1 : file.listFiles()) {
            rowkey++;
            zhenhao += (1425139200l - 1422720001l) * 30 / file.listFiles().length;
            final int rowkeyfinal = rowkey;
            new Thread(() -> {
                try {
                    HbaseController.upload("cjtest2", file1, rowkeyfinal);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            imgDataList1.add(MessageQueue.getImgDataMessage("" + rowkey, zhenhao, null));
        }
        new MessageQueue().sendMessage(getAddImgArrayMessage("1", 1422720001l, 1425139200l, imgDataList2, "小轿车").toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        new MessageQueue().sendMessage(getAddImgArrayMessage("1", 1420041600l, 1422720000l, imgDataList1, "小轿车").toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        long now = new Date().getTime();
        System.out.println(rowkey + " " + (now - date));
        while (true) {

        }
    }

    //http://localhost:8080/metadata/v1/rowkeysfromMap?typename=%E8%BD%A6%E7%89%8C&starttime=1420041600&endtime=1520041700&geohash=1&keyword=%E4%BA%ACA%20XX001
    @Test
    public void testImgMap() {
        String paizhao = "京AXX";
        for (int i = 100; i <= 600; i += 2) {

            List<Message.ImgData> imgDataList = new ArrayList<>();
            imgDataList.add(MessageQueue.getImgDataMessage("" + i, (long) i, null));
            imgDataList.add(MessageQueue.getImgDataMessage("" + (i + 1), (long) i + 1, null));
            new MessageQueue().sendMessage(getAddImgMap("1", 1420041600l, 1520041700l, imgDataList, paizhao + i, "车牌").toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
            System.out.println(paizhao + i);
        }
        while (true) {

        }
    }

    @Test
    public void uploadchepai() throws Exception {

        File file = new File("D:\\白天车牌");
        int rowkey = 50000;
        int i = 1;
        for (File file1 : file.listFiles()) {
            String chepai = file1.getName().split("_")[4];
            List<Message.ImgData> imgDataList = new ArrayList<>();
//            HbaseController.upload("cjtest2",HbaseController.getSource(file1),1420041600l,1422720000l,"1");

//            HbaseController.upload("cjtest2", file1, rowkey + i);
            imgDataList.add(MessageQueue.getImgDataMessage("" + (rowkey + i),  1420041600l+i, null));
            i++;
            new MessageQueue().sendMessageTo190(getAddImgMap("1", 1420041600l, 1422720000l, imgDataList, chepai, "车牌").toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
//
            System.out.println(chepai + " " + i);
        }
        while (true) {

        }
    }
//1000W-1200W这里有两百万的图片
    @Test
    public void upload() throws Exception {
        ExecutorService executorService= Executors.newFixedThreadPool(1000);
        new MessageQueue().sendMessage(getAddMetaDataTypeMessage("1", "SUV", VideoMetaDataType.IMG_ARRAY, null).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);
        File file = new File("D:\\十二类\\SUV");
        List<File> files = new ArrayList<>();
        files.addAll(Arrays.asList(file.listFiles()));

        int rowkey = 10000000;
        long zhenhao = 0;
        List<Message.ImgData> imgDataList2 = new ArrayList<>();
        long date = new Date().getTime();
        for (int i=1;i<=2000000;i++) {
            rowkey++;
            zhenhao += 30*10;
            File file1=files.get(i%files.size());
            final int rowkeyfinal = rowkey;
            executorService.execute(() -> {
                try {
                    HbaseController.upload("cjtest2", file1, rowkeyfinal);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            if(i%100000==0){
                System.out.println(i+" "+new Date());
                Thread.sleep(1000);
            }
//            imgDataList2.add(MessageQueue.getImgDataMessage("" + rowkey, zhenhao, null));
        }
        System.out.println(rowkey);
        while(true){}
    }

    public static void main(String[] args) throws Exception {
        new Upload().upload();
    }

    @Test
    public void testUpload()throws Exception{
        addInfo();
        //1000W到1090W
        int count=10000001;
        for(int i=1;i<=12;i++){
            List<Message.ImgData> imgDataList = new ArrayList<>();
            //一个月有60*60*24*30=
            long zhenping=(dates[i-1][1]-dates[i-1][0])/(900000/12);
            long zhenhao=1;

            for(int j=1;j<=900000/12;j++) {
                zhenhao+=zhenping;
                imgDataList.add(MessageQueue.getImgDataMessage("" + count++, dates[i-1][0]+zhenhao, null));
            }
            new MessageQueue().sendMessageTo190(getAddImgArrayMessage("1", dates[i-1][0], dates[i-1][1], imgDataList, "SUV").toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
            System.out.println(new Date());
        }
        while(true){}
    }
    @Test
    public void addVideo() throws IOException {
        File file=new File("D://little.mp4");
        HbaseController.upload("video",HbaseController.getSource(file),1420041600l,1422720000l,"1");
    }

    public void addInfo()throws Exception{
        new MessageQueue().sendMessageTo190(getAddMetaDataTypeMessage("1", "SUV", VideoMetaDataType.IMG_ARRAY, null).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);
        //一月到二月
        new MessageQueue().sendMessageTo190(getStoreVideoMessage("1", 1420041600l, 1422720000l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessageTo190(getStoreVideoMessage("1", 1422720001l, 1425139200l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessageTo190(getStoreVideoMessage("1", 1425139201l, 1427817600l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessageTo190(getStoreVideoMessage("1", 1427817601l, 1430409600l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessageTo190(getStoreVideoMessage("1", 1430409601l, 1433088000l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessageTo190(getStoreVideoMessage("1", 1433088001l, 1435680000l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessageTo190(getStoreVideoMessage("1", 1435680001l, 1438358400l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessageTo190(getStoreVideoMessage("1", 1438358401l, 1441036800l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessageTo190(getStoreVideoMessage("1", 1441036801l, 1443628800l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessageTo190(getStoreVideoMessage("1", 1443628801l, 1446307200l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessageTo190(getStoreVideoMessage("1", 1446307201l, 1448899200l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);

        new MessageQueue().sendMessageTo190(getStoreVideoMessage("1", 1448899201l, 1451577600l).toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        Thread.sleep(100);
    }

    @Test
    public void test(){
        for(int i=1;i<=12;i++) {
            long zhenping=(dates[i-1][1]-dates[i-1][0])/(900000/12);
            System.out.println(zhenping);
        }
    }

}
