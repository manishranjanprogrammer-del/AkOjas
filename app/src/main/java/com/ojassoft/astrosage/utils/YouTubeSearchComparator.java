package com.ojassoft.astrosage.utils;

import com.ojassoft.astrosage.beans.PlaylistVedio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class YouTubeSearchComparator implements Comparator<PlaylistVedio> {
    @Override
    public int compare(PlaylistVedio o1, PlaylistVedio o2) {
        PlaylistVedio.Snippet contentDetails1 = o1.getSnippet();
        PlaylistVedio.Snippet contentDetails2 = o2.getSnippet();
        String publishAt1 = contentDetails1.getPublishedAt();
        String publishAt2 = contentDetails2.getPublishedAt();
        Date date1 = getDateFromString(publishAt1);
        Date date2 = getDateFromString(publishAt2);
        return date2.compareTo(date1);
    }

    private Date getDateFromString(String dateStr) {
        Date date = null;

        //String dtStart = "2010-10-15T09:27:37Z";
        // yyyy-MM-dd'T'HH:mm:ss.SSSZ
        //2017-07-03T11:15:15.000Z
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        // new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'000Z'");
        try {
            date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).parse(dateStr.replaceAll("Z$", "+0000"));
            //date = format.parse(dateStr);
            System.out.println(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

}