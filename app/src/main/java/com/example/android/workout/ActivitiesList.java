package com.example.android.workout;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.workout.data.ActivitiesCursorAdapter;
import com.example.android.workout.data.MuscleGroupCursorAdapter;
import com.example.android.workout.data.WorkoutContract;

import static com.example.android.workout.R.id.muscleListView;

public class ActivitiesList extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private final String LOG_TAG = ActivitiesList.class.getSimpleName();

    private static final int URL_LOADER = 0;

    ActivitiesCursorAdapter mActivitiesAdapter;

    private Uri mCurrentActivity;

    private Uri mCurrentMuscleGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        mCurrentMuscleGroup = intent.getData();
//       mNameEditText = (EditText) findViewById(R.id.MuscleNameEditText);

        //set up FAB to open Product Editor
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitiesList.this, AddMuscle.class);
                startActivity(intent);
            }
        });

        ListView listView = (ListView) findViewById(R.id.muscleListView);
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

//        setTitle("Activities");
//        //TODO load activities
//        mActivitiesAdapter = new ActivitiesCursorAdapter(this, null);
//        muscleListView.setAdapter(mActivitiesAdapter);
//        getSupportLoaderManager().initLoader(URL_LOADER, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                WorkoutContract.MuscleGroupEntry._ID,
                WorkoutContract.MuscleGroupEntry.COLUMN_MG_COLOR,
                WorkoutContract.MuscleGroupEntry.COLUMN_MG_IMAGE,
                WorkoutContract.MuscleGroupEntry.COLUMN_MG_NAME
        };
        return new CursorLoader(
                this,
                WorkoutContract.MuscleGroupEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mActivitiesAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mActivitiesAdapter.swapCursor(null);
    }
}
