package com.ch.util;

import com.ch.dao.master.BerthMapper;
import com.ch.dataobject.master.Berth;
import com.ch.service.BerthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.List;
@Component
public class BerthConstant {

    @Autowired
    private BerthService berthService;


    private static BerthService service;
    DecimalFormat df = new DecimalFormat("0.00");

    public @PostConstruct
    void init(){
        service= berthService;
    }

    public void sendAreciveAll(String ip, int port, int berthId1,int berthId2) throws IOException {
        Socket socket = new Socket(ip,502);
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

        System.out.println(addr1+"--------"+addr2+"----------"+addr3+"----------------------12");
        /*
       String addr1="00000000";
        String addr2="00000000";
        String addr3="00000000";*/

        Berth berth1=service.findById(berthId1);
        Berth berth2=service.findById(berthId2);

        // 左侧换容器指示 addr1.substring(3,4)
        if(addr1.substring(3,4).equals("1")){
            //左侧更换容器 泊位id  红绿灯状态 type  泊位状态 status   泊位内容led
            boolean isOk=service.updateTypeOrContentAndStatus(berthId1,1,2,"更换容器");
        }else{
            //获取泊位信息
            if(berth1.getStatus()==2){
                Double b1=berth1.getBerthtotalcapacity()-berth1.getBerthusecapacity();
                String msg="剩余"+df.format(b1/1000)+"吨";
                boolean isOk=service.updateTypeOrContentAndStatus(berthId1,0,0,msg);
            }
        }
        //右侧换容器指示  addr2.substring(3,4)
        if(addr2.substring(3,4).equals("1")){
            //左侧更换容器 泊位id  红绿灯状态 type  泊位状态 status   泊位内容led
            boolean isOk=service.updateTypeOrContentAndStatus(berthId2,1,2,"更换容器");
        }else{
            //获取泊位信息
            if(berth2.getStatus()==2) {
                Double b1 = berth2.getBerthtotalcapacity() - berth2.getBerthusecapacity();
                String msg = "剩余" + df.format(b1 / 1000) + "吨";
                boolean isOk = service.updateTypeOrContentAndStatus(berthId2, 0, 0, msg);
            }
        }

        //左侧激活指示   addr1.substring(2,3)
        if(addr1.substring(4,5).equals("1")){
            //边门动作开
            if(addr1.substring(7,8).equals("1")){
                //更新边门动作
                boolean isOk=service.updateOnoff(berthId1,1);
            }else{
                boolean isOk=service.updateOnoff(berthId1,0);
            }
            //溜槽动作开 5，6
            if(addr1.substring(5,6).equals("1")){
                //更新溜槽动作
                boolean isOk=service.updateDownspouting(berthId1,1);
            }else{
                boolean isOk=service.updateDownspouting(berthId1,0);
            }
        }else{
            service.updateOnoff(berthId1,0);
            service.updateDownspouting(berthId1,0);
        }
        //右侧激活指示   addr2.substring(4,5)
        if(addr2.substring(4,5).equals("1")){
            //边门动作开
            if(addr1.substring(7,8).equals("1")){
                boolean isOk=service.updateOnoff(berthId2,1);
            }else{
                boolean isOk=service.updateOnoff(berthId2,0);
            }
            //溜槽动作开 5，6
            if(addr1.substring(5,6).equals("1")){
                boolean isOk=service.updateDownspouting(berthId2,1);
            }else{
                boolean isOk=service.updateDownspouting(berthId2,0);
            }
        }else{
            service.updateOnoff(2,0);
            service.updateDownspouting(2,0);
        }
        //左侧上红外线  addr2.substring(1,2)
        if(addr2.substring(5,6).equals("1")){
            if(berth1.getStatus()==0){
                boolean isOk=service.updateTypeOrContentAndStatus(berthId1,1,0,"工作中");
               /* if(berth1.getBerthwaitnumber()>0){
                    boolean isOk1=service.updateWaitNumber(berthId1);
                }*/
            }
        }else{
            if(berth1.getStatus()==0) {
                if(berth1.getType()==1){
                    if(berth1.getBerthwaitnumber()>0){
                        boolean isOk1=service.updateWaitNumber(berthId1);
                    }
                }
                Double b1 = berth1.getBerthtotalcapacity() - berth1.getBerthusecapacity();
                String msg = "剩余" + df.format(b1 / 1000) + "吨";
                boolean isOk = service.updateTypeOrContentAndStatus(berthId1, 0, 0, msg);
            }
        }
        //右侧上红外线  addr3.substring(1,2)
        if(addr3.substring(5,6).equals("1")){
            if(berth2.getStatus()==0){
                boolean isOk=service.updateTypeOrContentAndStatus(berthId2,1,0,"工作中");
               /* if(berth2.getBerthwaitnumber()>0){
                    boolean isOk1=service.updateWaitNumber(berthId2);
                }*/
            }
        }else{
            if(berth2.getStatus()==0) {
                if(berth2.getType()==1){
                    if(berth2.getBerthwaitnumber()>0){
                        boolean isOk1=service.updateWaitNumber(berthId2);
                    }
                }
                Double b1 = berth2.getBerthtotalcapacity() - berth2.getBerthusecapacity();
                String msg = "剩余" + df.format(b1 / 1000) + "吨";
                boolean isOk = service.updateTypeOrContentAndStatus(berthId2, 0, 0, msg);
            }
        }
        socket.close();

    }







