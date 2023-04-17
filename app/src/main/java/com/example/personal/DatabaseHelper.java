package com.example.personal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME= "PPT";
    public static final String TABLE_NAME= "alarm_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Category";
    public static final String COL_3 = "ProgTime";
    public static final String COL_4 = "RealTime";
    public static final String COL_5 = "Subject";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDatabase="CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Category TEXT, ProgTime TEXT,RealTime TEXT,Subject TEXT)";
        db.execSQL(createDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String id, String category, String progTime, String realTime, String subject){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_1,id);
        cv.put(COL_2,category);
        cv.put(COL_3,progTime);
        cv.put(COL_4,realTime);
        cv.put(COL_5,subject);
        long result = db.insert(TABLE_NAME,null,cv);
        Log.d("DataAdded","Result: "+id+" c "+category+" p "+progTime+" r "+realTime+" s "+subject +" r "+result);
        if(result == -1)
            return false;
        else
            return true;
    }
    public Cursor getCurrent(String ID)
    {

        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT * FROM "+TABLE_NAME+" WHERE "+COL_1+"= ' "+ID+" ' ";

        Cursor data=null;
        if(db!=null)
        {
            data=db.rawQuery(query,null);
            data.moveToFirst();
            db.close();
        }
        return  data;
    }

}
