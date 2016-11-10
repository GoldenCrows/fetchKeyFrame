package com.bupt.videometadata;

import com.bupt.videometadata.util.DateUtil;
import org.junit.Test;

/**
 * @author Che Jin <jotline@github>
 */
public class TestDate {
    @Test
    public void testDate() throws Exception {
        System.out.println(DateUtil.transfer("2015-1-1 0:0:0"));
        System.out.println(DateUtil.transfer("2015-12-1 00:00:00"));
    }
}