    public void sendArecive12(String ip, int port, int berth) throws IOException {
        Socket socket = new Socket("192.168.1.12",502);
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

        System.out.println(addr1+"--------"+addr2+"----------"+addr3+"----------------------12");
        /*
       String addr1="00000000";
        String addr2="00000000";
        String addr3="00000000";*/

        Berth berth1=service.findById(1);
        Berth berth2=service.findById(2);

        // 左侧换容器指示 addr1.substring(3,4)
        if(addr1.substring(3,4).equals("1")){
            //左侧更换容器 泊位id  红绿灯状态 type  泊位状态 status   泊位内容led
            boolean isOk=service.updateTypeOrContentAndStatus(1,1,2,"更换容器");
        }else{
            //获取泊位信息
            if(berth1.getStatus()==2){
                Double b1=berth1.getBerthtotalcapacity()-berth1.getBerthusecapacity();
                String msg="剩余"+df.format(b1/1000)+"吨";
                boolean isOk=service.updateTypeOrContentAndStatus(1,0,0,msg);
            }
        }
        //右侧换容器指示  addr2.substring(3,4)
        if(addr2.substring(3,4).equals("1")){
            //左侧更换容器 泊位id  红绿灯状态 type  泊位状态 status   泊位内容led
            boolean isOk=service.updateTypeOrContentAndStatus(2,1,2,"更换容器");
        }else{
            //获取泊位信息
            if(berth2.getStatus()==2) {
                Double b1 = berth2.getBerthtotalcapacity() - berth2.getBerthusecapacity();
                String msg = "剩余" + df.format(b1 / 1000) + "吨";
                boolean isOk = service.updateTypeOrContentAndStatus(2, 0, 0, msg);
            }
        }

        //左侧激活指示   addr1.substring(2,3)
        if(addr1.substring(4,5).equals("1")){
            //边门动作开
            if(addr1.substring(7,8).equals("1")){
                //更新边门动作
                boolean isOk=service.updateOnoff(1,1);
            }else{
                boolean isOk=service.updateOnoff(1,0);
            }
            //溜槽动作开 5，6
            if(addr1.substring(5,6).equals("1")){
                //更新溜槽动作
                boolean isOk=service.updateDownspouting(1,1);
            }else{
                boolean isOk=service.updateDownspouting(1,0);
            }
        }else{
            service.updateOnoff(1,0);
            service.updateDownspouting(1,0);
        }
        //右侧激活指示   addr2.substring(4,5)
        if(addr2.substring(4,5).equals("1")){
            //边门动作开
            if(addr1.substring(7,8).equals("1")){

                boolean isOk=service.updateOnoff(2,1);
            }else{
                boolean isOk=service.updateOnoff(2,0);
            }
            //溜槽动作开 5，6
            if(addr1.substring(5,6).equals("1")){
                boolean isOk=service.updateDownspouting(2,1);
            }else{
                boolean isOk=service.updateDownspouting(2,0);
            }
        }else{
            service.updateOnoff(2,0);
            service.updateDownspouting(2,0);
        }
        //左侧上红外线  addr2.substring(1,2)
        if(addr2.substring(5,6).equals("1")){
            if(berth1.getStatus()==0){
                boolean isOk=service.updateTypeOrContentAndStatus(1,1,0,"工作中");
                if(berth1.getBerthwaitnumber()>0){
                    boolean isOk1=service.updateWaitNumber(1);
                }
            }
        }
        //右侧上红外线  addr3.substring(1,2)
        if(addr3.substring(5,6).equals("1")){
            if(berth2.getStatus()==0){
                boolean isOk=service.updateTypeOrContentAndStatus(2,1,0,"工作中");
                if(berth2.getBerthwaitnumber()>0){
                    boolean isOk1=service.updateWaitNumber(2);
                }
            }
        }
        socket.close();

    }

