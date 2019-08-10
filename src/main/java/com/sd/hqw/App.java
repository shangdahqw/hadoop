package com.sd.hqw;

import java.util.StringTokenizer;

/** Hello world! */
public class App {
  public static void main(String[] args) {
    System.out.println("Hello World!");

    String s = new String("The=Java=platform=is=the=ideal=platform=for=network=computing");
    // 分词器构造函数三个参数，第一个是待分隔的字符串，第二个为分隔字符串，以字符为分隔单位（比如the，可能匹配到e，就会分隔），
    // 第三个参数说明是否要把分割字符串作为标记返回
    StringTokenizer st = new StringTokenizer(s, "the", true);
    System.out.println("Token Total:" + st.countTokens());
    while (st.hasMoreElements()) {
      System.out.println(st.nextToken());
    }
  }
}
