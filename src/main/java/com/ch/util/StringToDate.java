package com.ch.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StringToDate {

    /**
     * 获取前一周日期集合
     * @return
     * @throws ParseException
     */
    public static List<String> getStatetime() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Calendar c = Calendar.getInstance();
        List<String> list = new ArrayList<>();
        list.add(sdf.format(c.getTime()));
        for (int i=1;i<7;i++){
            c.add(Calendar.DATE, - 1);
            Date date = c.getTime();
            String day = sdf.format(date);
            list.add(day);
        }
        return list;
    }

    /**
     * 获取当前时间的前一天
     * @return
     */
    public static String lastDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1); //得到前一天
        Date date = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String rsDate = df.format(date);
        return rsDate;
    }

    /**
     * 判断string 能否转换成 date
     *
     * @param str
     * @return
     */
    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            formatDateTime.parse(str);
            convertSuccess = true;
            return convertSuccess;
        } catch (ParseException e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        try {
            formatDate.parse(str);
            convertSuccess = true;
            return convertSuccess;
        } catch (ParseException e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }

        SimpleDateFormat month1 = new SimpleDateFormat("yyyy-MM");
        try {
            month1.parse(str);
            convertSuccess = true;
            return convertSuccess;
        } catch (ParseException e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }

        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        try {
            year.parse(str);
            convertSuccess = true;
            return convertSuccess;
        } catch (ParseException e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }
        /**
         * @param string string类型的时间格式转换(String-Date)
         * @return
         * @throws ParseException
         */
        public static Date convertStringToDate(String string) throws ParseException {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return sdf.parse(string);
            } catch (ParseException e) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
                return sdf.parse(string);
            }
        }

    /**
     * 获取现在时间的格式转换(Date-String)
     *
     * @return返回字符串格式 yyyy-MM-dd
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        System.out.println(dateString);
        return dateString;
    }


    /**
     * 获取现在时间的格式转换(Date-String)
     *
     * @return返回字符串格式 yyyy-MM-dd
     */
    public static String dateToString(Date currentTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        System.out.println(dateString);
        return dateString;
    }

    /**
     * 获取现在时间的格式转换(Date-String)
     *
     * @return返回字符串格式 yyyy-MM-dd
     */
    public static String convertDateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 判断MM和dd的位数,如果是一位就在前面加个0,返回一个新的字符串
     * @param data
     * @return
     * @throws ParseException
     */
    public static String changeDate(String data) throws ParseException {
        String rs = null;

        if (data.length()==4){
            rs = data;
        }else if(data.length()>4&&data.length()<=7){
            if (data.length()==6){
                rs = String.valueOf(new StringBuffer(data.substring(0,data.length()-1)).append("0").append(data.substring(data.length()-1,data.length())));
            }else{
                rs = data;
            }
        }else if (data.length()>7&&data.length()<=10){
            if (data.length()==8){
                String r1 = data.substring(5,6);
                rs = String.valueOf(new StringBuffer(data.substring(0,5)).append("0").append(r1).append("-").append("0").append(data.substring(data.length()-1,data.length())));
            }
            String r3 = data.substring(5,7);
            if (data.length()==9){
                 //月份需要修改
                if (r3.contains("-")){
                    rs = String.valueOf(new StringBuffer(data.substring(0,5)).append("0").append(data.substring(5,6)).append("-").append("0").append(data.substring(data.length()-1,data.length())));
                }else{//多少号需要修改
                    rs = String.valueOf(new StringBuffer(data.substring(0,8)).append("0").append(data.substring(data.length()-1,data.length())));
                }
            }
            if (data.length()==10){
                rs=data;
            }
        }
        return rs;
    }


    public static void main(String[] args) {

        String str="76543210";

        System.out.println(str.substring(5,6));

    }

}
