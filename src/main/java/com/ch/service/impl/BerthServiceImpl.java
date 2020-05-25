package com.ch.service.impl;


import com.ch.dao.master.BerthMapper;
import com.ch.dao.master.StopsOutBoundMapper;
import com.ch.dao.master.VoiceContentMapper;
import com.ch.dao.master.VoiceRecordMapper;
import com.ch.dataobject.master.Berth;
import com.ch.dataobject.master.StopsOutBound;
import com.ch.dataobject.master.VoiceContent;
import com.ch.service.BerthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BerthServiceImpl implements BerthService {

    @Autowired
    private BerthMapper berthMapper;
    @Autowired
    private VoiceContentMapper voiceContentMapper;

    @Autowired
    private VoiceRecordMapper voiceRecordMapper;

    @Autowired
    private StopsOutBoundMapper stopsOutBoundMapper;

    @Override
    public List<Berth> getBerthList() {
        return berthMapper.getBerthList();
    }

    @Override
    public Berth findById(Integer id) {
        return berthMapper.findById(id);
    }

    @Override
    public boolean updateStatus(Integer id, Integer status) {
        return berthMapper.updateStatus(id,status);
    }

    @Override
    public boolean addBerth(String weight, Integer isContainer) {
        return berthMapper.addBerth(weight,isContainer);
    }

    @Override
    public boolean updateStatus1(Integer id, Integer status, Double berthtotalcapacity) {
        return berthMapper.updateStatus1(id,status,berthtotalcapacity);
    }

    @Override
    public boolean addReplaceContainer(Integer id) {

        return berthMapper.addReplaceContainer(id);
    }

    @Override
    public boolean updateBerthLoad(String weight, Integer id) {
        return berthMapper.updateBerthLoad(weight,id);
    }

    @Override
    public List<VoiceContent> getListVoiceContent() {
        return voiceContentMapper.voiceContentMapper();
    }

    @Override
    public boolean manualVoice(Integer bid, String remark) {
        return voiceRecordMapper.manualVoice(bid,remark);
    }

    @Override
    public List<StopsOutBound> getStopsOutBoundList() {
        return stopsOutBoundMapper.getStopsOutBoundList();
    }

    @Override
    public List<StopsOutBound> getOutBoundList(int page, String startTime, String endTime, String carNumber) {
        return stopsOutBoundMapper.getOutBoundList(page,startTime,endTime,carNumber);
    }

    @Override
    public List<StopsOutBound> getExportOutBoundList(String startTime, String endTime, String carNumber) {
        return stopsOutBoundMapper.getExportOutBoundList(startTime,endTime,carNumber);
    }

    @Override
    public int getOutBoundListCount(int page, String startTime, String endTime, String carNumber) {
        return stopsOutBoundMapper.getOutBoundListCount(page,startTime,endTime,carNumber);
    }

    @Override
    public boolean deleteOutBound(Integer obId) {
        return stopsOutBoundMapper.deleteOutBound(obId);
    }

    @Override
    public boolean updateDownspouting(Integer id, Integer downspouting) {
        return berthMapper.updateDownspouting(id,downspouting);
    }

    @Override
    public boolean updateOnoff(Integer id, Integer onoff) {
        return berthMapper.updateOnoff(id,onoff);
    }

    @Override
    public boolean updateType(Integer id,int type) {
        return berthMapper.updateType(id,type);
    }

    @Override
    public List<Berth> getBerthList1() {
        return berthMapper.getBerthList1();
    }

    @Override
    public boolean updateTypeOrContentAndStatus(int berthId, int type, int status, String content) {
        return berthMapper.updateTypeOrContentAndStatus(berthId,type,status,content);
    }

    @Override
    public boolean updateContent(Integer id, String msg) {
        return berthMapper.updateContent(id,msg);
    }

    @Override
    public boolean updateWaitNumber(int i) {
        return berthMapper.updateWaitNumber(i);
    }

    @Override
    public StopsOutBound findStopById(int obId) {
        return stopsOutBoundMapper.findStopById(obId);
    }

    @Override
    public boolean updateBerthUseTotal(Integer berthnumber, Double cargoweight) {
        return berthMapper.updateBerthUseTotal(berthnumber,cargoweight);
    }

    @Override
    public boolean updateNewBerthStatus(int obId, int berthId) {
        return stopsOutBoundMapper.updateNewBerthStatus(obId,berthId);
    }

    @Override
    public boolean updateBerthUseTotalAdd(int berthId, Double cargoweight) {
        return berthMapper.updateBerthUseTotalAdd(berthId,cargoweight);
    }


}