    public void sendArecive34(String ip, int port, int berth) throws IOException {
        Socket socket = new Socket("192.168.1.34",502);
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
        System.out.println(addr1+"--------"+addr2+"----------"+addr3+"----------------------34");
        // System.out.println(addr1+"--------"+addr2+"----------"+addr3);
        /*
       String addr1="00000000";
        String addr2="00000000";
        String addr3="00000000";*/

        Berth berth1=service.findById(3);
        Berth berth2=service.findById(4);

        // 左侧换容器指示 addr1.substring(3,4)
        if(addr1.substring(3,4).equals("1")){
            //左侧更换容器 泊位id  红绿灯状态 type  泊位状态 status   泊位内容led
            boolean isOk=service.updateTypeOrContentAndStatus(3,1,2,"更换容器");
        }else{

            if(berth1.getStatus()==2) {
                //获取泊位信息
                Double b1 = berth1.getBerthtotalcapacity() - berth1.getBerthusecapacity();
                String msg = "剩余" + df.format(b1 / 1000) + "吨";
                boolean isOk = service.updateTypeOrContentAndStatus(3, 0, 0, msg);
            }


        }
        //右侧换容器指示  addr2.substring(3,4)
        if(addr2.substring(3,4).equals("1")){
            //左侧更换容器 泊位id  红绿灯状态 type  泊位状态 status   泊位内容led
            boolean isOk=service.updateTypeOrContentAndStatus(4,1,2,"更换容器");
        }else{
            //获取泊位信息
            if(berth2.getStatus()==2) {
                Double b1 = berth2.getBerthtotalcapacity() - berth2.getBerthusecapacity();
                String msg = "剩余" + df.format(b1 / 1000) + "吨";
                boolean isOk = service.updateTypeOrContentAndStatus(4, 0, 0, msg);
            }
        }

        //左侧激活指示   addr1.substring(2,3)
        if(addr1.substring(4,5).equals("1")){
            //边门动作开
            if(addr1.substring(7,8).equals("1")){
                //更新边门动作
                boolean isOk=service.updateOnoff(3,1);
            }else{
                boolean isOk=service.updateOnoff(3,0);
            }
            //溜槽动作开 5，6
            if(addr1.substring(5,6).equals("1")){
                //更新溜槽动作
                boolean isOk=service.updateDownspouting(3,1);
            }else{
                boolean isOk=service.updateDownspouting(3,0);
            }
        }else{
            service.updateOnoff(3,0);
            service.updateDownspouting(3,0);
        }
        //右侧激活指示   addr2.substring(4,5)
        if(addr2.substring(4,5).equals("1")){
            //边门动作开
            if(addr1.substring(7,8).equals("1")){

                boolean isOk=service.updateOnoff(4,1);
            }else{
                boolean isOk=service.updateOnoff(4,0);
            }
            //溜槽动作开 5，6
            if(addr1.substring(5,6).equals("1")){
                boolean isOk=service.updateDownspouting(4,1);
            }else{
                boolean isOk=service.updateDownspouting(4,0);
            }
        }else{
            service.updateOnoff(4,0);
            service.updateDownspouting(4,0);
        }
        //左侧上红外线  addr2.substring(1,2)
        if(addr2.substring(5,6).equals("1")){
            if(berth1.getStatus()==0){
                boolean isOk=service.updateTypeOrContentAndStatus(3,1,0,"工作中");
                if(berth1.getBerthwaitnumber()>0){
                    boolean isOk1=service.updateWaitNumber(3);
                }
            }
        }
        //右侧上红外线  addr3.substring(1,2)
        if(addr3.substring(5,6).equals("1")){
            if(berth2.getStatus()==0){
                boolean isOk=service.updateTypeOrContentAndStatus(4,1,0,"工作中");
                if(berth2.getBerthwaitnumber()>0){
                    boolean isOk1=service.updateWaitNumber(4);
                }
            }
        }
        socket.close();

    }

