package com.teamhike.teamhike.SqliteDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.teamhike.teamhike.Models.User;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteOpenHelper {

    private static final String DB_NAME = "information.db";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE_USERS = "users";
    private static final String DB_COLUMN_ID = "id";
    public static final String DB_COLUMN_USER_UNIQUE_ID = "user_unique_id";
    public static final String DB_COLUMN_PHONE_NUMBER = "phone_number";
    public static final String DB_COLUMN_LOGGED = "logged";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS " + DB_TABLE_USERS + " (" +
            DB_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DB_COLUMN_USER_UNIQUE_ID + " TEXT, " +
            DB_COLUMN_PHONE_NUMBER + " TEXT, " +
            DB_COLUMN_LOGGED + " TEXT);";

    private static final String DELETE_TABLE_USERS = "DROP TABLE IF EXISTS " + DB_TABLE_USERS + ";";

    public DataBase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE_USERS);
        onCreate(db);
    }

    public void registerLocalUser(String userUniqueId, String phoneNumber, String logged) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DB_COLUMN_USER_UNIQUE_ID, userUniqueId);
        cv.put(DB_COLUMN_PHONE_NUMBER, phoneNumber);
        cv.put(DB_COLUMN_LOGGED, logged);
        db.insert(DB_TABLE_USERS, null, cv);
        db.close();
    }

    public List<User> getLocalUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DB_TABLE_USERS, null);
        if (cursor.moveToFirst()) {
            List<User> users = new ArrayList<>();
            do {
                User user = new User();
//                user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(DB_COLUMN_PHONE_NUMBER)));
                user.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(DB_COLUMN_PHONE_NUMBER)));
//                user.setLogged(cursor.getString(cursor.getColumnIndex(DB_COLUMN_LOGGED)));
                user.setLogged(cursor.getString(cursor.getColumnIndexOrThrow(DB_COLUMN_LOGGED)));
                users.add(user);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
            return users;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    public User getActiveLocalUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DB_TABLE_USERS + " WHERE " + DB_COLUMN_LOGGED + " = 'yes' ", null);
        if (cursor.moveToFirst()) {
            User user = new User();
            user.setUniqueId(cursor.getString(cursor.getColumnIndexOrThrow(DB_COLUMN_USER_UNIQUE_ID)));
//            user.setUniqueId(cursor.getString(cursor.getColumnIndex(DB_COLUMN_USER_UNIQUE_ID)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(DB_COLUMN_PHONE_NUMBER)));
//            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(DB_COLUMN_PHONE_NUMBER)));
            user.setLogged(cursor.getString(cursor.getColumnIndexOrThrow(DB_COLUMN_LOGGED)));
//            user.setLogged(cursor.getString(cursor.getColumnIndex(DB_COLUMN_LOGGED)));
            cursor.close();
            db.close();
            return user;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    public void updateLocalUser(String set_column, String set_value, String where_column, String where_value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(set_column, set_value);
        db.update(DB_TABLE_USERS, cv, where_column + " = ?", new String[]{where_value});
        db.close();
    }
}
