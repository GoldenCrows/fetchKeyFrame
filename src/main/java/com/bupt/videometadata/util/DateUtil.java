package com.bupt.videometadata.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Che Jin <jotline@github>
 */
public class DateUtil {
    //输入一个日期，返回一个Long，格式"yyyy-MM-dd HH:mm:ss"
    public static Long transfer(String date) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.parse(date).getTime()/1000;
    }
    public static String transfer(Long date)throws Exception{
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(new Date(date*1000));
    }
}
