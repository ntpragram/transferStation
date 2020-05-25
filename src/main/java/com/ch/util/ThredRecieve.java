package com.ch.util;

import java.io.*;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;
import com.serotonin.util.queue.ByteQueue;
import onbon.bx05.Bx5GEnv;

public class ThredRecieve {


    public static byte hextobyte(String in) {
        return (byte) Integer.parseInt(in, 16);

    }

    public static void main(String[] args) throws Exception {

        String str="01234567";//00010000

        System.out.println(str.substring(7,8));

       /* Bx5GEnv.initial("log.properties",30000);
        try {
            TrafficLightsUtil.SendDynamic("192.168.1.20",5005,"更换容器");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    /*   Socket socket = new Socket("192.168.1.12",502);
        //获取socket流中的输出流。 向服务器输出消息，即发消息到服务器
        OutputStream out = socket.getOutputStream();
        InputStream is=socket.getInputStream();
        byte[] data=new byte[100];//数据组
        byte[] bts1=new byte[12];
        bts1[0]=0x00;
        bts1[1]= 0x00;
        bts1[2]= 0x00;
        bts1[3]= 0x00;
        bts1[4]= 0x00;
        bts1[5]=0x06;
        bts1[6]=0x01;
        bts1[7]=0x03;
        bts1[8]=0x00;
        bts1[9]=0x00;
        bts1[10]=0x00;
        bts1[11]=0x05;
        out.write(bts1);
        out.flush();
        byte[] bs = new byte[32];
        is.read(bs);
        String addr1=toBinary(bs[10],8);//对应  0000地址
        String addr2=toBinary(bs[12],8);//对应0001
        String addr3=toBinary(bs[14],8);//对应0002

        // 第五位 左侧换容器指示 addr1.substring(3,4)



        //右侧换容器指示  addr2.substring(3,4)


        //左侧激活指示   addr1.substring(2,3)


        //右侧激活指示   addr2.substring(2,3)


        //左侧上红外线  addr2.substring(1,2)


        //左侧上红外线  addr3.substring(1,2)*/

    }

    public static String toBinary(int num, int digits) {
        String cover = Integer.toBinaryString(1 << digits).substring(1);
        String s = Integer.toBinaryString(num);
        return s.length() < digits ? cover.substring(s.length()) + s : s;
    }
    private static byte toByte(char c) {
        byte b = (byte) "0123456789abcdef".indexOf(c);
        return b;
    }
    public static byte[] hexStr2BinArr(String hexStr){
        //hexString的长度对2取整，作为bytes的长度
        int len = hexStr.length()/2;
        byte[] bytes = new byte[len];
        byte high = 0;//字节高四位
        byte low = 0;//字节低四位
        for(int i=0;i<len;i++){
            //右移四位得到高位
            high = (byte)((hexStr.indexOf(hexStr.charAt(2*i)))<<4);
            low = (byte)hexStr.indexOf(hexStr.charAt(2*i+1));
            bytes[i] = (byte) (high|low);//高地位做或运算
        }
        return bytes;
    }


    private static String byte2hex(byte [] buffer){
        String h = "";

        for(int i = 0; i < buffer.length; i++){
            String temp = Integer.toHexString(buffer[i] & 0xFF);
            if(temp.length() == 1){
                temp = "0" + temp;
            }
            h = h + " "+ temp;
        }

        return h;

    }


    public static byte[] to_byte(String[] strs) {
        byte[] bytes=new byte[strs.length];
        for (int i=0; i<strs.length; i++) {
            bytes[i]=Byte.parseByte(strs[i]);
        }
        return bytes;
    }

}
