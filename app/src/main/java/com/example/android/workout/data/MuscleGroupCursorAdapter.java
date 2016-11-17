package com.example.android.workout.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.workout.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Rob on 11/14/2016.
 */

public class MuscleGroupCursorAdapter extends CursorAdapter {


    public MuscleGroupCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.muscle_group_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final int itemId = cursor.getInt(cursor.getColumnIndex(WorkoutContract.MuscleGroupEntry._ID));

        //find views
        final TextView tvName = (TextView) view.findViewById(R.id.muscleName);
        final ImageView ivMuscle = (ImageView) view.findViewById(R.id.muscleImage);

        //find columns in table
        int nameColumnIndex = cursor.getColumnIndex(WorkoutContract.MuscleGroupEntry.COLUMN_MG_NAME);
        int imageColumnIndex = cursor.getColumnIndex(WorkoutContract.MuscleGroupEntry.COLUMN_MG_IMAGE);

        //get data from table
        String name = cursor.getString(nameColumnIndex);
        String image = cursor.getString(imageColumnIndex);

        //assign data to views
        tvName.setText(name);
        Picasso.with(context).load(image).into(ivMuscle);
    }
}
