package com.example.shop.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Владислав on 18.05.2017.
 */

public class LocalDB extends SQLiteOpenHelper {
    public LocalDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "Shop", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table if not exists Products (" +
                "id integer primary key autoincrement, " +
                "Title text not null, " +
                "Category text not null," +
                "Amount integer not null," +
                "Price real not null," +
                "Add_date integer not null);");

        db.execSQL("create table if not exists tempProducts (" +
                "id integer primary key autoincrement, " +
                "Title text not null, " +
                "Category text not null," +
                "Amount integer not null," +
                "Price real not null," +
                "Add_date integer not null);");

        db.execSQL("create table if not exists ProductInfo (" +
                "id integer primary key autoincrement, " +
                "Name text not null," +
                "Company text not null, " +
                "Annotation text not null," +
                "Release_date integer not null);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists Products");
        onCreate(db);
    }
}
