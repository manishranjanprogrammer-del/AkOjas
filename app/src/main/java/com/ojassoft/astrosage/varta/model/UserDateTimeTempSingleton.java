package com.ojassoft.astrosage.varta.model;

public class UserDateTimeTempSingleton {
    private static long instanceCreateTimeMs ;

    private static UserDateTimeTempSingleton instance;

    private UserDateTimeTempSingleton() {
        // Private constructor to prevent instantiation
    }

    public synchronized static UserDateTimeTempSingleton getInstance() {
        if (instance == null || (System.currentTimeMillis() - instanceCreateTimeMs > 1000*60*60)) { // if instance creation time is greter than 1 hour then create new
            instanceCreateTimeMs = System.currentTimeMillis();
            instance = new UserDateTimeTempSingleton();
        }
        return instance;
    }
    String day;
    String month;
    String year;
    String hour;
    String minute;
    String second;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }
}
