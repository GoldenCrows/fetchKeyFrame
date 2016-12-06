import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author Che Jin <jotline@github>
 */
public class RedisToRedis {
    @Test
    public void test() {
        Jedis jedislocal = new Jedis("127.0.0.1", 6379);
        Jedis jedis190 = new Jedis("10.103.249.190", 6379);

        jedislocal.lrange("commend_queue".getBytes(),0,-1).stream().forEach(item->
                jedis190.rpush("commend_queue".getBytes(),item)
        );

    }
    @Test
    public void insertSomeData(){
        Jedis jedis=new Jedis("10.103.249.190", 6380);
        for(int i=1;i<=1000;i++){
            jedis.set(""+i,i+"");
        }
    }
}
