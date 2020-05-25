package com.ch.dao.master;

import com.ch.dataobject.master.ReplaceContainer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplaceContainerMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ReplaceContainer record);

    int insertSelective(ReplaceContainer record);

    ReplaceContainer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ReplaceContainer record);

    int updateByPrimaryKey(ReplaceContainer record);

    List<ReplaceContainer> addReplaceContainer(int page, String startTime, String endTime);

    int getContainerRecord(@Param("startTime")String startTime ,@Param("endTime")String endTime);

    List<ReplaceContainer> getUpdateContainerRecord(@Param("page")int page, @Param("startTime")String startTime ,@Param("endTime")String endTime);

    List<ReplaceContainer> getExportContainerRecord(@Param("startTime")String startTime ,@Param("endTime")String endTime);

    boolean deleteContainerRecord(Integer containerrecordId);
}