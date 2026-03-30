package com.ojassoft.astrosage.beans;

import java.util.ArrayList;

/**
 * Created by ojas on १७/१/१८.
 */

public class HinduCalenderData {
    public ArrayList<MonthDataDetail> hinducalendar;


    public ArrayList<MonthDataDetail> getHinducalendar() {
        return hinducalendar;
    }

    public void setHinducalendar(ArrayList<MonthDataDetail> hinducalendar) {
        this.hinducalendar = hinducalendar;
    }

    public class MonthDataDetail {
        String monthname;
        ArrayList<ArrayList<FestDetail>> mainList;
        public ArrayList<FestDetail> monthdata;

        public String getMonthname() {
            return monthname;
        }

        public void setMonthname(String monthname) {
            this.monthname = monthname;
        }

        public ArrayList<FestDetail> getMonthdata() {
            return monthdata;
        }

        public void setMonthdata(ArrayList<FestDetail> monthdata) {
            this.monthdata = monthdata;
        }

       public class FestDetail {
            String festName;
            String festDate;
            String festUrl;
            String festImgUrl;

           public String getFestival_page_view() {
               return festival_page_view;
           }

           public void setFestival_page_view(String festival_page_view) {
               this.festival_page_view = festival_page_view;
           }

           String festival_page_view;

            public String getFestName() {
                return festName;
            }

            public void setFestName(String festName) {
                this.festName = festName;
            }

            public String getFestDate() {
                return festDate;
            }

            public void setFestDate(String festDate) {
                this.festDate = festDate;
            }

            public String getFestUrl() {
                return festUrl;
            }

            public void setFestUrl(String festUrl) {
                this.festUrl = festUrl;
            }

            public String getFestImgUrl() {
                return festImgUrl;
            }

            public void setFestImgUrl(String festImgUrl) {
                this.festImgUrl = festImgUrl;
            }


        }


    }
}