    public void sendArecive56(String ip, int port, int berth) throws IOException {
        Socket socket = new Socket("192.168.1.56",502);
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
        System.out.println(addr1+"--------"+addr2+"----------"+addr3+"----------------------56");
        // System.out.println(addr1+"--------"+addr2+"----------"+addr3);
        /*
       String addr1="00000000";
        String addr2="00000000";
        String addr3="00000000";*/

        Berth berth1=service.findById(5);
        Berth berth2=service.findById(6);

        // 左侧换容器指示 addr1.substring(3,4)
        if(addr1.substring(3,4).equals("1")){
            //左侧更换容器 泊位id  红绿灯状态 type  泊位状态 status   泊位内容led
            boolean isOk=service.updateTypeOrContentAndStatus(5,1,2,"更换容器");
        }else{
            //获取泊位信息
            if(berth1.getStatus()==2) {
                Double b1 = berth1.getBerthtotalcapacity() - berth1.getBerthusecapacity();
                String msg = "剩余" + df.format(b1 / 1000) + "吨";
                boolean isOk = service.updateTypeOrContentAndStatus(5, 0, 0, msg);
            }
        }
        //右侧换容器指示  addr2.substring(3,4)
        if(addr2.substring(3,4).equals("1")){
            //左侧更换容器 泊位id  红绿灯状态 type  泊位状态 status   泊位内容led
            boolean isOk=service.updateTypeOrContentAndStatus(6,1,2,"更换容器");
        }else{
            //获取泊位信息
            if(berth2.getStatus()==2) {
                Double b1 = berth2.getBerthtotalcapacity() - berth2.getBerthusecapacity();
                String msg = "剩余" + df.format(b1 / 1000) + "吨";
                boolean isOk = service.updateTypeOrContentAndStatus(6, 0, 0, msg);
            }
        }

        //左侧激活指示   addr1.substring(2,3)
        if(addr1.substring(4,5).equals("1")){
            //边门动作开
            if(addr1.substring(7,8).equals("1")){
                //更新边门动作
                boolean isOk=service.updateOnoff(5,1);
            }else{
                boolean isOk=service.updateOnoff(5,0);
            }
            //溜槽动作开 5，6
            if(addr1.substring(5,6).equals("1")){
                //更新溜槽动作
                boolean isOk=service.updateDownspouting(5,1);
            }else{
                boolean isOk=service.updateDownspouting(5,0);
            }
        }else{
            service.updateOnoff(5,0);
            service.updateDownspouting(5,0);
        }
        //右侧激活指示   addr2.substring(4,5)
        if(addr2.substring(4,5).equals("1")){
            //边门动作开
            if(addr1.substring(7,8).equals("1")){

                boolean isOk=service.updateOnoff(6,1);
            }else{
                boolean isOk=service.updateOnoff(6,0);
            }
            //溜槽动作开 5，6
            if(addr1.substring(5,6).equals("1")){
                boolean isOk=service.updateDownspouting(6,1);
            }else{
                boolean isOk=service.updateDownspouting(6,0);
            }
        }else{
            service.updateOnoff(6,0);
            service.updateDownspouting(6,0);
        }
        //左侧上红外线  addr2.substring(1,2)
        if(addr2.substring(5,6).equals("1")){
            if(berth1.getStatus()==0){
                boolean isOk=service.updateTypeOrContentAndStatus(5,1,0,"工作中");
                if(berth1.getBerthwaitnumber()>0){
                    boolean isOk1=service.updateWaitNumber(5);
                }
            }
        }
        //右侧上红外线  addr3.substring(1,2)
        if(addr3.substring(5,6).equals("1")){
            if(berth2.getStatus()==0){
                boolean isOk=service.updateTypeOrContentAndStatus(6,1,0,"工作中");
                if(berth2.getBerthwaitnumber()>0){
                    boolean isOk1=service.updateWaitNumber(6);
                }
            }
        }
        socket.close();

    }

