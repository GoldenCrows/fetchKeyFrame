package com.bupt.videometadata.util;

import com.bupt.videometadata.hbase.HbaseController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Che Jin <jotline@github>
 */
public class ZipUtil {
    public static void ZipMultiFile(List<Integer> rowkeys, OutputStream outputStream) {
        try {
            ZipOutputStream zipOut = new ZipOutputStream(outputStream);
            for(Integer rowkey:rowkeys){
                zipOut.putNextEntry(new ZipEntry(rowkey+".jpg"));
                zipOut.write(HbaseController.getImageByRowKey("cjtest2",rowkey));
            }
            zipOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
