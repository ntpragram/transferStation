package com.ch.dao.master;

import com.ch.dataobject.master.Car;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarMapper {
    int getCarListCount( @Param("startTime")String startTime , @Param("endTime")String endTime,@Param("carNumber")String carNumber);
    List<Car> getCarList(@Param("page")int page, @Param("startTime")String startTime , @Param("endTime")String endTime,@Param("carNumber")String carNumber);
    List<Car> getExportCarList(String carNumber);
    boolean deleteCar(int cid);

    //List<Car> getCarList(HashMap<String, Object> map);
}