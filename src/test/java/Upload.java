import com.bupt.videometadata.conf.MetaDataConfig;
import com.bupt.videometadata.hbase.HbaseController;
import com.bupt.videometadata.message.MessageQueue;
import com.bupt.videometadata.proto.Message;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.bupt.videometadata.message.MessageQueue.getAddImgArrayMessage;

/**
 * @author Che Jin <jotline@github>
 */
public class Upload {
    @Test
    public void uploadSUV()throws Exception{
        File file = new File("D:\\十二类\\大卡车");
        int rowkey=1000;
        List<Message.ImgData> imgDataList=new ArrayList<>();
        for(File file1:file.listFiles())
        {
            rowkey+=60;
            HbaseController.upload("cjtest2",file1,rowkey);
            imgDataList.add(MessageQueue.getImgDataMessage(""+rowkey,(long)rowkey,null));
            System.out.println(rowkey);
        }
        MessageQueue.sendMessage(getAddImgArrayMessage("1", 1420041600l, 1520041700l,imgDataList,"大卡车").toByteArray(), MetaDataConfig.REDIS_RECIVE_MESSAGE);
        while(true){

        }
    }
}
