package com.ch.service;

import com.ch.dataobject.master.Berth;
import com.ch.dataobject.master.StopsOutBound;
import com.ch.dataobject.master.VoiceContent;

import java.util.List;

public interface BerthService {


    List<Berth> getBerthList();

    Berth findById(Integer id);

    boolean updateStatus(Integer id, Integer status);

    boolean addBerth(String weight, Integer isContainer);

    boolean updateStatus1(Integer id, Integer status, Double berthtotalcapacity);

    boolean addReplaceContainer(Integer id);

    boolean updateBerthLoad(String weight, Integer id);

    List<VoiceContent> getListVoiceContent();

    boolean manualVoice(Integer bid, String remark);

    List<StopsOutBound> getStopsOutBoundList();

    List<StopsOutBound> getOutBoundList(int page, String startTime, String endTime, String carNumber);
    List<StopsOutBound> getExportOutBoundList(String startTime,String endTime,String carNumber);
    int getOutBoundListCount(int page, String startTime, String endTime, String carNumber);

    boolean deleteOutBound(Integer obId);

    boolean updateDownspouting(Integer id, Integer downspouting);

    boolean updateOnoff(Integer id, Integer onoff);

    boolean updateType(Integer id,int type);

    List<Berth> getBerthList1();

    boolean updateTypeOrContentAndStatus(int berthId, int type, int status, String content);

    boolean updateContent(Integer id, String msg);

    boolean updateWaitNumber(int i);


    StopsOutBound findStopById(int obId);

    boolean updateBerthUseTotal(Integer berthnumber, Double cargoweight);

    boolean updateNewBerthStatus(int obId, int berthId);

    boolean updateBerthUseTotalAdd(int berthId, Double cargoweight);
}
