package com.libojassoft.android.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.libojassoft.android.models.NotesModel;

import java.util.ArrayList;
import java.util.List;

import static com.libojassoft.android.dao.SqliteDbHelper.TABLE_NOTES;

public class NotesDBManager {

    private SqliteDbHelper dbHelper;

    private Context context;

    public NotesDBManager(Context c) {
        context = c;
        dbHelper = new SqliteDbHelper(context);
    }


    /**
     * insert the notes into db
     *
     * @param notesModel
     */
    public int addNotes(NotesModel notesModel) {
        int result = 0;
        if (notesModel == null) return result;
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        try {
            ContentValues contentValue = new ContentValues();
            contentValue.put(SqliteDbHelper.MONTH, notesModel.getMonth());
            contentValue.put(SqliteDbHelper.DAY, notesModel.getDay());
            contentValue.put(SqliteDbHelper.YEAR, notesModel.getYear());
            contentValue.put(SqliteDbHelper.CATEGORY_ID, notesModel.getCategoryId());
            contentValue.put(SqliteDbHelper.TAGS, notesModel.getTags());
            contentValue.put(SqliteDbHelper.NOTES, notesModel.getNotes());
            contentValue.put(SqliteDbHelper.TIMESTAMP, notesModel.getTimestamp());

            result = (int) sqLiteDatabase.insert(TABLE_NOTES, null, contentValue);
        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }

    /**
     * update the notes into db
     *
     * @param notesModel
     */
    public int updateNotes(NotesModel notesModel) {

        int result = 0;
        if (notesModel == null) return result;
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        try {

            ContentValues contentValue = new ContentValues();
            contentValue.put(SqliteDbHelper.CATEGORY_ID, notesModel.getCategoryId());
            contentValue.put(SqliteDbHelper.TAGS, notesModel.getTags());
            contentValue.put(SqliteDbHelper.NOTES, notesModel.getNotes());
            contentValue.put(SqliteDbHelper.TIMESTAMP, notesModel.getTimestamp());

            result = sqLiteDatabase.update(TABLE_NOTES, contentValue, "month=? and day=? and year=?",
                    new String[]{notesModel.getMonth() + "", notesModel.getDay() + "", notesModel.getYear() + ""});

        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }

    /**
     * update the notes into db
     *
     * @param notesModel
     */
    public int updateNotesById(NotesModel notesModel) {

        int result = 0;
        if (notesModel == null) return result;
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        try {

            ContentValues contentValue = new ContentValues();
            contentValue.put(SqliteDbHelper.MONTH, notesModel.getMonth());
            contentValue.put(SqliteDbHelper.DAY, notesModel.getDay());
            contentValue.put(SqliteDbHelper.YEAR, notesModel.getYear());
            contentValue.put(SqliteDbHelper.CATEGORY_ID, notesModel.getCategoryId());
            contentValue.put(SqliteDbHelper.TAGS, notesModel.getTags());
            contentValue.put(SqliteDbHelper.NOTES, notesModel.getNotes());
            contentValue.put(SqliteDbHelper.TIMESTAMP, notesModel.getTimestamp());

            result = sqLiteDatabase.update(TABLE_NOTES, contentValue, "id=?",
                    new String[]{notesModel.getId() + ""});

        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }

    /**
     * delete the note from db
     *
     * @param id
     */
    public int deleteNotesById(int id) {

        int result;
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        try {

            result = sqLiteDatabase.delete(TABLE_NOTES, "id=?", new String[]{id + ""});

        } finally {
            sqLiteDatabase.close();
        }
        return result;
    }

    /**
     * get the notes list from db
     *
     * @param month
     * @param year
     * @return notes list
     */
    public List<NotesModel> getNotesList(int month, int year) {

        List<NotesModel> notesModelList = new ArrayList<>();

        String selectQuery = "SELECT  id,month,day,year,categoryId,tags,notes,timestamp FROM " + TABLE_NOTES + " WHERE month=" + month + " and year=" + year;

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        NotesModel notesModel = new NotesModel();
                        notesModel.setId(cursor.getInt(0));
                        notesModel.setMonth(cursor.getInt(1));
                        notesModel.setDay(cursor.getInt(2));
                        notesModel.setYear(cursor.getInt(3));
                        notesModel.setCategoryId(cursor.getInt(4));
                        notesModel.setTags(cursor.getString(5));
                        notesModel.setNotes(cursor.getString(6));
                        notesModel.setTimestamp(cursor.getString(7));

                        notesModelList.add(notesModel);
                    } while (cursor.moveToNext());
                }

            } finally {
                cursor.close();
            }

        } finally {
            sqLiteDatabase.close();
        }

