package com.bupt.videometadata.hbase;

import io.leopard.javahost.JavaHost;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
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
        hbaseconf.set("hbase.zookeeper.quorum", "10.103.249.191,10.103.240.42");
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

    public static void upload(String tableName) throws Exception {
        Table table = connection.getTable(TableName.valueOf(tableName));
        //// TODO: 2016/10/14 这里应该有一个rowkey的生成策略，目前只是简单做
        Put put = new Put(Bytes.toBytes(8));
        System.out.println(new Date());
        put.addColumn(Bytes.toBytes("video"), Bytes.toBytes("vid"), HbaseController.getSource(new File("d://test.mp4")));
        table.put(put);
        System.out.println(new Date());
    }

    public static void download(String tableName) throws Exception {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get1 = new Get(Bytes.toBytes(11));
        Result result = table.get(get1);
        result.getExists();
//        result.getValue(Bytes.toBytes("video"), Bytes.toBytes("vid"));
        System.out.println(result);


    }

    public static void main(String[] args) throws Exception {
//        HbaseController.create("cjtest2");
//        HbaseController.upload("cjtest2");
        System.out.println(new Date());
        HbaseController.download("cjtest2");
        System.out.println(new Date());

    }
}

