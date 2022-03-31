package com.urban.urbanreport.CustomClass;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    // static variable
    private static final int DATABASE_VERSION = 1;
    // Database name
    private static final String DATABASE_NAME = "report.db";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    //Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String Qtblcabang = "CREATE TABLE tbl_cabang (kode varchar(255) PRIMARY KEY, nama VARCHAR(255), alias VARCHAR(255), jenis_store VARCHAR(255), jenis_cabang VARCHAR(255), brand_cabang VARCHAR(255))";
        db.execSQL(Qtblcabang);
        String Qtblartikel = "CREATE TABLE tbl_artikel (kode varchar(255) PRIMARY KEY, nama varchar(255), deskripsi varchar(255))";
        db.execSQL(Qtblartikel);
        String QAPI = "CREATE TABLE tbl_api (id INTEGER PRIMARY KEY AUTOINCREMENT, nama varchar(255))";
        db.execSQL(QAPI);
        String Qtblcountingdet = "CREATE TABLE tbl_promosi (kode varchar(255) PRIMARY KEY, nama varchar(255))";
        db.execSQL(Qtblcountingdet);
    }

    // on Upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tbl_cabang");
        db.execSQL("DROP TABLE IF EXISTS tbl_artikel");
        db.execSQL("DROP TABLE IF EXISTS tbl_promosi");
        db.execSQL("DROP TABLE IF EXISTS tbl_api");
        onCreate(db);
    }
}

