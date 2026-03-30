package com.ojassoft.astrosage.varta.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteDbHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NOTIFICATION = "NOTIFICATION";

    // Bookmark Name
    public static final String TABLE_BOOKMARK = "BOOKMARK";

    // Table Noticication columns
    public static final String ID = "id";
    public static final String MESSAGE = "message";
    public static final String TITLE = "title";
    public static final String LINK = "link";
    public static final String NT_ID = "ntId";
    public static final String EXTRA = "extra";
    public static final String IMG_URL = "imgUrl";
    public static final String BLOG_ID = "blogId";
    public static final String NOTIFICATION_TYPE = "notificationType";
    public static final String TIMESTAMP = "timestamp";

    // Table Bookmark columns
    public static final String ASTROLOGER_ID = "astrologer_id";
    public static final String BOOKMARK_STATUS = "bookmark_status";


    // Database Information
    static final String DB_NAME = "NOTIFICATION.DB";

    // database version
    static final int DB_VERSION = 2;

    // Creating NOTES table query
    private static final String CREATE_TABLE_NOTIFICATION = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFICATION + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MESSAGE + " TEXT, "
            + TITLE + " TEXT, "
            + LINK + " TEXT, "
            + NT_ID + " TEXT, "
            + EXTRA + " TEXT, "
            + IMG_URL + " TEXT, "
            + BLOG_ID + " TEXT, "
            + NOTIFICATION_TYPE + " INTEGER, "
            + TIMESTAMP + " TEXT);";

    // Creating BOOKMARK table query
    private static final String CREATE_TABLE_BOOKMARK = "CREATE TABLE IF NOT EXISTS " + TABLE_BOOKMARK + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ASTROLOGER_ID + " TEXT, "
            + BOOKMARK_STATUS + " TEXT);";


    public SqliteDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_NOTIFICATION);
            db.execSQL(CREATE_TABLE_BOOKMARK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
