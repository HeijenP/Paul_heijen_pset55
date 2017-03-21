package com.example.paul.paul_heijen_pset41;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class DBManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManager(Context c) { context = c; }


    public DBManager open() throws SQLException {
        dbHelper = DatabaseHelper.getInstance(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }



    public long insert(String name) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME_PARENT, name);
        long id = database.insert(DatabaseHelper.TABLE_NAME_PARENT, null, contentValue);
        fetch();
        return id;

    }

    public Cursor fetch() {
        String[] columns = new String[] {DatabaseHelper._ID, DatabaseHelper.NAME_PARENT};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_PARENT, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NAME_PARENT, name);
        int i = database.update(DatabaseHelper.TABLE_NAME_PARENT, contentValues,
                DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME_PARENT, DatabaseHelper._ID + " = " + _id, null);
    }


}
