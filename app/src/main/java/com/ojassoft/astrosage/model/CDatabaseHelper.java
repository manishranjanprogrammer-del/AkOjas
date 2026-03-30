package com.ojassoft.astrosage.model;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.ojassoft.astrosage.R;

public class CDatabaseHelper extends SQLiteOpenHelper {
    /**
     * This field is used to create data base,so the database name is assigned
     * in this field ,used in this application.
     */
    public static final String DATABASE_NAME = "OjassoftMKDb";

    /**
     * This is a context variable used in this class.
     */
    protected Context context;

    /**
     * This is the constructor of this class to initialize variables,used in the
     * whole class
     */
    /**
     * This constructor is added by Bijendra on 18-Dec-2012
     *
     * @param context
     */
    public CDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 12 /* in app version 3.0 */); // DB
        // version
        // 5 in
        // app
        // version
        // 2.99
        this.context = context; // db version increse by 1 before 7 now 8 by
        // Shelendra on 28 .05 .2015 because off
        // timezone fix for LEICESTER city in db.
    }

    /**
     * This function is called with different parameters to create table with
     * data in database for this application.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        createOrInsertData(db, R.raw.sqlcountry);
        createOrInsertData(db, R.raw.sqltimezone);
        createOrInsertData(db, R.raw.sqlcity);
        createOrInsertData(db, R.raw.sqlpersonalinfomisc);
        createOrInsertData(db, R.raw.sqlchartinfo);
        createOrInsertData(db, R.raw.sqlpersonalinfo);
        createOrInsertData(db, R.raw.sqlchathistory);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
     * .SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if ((oldVersion != newVersion) && (newVersion == 7)) {
            db.execSQL("ALTER TABLE tblHoroPersonalInfo ADD M_TimeStamp long not null default 0");
            db.execSQL("ALTER TABLE tblHoroPersonalInfo ADD onlinechartid VARCHAR(30)");
            Cursor cursorOnlineIds = db
                    .rawQuery(
                            "SELECT localchartid, onlinechartid FROM tblLocalOnlineChartRelation",
                            null);
            String[][] onlineLocalRelation = null;
            if (cursorOnlineIds != null) {
                if (cursorOnlineIds.getCount() > 0) {
                    onlineLocalRelation = new String[cursorOnlineIds.getCount()][2];
                    if (cursorOnlineIds.moveToFirst()) {
                        int i = 0;
                        do {
                            onlineLocalRelation[i][0] = cursorOnlineIds
                                    .getString(0);
                            onlineLocalRelation[i][1] = cursorOnlineIds
                                    .getString(1);
                            i++;
                        } while (cursorOnlineIds.moveToNext());
                        for (int j = 0; j < onlineLocalRelation.length; j++) {
                            ContentValues args = new ContentValues();
                            args.put("onlinechartid", onlineLocalRelation[j][1]);
                            db.update("tblHoroPersonalInfo", args, "id" + "="
                                    + onlineLocalRelation[j][0], null);
                        }
                    }
                }
            }
        }
        if ((oldVersion != newVersion) && (newVersion == 9)) {
            db.execSQL("ALTER TABLE tblHoroPersonalInfo ADD M_State VARCHAR(50) DEFAULT 'not define'");
            db.execSQL("ALTER TABLE tblHoroPersonalInfo ADD M_Country VARCHAR(50) DEFAULT 'not define'");
        }
        if(newVersion == 12){
            db.execSQL("DROP TABLE IF EXISTS tblKundaliAiChatHistory");
            createOrInsertData(db, R.raw.sqlchathistory);
        }
    }

    /**
     * This is a main function called by other function to create single table
     * based on Resource id of xml file in RAW folder in the project
     *
     * @param dbIn
     * @param intResourceID
     * @return void
     */
    private void createOrInsertData(SQLiteDatabase dbIn, int intResourceID) {
        String s;
        try {
            InputStream in = context.getResources().openRawResource(
                    intResourceID);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = builder.parse(in, null);
            NodeList statements = doc.getElementsByTagName("statement");
            for (int i = 0; i < statements.getLength(); i++) {
                s = statements.item(i).getChildNodes().item(0).getNodeValue();
                dbIn.execSQL(s);
            }
        } catch (Throwable t) {
            Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            this.close();
        } catch (Exception e) {

        }
        super.finalize();
    }

}
