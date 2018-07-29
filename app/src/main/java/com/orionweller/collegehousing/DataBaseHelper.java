package com.orionweller.collegehousing;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import static android.os.Build.ID;
import static android.provider.Telephony.Mms.Part.TEXT;
import static java.util.Calendar.DATE;
import static javax.xml.xpath.XPathConstants.STRING;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Apartments.db";
    private static final int DATABASE_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // you can use an alternate constructor to specify a database location
        // (such as a folder on the sd card)
        // you must ensure that this folder is available and you have permission
        // to write to it
        //super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);

    }

//    public Cursor getApartments() {
//
//        SQLiteDatabase db = getReadableDatabase();
//        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//
//        //String [] sqlSelect = {"0 _id", "FirstName", "LastName"};
//        String []  sqlSelect = {"*"};
//        String sqlTables = "apts";
//
//        qb.setTables(sqlTables);
//        Cursor c = qb.query(db, sqlSelect, null, null,
//                null, null, null);
//
//        c.moveToFirst();
//        return c;
//
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+"favorites"+" (id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Rent_shared_room_year INTEGER, " +
                "Latitude TEXT,  "+" Longitude TEXT, "+" Distance FLOAT)");    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+"favorites");
        onCreate(db);
    }
}