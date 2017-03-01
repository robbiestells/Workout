package com.example.android.workout;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.workout.data.ActivitiesCursorAdapter;
import com.example.android.workout.data.WorkoutContract.ActivityEntry;
import com.example.android.workout.data.WorkoutContract.MuscleGroupEntry;
import com.squareup.picasso.Picasso;

import static android.R.attr.data;
import static com.example.android.workout.R.id.muscleListView;
import static com.example.android.workout.R.id.toolbarText;

public class ActivitiesList extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private final String LOG_TAG = ActivitiesList.class.getSimpleName();

    private static final int MUSCLE_LOADER = 0;
    private static final int ACTIVITY_LOADER = 1;

    ActivitiesCursorAdapter mActivitiesAdapter;

    private Uri mCurrentActivity;

    private Uri mCurrentMuscleGroup;

    private String muscleId;

    private boolean muscleFound = false;

    private String muscleGroup;

    private String muscleImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        mCurrentMuscleGroup = intent.getData();
//       mNameEditText = (EditText) findViewById(R.id.MuscleNameEditText);

        muscleGroup = intent.getStringExtra("group");
        muscleImage = intent.getStringExtra("image");

        //set up FAB to open Product Editor
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitiesList.this, AddActivity.class);
                intent.putExtra("MuscleId", muscleId);
                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(MUSCLE_LOADER, null, this);

        ListView listView = (ListView) findViewById(muscleListView);
        TextView emptyText = (TextView) findViewById(R.id.empty_text);
        emptyText.setText("Add Activity");
        View emptyView = findViewById(R.id.empty_text);
        listView.setEmptyView(emptyView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(MainActivity.this, MainActivity.class);
//
//                Uri selectedMuscle = ContentUris.withAppendedId(WorkoutContract.MuscleGroupEntry.CONTENT_URI, id);
//
//                intent.setData(selectedMuscle);
//
//                startActivity(intent);
            }
        });

        TextView toolBarText = (TextView) findViewById(R.id.toolbarText);
        toolBarText.setText(muscleGroup);

        ImageView toolBarImage = (ImageView) findViewById(R.id.toolbarImage);
        if (!muscleImage.isEmpty()) {
            Picasso.with(this).load(muscleImage).into(toolBarImage);
        }
//        //TODO load activities
        mActivitiesAdapter = new ActivitiesCursorAdapter(this, null);
        listView.setAdapter(mActivitiesAdapter);
        //getSupportLoaderManager().initLoader(MUSCLE_LOADER, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle args) {
        if (i == 0) {
            String[] projection = {
                    MuscleGroupEntry._ID,
                    MuscleGroupEntry.COLUMN_MG_COLOR,
                    MuscleGroupEntry.COLUMN_MG_IMAGE,
                    MuscleGroupEntry.COLUMN_MG_NAME
            };
            return new CursorLoader(
                    this,
                    mCurrentMuscleGroup,
                    projection,
                    null,
                    null,
                    null
            );
        } else {

            String[] projection = {
                    ActivityEntry._ID,
                    ActivityEntry.COLUMN_ACTIVITY_MG_ID,
                    ActivityEntry.COLUMN_ACTIVITY_NAME,
                    ActivityEntry.COLUMN_ACTIVITY_DESCRIPTION,
                    ActivityEntry.COLUMN_ACTIVITY_IMAGE,
                    ActivityEntry.COLUMN_ACTIVITY_VIDEO
            };

            String selection = ActivityEntry.COLUMN_ACTIVITY_MG_ID + "=?";
            String[] selectionArgs = {muscleId};

            return new CursorLoader(
                    this,
                    ActivityEntry.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    null
            );
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!muscleFound) {
            findActivities(data);
        } else{
            //populate listview
            mActivitiesAdapter.swapCursor(data);

        }
    }

    private void findActivities(Cursor data) {
        if (data.moveToFirst()) {
            int idColumnIndex = data.getColumnIndex(MuscleGroupEntry._ID);

            muscleId = data.getString(idColumnIndex);
            if (muscleId!=null)
            muscleFound = true;

            getSupportLoaderManager().initLoader(ACTIVITY_LOADER, null, this);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mActivitiesAdapter.swapCursor(null);
    }
}
