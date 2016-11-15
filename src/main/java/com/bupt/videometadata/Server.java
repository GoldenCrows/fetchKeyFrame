package com.bupt.videometadata;

import com.bupt.videometadata.conf.MetaDataConfig;
import com.bupt.videometadata.message.MessageQueue;
import com.bupt.videometadata.redis.MyJedisPubSub;
import com.bupt.videometadata.util.SerializableUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;

/**
 * @author Che Jin <jotline@github>
 */
public class Server {
    //这个类用来放所有的东西,负责维护整个系统的摄像头结构，负责开启消息队列接收服务，维持配置，维持持久化服务
    public static MetaDataManager manager;

    static{
        MetaDataConfig.init();
    }
    private static final JedisPool JEDIS_POOL = new JedisPool(MetaDataConfig.REDIS_URL,MetaDataConfig.REDIS_PORT);


    public static void  init() throws Exception{
        //整体落地到redis不可靠，需要维护一个操作队列，把所有的操作都保持在队列中，强时效性，只需要不断读取队列中的即可
        //或者抛弃一部分的性能，引入分布式锁
        manager=new MetaDataManager(10);
        manager.addCamera("1",new Camera(30));
        manager.addCamera("2",new Camera(30));
        manager.addCamera("3",new Camera(30));
        manager.addCamera("4",new Camera(30));
        MyJedisPubSub pubSub=new MyJedisPubSub(manager);
        try (Jedis jedis = JEDIS_POOL.getResource()){
            jedis.lrange(MetaDataConfig.REDIS_COMMEND_QUEUE.getBytes(),0,-1).stream().forEach(item->
                    pubSub.onMessage(null,item)
            );
        }
        MessageQueue.reciveMessage(MetaDataConfig.REDIS_RECIVE_MESSAGE, manager);

    }


}
