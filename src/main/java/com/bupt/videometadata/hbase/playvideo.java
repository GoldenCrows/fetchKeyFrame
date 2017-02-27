//package com.bupt.videometadata.hbase;
//
//import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
//import org.apache.hadoop.io.IOUtils;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//
//public class MyStreamServlet extends HttpServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        // TODO Auto-generated method stub
//        doPost(req, resp);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        // TODO Auto-generated method stub
//        try {
//            preview(req, resp);
//        } catch (ClassNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }
//
//    public void preview(HttpServletRequest req, HttpServletResponse resp) throws Exception, IOException {
//
//        byte[] bytes = HbaseController.getImageByRowKey("video", 1);
//
//        InputStream in = new ByteInputStream(bytes, bytes.length);
//        int fileLen = bytes.length;
//        String range = req.getHeader("Range");
//        resp.setHeader("Content-type", "video/mp4");
//        OutputStream out = resp.getOutputStream();
//        if (range == null) {
//            resp.setHeader("Content-Disposition", "attachment; filename=video");
//            resp.setContentType("application/octet-stream");
//            resp.setContentLength(fileLen);
//            IOUtils.copyBytes(in, out, fileLen, false);
//        } else {
//            long start = Integer.valueOf(range.substring(range.indexOf("=") + 1, range.indexOf("-")));
//            long count = fileLen - start;
//            long end;
//            if (range.endsWith("-"))
//                end = fileLen - 1;
//            else
//                end = Integer.valueOf(range.substring(range.indexOf("-") + 1));
//            String ContentRange = "bytes " + String.valueOf(start) + "-" + end + "/" + String.valueOf(fileLen);
//            resp.setStatus(206);
//            resp.setContentType("video/mpeg4");
//            resp.setHeader("Content-Range", ContentRange);
//            in.skip(start);
//            try {
//                IOUtils.copyBytes(in, out, count, false);
//            } catch (Exception e) {
//                throw e;
//            }
//        }
//        in.close();
//        in = null;
//        out.close();
//        out = null;
//    }
//}
//
