package com.example.android.workout.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.workout.R;
import com.example.android.workout.data.WorkoutContract.ActivityEntry;

/**
 * Created by rsteller on 11/16/2016.
 */

public class ActivitiesCursorAdapter extends CursorAdapter {

    public ActivitiesCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.muscle_group_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final int itemId = cursor.getInt(cursor.getColumnIndex(ActivityEntry._ID));

        //find name, quanity, price, and image views
        final TextView tvName = (TextView) view.findViewById(R.id.muscleName);

        //find columns in table
        int nameColumnIndex = cursor.getColumnIndex(ActivityEntry.COLUMN_ACTIVITY_NAME);

        //get data from table
        String name = cursor.getString(nameColumnIndex);

        //assign data to views
        tvName.setText(name);
    }
}
