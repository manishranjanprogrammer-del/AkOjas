package com.ojassoft.astrosage.varta.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.ojassoft.astrosage.varta.model.BookmarkModel;
import com.ojassoft.astrosage.varta.model.NotificationModel;

import java.util.ArrayList;
import java.util.List;

import static com.ojassoft.astrosage.varta.dao.SqliteDbHelper.TABLE_BOOKMARK;
import static com.ojassoft.astrosage.varta.dao.SqliteDbHelper.TABLE_NOTIFICATION;

/**
 * This clas handle the db operations related to notification
 */
public class NotificationDBManager {

    private SqliteDbHelper dbHelper;

    private Context context;

    public NotificationDBManager(Context c) {
        context = c;
        dbHelper = new SqliteDbHelper(context);
    }


    /**
     * insert the notification into db
     *
     * @param notificationModel
     */
    public int addNotification(NotificationModel notificationModel) {
        int result = 0;
        if (notificationModel == null) return result;
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        try {

            ContentValues contentValue = new ContentValues();
            contentValue.put(SqliteDbHelper.MESSAGE, notificationModel.getMessage());
            contentValue.put(SqliteDbHelper.TITLE, notificationModel.getTitle());
            contentValue.put(SqliteDbHelper.LINK, notificationModel.getLink());
            contentValue.put(SqliteDbHelper.NT_ID, notificationModel.getNtId());
            contentValue.put(SqliteDbHelper.EXTRA, notificationModel.getExtra());
            contentValue.put(SqliteDbHelper.IMG_URL, notificationModel.getImgUrl());
            contentValue.put(SqliteDbHelper.BLOG_ID, notificationModel.getBlogId());
            contentValue.put(SqliteDbHelper.NOTIFICATION_TYPE, notificationModel.getNotificationType());
            contentValue.put(SqliteDbHelper.TIMESTAMP, notificationModel.getTimestamp());

            result = (int) sqLiteDatabase.insert(TABLE_NOTIFICATION, null, contentValue);
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }


    /**
     * insert the bookmark status into db
     * @param bookmarkModel
     */
    public int addBookmark(BookmarkModel bookmarkModel) {
        int result = 0;
        if (bookmarkModel == null) return result;
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        try {

            ContentValues contentValue = new ContentValues();
            contentValue.put(SqliteDbHelper.ASTROLOGER_ID, bookmarkModel.getAstrologerId());
            contentValue.put(SqliteDbHelper.BOOKMARK_STATUS, bookmarkModel.getBookmarkStatus());

            result = (int) sqLiteDatabase.insert(TABLE_BOOKMARK, null, contentValue);
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }


    /**
     * get the notification list from db
     * @param startIndex, endIndex
     * @return Notification List
     */
    public List<NotificationModel> getNotificationList(int startIndex, int endIndex) {

        List<NotificationModel> notificationModels = new ArrayList<>();

        String selectQuery = "SELECT  id,message,title,link,ntId,extra,imgUrl,blogId,notificationType,timestamp FROM " + TABLE_NOTIFICATION +
                " order by timestamp DESC Limit " + startIndex + "," + endIndex;

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        NotificationModel notificationModel = new NotificationModel();
                        notificationModel.setId(cursor.getInt(0));
                        notificationModel.setMessage(cursor.getString(1));
                        notificationModel.setTitle(cursor.getString(2));
                        notificationModel.setLink(cursor.getString(3));
                        notificationModel.setNtId(cursor.getString(4));
                        notificationModel.setExtra(cursor.getString(5));
                        notificationModel.setImgUrl(cursor.getString(6));
                        notificationModel.setBlogId(cursor.getString(7));
                        notificationModel.setNotificationType(cursor.getInt(8));
                        notificationModel.setTimestamp(cursor.getString(9));

                        notificationModels.add(notificationModel);
                    } while (cursor.moveToNext());
                }

            } finally {
                cursor.close();
            }

        } finally {
            sqLiteDatabase.close();
        }

        return notificationModels;
    }

    /**
     * get the bookmark list from db
     * @return Notification List
     */
    public List<BookmarkModel> getBookMarkList() {

        List<BookmarkModel> bookmarkModelList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_BOOKMARK ;
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        BookmarkModel notificationModel = new BookmarkModel();
                        notificationModel.setId(cursor.getInt(0));
                        notificationModel.setAstrologerId(cursor.getString(1));
                        notificationModel.setBookmarkStatus(cursor.getString(2));
                        bookmarkModelList.add(notificationModel);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        } finally {
            sqLiteDatabase.close();
        }
        return bookmarkModelList;
    }

    public void deleteBookmarkRow(String value)
    {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("DELETE FROM " + TABLE_BOOKMARK + " WHERE "+SqliteDbHelper.ASTROLOGER_ID +"='"+value+"'");
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteBookmark()
    {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("DELETE FROM " + TABLE_BOOKMARK);
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

