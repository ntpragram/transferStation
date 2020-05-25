package com.ch.service.impl;

import com.ch.dao.master.*;
import com.ch.dao.second.StandardMapper;
import com.ch.dataobject.master.*;
import com.ch.dataobject.second.Standard;
import com.ch.service.PublicService;
import com.ch.util.LED;
import com.ch.util.MD5;
import com.ch.util.VoiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

import static com.ch.util.StringToDate.convertStringToDate;

@Service
public class PublicServiceImpl implements PublicService {

    @Autowired
    private ReplaceContainerMapper replaceContainerMapper;

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private StopsOutBoundMapper stopsOutBoundMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private StandardMapper standardMapper;

    @Autowired
    private BerthMapper berthMapper;


    @Override
    public List<ReplaceContainer> getUpdateContainerRecord(int page, String startTime, String endTime) {
        return replaceContainerMapper.getUpdateContainerRecord(page,startTime,endTime);
    }

    @Override
    public List<ReplaceContainer> getExportContainerRecord(String startTime, String endTime) {
        return replaceContainerMapper.getExportContainerRecord(startTime,endTime);
    }

    @Override
    public int getContainerRecord(String startTime, String endTime) {
        return replaceContainerMapper.getContainerRecord(startTime,endTime);
    }

    @Override
    public boolean deleteContainerRecord(Integer containerrecordId) {
        return replaceContainerMapper.deleteContainerRecord(containerrecordId);
    }

    @Override
    public int getCarListCount(String startTime, String endTime, String carNumber) {
        return carMapper.getCarListCount(startTime,endTime,carNumber);
    }

    @Override
    public List<Car> getCarList(int page, String startTime, String endTime, String carNumber) {
        return carMapper.getCarList(page,startTime,endTime,carNumber);
    }

    @Override
    public List<Car> getExportCarList(String carNumber) {
        return carMapper.getExportCarList(carNumber);
    }

    @Override
    public int getAllWeightForByCarNumber(String licenseplatenumber) {
        return stopsOutBoundMapper.getAllWeightForByCarNumber(licenseplatenumber);
    }

    @Override
    public int getTrainNumber(String licenseplatenumber) {
        return stopsOutBoundMapper.getTrainNumber(licenseplatenumber);
    }

    @Override
    public List<Statistical> getDailyStatistical() {
        return stopsOutBoundMapper.getDailyStatistical();
    }

    @Override
    public List<Statistical> getMonthStatistical() {
        return stopsOutBoundMapper.getMonthStatistical();
    }

    @Override
    public List<Statistical> getYearStatistical() {
        return stopsOutBoundMapper.getYearStatistical();
    }

    @Override
    public Admin verifyAdmin(String adminAccount, String adminPwd) {
        return adminMapper.verifyAdmin(adminAccount, MD5.enctypeMD5(adminAccount+adminPwd));
    }

    @Override
    public List<Admin> listAllAdmin() {
        return adminMapper.listAllAdmin();
    }

    @Override
    public boolean deleteCar(int cid) {
        return carMapper.deleteCar(cid);
    }

