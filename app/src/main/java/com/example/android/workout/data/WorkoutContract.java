package com.example.android.workout.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Rob on 11/14/2016.
 */

public class WorkoutContract {
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    private WorkoutContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.workout";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MUSCLEGROUP = "muscle";
    public static final String PATH_ACTIVITIES = "activity";
    public static final String PATH_SESSIONS = "session";


    /* Inner class that defines the table contents of the muscle group table */
    public static final class MuscleGroupEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MUSCLEGROUP);

        //string with table uri
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MUSCLEGROUP;

        //string with item uri
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MUSCLEGROUP;

        // Table name
        public static final String TABLE_NAME = "muscle";

        //Column Names
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_MG_NAME = "muscle_group_name";
        public static final String COLUMN_MG_IMAGE = "muscle_group_image";
        public static final String COLUMN_MG_COLOR = "muscle_group_color";

    }

    /* Inner class that defines the table contents of the activity table */
    public static final class ActivityEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ACTIVITIES);

        //string with table uri
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACTIVITIES;

        //string with item uri
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACTIVITIES;


        public static final String TABLE_NAME = "activity";

        //Column Names
        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_ACTIVITY_MG_ID = "muscle_group_id";
        public static final String COLUMN_ACTIVITY_NAME = "activity_name";
        public static final String COLUMN_ACTIVITY_DESCRIPTION = "activity_description";
        public static final String COLUMN_ACTIVITY_IMAGE = "activity_image";
        public static final String COLUMN_ACTIVITY_VIDEO = "activity_video";

        public static Uri buildActivityUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
//
//    /* Inner class that defines the table contents of the activity table */
//    public static final class SessionEntry implements BaseColumns {
//
//        public static final Uri CONTENT_URI =
//                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SESSIONS).build();
//
//        public static final String CONTENT_TYPE =
//                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SESSIONS;
//        public static final String CONTENT_ITEM_TYPE =
//                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SESSIONS;
//
//        public static final String TABLE_NAME = "session";
//
//        public static final String COLUMN_SESSION_ACTIVITY_ID = "activity_id";
//        public static final String COLUMN_SESSION_DATE = "session_date";
//        public static final String COLUMN_SESSION_WEIGHT = "session_weight";
//        public static final String COLUMN_SESSION_REPS = "session_reps";
//        public static final String COLUMN_SESSION_SETS = "session_sets";
//        public static final String COLUMN_SESSION_INCREASE = "session_increase";
//        public static final String COLUMN_SESSION_NOTES = "session_notes";
//
//        public static Uri buildSessionUri(long id) {
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }
//    }
}