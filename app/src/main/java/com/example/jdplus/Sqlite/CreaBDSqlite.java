package com.example.jdplus.Sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CreaBDSqlite extends SQLiteOpenHelper {


    private static final String DB_NAME = "juno_doctor.db";
    private static final int DB_VERSION = 1;
    public static String Nombre_tabla ="usuarios_chat";


    public CreaBDSqlite(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+Nombre_tabla+"(id integer  primary key AUTOINCREMENT, usuario TEXT, nombre TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
