package com.ch.service;

import com.ch.dataobject.master.Admin;
import com.ch.dataobject.master.Car;
import com.ch.dataobject.master.ReplaceContainer;
import com.ch.dataobject.master.Statistical;

import java.text.ParseException;
import java.util.List;

public interface PublicService {


    List<ReplaceContainer> getUpdateContainerRecord(int page, String startTime, String endTime);

    List<ReplaceContainer> getExportContainerRecord(String startTime ,String endTime);

    int getContainerRecord(String startTime, String endTime);

    boolean deleteContainerRecord(Integer containerrecordId);

    int getCarListCount( String startTime, String endTime, String carNumber);

    List<Car> getCarList(int page, String startTime, String endTime, String carNumber);

    List<Car> getExportCarList(String carNumber);

    int getAllWeightForByCarNumber(String licenseplatenumber);

    int getTrainNumber(String licenseplatenumber);

    List<Statistical> getDailyStatistical();

    List<Statistical> getMonthStatistical();

    List<Statistical> getYearStatistical();

    Admin verifyAdmin(String adminAccount, String adminPwd);

    List<Admin> listAllAdmin();

    boolean deleteCar(int cid);

    void updateInPut() throws ParseException;

    void updateBigLed();

    int searchBerth();
}