    public void sendArecive78(String ip, int port, int berth) throws IOException {
        Socket socket = new Socket("192.168.1.78",502);
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
        System.out.println(addr1+"--------"+addr2+"----------"+addr3+"----------------------78");
        // System.out.println(addr1+"--------"+addr2+"----------"+addr3);
        /*
       String addr1="00000000";
        String addr2="00000000";
        String addr3="00000000";*/

        Berth berth1=service.findById(7);
        Berth berth2=service.findById(8);

        // 左侧换容器指示 addr1.substring(3,4)
        if(addr1.substring(3,4).equals("1")){
            //左侧更换容器 泊位id  红绿灯状态 type  泊位状态 status   泊位内容led
            boolean isOk=service.updateTypeOrContentAndStatus(7,1,2,"更换容器");
        }else{
            //获取泊位信息
            if(berth1.getStatus()==2) {
                Double b1 = berth1.getBerthtotalcapacity() - berth1.getBerthusecapacity();
                String msg = "剩余" + df.format(b1 / 1000) + "吨";
                boolean isOk = service.updateTypeOrContentAndStatus(7, 0, 0, msg);
            }
        }
        //右侧换容器指示  addr2.substring(3,4)
        if(addr2.substring(3,4).equals("1")){
            //左侧更换容器 泊位id  红绿灯状态 type  泊位状态 status   泊位内容led
            boolean isOk=service.updateTypeOrContentAndStatus(8,1,2,"更换容器");
        }else{
            //获取泊位信息
            if(berth2.getStatus()==2) {
                Double b1 = berth2.getBerthtotalcapacity() - berth2.getBerthusecapacity();
                String msg = "剩余" + df.format(b1 / 1000) + "吨";
                boolean isOk = service.updateTypeOrContentAndStatus(8, 0, 0, msg);
            }
        }

        //左侧激活指示   addr1.substring(2,3)
        if(addr1.substring(4,5).equals("1")){
            //边门动作开
            if(addr1.substring(7,8).equals("1")){
                //更新边门动作
                boolean isOk=service.updateOnoff(7,1);
            }else{
                boolean isOk=service.updateOnoff(7,0);
            }
            //溜槽动作开 5，6
            if(addr1.substring(5,6).equals("1")){
                //更新溜槽动作
                boolean isOk=service.updateDownspouting(7,1);
            }else{
                boolean isOk=service.updateDownspouting(7,0);
            }
        }else{
            service.updateOnoff(7,0);
            service.updateDownspouting(7,0);
        }
        //右侧激活指示   addr2.substring(4,5)
        if(addr2.substring(4,5).equals("1")){
            //边门动作开
            if(addr1.substring(7,8).equals("1")){

                boolean isOk=service.updateOnoff(8,1);
            }else{
                boolean isOk=service.updateOnoff(8,0);
            }
            //溜槽动作开 5，6
            if(addr1.substring(5,6).equals("1")){
                boolean isOk=service.updateDownspouting(8,1);
            }else{
                boolean isOk=service.updateDownspouting(8,0);
            }
        }else{
            service.updateOnoff(8,0);
            service.updateDownspouting(8,0);
        }
        //左侧上红外线  addr2.substring(1,2)
        if(addr2.substring(5,6).equals("1")){
            if(berth1.getStatus()==0){
                boolean isOk=service.updateTypeOrContentAndStatus(7,1,0,"工作中");
                if(berth1.getBerthwaitnumber()>0){
                    boolean isOk1=service.updateWaitNumber(7);
                }
            }
        }
        //右侧上红外线  addr3.substring(1,2)
        if(addr3.substring(5,6).equals("1")){
            if(berth2.getStatus()==0){
                boolean isOk=service.updateTypeOrContentAndStatus(8,1,0,"工作中");
                if(berth2.getBerthwaitnumber()>0){
                    boolean isOk1=service.updateWaitNumber(8);
                }
            }
        }
        socket.close();

    }


