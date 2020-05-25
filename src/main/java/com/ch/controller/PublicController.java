package com.ch.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ch.dataobject.master.*;

import com.ch.service.BerthService;
import com.ch.service.PublicService;
import com.ch.util.Constant;
import com.ch.util.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;



@Controller("pub")
@RequestMapping("/pub")
public class PublicController {

    @Autowired
    private PublicService publicService;

    @Autowired
    private BerthService berthService;
    /**
     * 更换容器记录查询
     * @param page   第一页开始
     * @return
     */
    @RequestMapping(value = "getUpdateContainerRecord",method ={RequestMethod.POST})
    @ResponseBody
    public Result getUpdateContainerRecord(@RequestParam(name="page") Integer page,
                                           @RequestParam(name="startTime") String startTime,
                                           @RequestParam(name="endTime") String endTime,
                                           @RequestParam(name="isExport") Integer isExport){
        JSONObject jsonObject=new JSONObject();
        System.out.println("===================0000000000000000000000000");
        List<ReplaceContainer> replaceContainerList;
        if (isExport==1){
            replaceContainerList = publicService.getUpdateContainerRecord(Constant.getPage(page),startTime,endTime);
            List<ReplaceContainer> exportReplaceContainerList = publicService.getExportContainerRecord(startTime,endTime);
            jsonObject.put("exportReplaceContainerList",exportReplaceContainerList);
        }else{
            replaceContainerList = publicService.getUpdateContainerRecord(Constant.getPage(page),startTime,endTime);
        }
        //获取总的数据
        int allCount=publicService.getContainerRecord(startTime,endTime);
        jsonObject.put("replaceContainerList",replaceContainerList);
        jsonObject.put("allCount",allCount);
        jsonObject.put("size",10);
        System.out.println("39420--------------erasghuks");
        return Result.success(jsonObject);
    }

    /**
     * 删除记录
     * @param containerrecordId   记录主键id
     * @return
     */
    @RequestMapping(value = "deleteContainerRecord",method ={RequestMethod.POST})
    @ResponseBody
    public Result deleteContainerRecord(@RequestParam(name="containerrecordId") Integer containerrecordId){
        boolean isOk=publicService.deleteContainerRecord(containerrecordId);
        if(isOk){
            return Result.success();
        }else{
            return Result.failure("201","操作失败");
        }
    }

    /**
     *  车辆查询
     * @param page  页数  第一页
     * @param startTime   开始时间
     * @param endTime   结束时间
     * @param carNumber  车牌号
     * @return
     */
    @RequestMapping(value = "getCarList",method ={RequestMethod.POST})
    @ResponseBody
    public Result getCarList(@RequestParam(name="page") Integer page,
                             @RequestParam(name="startTime") String startTime,
                             @RequestParam(name="endTime") String endTime,
                             @RequestParam(name="carNumber") String carNumber,
                             @RequestParam(name="isExport") Integer isExport){
        JSONObject jsonObject=new JSONObject();
        List<Car> cars=new ArrayList<>();
        List<Car> carList;
        if (isExport==1){
            carList = publicService.getExportCarList(carNumber);
        }else{
            carList = publicService.getCarList(Constant.getPage(page),startTime,endTime,carNumber);
        }
        //查询每辆车倒了多少kg垃圾
        for(Car c: carList){
            //垃圾总重
            int allWeight=publicService.getAllWeightForByCarNumber(c.getLicenseplatenumber());
            //进站次数
            int trainNumber=publicService.getTrainNumber(c.getLicenseplatenumber());
            c.setTrainNumber(trainNumber);
            c.setTotalWeight(allWeight);
            cars.add(c);
        }
        if (isExport==1){
            jsonObject.put("exportCarList",cars);
            List<Car>   displayCarList = publicService.getCarList(Constant.getPage(page),startTime,endTime,carNumber);
            List<Car> carlist=new ArrayList<>();
            //查询每辆车倒了多少kg垃圾
            for(Car d: displayCarList){
                //垃圾总重
                int allWeight=publicService.getAllWeightForByCarNumber(d.getLicenseplatenumber());
                //进站次数
                int trainNumber=publicService.getTrainNumber(d.getLicenseplatenumber());
                d.setTrainNumber(trainNumber);
                d.setTotalWeight(allWeight);
                carlist.add(d);
            }
            jsonObject.put("carList",carlist);
        }else{
            jsonObject.put("carList",cars);
        }
        //获取总的数据
        int allCount=publicService.getCarListCount(startTime,endTime,carNumber);
        jsonObject.put("allCount",allCount);
        jsonObject.put("size",10);
        return Result.success(jsonObject);
    }

    /**
     * 删除车辆信息
     * @param cid
     * @return
     */
    @RequestMapping(value = "deleteCar",method ={RequestMethod.POST})
    @ResponseBody
    public Result deleteCar(@RequestParam(name="cid") int cid){
        boolean isOk = publicService.deleteCar(cid);
        if(isOk){
            return Result.success();
        }else{
            return Result.failure("201","操作失败");
        }
    }

