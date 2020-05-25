package com.ch.util;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class Constant {


    public static String HOST = "192.168.1.90";
    public static int PORT = 502;


    public static int getPage(Integer page) {
        page=(page-1)*10;
        return page;
    }

    /**
           * 两个double类型的数相除，保留两位小数
           * @param a
          * @param b
          * @param scale
           * @return
     */
     public static double divide(double a, double b, int scale){
                 BigDecimal bd1 = new BigDecimal(Double.toString(a));
                 BigDecimal bd2 = new BigDecimal(Double.toString(b));
                 return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
      }

    public static void main(String[] args) throws IOException{

         String key="11111111";
        //把2进制字符串key，转成10进制keys
         int  keys = Integer.parseInt(key, 2);//数字2代表进制，可以是各种进制，转换成10进制
        //把10进制keys转成16进制result，toUpperCase()是把小写字母转换成大写字母
        String result= Integer.toHexString(keys).toUpperCase();

        //result 输出结果位  FF

//        String encoded = Base64.getEncoder().encodeToString(result.getBytes());
        byte[] decoded = Base64.getDecoder().decode(result);
        //System.out.println( new String(decoded) );
        System.out.println(new String(decoded)+"---------------");
        byte[] data = {0x00,0x00,0x00,0x00,0x00,0x06,0x01,0x06,0x00,0x05,0x00,(byte)0x01};



    }


    public static byte[] conver16HexToByte(String hex16Str) {
        char[] c = hex16Str.toCharArray();
        byte[] b = new byte[c.length / 2];
        for (int i = 0; i < b.length; i++) {
            int pos = i * 2;
            b[i] = (byte) ("0123456789ABCDEF".indexOf(c[pos]) << 4 | "0123456789ABCDEF".indexOf(c[pos + 1]));
        }
        return b;
    }



public static byte[] addBytes(byte[] data1, byte[] data2){

			byte[]data3=new byte[data1.length + data2.length];
			System.arraycopy(data1,0,data3,0,data1.length);
			System.arraycopy(data2,0,data3,data1.length,data2.length);

			return data3;
    }

    public static void socketSend(String str) throws IOException {
        //客户端发数据到服务端
        /*
         * Tcp传输，客户端建立的过程。
         * 1，创建tcp客户端socket服务。使用的是Socket对象。
         *   建议该对象一创建就明确目的地。要连接的主机。
         * 2，如果连接建立成功，说明数据传输通道已建立。
         *   该通道就是socket流 ,是底层建立好的。 既然是流，说明这里既有输入，又有输出。
         *   想要输入或者输出流对象，可以找Socket来获取。
         *   可以通过getOutputStream(),和getInputStream()来获取两个字节流。
         * 3，使用输出流，将数据写出。
         * 4，关闭资源。
         */
        //创建客户端socket服务。
        //连接目标服务器的地址，192.168.1.100是目标服务器的地址，10002是目标服务器的端口
        Socket socket = new Socket("192.168.1.195",502);
        //获取socket流中的输出流。 向服务器输出消息，即发消息到服务器
        OutputStream out = socket.getOutputStream();
        //使用输出流将指定的数据写出去。
        String result= Integer.toHexString(Integer.parseInt(str, 2)).toUpperCase();
        byte[] bts=new byte[12];
        bts[0]=0x00;
        bts[1]= 0x00;
        bts[2]= 0x00;
        bts[3]= 0x00;
        bts[4]= 0x00;
        bts[5]=0x06;
        bts[6]=0x01;
        bts[7]=0x06;
        bts[8]=0x00;
        bts[9]=0x05;
        bts[10]=0x00;
        bts[11]=(byte)hextobyte(result);
        out.write(bts);
        socket.close();

    }

    public static void socketSend1(String str) throws IOException {
        //客户端发数据到服务端
        /*
         * Tcp传输，客户端建立的过程。
         * 1，创建tcp客户端socket服务。使用的是Socket对象。
         *   建议该对象一创建就明确目的地。要连接的主机。
         * 2，如果连接建立成功，说明数据传输通道已建立。
         *   该通道就是socket流 ,是底层建立好的。 既然是流，说明这里既有输入，又有输出。
         *   想要输入或者输出流对象，可以找Socket来获取。
         *   可以通过getOutputStream(),和getInputStream()来获取两个字节流。
         * 3，使用输出流，将数据写出。
         * 4，关闭资源。
         */
        //创建客户端socket服务。
        //连接目标服务器的地址，192.168.1.100是目标服务器的地址，10002是目标服务器的端口
        Socket socket = new Socket("192.168.1.100",502);
        //获取socket流中的输出流。 向服务器输出消息，即发消息到服务器
        OutputStream out = socket.getOutputStream();
        //使用输出流将指定的数据写出去。
        String result= Integer.toHexString(Integer.parseInt(str, 2)).toUpperCase();
        //System.out.println(result+"---------------------");
        byte[] bts1=new byte[12];
        bts1[0]=0x00;
        bts1[1]= 0x00;
        bts1[2]= 0x00;
        bts1[3]= 0x00;
        bts1[4]= 0x00;
        bts1[5]=0x06;
        bts1[6]=0x01;
        bts1[7]=0x06;
        bts1[8]=0x00;
        bts1[9]=0x06;
        bts1[10]=0x00;
        bts1[11]=(byte)hextobyte(result);
        out.write(bts1);
        socket.close();
    }

    public static byte hextobyte(String in) {
        return (byte) Integer.parseInt(in, 16);
    }
}
