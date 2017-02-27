package com.bupt.videometadata.hbase;

import io.leopard.javahost.JavaHost;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.io.ByteBufferInputStream;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Properties;

/**
 * @author Che Jin <jotline@github>
 */
public class HbaseController {
    private static Configuration hbaseconf;
    private static Connection connection;

    private static void loadDns() throws IOException {
        Resource resource = new ClassPathResource("/dns.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        JavaHost.updateVirtualDns(props);
    }

    static {

        hbaseconf = HBaseConfiguration.create();
        hbaseconf.set("hbase.zookeeper.property.clientPort", "2181");
        hbaseconf.set("hbase.zookeeper.quorum", "10.103.249.191,10.103.241.87");
        hbaseconf.set("hbase.master", "hdfs://10.103.249.190:60000");
        hbaseconf.set("hbase.client.keyvalue.maxsize", "524288000");
        hbaseconf.set("hbase.rpc.timeout", "6000000");
        try {
            loadDns();
            connection = ConnectionFactory.createConnection(hbaseconf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //// TODO: 2016/10/14 需要更改这里的方法，只是测试
    public static void create(String tableName) throws Exception {
        Admin admin = connection.getAdmin();
        TableName tableName1 = TableName.valueOf(tableName);
        HTableDescriptor tableDescriptor = new HTableDescriptor(tableName1);
        tableDescriptor.addFamily(new HColumnDescriptor("video"));
        tableDescriptor.addFamily(new HColumnDescriptor("picture"));
        tableDescriptor.addFamily(new HColumnDescriptor("text"));
        admin.createTable(tableDescriptor);
    }


    public static byte[] getSource(File file) throws IOException {
        int bufLength = (int) file.length();

        FileInputStream in = new FileInputStream(file);
        byte[] buf = new byte[bufLength];
        int readLen;
        ByteArrayOutputStream byout = new ByteArrayOutputStream();
        while ((readLen = in.read(buf, 0, bufLength)) > 0) {
            byout.write(buf, 0, readLen);
        }
        in.close();
        byte[] byteContent = byout.toByteArray();
        return byteContent;
    }

    public static void upload(String tableName, String path, int rowkey) throws Exception {
        Table table = connection.getTable(TableName.valueOf(tableName));
        //// TODO: 2016/10/14 这里应该有一个rowkey的生成策略，目前只是简单做
        Put put = new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes("video"), Bytes.toBytes("vid"), HbaseController.getSource(new File(path)));
        table.put(put);
    }

    public static void upload(String tableName, File file, int rowkey) throws Exception {
        Table table = connection.getTable(TableName.valueOf(tableName));
        //// TODO: 2016/10/14 这里应该有一个rowkey的生成策略，目前只是简单做
        Put put = new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes("video"), Bytes.toBytes("vid"), HbaseController.getSource(file));
        table.put(put);
    }

    public static void upload(String tableName, byte[] bytes, int rowkey) throws Exception {
        Table table = connection.getTable(TableName.valueOf(tableName));
        //// TODO: 2016/10/14 这里应该有一个rowkey的生成策略，目前只是简单做
        Put put = new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes("video"), Bytes.toBytes("vid"), bytes);
        table.put(put);
    }

    public static String  upload(String tableName,byte[]bytes,long starttime,long endtime,String geohash) throws IOException {
        String result=geohash+starttime+endtime;
        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(result));
        put.addColumn(Bytes.toBytes("video"), Bytes.toBytes("vid"), bytes);
        table.put(put);
        return result;
    }

    public static byte[] getImageByRowKey(String tableName, int rowkey) throws Exception {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get1 = new Get(Bytes.toBytes(rowkey));
        Result result = table.get(get1);
        byte[] resultb = null;
        for (Cell cell : result.rawCells()) {
            resultb = CellUtil.cloneValue(cell);
        }
        return resultb;
    }

    public static byte[] getImageByRowKey(String tableName, String rowkey) throws Exception {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get1 = new Get(Bytes.toBytes(rowkey));
        Result result = table.get(get1);
        byte[] resultb = null;
        for (Cell cell : result.rawCells()) {
            resultb = CellUtil.cloneValue(cell);
        }
        return resultb;
    }

    public static void getVideoByRowKey(String tableName, int rowkey, ServletOutputStream outputStream) throws Exception {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get1 = new Get(Bytes.toBytes(rowkey));
        Result result = table.get(get1);
        for (Cell cell : result.rawCells()) {
                outputStream.write(CellUtil.cloneValue(cell));
        }
    }

    public static void scan(String tableName) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.setMaxVersions();
        scan.setBatch(1000);
        scan.setFilter(new FirstKeyOnlyFilter());
        ResultScanner resultScanner = table.getScanner(scan);
        int rowCount = 0;
        for (Result result : resultScanner) {
            rowCount += result.size();
            if (rowCount % 1000 == 0) System.out.println(rowCount);
        }

        System.out.println(rowCount);
        resultScanner.close();
    }

    public static void main(String[] args) throws Exception {
//        HbaseController.create("video");
//        System.out.println(new Date());
//        HbaseController.upload("video",new File("D://little.mp4"),1);
//        HbaseController.upload("video",new File("D://90M.mp4"),1);
//        System.out.println(new Date());
    }

}

