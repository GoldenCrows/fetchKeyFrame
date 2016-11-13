import com.bupt.videometadata.Camera;
import com.bupt.videometadata.MetaDataManager;
import com.bupt.videometadata.proto.Message;
import com.bupt.videometadata.util.DateUtil;
import com.bupt.videometadata.util.SerializableUtil;
import com.google.protobuf.ByteString;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import static com.bupt.videometadata.message.MessageQueue.*;

/**
 * @author Che Jin <jotline@github>
 */
public class TestPubSub {
    //必须得先维护一个metaDataManager才行呀，必须得有一个camera的list
    @Test
    public void testSub() throws Exception {
//        MetaDataManager manager = new MetaDataManager(3);
//        //先瞎比搞，之后上真实数据
//        manager.getCameraMap().put("1", new Camera(30));
//        manager.getCameraMap().put("2", new Camera(30));
//        manager.getCameraMap().put("3", new Camera(30));
//
//        reciveMessage("metadata", manager);
//
//        sendMessage(getStoreVideoMessage("1", DateUtil.transfer("2015-03-25 0:12:33"),
//                DateUtil.transfer("2015-03-28 11:12:33")).toByteArray(), "metadata");
//        while (true) {
//        }


    }

    @Test
    public void testPub() throws Exception {
//        Jedis jedis=new Jedis();
//        jedis.set("test".getBytes(),getStoreVideoMessage("1", DateUtil.transfer("2015-03-25 0:12:33"),
//                DateUtil.transfer("2015-03-28 11:12:33")).toByteArray());
//
//        Message.MSG msg= Message.MSG.parseFrom(jedis.get("test".getBytes()));
//        System.out.println(msg.getType());





//        Message.MSG msg=Message.MSG.parseFrom(ByteString.copyFromUtf8(jedis.get("test")));




    }

    @Test
    public void test() {

    }


}