        return notesModelList;
    }


    /**
     * get notes behalf of month, day and year
     *
     * @param month
     * @param day
     * @param year
     * @return
     */
    public NotesModel getNotesModel(int month, int day, int year) {

        NotesModel notesModel = null;

        String selectQuery = "SELECT  id,month,day,year,categoryId,tags,notes,timestamp FROM " + TABLE_NOTES + " WHERE month=" + month + " and day=" + day + " and year=" + year;

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    notesModel = new NotesModel();
                    notesModel.setId(cursor.getInt(0));
                    notesModel.setMonth(cursor.getInt(1));
                    notesModel.setDay(cursor.getInt(2));
                    notesModel.setYear(cursor.getInt(3));
                    notesModel.setCategoryId(cursor.getInt(4));
                    notesModel.setTags(cursor.getString(5));
                    notesModel.setNotes(cursor.getString(6));
                    notesModel.setTimestamp(cursor.getString(7));
                }

            } finally {
                cursor.close();
            }

        } finally {
            sqLiteDatabase.close();
        }

        return notesModel;
    }

    /**
     * get notes behalf of id
     *
     * @param id
     * @return
     */
    public NotesModel getNotesModelById(int id) {

        NotesModel notesModel = null;

        String selectQuery = "SELECT  id,month,day,year,categoryId,tags,notes,timestamp FROM " + TABLE_NOTES + " WHERE id=" + id;

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    notesModel = new NotesModel();
                    notesModel.setId(cursor.getInt(0));
                    notesModel.setMonth(cursor.getInt(1));
                    notesModel.setDay(cursor.getInt(2));
                    notesModel.setYear(cursor.getInt(3));
                    notesModel.setCategoryId(cursor.getInt(4));
                    notesModel.setTags(cursor.getString(5));
                    notesModel.setNotes(cursor.getString(6));
                    notesModel.setTimestamp(cursor.getString(7));
                }

            } finally {
                cursor.close();
            }

        } finally {
            sqLiteDatabase.close();
        }

        return notesModel;
    }

    /**
     * get searched notes list
     *
     * @param month
     * @param filter
     * @return
     */
    public List<NotesModel> getSearchedNotesList(int month, int year, String filter) {

        List<NotesModel> notesModelList = new ArrayList<>();

        String selectQuery = "SELECT  id,month,day,year,categoryId,tags,notes,timestamp FROM " + TABLE_NOTES + " WHERE month=" + month + " and year=" + year + " and (notes LIKE '%" + filter + "%' or tags LIKE '%" + filter + "%')";

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        NotesModel notesModel = new NotesModel();
                        notesModel.setId(cursor.getInt(0));
                        notesModel.setMonth(cursor.getInt(1));
                        notesModel.setDay(cursor.getInt(2));
                        notesModel.setYear(cursor.getInt(3));
                        notesModel.setCategoryId(cursor.getInt(4));
                        notesModel.setTags(cursor.getString(5));
                        notesModel.setNotes(cursor.getString(6));
                        notesModel.setTimestamp(cursor.getString(7));

                        notesModelList.add(notesModel);
                    } while (cursor.moveToNext());
                }

            } finally {
                cursor.close();
            }

        } finally {
            sqLiteDatabase.close();
        }

        return notesModelList;
    }

    /**
     * get searched notes list
     * @param filter
     * @return
     */
    public List<NotesModel> getGenSearchedNotesList(String filter) {

        List<NotesModel> notesModelList = new ArrayList<>();

        String selectQuery = "SELECT  id,month,day,year,categoryId,tags,notes,timestamp FROM " + TABLE_NOTES + " WHERE (notes LIKE '%" + filter + "%' or tags LIKE '%" + filter + "%')";

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        NotesModel notesModel = new NotesModel();
                        notesModel.setId(cursor.getInt(0));
                        notesModel.setMonth(cursor.getInt(1));
                        notesModel.setDay(cursor.getInt(2));
                        notesModel.setYear(cursor.getInt(3));
                        notesModel.setCategoryId(cursor.getInt(4));
                        notesModel.setTags(cursor.getString(5));
                        notesModel.setNotes(cursor.getString(6));
                        notesModel.setTimestamp(cursor.getString(7));

                        notesModelList.add(notesModel);
                    } while (cursor.moveToNext());
                }

            } finally {
                cursor.close();
            }

        } finally {
            sqLiteDatabase.close();
        }

        return notesModelList;
    }

    /**
     * get the notes list from db
     *
     * @return notes list
     */
    public List<NotesModel> getAllNotes() {

        List<NotesModel> notesModelList = new ArrayList<>();

        String selectQuery = "SELECT  id,month,day,year,categoryId,tags,notes,timestamp FROM " + TABLE_NOTES + " WHERE notes !='' order by timestamp desc";

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        NotesModel notesModel = new NotesModel();
                        notesModel.setId(cursor.getInt(0));
                        notesModel.setMonth(cursor.getInt(1));
                        notesModel.setDay(cursor.getInt(2));
                        notesModel.setYear(cursor.getInt(3));
                        notesModel.setCategoryId(cursor.getInt(4));
                        notesModel.setTags(cursor.getString(5));
                        notesModel.setNotes(cursor.getString(6));
                        notesModel.setTimestamp(cursor.getString(7));

                        notesModelList.add(notesModel);
                    } while (cursor.moveToNext());
                }

            } finally {
                cursor.close();
            }

        } finally {
            sqLiteDatabase.close();
        }

        return notesModelList;
    }

}

