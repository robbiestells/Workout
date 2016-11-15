package com.example.android.workout.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.workout.data.WorkoutContract.ActivityEntry;
import com.example.android.workout.data.WorkoutContract.MuscleGroupEntry;
import com.example.android.workout.data.WorkoutContract.SessionEntry;

/**
 * Created by Rob on 11/14/2016.
 */

public class WorkoutDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "workout.db";

    public WorkoutDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MUSCLEGROUP_TABLE = "CREATE TABLE " + MuscleGroupEntry.TABLE_NAME + " (" +
                MuscleGroupEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MuscleGroupEntry.COLUMN_MG_NAME + " TEXT UNIQUE NOT NULL, " +
                MuscleGroupEntry.COLUMN_MG_IMAGE + " TEXT, " +
                MuscleGroupEntry.COLUMN_MG_COLOR + " TEXT " +
                " );";

        final String SQL_CREATE_ACTIVITIES_TABLE = "CREATE TABLE " + ActivityEntry.TABLE_NAME + " (" +
                ActivityEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the musclegroup entry associated with this activity data
                ActivityEntry.COLUMN_ACTIVITY_MG_ID + " INTEGER NOT NULL, " +
                ActivityEntry.COLUMN_ACTIVITY_NAME + " TEXT NOT NULL, " +
                ActivityEntry.COLUMN_ACTIVITY_DESCRIPTION + " TEXT, " +
                ActivityEntry.COLUMN_ACTIVITY_IMAGE + " TEXT, " +
                ActivityEntry.COLUMN_ACTIVITY_VIDEO + " TEXT, " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + ActivityEntry.COLUMN_ACTIVITY_MG_ID + ") REFERENCES " +
                MuscleGroupEntry.TABLE_NAME + " (" + MuscleGroupEntry._ID + " );";

        final String SQL_CREATE_SESSIONS_TABLE = "CREATE TABLE " + SessionEntry.TABLE_NAME + " (" +
                SessionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the activity entry associated with this session data
                SessionEntry.COLUMN_SESSION_ACTIVITY_ID + " INTEGER NOT NULL, " +
                SessionEntry.COLUMN_SESSION_DATE + " INTEGER NOT NULL, " +
                SessionEntry.COLUMN_SESSION_WEIGHT + " REAL NOT NULL, " +
                SessionEntry.COLUMN_SESSION_REPS + " INTEGER NOT NULL, " +
                SessionEntry.COLUMN_SESSION_SETS + " INTEGER NOT NULL, " +
                SessionEntry.COLUMN_SESSION_INCREASE + " INTEGER NOT NULL, " +
                SessionEntry.COLUMN_SESSION_NOTES + " TEXT, " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + SessionEntry.COLUMN_SESSION_ACTIVITY_ID + ") REFERENCES " +
                ActivityEntry.TABLE_NAME + " (" + ActivityEntry._ID + " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MUSCLEGROUP_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ACTIVITIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SESSIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MuscleGroupEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ActivityEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SessionEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