    @Override
    public void updateInPut() throws ParseException {

        //首先查询是否有新数据生成
        Standard standard= standardMapper.findNewInformation();
        Berth berth;
        StopsOutBound stopsOutBound=new StopsOutBound();
        if(standard !=null && !"".equals(standard)){
        stopsOutBound.setIfberth(1);
        stopsOutBound.setIfweighting(1);
        stopsOutBound.setIfwaitweight(1);
        stopsOutBound.setCargoweight(standard.getfNet()<50?standard.getfNet()*1000:standard.getfNet());;//货物重量
        stopsOutBound.setVehicleweight(standard.getfTare()<50?standard.getfTare()*1000:standard.getfTare());//车重
        stopsOutBound.setInstitute("垃圾中转站");
        stopsOutBound.setLicenseplatenumber(standard.getfCarno());
        stopsOutBound.setTotalweight(standard.getfNet()<50?standard.getfNet()*1000:standard.getfNet() + standard.getfTare()<50?standard.getfTare()*1000:standard.getfTare());//总重量
        stopsOutBound.setInputtime(convertStringToDate(standard.getfNettime()));
         //查询可用空闲泊位
         berth = berthMapper.findBerth(standard.getfNet()<50?standard.getfNet()*1000:standard.getfNet());
         if(berth != null && !"".equals(berth)){
             String bStr="";
             if(berth.getId()==10){
                 bStr="0";
             }else{
                 bStr=berth.getId().toString();
             }

             stopsOutBound.setBerthnumber(berth.getId());
             stopsOutBound.setStatus(1);
             stopsOutBound.setInformation(standard.getfCarno() + "进入" +bStr + "号泊位工作");

             boolean isOk = stopsOutBoundMapper.insertStopsOutBound(stopsOutBound);
             if (isOk) {
                 //更新数据库为100
                 boolean isOk1 = standardMapper.updateStandardStatus(standard.getfId());
                 if (isOk1) {
                     //更新容器已用容量
                     boolean isOK2 = berthMapper.updateBerthUseCap(berth.getId(), standard.getfNet()<50?standard.getfNet()*1000:standard.getfNet());
                     if (isOK2) {
                         try {
                             Thread.sleep(3000);
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                         VoiceUtil.voicePlayBack(standard.getfCarno() + "进入" + bStr + "号泊位工作");
                     }
                 }
             }
         }else {
             berth = berthMapper.findBerth1(standard.getfNet()<50?standard.getfNet()*1000:standard.getfNet());
             if (berth == null || "".equals(berth)) {

                 stopsOutBound.setBerthnumber(-1);
                 stopsOutBound.setStatus(-1);
                 stopsOutBound.setInformation(standard.getfCarno() + "当前暂无泊位分配");
                 boolean isOk = stopsOutBoundMapper.insertStopsOutBound(stopsOutBound);
                 if(isOk){
                     standardMapper.updateStandardStatus(standard.getfId());
                 }
                 return;
             }else{
                 String bStr="";
                 if(berth.getId()==10){
                     bStr="0";
                 }else{
                     bStr=berth.getId().toString();
                 }

                 stopsOutBound.setStatus(1);
                 stopsOutBound.setInformation(standard.getfCarno() + "进入" + bStr + "号泊位工作");
                 stopsOutBound.setBerthnumber(berth.getId());
                 boolean isOk = stopsOutBoundMapper.insertStopsOutBound(stopsOutBound);
                 if (isOk) {
                     //更新数据库为100
                     boolean isOk1 = standardMapper.updateStandardStatus(standard.getfId());
                     if (isOk1) {
                         //更新容器已用容量
                         boolean isOK2 = berthMapper.updateBerthUseCap1(berth.getId(), standard.getfNet()<50?standard.getfNet()*1000:standard.getfNet());
                         if (isOK2) {
                             try {
                                 Thread.sleep(3000);
                             } catch (InterruptedException e) {
                                 e.printStackTrace();
                             }
                             VoiceUtil.voicePlayBack(standard.getfCarno() + "进入" +bStr + "号泊位工作");
                         }
                     }
                 }
             }
         }
        }





    }

    @Override
    public void updateBigLed() {

        List<StopsOutBound> stopsOutBoundList=stopsOutBoundMapper.getStopsOutBoundListNew();
        String str="";
        for(StopsOutBound list:stopsOutBoundList){
            str+=list.getInformation();
        }
        try {
            new LED().SendDynamicProgam(str);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int searchBerth() {
        StopsOutBound stopsOutBound= stopsOutBoundMapper.findNewSOB();
        if(stopsOutBound==null){
            return  0;
        }
        //查询是否有合适泊位
        Berth berth=berthMapper.findBerth(stopsOutBound.getCargoweight());
        if(berth !=null && !"".equals(berth)){
            //更新泊位到该数据下并且将状态改成工作状态
            String bStr="";
            if(berth.getId()==10){
                bStr="0";
            }else{
                bStr=berth.getId().toString();
            }

            String msg=stopsOutBound.getLicenseplatenumber()+"进入"+bStr+"号泊位工作";
            boolean isOk=stopsOutBoundMapper.updateBerthAndStatus(stopsOutBound.getId(),berth.getId(),msg);
            if(isOk){
                //更新泊位信息
                boolean isOK2 = berthMapper.updateBerthUseCap(berth.getId(), stopsOutBound.getCargoweight());
                if (isOK2) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    VoiceUtil.voicePlayBack(stopsOutBound.getLicenseplatenumber() + "进入" + bStr + "号泊位工作");
                }
            }
            return 1;
        }

        Berth berth1=berthMapper.findBerth1(stopsOutBound.getCargoweight());
        if(berth1 !=null && !"".equals(berth1)){
            //更新泊位到该数据下并且将状态改成工作状态
            String bStr="";
            if(berth1.getId()==10){
                bStr="0";
            }else{
                bStr=berth1.getId().toString();
            }

            String msg=stopsOutBound.getLicenseplatenumber()+"进入"+bStr+"号泊位工作";
            boolean isOk=stopsOutBoundMapper.updateBerthAndStatus(stopsOutBound.getId(),berth1.getId(),msg);
            if(isOk){
                //更新泊位信息
                boolean isOK2 = berthMapper.updateBerthUseCap1(berth1.getId(), stopsOutBound.getCargoweight());
                if (isOK2) {
                    VoiceUtil.voicePlayBack(stopsOutBound.getLicenseplatenumber() + "进入" + bStr + "号泊位等待");
                }
            }
            return 1;
        }


            return 1;



    }
}
