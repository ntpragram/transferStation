package com.ch.dao.master;

import com.ch.dataobject.master.Berth;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BerthMapper {


    List<Berth> getBerthList();

    Berth findById(Integer id);

    boolean updateStatus(Integer id, Integer status);

    boolean addBerth(String weight, Integer isContainer);

    boolean updateStatus1(Integer id, Integer status, Double berthtotalcapacity);

    boolean addReplaceContainer(Integer id);

    boolean updateBerthLoad( String weight, Integer id);

    Berth findBerth(Double getfNet);

    boolean updateBerthUseCap(Integer id, Double getfNet);

    Berth findBerth1(Double getfNet);

    boolean updateBerthUseCap1(Integer id, Double getfNet);

    boolean updateDownspouting(Integer id, Integer downspouting);

    boolean updateOnoff(Integer id, Integer onoff);

    boolean updateType(Integer id,int type);

    List<Berth> getBerthList1();

    boolean updateTypeOrContentAndStatus(@Param("berthId") int berthId, @Param("type") int type,@Param("status") int status,@Param("content") String content);

    boolean updateContent( @Param("berthId") Integer id, @Param("content") String msg);

    boolean updateWaitNumber(@Param("berthId") int i);

    boolean updateBerthUseTotal(@Param("berthnumber") Integer berthnumber,@Param("cargoweight") Double cargoweight);

    boolean updateBerthUseTotalAdd(@Param("berthId") int berthId,@Param("cargoweight") Double cargoweight);
}