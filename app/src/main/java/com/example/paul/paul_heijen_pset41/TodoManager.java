package com.example.paul.paul_heijen_pset41;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


/**
 * Created by Paul on 10/03/2017.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



public class TodoManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public TodoManager(Context c) { context = c; }

    public TodoManager open() throws SQLException {
        dbHelper = DatabaseHelper.getInstance(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }


    public long insert(String name, long listid) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME_CHILD, name);
        contentValue.put(DatabaseHelper.TODOLISTID, listid);
        long id = database.insert(DatabaseHelper.TABLE_NAME_CHILD, null, contentValue);
        fetch();
        return id;

    }

    public Cursor fetchItemsFromList(long listid) {
        String whereClause = "todolistid =?";
        String[] whereArgs = new String[]{String.valueOf(listid)};
        String[] columns = new String[] {DatabaseHelper._ID, DatabaseHelper.NAME_CHILD};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_CHILD, columns, whereClause, whereArgs, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetch() {
        String[] columns = new String[] {DatabaseHelper._ID, DatabaseHelper.NAME_CHILD};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_CHILD, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NAME_CHILD, name);
        int i = database.update(DatabaseHelper.TABLE_NAME_CHILD, contentValues,
                DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME_CHILD, DatabaseHelper._ID + " = " + _id, null);
    }


}


