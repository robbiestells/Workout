package com.example.android.workout.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.workout.data.WorkoutContract.MuscleGroupEntry;

/**
 * Created by Rob on 11/14/2016.
 */

public class WorkoutProvider extends ContentProvider {
    private static final String LOG_TAG = WorkoutProvider.class.getSimpleName();

    private WorkoutDbHelper mHelper;

    private static final int MUSCLE = 100;
    private static final int MUSCLE_ID = 101;
    private static final int Activity = 200;
    private static final int SESSION = 300;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(WorkoutContract.CONTENT_AUTHORITY, WorkoutContract.PATH_MUSCLEGROUP, MUSCLE);
        sUriMatcher.addURI(WorkoutContract.CONTENT_AUTHORITY, WorkoutContract.PATH_MUSCLEGROUP + "/#", MUSCLE_ID);
    }
    @Override
    public boolean onCreate() {
        mHelper = new WorkoutDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mHelper.getReadableDatabase();

        Cursor cursor;

        //determine whether a specific product is being queried
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MUSCLE:
                cursor = database.query(MuscleGroupEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MUSCLE_ID:
                selection = MuscleGroupEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(MuscleGroupEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        //set notification URI on the cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MUSCLE:
                return MuscleGroupEntry.CONTENT_TYPE;
            case MUSCLE_ID:
                return MuscleGroupEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MUSCLE:
                return insertMuscle(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertMuscle(Uri uri, ContentValues contentValues) {
        //Check that name is not null
        String name = contentValues.getAsString(MuscleGroupEntry.COLUMN_MG_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Product requires a name");
        }

        SQLiteDatabase database = mHelper.getWritableDatabase();

        long id = database.insert(MuscleGroupEntry.TABLE_NAME, null, contentValues);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //notify listeners that there has been a change
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
