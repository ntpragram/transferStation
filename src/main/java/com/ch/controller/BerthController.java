package com.ch.controller;

import com.alibaba.fastjson.JSONObject;
import com.ch.dataobject.master.Berth;
import com.ch.dataobject.master.StopsOutBound;
import com.ch.dataobject.master.VoiceContent;
import com.ch.service.BerthService;
import com.ch.util.Constant;
import com.ch.util.Result;
import com.ch.util.TrafficLightsUtil;
import com.ch.util.VoiceUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Controller("berth")
//在url上面通过/berth被访问到
@RequestMapping("/berth")
public class BerthController {
    @Autowired
    private BerthService berthService;
    DecimalFormat df = new DecimalFormat("0.00");

    //获取泊位列表接口
    @RequestMapping(value = "getBerthList",method ={RequestMethod.POST})
    @ResponseBody
    public Result getBerthList(){
        //System.out.println("获取泊位列表");
        List<Berth> list=berthService.getBerthList();
        List<VoiceContent> listContent=berthService.getListVoiceContent();
        List<StopsOutBound> stopsOutBoundList=berthService.getStopsOutBoundList();

        List<Berth> bls=new ArrayList<>();
        for(Berth bl:list){
            bl.setId(bl.getId());
            bl.setBerthtotalcapacity(bl.getBerthtotalcapacity());
            bl.setBerthusecapacity(bl.getBerthusecapacity());
            bl.setStatus(bl.getStatus());//
            bl.setRemainingweight(bl.getBerthtotalcapacity()-bl.getBerthusecapacity());
            bl.setResidualpercentage(Double.parseDouble(df.format(Constant.divide(bl.getBerthusecapacity(),bl.getBerthtotalcapacity(),2)*100)));
            bl.setDownspouting(bl.getDownspouting());
            bl.setOnoff(bl.getOnoff());
            bls.add(bl);
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("BerthList",bls);
        jsonObject.put("VoiceContentList",listContent);
        jsonObject.put("stopsOutBoundList",stopsOutBoundList);
        return Result.success(jsonObject);
    }
    /**
     * 暂停使用,更换容器,恢复使用
     * @param id  泊位id
     * @param status 0 正常使用  1 暂停使用  2 更换容器
     * @return
     */
    @RequestMapping(value = "stopUsingOrReplaceContainerOrRecoveryUses",method ={RequestMethod.POST})
    @ResponseBody
    public Result stopUsingOrReplaceContainerOrRecoveryUses(@RequestParam(name="id") Integer id,@RequestParam(name="status") Integer status) throws Exception {
        boolean isOk=false;
        String msg="";
        DecimalFormat df   = new DecimalFormat("######0.00");
        //TrafficLightsUtil.SendDynamic("192.168.1.205",5005,"更换容器");
        Berth berth1=berthService.findById(id);
        if(status==0){
            //读取泊位剩余容量
            Double b1=berth1.getBerthtotalcapacity()-berth1.getBerthusecapacity();
            msg="剩余"+df.format(b1/1000)+"吨";
        }else if(status==1){
            msg="暂停使用";
        }else{
            msg="更换容器";
        }
        berthService.updateContent(id,msg);
        if(status==0){
            //首先查询之前状态
            Berth berth=berthService.findById(id);
            //说明是更换容器后恢复使用
            if(berth.getStatus()==2){
                isOk=berthService.updateStatus1(id,status,0.0);
                if(isOk){
                    berthService.addReplaceContainer(id);
                }
            }else{
                isOk=berthService.updateStatus(id,status);
            }
            berthService.updateType(id,0);
        }else{
            isOk=berthService.updateStatus(id,status);
            berthService.updateType(id,1);
        }
        if(berth1.getId()!=11){
            Berth berth2=berthService.findById(id);
            try {
                TrafficLightsUtil.SendDynamic(berth1.getIp(),5005,berth2.getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String str="";
        List<Berth> list=berthService.getBerthList1();
        for(Berth bl:list){
            str=bl.getType()+str;
        }
        Constant.socketSend(str.substring(3,11));
        Thread.sleep(100);
        Constant.socketSend1("00000"+str.substring(0,3));
        if(isOk){
            return Result.success();
        }else{
            return Result.failure("201","操作失败");
        }
    }

    /**
     *  添加泊位
     * @param isContainer 是否有容器 0有 2 没有
     * @param weight 重量
     * @return
     */
    @RequestMapping(value = "addBerth",method ={RequestMethod.POST})
    @ResponseBody
    public Result addBerth(@RequestParam(name="weight") String weight,@RequestParam(name="isContainer") Integer isContainer){
        boolean isOk=berthService.addBerth(weight,0);
        if(isOk){
            return Result.success();
        }else{
            return Result.failure("201","操作失败");
        }
    }
    /**
     *
     * 泊位载重临时修改
     * @param weight  重量
     * @param id    泊位id
     * @return
     */
    @RequestMapping(value = "updateBerthLoad",method ={RequestMethod.POST})
    @ResponseBody
    public Result updateBerthLoad(@RequestParam(name="weight") String weight,@RequestParam(name="id") Integer id){
        boolean isOk=berthService.updateBerthLoad(weight,id);
        if(isOk){
            return Result.success();
        }else{
            return Result.failure("201","操作失败");
        }
    }

    /**
     * 手动语音播报
     * @param remark    内容
     * @param bid   泊位id
     * @return
     */
    @RequestMapping(value = "manualVoice",method ={RequestMethod.POST})
    @ResponseBody
    public Result manualVoice(@RequestParam(name="remark") String remark, @RequestParam(name="bid") Integer bid){
        boolean isOk=berthService.manualVoice(bid,remark);
        if(isOk){
            VoiceUtil.voicePlayBack(bid+"号泊位"+remark);
            return Result.success();
        }else{
            return Result.failure("201","操作失败");
        }
    }

    /**
     * 获取垃圾倒置列表接口
     * @param page
     * @param startTime
     * @param endTime
     * @param carNumber
     * @return
     */
    @RequestMapping(value = "getOutBoundList",method ={RequestMethod.POST})
    @ResponseBody
    public Result getOutBoundList(@RequestParam(name="page") Integer page,
                                  @RequestParam(name="startTime") String startTime,
                                  @RequestParam(name="endTime") String endTime,
                                  @RequestParam(name="carNumber") String carNumber,
                                  @RequestParam(name="isExport") Integer isExport){
        JSONObject jsonObject=new JSONObject();
        List<StopsOutBound> stopsOutBoundList;
        if (isExport==1){
            stopsOutBoundList=berthService.getOutBoundList(Constant.getPage(page),startTime,endTime,carNumber);
            List<StopsOutBound> exportStopsOutBoundList=berthService.getExportOutBoundList(startTime,endTime,carNumber);
            jsonObject.put("exportStopsOutBoundList",exportStopsOutBoundList);
        }else{
            stopsOutBoundList=berthService.getOutBoundList(Constant.getPage(page),startTime,endTime,carNumber);
        }
        int allCount=berthService.getOutBoundListCount(Constant.getPage(page),startTime,endTime,carNumber);
        jsonObject.put("stopsOutBoundList",stopsOutBoundList);
        jsonObject.put("allCount",allCount);
        jsonObject.put("size",10);

        return Result.success(jsonObject);
    }

    /**
     * 获取垃圾倒置列表删除
     * @param obId
     * @return
     */
    @RequestMapping(value = "deleteOutBound",method ={RequestMethod.POST})
    @ResponseBody
    public Result deleteOutBound(@RequestParam(name="obId") Integer obId
                                 ){
        boolean isOk=berthService.deleteOutBound(obId);
        if(isOk){
            return Result.success();
        }else{
            return Result.failure("201","操作失败");
        }
    }

    /**
     *
     * @param bid
     * @param downspouting
     * @return
     */
    @RequestMapping(value = "changeDownspoutingStatus",method ={RequestMethod.POST})
    @ResponseBody
    public Result changeDownspoutingStatus(@RequestParam(name="bid") Integer bid,@RequestParam(name="downspouting") Integer downspouting){
        boolean isOk=false;
        if (downspouting==0){
            isOk = berthService.updateDownspouting(bid,1);
        }else{
           isOk =  berthService.updateDownspouting(bid,0);
        }
        if(isOk){
            return Result.success();
        }else{
            return Result.failure("201","操作失败");
        }

    }

    /**
     *
     * @param bid
     * @param onoff
     * @return
     */
    @RequestMapping(value = "changeOnoffStatus",method ={RequestMethod.POST})
    @ResponseBody
    public Result changeOnoffStatus(@RequestParam(name="bid") Integer bid,@RequestParam(name="onoff") String onoff){
        boolean isOk=false;
        if (onoff.equals("true")){
            isOk = berthService.updateOnoff(bid,0);

//            new Thread_Send(0,0,true);
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }else{
            isOk =  berthService.updateOnoff(bid,1);

//            new Thread_Send(0,0,false);
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        if(isOk){
            return Result.success();
        }else{
            return Result.failure("201","操作失败");
        }
    }

    /**
     * 声光报警器
     * @param status
     * @return
     */
    @RequestMapping(value = "voiceController",method ={RequestMethod.POST})
    @ResponseBody
    public Result voiceController(@RequestParam(name="status") boolean status){
        int type=0;
        if(status){
            type=1;
        }
        String str="";
        boolean isOk=berthService.updateType(11,type);
        if(isOk){
            List<Berth> list=berthService.getBerthList1();
            for(Berth bl:list){
                str=bl.getType()+str;
            }
            try {
                Constant.socketSend1("00000"+str.substring(0,3));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Result.success();
        }else{
            return Result.failure("201","错误");
        }
    }

}
