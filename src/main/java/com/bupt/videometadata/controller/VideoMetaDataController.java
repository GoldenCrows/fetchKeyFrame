package com.bupt.videometadata.controller;

import com.bupt.videometadata.Server;
import com.bupt.videometadata.collections.VideoMetaDataType;
import com.bupt.videometadata.collections.value.VideoMetaDataImg;
import com.bupt.videometadata.collections.value.VideoMetaDataImgArray;
import com.bupt.videometadata.hbase.HbaseController;
import com.bupt.videometadata.util.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;

@Controller
@RequestMapping("/metadata")
public class VideoMetaDataController {

    @GetMapping("/image")
    public void getImageByRowKey(
            HttpServletRequest re, HttpServletResponse response,
            @RequestParam(value = "rowkey", defaultValue = "") int rowkey) throws Exception {
        response.setContentType("image/jpg");
        OutputStream os = response.getOutputStream();
        os.write(HbaseController.getImageByRowKey("cjtest2", rowkey));
        os.flush();
        os.close();
    }

    @GetMapping("/init")
    @ResponseBody
    public String init() throws Exception {

        Server.init();
        return "success";
    }


    @GetMapping("/camera")
    @ResponseBody
    public Set<String> getCameraIds() {
        return Server.manager.getCameraMap().keySet();
    }

    @GetMapping("/type/{geohash}")
    @ResponseBody
    public Map<String, VideoMetaDataType> getCameraTypesByGeoHash(
            @PathVariable(value = "geohash") String geohash) {
        Map<String, VideoMetaDataType> result = Server.manager.getCamera(geohash).listMetaDataType();
        return result == null ? new HashMap<String, VideoMetaDataType>() : result;
    }
//http://localhost:8080/metadata/rowkeys?typename=%E9%BB%84%E8%89%B2&starttime=2014-1-1%200:0:0&endtime=2016-12-1%200:0:0&geohash=1
    @GetMapping("/rowkeys")
    @ResponseBody
    public List<String> getRowKeys(
            @RequestParam(value = "typename", defaultValue = "") String typename,
            @RequestParam(value = "starttime", defaultValue = "2016-1-1 0:0:0") String starttime,
            @RequestParam(value = "endtime", defaultValue = "") String endtime,
            @RequestParam(value = "geohash", defaultValue = "") String geohash) {
        try {
            Long start = DateUtil.transfer(starttime);
            Long end = endtime.equals("") ? new Date().getTime() / 1000 : DateUtil.transfer(endtime);
            VideoMetaDataType type = Server.manager.getCamera(geohash).getTypeByName(typename);
            System.out.println(start+"  "+end+"  "+type);
            if (type == null) return null;
            if (type.equals(VideoMetaDataType.IMG_ARRAY)) {
                List<String> result = new ArrayList<String>();
                Server.manager.getCamera(geohash).findMetaDatas(start, end, typename).stream().map(item ->
                        ((VideoMetaDataImgArray) item.getValue()).getArray()).forEach(item ->
                        item.forEach(itemitem ->
                                result.add(itemitem.getPath()))
                );
                return result;
            }
            if(type.equals((VideoMetaDataType.IMG))){
                List<String> result = new ArrayList<String>();
                Server.manager.getCamera(geohash).findMetaDatas(start, end, typename).stream().map(item ->
                        ((VideoMetaDataImg) item.getValue())).forEach(item ->
                                result.add(item.getPath())
                );
                return result;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @GetMapping("/hello")
    @ResponseBody
    public String getImageByRowKey(
            @RequestParam(value = "rowkey", defaultValue = "") String rowkey) throws Exception {
        return rowkey;
    }
}