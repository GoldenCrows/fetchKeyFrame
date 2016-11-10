import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_videoio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;
import static org.bytedeco.javacpp.opencv_videoio.*;

/**
 * @author Che Jin <jotline@github>
 */
public class testTime {
    private static FileSystem fs;

    static {
        Configuration conf = new Configuration();
        try {
            fs = FileSystem.get(URI.create("hdfs://10.103.249.190:9000/"), conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static void main(String[] args) throws Exception{
        Long x1 = new Date().getTime();
        System.out.println();
        testTime.downloadVedio("/cj/avi.avi","/home/cj/avi.avi");
        opencv_videoio.CvCapture cvCapture = cvCreateFileCapture("d:/avi.avi");
        cvSetCaptureProperty(cvCapture, CV_CAP_PROP_POS_MSEC, 100000);
        Long x2 = new Date().getTime();

        System.out.println(x2 - x1);

        opencv_core.IplImage frammeImage = cvQueryFrame(cvCapture);
        //cvShowImage( "Trackbar", frammeImage);
        Long x3 = new Date().getTime();

        System.out.println(x3 - x2);
        cvSaveImage("d://aaa" + new Date().getTime()+ ".jpg", frammeImage);

    }


}
