package com.example.anubharora.todo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by anubharora on 9/12/16.
 */
public class TaskDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "taskdb";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "tasks";

    public static final String COL_TASK_TITLE = "title";
    public static final String DB_ID = "_id" ;

    public TaskDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE + " ( " +
                DB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TASK_TITLE + " TEXT NOT NULL);";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE);
        onCreate(db);

    }
}