    public void sendArecive90(String ip, int port, int berth) throws IOException {
        Socket socket = new Socket("192.168.1.90",502);
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
        System.out.println(addr1+"--------"+addr2+"----------"+addr3+"----------------------90");
        // System.out.println(addr1+"--------"+addr2+"----------"+addr3);
        /*
       String addr1="00000000";
        String addr2="00000000";
        String addr3="00000000";*/

        Berth berth1=service.findById(9);
        Berth berth2=service.findById(10);

        // 左侧换容器指示 addr1.substring(3,4)
        if(addr1.substring(3,4).equals("1")){
            //左侧更换容器 泊位id  红绿灯状态 type  泊位状态 status   泊位内容led
            boolean isOk=service.updateTypeOrContentAndStatus(9,1,2,"更换容器");
        }else{
            //获取泊位信息
            if(berth1.getStatus()==2) {
                Double b1 = berth1.getBerthtotalcapacity() - berth1.getBerthusecapacity();
                String msg = "剩余" + df.format(b1 / 1000) + "吨";
                boolean isOk = service.updateTypeOrContentAndStatus(9, 0, 0, msg);
            }
        }
        //右侧换容器指示  addr2.substring(3,4)
        if(addr2.substring(3,4).equals("1")){
            //左侧更换容器 泊位id  红绿灯状态 type  泊位状态 status   泊位内容led
            boolean isOk=service.updateTypeOrContentAndStatus(10,1,2,"更换容器");
        }else{
            //获取泊位信息
            if(berth2.getStatus()==2) {
                Double b1 = berth2.getBerthtotalcapacity() - berth2.getBerthusecapacity();
                String msg = "剩余" + df.format(b1 / 1000) + "吨";
                boolean isOk = service.updateTypeOrContentAndStatus(10, 0, 0, msg);
            }
        }

        //左侧激活指示   addr1.substring(2,3)
        if(addr1.substring(4,5).equals("1")){
            //边门动作开
            if(addr1.substring(7,8).equals("1")){
                //更新边门动作
                boolean isOk=service.updateOnoff(9,1);
            }else{
                boolean isOk=service.updateOnoff(9,0);
            }
            //溜槽动作开 5，6
            if(addr1.substring(5,6).equals("1")){
                //更新溜槽动作
                boolean isOk=service.updateDownspouting(9,1);
            }else{
                boolean isOk=service.updateDownspouting(9,0);
            }
        }else{
            service.updateOnoff(9,0);
            service.updateDownspouting(9,0);
        }
        //右侧激活指示   addr2.substring(4,5)
        if(addr2.substring(4,5).equals("1")){
            //边门动作开
            if(addr1.substring(7,8).equals("1")){

                boolean isOk=service.updateOnoff(10,1);
            }else{
                boolean isOk=service.updateOnoff(10,0);
            }
            //溜槽动作开 5，6
            if(addr1.substring(5,6).equals("1")){
                boolean isOk=service.updateDownspouting(10,1);
            }else{
                boolean isOk=service.updateDownspouting(10,0);
            }
        }else{
            service.updateOnoff(10,0);
            service.updateDownspouting(10,0);
        }
        //左侧上红外线  addr2.substring(1,2)
        if(addr2.substring(5,6).equals("1")){
            if(berth1.getStatus()==0){
                boolean isOk=service.updateTypeOrContentAndStatus(9,1,0,"工作中");
                if(berth1.getBerthwaitnumber()>0){
                    boolean isOk1=service.updateWaitNumber(9);
                }
            }
        }
        //右侧上红外线  addr3.substring(1,2)
        if(addr3.substring(5,6).equals("1")){
            if(berth2.getStatus()==0){
                boolean isOk=service.updateTypeOrContentAndStatus(10,1,0,"工作中");
                if(berth2.getBerthwaitnumber()>0){
                    boolean isOk1=service.updateWaitNumber(10);
                }
            }
        }
        socket.close();

    }






    public static String toBinary(int num, int digits) {
        String cover = Integer.toBinaryString(1 << digits).substring(1);
        String s = Integer.toBinaryString(num);
        return s.length() < digits ? cover.substring(s.length()) + s : s;
    }

}
