package com.bupt.videometadata.controller;

import com.bupt.videometadata.Server;
import com.bupt.videometadata.collections.VideoFileData;
import com.bupt.videometadata.collections.VideoMetaDataType;
import com.bupt.videometadata.collections.value.VideoMetaDataImg;
import com.bupt.videometadata.collections.value.VideoMetaDataImgArray;
import com.bupt.videometadata.collections.value.VideoMetaDataImgMap;
import com.bupt.videometadata.hbase.HbaseController;
import com.bupt.videometadata.util.DateUtil;
import com.bupt.videometadata.util.ZipUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/v1")
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
            @RequestParam(value = "starttime") Long starttime,
            @RequestParam(value = "endtime") Long endtime,
            @RequestParam(value = "geohash", defaultValue = "") String geohash,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pagesize", defaultValue = "10") int pagesize) {
        try {
            VideoMetaDataType type = Server.manager.getCamera(geohash).getTypeByName(typename);
            System.out.println(starttime + "  " + endtime + "  " + type);
            if (type == null) return null;
            if (type.equals(VideoMetaDataType.IMG_ARRAY)) {

                List<VideoMetaDataImg> result = new ArrayList<>();
                List<VideoFileData> videoFileDatas = Server.manager.getCamera(geohash).findFiles(starttime, endtime);
                for (VideoFileData fileData : videoFileDatas) {
                    List<VideoMetaDataImg> result1 = new ArrayList<>();
                    ((VideoMetaDataImgArray) fileData.getVideoMetaDatas().get(typename).getValue()).getArray().forEach(result1::add);
                    result.addAll(result1.stream().filter(item -> (item.getFrame() < endtime) & (item.getFrame() > starttime)).collect(Collectors.toList()));
                }

                List<String> realresult = new ArrayList<>();
                realresult.add(result.size() + "");
                if (result.size() >= 10)
                    result = result.subList((page - 1) * pagesize, (page) * pagesize);

                realresult.addAll(result.stream().map(item -> {
                    String res = "";
                    try {
                        res = item.getPath() + "`" + DateUtil.transfer(item.getFrame());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return res;
                }).collect(Collectors.toList()));
                return realresult;

            }
            if (type.equals((VideoMetaDataType.IMG))) {
                List<String> result = new ArrayList<String>();
                Server.manager.getCamera(geohash).findMetaDatas(starttime, endtime, typename).stream().map(item ->
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

    @GetMapping("/rowkeysfromMap")
    @ResponseBody
    public List<String> getRowKeysFromMap(
            @RequestParam(value = "typename", defaultValue = "") String typename,
            @RequestParam(value = "starttime") Long starttime,
            @RequestParam(value = "endtime") Long endtime,
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "geohash", defaultValue = "") String geohash) {
        VideoMetaDataType type = Server.manager.getCamera(geohash).getTypeByName(typename);
        if (type.equals(VideoMetaDataType.IMG_MAP)) {
            List<VideoMetaDataImg> result = new ArrayList<>();
            Server.manager.getCamera(geohash).findMetaDatas(starttime, endtime, typename).stream().map(item ->
                    ((VideoMetaDataImgMap) item.getValue()).getMap().get(keyword).getArray()).forEach(item ->
                    item.forEach(result::add)
            );
            return result.stream().filter(item ->
                    (item.getFrame() < endtime) &
                            (item.getFrame() > starttime)
            ).map(VideoMetaDataImg::getPath).collect(Collectors.toList());
        }


        return null;
    }

    @GetMapping("/download")
    @ResponseBody
    public void download(
            HttpServletRequest re, HttpServletResponse response,
            @RequestParam(value = "typename", defaultValue = "") String typename,
            @RequestParam(value = "starttime") Long starttime,
            @RequestParam(value = "endtime") Long endtime,
            @RequestParam(value = "geohash", defaultValue = "") String geohash) {
        try {
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition","attachment; filename=picture.zip");
            VideoMetaDataType type = Server.manager.getCamera(geohash).getTypeByName(typename);
            System.out.println(starttime + "  " + endtime + "  " + type);
            if (type == null) return ;
            if (type.equals(VideoMetaDataType.IMG_ARRAY)) {

                List<Integer> result = new ArrayList<>();
                List<VideoFileData> videoFileDatas = Server.manager.getCamera(geohash).findFiles(starttime, endtime);
                for (VideoFileData fileData : videoFileDatas) {
                    List<VideoMetaDataImg> result1 = new ArrayList<>();
                    ((VideoMetaDataImgArray) fileData.getVideoMetaDatas().get(typename).getValue()).getArray().forEach(result1::add);
                    result.addAll(result1.stream().filter(item -> (item.getFrame() < endtime) & (item.getFrame() > starttime)).map(item->Integer.parseInt(item.getPath())).collect(Collectors.toList()));
                }

                ZipUtil.ZipMultiFile(result,response.getOutputStream());
            }
            if (type.equals((VideoMetaDataType.IMG))) {
                List<Integer> result = new ArrayList<Integer>();
                Server.manager.getCamera(geohash).findMetaDatas(starttime, endtime, typename).stream().map(item ->
                        ((VideoMetaDataImg) item.getValue())).forEach(item ->
                        result.add(Integer.parseInt(item.getPath()))
                );
                ZipUtil.ZipMultiFile(result,response.getOutputStream());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ;

    }

    @GetMapping("/hello")
    @ResponseBody
    public String getImageByRowKey(
            @RequestParam(value = "rowkey", defaultValue = "") String rowkey) throws Exception {
        return rowkey;
    }
}