package com.example.paul.paul_heijen_pset41;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper helper;

    private static final String DB_NAME = "MULTIPLETODOLISTSDATABASE";
    private static final int DB_VERSION = 1;

    public static final String TABLE_NAME_PARENT = "todolists";
    public static final String TABLE_NAME_CHILD = "todoitems";

    public static final String _ID = "_id";
    public static final String NAME_PARENT = "todolistname";
    public static final String NAME_CHILD = "todoitemname";
    public static final String TODOLISTID = "todolistid";


    private static final String CREATE_TABLE_LISTS = "create table " + TABLE_NAME_PARENT
            + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ NAME_PARENT +
            " TEXT NOT NULL " + ");";

    private static final String CREATE_TABLE_ITEMS = "create table " + TABLE_NAME_CHILD
            + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ NAME_CHILD +
            " TEXT NOT NULL, " + TODOLISTID + " TEXT NOT NULL " + ");";

    public static synchronized DatabaseHelper getInstance(Context context) {
        if(helper == null ) {
            helper = new DatabaseHelper(context.getApplicationContext());
        }
        return helper;
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("sd","starting to create table for lists and table for items");
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_PARENT);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_CHILD);
        db.execSQL(CREATE_TABLE_LISTS);
        db.execSQL(CREATE_TABLE_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_PARENT);
        db.execSQL("DROP TABEL IF EXISTS "+ TABLE_NAME_CHILD);
        onCreate(db);
    }
}