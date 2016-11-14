package com.bupt.videometadata.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;

/**
 * @author Che Jin <jotline@github>
 */
//封装了操作hdfs的操作
public class HdfsController {
    private static FileSystem fs;

    static {
        Configuration conf = new Configuration();
        try {
            fs = FileSystem.get(URI.create("hdfs://10.103.249.190:9000/"), conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ls(String rootPath) throws Exception {
        RemoteIterator<LocatedFileStatus> rmIterator = fs.listLocatedStatus(new Path(rootPath));
        while (rmIterator.hasNext()) {
            Path path = rmIterator.next().getPath();
            if (fs.isDirectory(path)) {
                System.out.println("-----------DirectoryName: " + path.getName());
            } else if (fs.isFile(path)) {
                System.out.println("-----------FileName: " + path.getName());
            }
        }
    }

    public static void uploadVedio(String filepath, String dstpath,String filename) throws Exception {
        String dstfile = dstpath + "/" + filename;
        FSDataOutputStream out = fs.create(new Path(dstfile), true);
        FileInputStream in = new FileInputStream(filepath);
        byte[] buf = new byte[1024];
        int readbytes = 0;
        while ((readbytes = in.read(buf)) > 0) {
            out.write(buf, 0, readbytes);
        }
        in.close();
        out.close();
    }

    public static void uploadVedio(InputStream in, String dstpath, String filename)throws Exception{
        String dstfile = dstpath + "/" + filename;
        FSDataOutputStream out = fs.create(new Path(dstfile), true);
        byte[] buf = new byte[1024];
        int readbytes = 0;
        while ((readbytes = in.read(buf)) > 0) {
            out.write(buf, 0, readbytes);
        }
        in.close();
        out.close();
    }

    public static void downloadVedio(String hdfsfile, String localpath) throws Exception {
        Long start = new Date().getTime();
        String[] args = hdfsfile.split("/");
        String dstfile = localpath + "/" + args[args.length - 1];
        FSDataInputStream in = fs.open(new Path(hdfsfile));

        FileOutputStream out = new FileOutputStream(dstfile, true);

        byte[] buf = new byte[1024];
        int readbytes = 0;
        while ((readbytes = in.read(buf)) > 0) {
            out.write(buf, 0, readbytes);
        }

        Long end = new Date().getTime();
        System.out.println("uploadDone time :" + (end - start));
        in.close();
        out.close();
    }



}


