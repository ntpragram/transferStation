package com.ch;

import com.ch.dataobject.master.Admin;
import com.ch.dataobject.master.Berth;
import com.ch.service.BerthService;
import com.ch.service.PublicService;
import com.ch.util.BerthConstant;
import com.ch.util.Constant;
import com.ch.util.TrafficLightsUtil;
import onbon.bx05.Bx5GEnv;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;


//可以开始springboot自动化配置
//将类被托管
@SpringBootApplication(scanBasePackages = {"com.ch"})
@RestController
@MapperScan("com.ch.dao")
@EnableScheduling
public class App 
{
    @Autowired
    private PublicService publicService;
    @Autowired
    private BerthService berthService;
    @Scheduled(fixedRate = 10000)
    public void reportCurrent(){
       System.out.println("1111");

        //查询数据库里面是否有未分配泊位的信息
        int count=publicService.searchBerth();
        //查询sqlserver数据库是否有新数据
        if(count==0){
            try {
                publicService.updateInPut();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //更新大屏数据
         publicService.updateBigLed();
        try {
            new BerthConstant().sendAreciveAll("192.168.1.12",502,1,2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            new BerthConstant().sendAreciveAll("192.168.1.34",502,3,4);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new BerthConstant().sendAreciveAll("192.168.1.56",502,5,6);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new BerthConstant().sendAreciveAll("192.168.1.78",502,7,8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new BerthConstant().sendAreciveAll("192.168.1.90",502,9,10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //更新led屏幕
        List<Berth> list=berthService.getBerthList1();
        String str="";
        for(Berth bl:list){
            str=bl.getType()+str;
            try {
                TrafficLightsUtil.SendDynamic(bl.getIp(),5005,bl.getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            Constant.socketSend(str.substring(3,11));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Constant.socketSend1("00000"+str.substring(0,3));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) throws Exception {
        Bx5GEnv.initial("log.properties",30000);
        SpringApplication.run(App.class,args);
    }


    public static void test(){

        /*try {
            new BerthConstant().sendArecive12("192.168.1.12",502,1,2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new BerthConstant().sendArecive34("192.168.1.34",502,1);
        } catch (IOException e) {
            e.printStackTrace();
        }

      *//*  try {
            new BerthConstant().sendArecive56("192.168.1.56",502,1);
        } catch (IOException e) {
            e.printStackTrace();
        }*//*

        try {
            new BerthConstant().sendArecive78("192.168.1.78",502,1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new BerthConstant().sendArecive90("192.168.1.90",502,1);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
