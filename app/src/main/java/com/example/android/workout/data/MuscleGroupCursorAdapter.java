package com.example.android.workout.data;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.workout.ActivitiesList;
import com.example.android.workout.MainActivity;
import com.example.android.workout.R;

import static android.R.attr.id;

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
    public void bindView(View view, final Context context, Cursor cursor) {
        final int itemId = cursor.getInt(cursor.getColumnIndex(WorkoutContract.MuscleGroupEntry._ID));

        //find views
        final TextView tvName = (TextView) view.findViewById(R.id.muscleName);
        final ImageView ivMuscle = (ImageView) view.findViewById(R.id.muscleImage);

        //find columns in table
        int nameColumnIndex = cursor.getColumnIndex(WorkoutContract.MuscleGroupEntry.COLUMN_MG_NAME);
        int imageColumnIndex = cursor.getColumnIndex(WorkoutContract.MuscleGroupEntry.COLUMN_MG_IMAGE);

        //get data from table
        final String name = cursor.getString(nameColumnIndex);
        final String image = cursor.getString(imageColumnIndex);

        String color = "#c20066ff";
        ColorFilter colorFilter = new LightingColorFilter(Color.parseColor(color), Color.parseColor(color));
        ivMuscle.setColorFilter(colorFilter);

        //assign data to views
        tvName.setText(name);
        if (!image.isEmpty()) {
            ivMuscle.setImageBitmap(BitmapFactory.decodeFile(image));
            Glide.with(context).load(image).into(ivMuscle);
        } else {
            tvName.setTextColor(Color.BLACK);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivitiesList.class);
                Uri selectedMuscle = ContentUris.withAppendedId(WorkoutContract.MuscleGroupEntry.CONTENT_URI, itemId);

                intent.setData(selectedMuscle);

                //TODO get selected group
                intent.putExtra("group", name);
                intent.putExtra("image", image);

                context.startActivity(intent);
            }
        });
    }
}
