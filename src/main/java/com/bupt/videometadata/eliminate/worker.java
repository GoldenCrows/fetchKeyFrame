package com.bupt.videometadata.eliminate;

import com.bupt.videometadata.Camera;
import com.bupt.videometadata.MetaDataManager;
import com.bupt.videometadata.collections.VideoMetaDataType;
import com.bupt.videometadata.collections.value.VideoMetaDataImgArray;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Che Jin <jotline@github>
 */

//用来淘汰数据的worker，整个集群只有一个在跑，还需要些通信协议，淘汰了的数据告诉其他节点
public class Worker {

    private static Worker WORKER = null;

    private Worker(MetaDataManager manager) {
        this.manager = manager;
    }

    //存放所有的整体热度
    private Map<String, Map<String, Double>> HotMap = new HashMap<>();

    //当前周期积累的热度
    private Map<String, Map<String, Double>> CurrentHotMap = new HashMap<>();

    //上一周期积累的热度
    private Map<String, Map<String, Double>> LastHotMap = new HashMap<>();

    //todo 上一次执行的时间，需要初始化
    private Long lastWorkTime;


    private MetaDataManager manager;

    public static Worker getInstance(MetaDataManager manager) {
        if (WORKER == null) {
            synchronized (Worker.class) {
                if (WORKER == null) {
                    WORKER = new Worker(manager);
                    return WORKER;
                }
            }
        }
        return WORKER;
    }

    public void work() {
        new Thread(() -> {
            Map<String, Camera> cameraMap = manager.getCameraMap();
            //每一个摄像头
            for (String geohash : cameraMap.keySet()) {
                //
                Map<String, Double> CHMap = CurrentHotMap.get(geohash);
                Map<String, Double> LHMap = LastHotMap.get(geohash);
                Map<String, Double> HMap = HotMap.get(geohash);

                Map<String, Double> pingjiaMap = new HashMap<>();
                Long now = new Date().getTime() / (1000 * 60);//分钟数
                for (String type : cameraMap.get(geohash).listMetaDataType().keySet()) {
                    Double CHOT = CHMap.get(type) == null ? 0d : CHMap.get(type);
                    Double LHOT = LHMap.get(type) == null ? 0d : LHMap.get(type);
                    Double HOT = HMap.get(type) == null ? 0d : HMap.get(type);

                    Double num = (0.7 * CHOT / (now - lastWorkTime) + 0.2 * LHOT + 0.1 * HOT);
                    HotMap.get(geohash).put(type, num);
                    CurrentHotMap.get(geohash).put(type, 0d);
                    LastHotMap.get(geohash).put(type, CHOT / (now - lastWorkTime));
                    pingjiaMap.put(type, num);
                }
                lastWorkTime = now;

                List<String> sortedtype = pingjiaMap.keySet().stream().
                        filter(item -> cameraMap.get(geohash).getTypeByName(item).equals(VideoMetaDataType.IMG_ARRAY)).
                        sorted((a, b) ->
                                pingjiaMap.get(a) - pingjiaMap.get(b) >= 0 ? 1 : -1
                        ).collect(Collectors.toList());

                if (sortedtype == null || sortedtype.size() == 0)
                    return;
                VideoMetaDataImgArray min = ((VideoMetaDataImgArray) (cameraMap.get(geohash).getVideoList().findFilesByNum(1).get(0)
                        .getVideoMetaDatas().get(sortedtype.get(0)).getValue()));
                VideoMetaDataImgArray secoundmin = ((VideoMetaDataImgArray) (cameraMap.get(geohash).getVideoList().findFilesByNum(1).get(0)
                        .getVideoMetaDatas().get(sortedtype.get(1)).getValue()));
                min.setArray(min.getArray().subList(15, min.getArray().size()));
                secoundmin.setArray(secoundmin.getArray().subList(10, min.getArray().size()));

                min.getArray().get(0).getPath();//这里不用发消息给其他节点了，只需要删除HBASE中的图片即可
                //查询的时候如果HBASE中没有这个图片，那么就删除掉即可。
            }
        }).start();
    }

    public static void main(String[] args) {

    }
}
