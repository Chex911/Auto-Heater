package com.example.marcinwlodarczyk.tabbed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zvgaa on 7/5/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBhelper";
    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // создаем таблицу с полями
        Log.d(TAG, "--- onCreate database ---");
        // создаем таблицу с полями

        db.execSQL("create table  statistic ("
                + "id integer primary key autoincrement,"
                + "date date default (date('now')),"
                + "time integer,"
                + "temperature integer"
                + ");");

        db.execSQL("create table  image ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "source text"
                + ");");
        db.execSQL("create table if not exists user ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "temp_bool integer,"
                + "temp text,"
                + "time_bool integer,"
                + "time text,"
                + "image integer"
//                   + "foreign key (image) references image(id)"
                + ");");

    }
    public void insert(DBHelper dbHelper,String[][] params,String table)
    {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(TAG, "--- Insert in mytable: ---");
        for(int i=0; i<params.length;i++) {
            cv.put(params[i][0], params[i][1]);
        }


        long rowID = db.insert(table, null, cv);
        Log.d(TAG, "row inserted, ID = " + rowID);
        //       dbHelper.test_insert(db);
        //  int clearCount = db.delete("mytable", null, null);
        ;
        dbHelper.close();
    }

//        public void test_insert(SQLiteDatabase db)
//        {
//            ContentValues cv = new ContentValues();
////            cv.put("name","User 1");
////            cv.put("temp_bool",0);
////            cv.put("temp",25);
////            cv.put("time_bool",0);
////            cv.put("time",20);
//            cv.put("name","photo1");
//            cv.put("source","mat/ter/it");
//            db.insert("image",null,cv);
//        }

    public void update_where(DBHelper dbHelper ,String new_value, String params, String where,String table,String current)
    {
        ContentValues cv = new ContentValues();
        cv.put(params,new_value);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(table,cv,where + "=" + current,null);
        dbHelper.close();
        Log.d(TAG,"Update "+params+" to "+new_value);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public String [] select(DBHelper dbHelper,String TableName,String ColumnName )
    {
        String [] temp_address;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selectQuery = "SELECT * FROM "+TableName;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            temp_address=new String[c.getCount()];
            int i=0;
            temp_address[i]=c.getString(c.getColumnIndex(ColumnName));
            while (c.moveToNext()){
                i++;
            temp_address[i]= c.getString(c.getColumnIndex(ColumnName));
            }
            c.close();
            dbHelper.close();
            Log.d(TAG, Arrays.toString(temp_address));
            return  temp_address;
        }
        c.close();
        dbHelper.close();
        return new String[]{"Answer is Emprty!"};


    }
    public String select(DBHelper dbHelper ,String TableName, String ParameterName,String condition)
    {
        String temp_address="";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selectQuery = "SELECT "+ParameterName+" FROM "+TableName+" "+condition+";";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
          temp_address = c.getString(c.getColumnIndex(ParameterName));
        }
        c.close();
        dbHelper.close();
        Log.d(TAG,ParameterName+": "+temp_address);
        return  temp_address;
    }
    public void completeData(DBHelper dbHelper)
    {
        String temp_address="";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(Calendar.getInstance().getTime());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selectQuery = "SELECT date FROM statistic " +
                "ORDER BY date DESC LIMIT 1;";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            temp_address = c.getString(0);
        }
        selectQuery = "SELECT julianday('"+date+"')" +
                " - julianday('"+temp_address+"') from statistic;";
        c = db.rawQuery(selectQuery, null);
        String[] divdate= temp_address.split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(divdate[2]));
        calendar.set(Calendar.MONTH,Integer.parseInt(divdate[1])-1);
        calendar.set(Calendar.YEAR, Integer.parseInt(divdate[0]));
        if (c.moveToFirst()) {
            temp_address = c.getString(0);
        }
        Log.d("DATABASE",temp_address);
        c.close();
        dbHelper.close();

        for (int i=0;i<Integer.parseInt(temp_address);i++)
        {
            calendar.add(Calendar.DATE, 1);
            date=df.format(calendar.getTime());
            dbHelper.insert(dbHelper,new String[][]{{"date",date},{"time","0"},{"temperature","0"}},"statistic");
        }


    }

}