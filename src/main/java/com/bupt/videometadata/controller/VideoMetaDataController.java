package com.bupt.videometadata.controller;

import com.bupt.videometadata.hbase.HbaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

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

    @GetMapping("/hello")
    @ResponseBody
    public String getImageByRowKey(
            @RequestParam(value = "rowkey", defaultValue = "") String rowkey) throws Exception {
        return rowkey;
    }
}