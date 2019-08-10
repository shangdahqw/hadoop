package com.sd.hqw;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;

public class HdfsDemo {

  private static String SOURCE_PATH = "/Users/huangqiangwen/Desktop/tips.txt";
  private static String DEST_PATH = "/test/mk222222.txt";
  private static String MASTER_URI = "hdfs://39.108.184.144:9000";

  /** 测试上传文件 */
  @Test
  public void testUpload() throws Exception {

    Configuration conf = new Configuration();
    // System.setProperty("HADOOP_USER_NAME","root");
    conf.set("dfs.client.use.datanode.hostname", "true");

    FileSystem fs = FileSystem.get(new URI(MASTER_URI), conf);
    InputStream in = new FileInputStream(SOURCE_PATH);
    OutputStream out = fs.create(new Path(DEST_PATH));
    IOUtils.copyBytes(in, out, 1024, true);
    fs.close();

    ArrayList arrayList = new ArrayList();
  }

  /**
   * 测试下载文件
   *
   * @throws Exception
   */
  @Test
  public void testDownload() throws Exception {
    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.get(new URI(MASTER_URI), conf);
    //		InputStream in = new FileInputStream("e:\\hadoop.avi");
    //		OutputStream out = fs.create(new Path("/demo/b.avi"));
    InputStream in = fs.open(new Path(DEST_PATH));
    OutputStream out = new FileOutputStream("/Users/huangqiangwen/Desktop/tips2.txt");
    IOUtils.copyBytes(in, out, conf);
  }

  /** 复制文件到远程文件系统，也可以看做是上传 */
  public static void copyFile(Configuration conf, String uri, String local, String remote)
      throws IOException {
    FileSystem fs = FileSystem.get(URI.create(uri), conf);
    fs.copyFromLocalFile(new Path(local), new Path(remote));
    System.out.println("copy from: " + local + " to " + remote);
    fs.close();
  }

  @Test
  public void testCopyFile() throws IOException {
    // 使用命令：hadoop fs -ls  /hbase/upload， 可以查看复制到/hbase/upload目录下的文件
    copyFile(new Configuration(), MASTER_URI, SOURCE_PATH, "/test/tips.txt");
  }

  /** 在hdfs文件系统下创建目录 */
  public static void makeDir(Configuration conf, String uri, String remoteFile) throws IOException {
    FileSystem fs = FileSystem.get(URI.create(uri), conf);
    // System.setProperty("HADOOP_USER_NAME","root");

    Path path = new Path(remoteFile);

    fs.mkdirs(path);
    System.out.println("创建文件夹" + remoteFile);
  }

  @Test
  public void testMakeDir() throws IOException {
    makeDir(new Configuration(), MASTER_URI, "/test/upload/");
  }

  /** 删除文件 */
  public static void delete(Configuration conf, String uri, String filePath) throws IOException {
    Path path = new Path(filePath);
    FileSystem fs = FileSystem.get(URI.create(uri), conf);
    fs.deleteOnExit(path);
    System.out.println("Delete: " + filePath);
    fs.close();
  }

  @Test
  public void testDelete() throws IOException {
    delete(new Configuration(), MASTER_URI, "/test/tips.txt");
  }

  /** 查看文件内容 */
  public static void cat(Configuration conf, String uri, String remoteFile) throws IOException {
    Path path = new Path(remoteFile);
    FileSystem fs = FileSystem.get(URI.create(uri), conf);
    FSDataInputStream fsdis = null;
    System.out.println("cat: " + remoteFile);
    try {
      fsdis = fs.open(path);
      IOUtils.copyBytes(fsdis, System.out, 4096, false);
    } finally {
      IOUtils.closeStream(fsdis);
      fs.close();
    }
  }

  @Test
  public void testCat() throws IOException {
    // hbase/hbase.version 这个文件已经存在
    cat(new Configuration(), MASTER_URI, "/test/mk.txt");
  }

  /** 查看目录下面的文件 */
  public static void ls(Configuration conf, String uri, String folder) throws IOException {
    Path path = new Path(folder);
    FileSystem fs = FileSystem.get(URI.create(uri), conf);
    FileStatus[] list = fs.listStatus(path);
    System.out.println("ls: " + folder);
    System.out.println("==========================================================");
    for (FileStatus f : list) {
      System.out.printf(
          "name: %s, folder: %s, size: %d\n", f.getPath(), f.isDirectory(), f.getLen());
    }
    System.out.println("==========================================================");
    fs.close();
  }

  @Test
  public void testLs() throws IOException {
    ls(new Configuration(), MASTER_URI, "/hbase/upload");
  }
}
