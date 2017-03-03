package com.utn.android.clase2;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * Created by MDECASTRO on 23/02/2017.
 */

public class BaseHelper extends SQLiteOpenHelper {

    public static final String TABLA_PERSONA = "personas";
    public static final String COL_ID = "_id";
    public static final String COL_NOMBRE = "nombre";
    public static final String COL_APELLIDO = "apellido";
    public static final String COL_TELEFONO = "telefono";
    public static final String COL_DIRECCION = "direccion";
    public static final String COL_FOTO = "foto";

    private static final String DATABASE_NAME = "Contactos.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLA_PERSONA + "( " + COL_ID
            + " integer primary key autoincrement, "
            + COL_NOMBRE + " text not null,"
            + COL_APELLIDO + " text not null,"
            + COL_TELEFONO + " text ,"
            + COL_DIRECCION + " text ,"
            + COL_FOTO + " text " +
            ");";

    public BaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(BaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PERSONA);
        onCreate(db);
    }


}