    /**
     * 日统计
     * @return
     */
    @RequestMapping(value = "getDailyStatistical",method ={RequestMethod.POST})
    @ResponseBody
    public Result getDailyStatistical(){
        List<Statistical> statistical = publicService.getDailyStatistical();
        JSONArray jsonArray1=new JSONArray();
        JSONArray jsonArray2=new JSONArray();
        JSONArray jsonArray3=new JSONArray();
        DecimalFormat df = new DecimalFormat("0.00");
        for (Statistical s:statistical){
            String xDate = s.getXdate();
            jsonArray1.add(xDate);
            Double garbageWeight = s.getGarbageWeight();
            jsonArray2.add(df.format(garbageWeight/1000));
            Integer quantity = s.getQuantity();
            jsonArray3.add(quantity);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hourList",jsonArray1);
        jsonObject.put("garbageWeightList",jsonArray2);
        jsonObject.put("quantityList",jsonArray3);
        return Result.success(jsonObject);
    }

    /**
     * 月统计
     * @return
     */
    @RequestMapping(value = "getMonthStatistical",method ={RequestMethod.POST})
    @ResponseBody
    public Result getMonthStatistical(){
        List<Statistical> statistical = publicService.getMonthStatistical();
        JSONArray jsonArray1=new JSONArray();
        JSONArray jsonArray2=new JSONArray();
        JSONArray jsonArray3=new JSONArray();
        DecimalFormat df = new DecimalFormat("0.00");
        for (Statistical s:statistical){
            String xDate = s.getXdate();
            jsonArray1.add(xDate);
            Double garbageWeight = s.getGarbageWeight();
            jsonArray2.add(df.format(garbageWeight/1000));
            Integer quantity = s.getQuantity();
            jsonArray3.add(quantity);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("monthList",jsonArray1);
        jsonObject.put("garbageWeightList",jsonArray2);
        jsonObject.put("quantityList",jsonArray3);
        return Result.success(jsonObject);
    }

    /**
     * 年统计
     * @return
     */
    @RequestMapping(value = "getYearStatistical",method ={RequestMethod.POST})
    @ResponseBody
    public Result getYearStatistical(){
        List<Statistical> statistical = publicService.getYearStatistical();
        JSONArray jsonArray1=new JSONArray();
        JSONArray jsonArray2=new JSONArray();
        JSONArray jsonArray3=new JSONArray();
        DecimalFormat df = new DecimalFormat("0.00");
        for (Statistical s:statistical){
            String xDate = s.getXdate();
            jsonArray1.add(xDate);
            Double garbageWeight = s.getGarbageWeight();
            jsonArray2.add(df.format(garbageWeight/1000));
            Integer quantity = s.getQuantity();
            jsonArray3.add(quantity);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("yearList",jsonArray1);
        jsonObject.put("garbageWeightList",jsonArray2);
        jsonObject.put("quantityList",jsonArray3);
        return Result.success(jsonObject);
    }


    @RequestMapping(value = "verify",method ={RequestMethod.POST})
    @ResponseBody
    public Result getAdminVerify(@RequestParam(name="adminAccount") String adminAccount,
                                 @RequestParam(name="adminPwd") String adminPwd){
        Admin admin = publicService.verifyAdmin(adminAccount,adminPwd);
        String loginStatus = "0";
        JSONObject jsonObject = new JSONObject();
        if (admin == null) {
            List<Admin> newAdmin = publicService.listAllAdmin();
            for (Admin a : newAdmin) {
                //判断admin表中是否存在该账户
                if (!a.getAdminaccount().contains(adminAccount)) {
                    loginStatus = "1";
                    jsonObject.put("loginStatus",loginStatus);
                    return Result.success(jsonObject);
                }
                //判断admin表中账户存在的情况下密码是否正确
                else if (!a.getAdminpwd().contains(adminPwd)) {
                    loginStatus = "2";
                    jsonObject.put("loginStatus",loginStatus);
                    return Result.success(jsonObject);
                }
            }
        }
        return Result.success("");
    }

    /**
     * 更改泊位(车辆从原先泊位到达另一泊位)
     * @param obId
     * @param berthId
     * @return
     */
    @RequestMapping(value = "updateStopBerth",method ={RequestMethod.POST})
    @ResponseBody
    public Result updateStopBerth(@RequestParam(name="obId") int obId,@RequestParam(name="berthId") int berthId){
        //查询当前所属泊位
        StopsOutBound stopsOutBound=berthService.findStopById(obId);
       // System.out.println(stopsOutBound+"--------------------");
        //将当前所属泊位已用容量减当前泊位重量
        boolean isOK=berthService.updateBerthUseTotal(stopsOutBound.getBerthnumber(),stopsOutBound.getCargoweight());
        if(isOK){
            //将新的泊位更新到数据库
            boolean isOk1=berthService.updateNewBerthStatus(obId,berthId);
            if(isOk1){
            //将新泊位的已用容量更新
                berthService.updateBerthUseTotalAdd(berthId,stopsOutBound.getCargoweight());
            }
            return Result.success();
        }else {
            return Result.failure("201", "操作失败");
        }
        //return Result.success(stopsOutBound);
    }



}
