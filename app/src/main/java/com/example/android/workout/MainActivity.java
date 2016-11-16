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

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int URL_LOADER = 0;

    MuscleGroupCursorAdapter mCursorAdapter;

    ActivitiesCursorAdapter mActivitiesAdapter;

    private Uri mCurrentProductUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

//        mNameEditText = (EditText) findViewById(R.id.MuscleNameEditText);

        //set up FAB to open Product Editor
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddMuscle.class);
                startActivity(intent);
            }
        });

        ListView muscleListView = (ListView) findViewById(R.id.muscleListView);
        View emptyView = findViewById(R.id.empty_text);
        muscleListView.setEmptyView(emptyView);

        muscleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);

                Uri selectedMuscle = ContentUris.withAppendedId(WorkoutContract.MuscleGroupEntry.CONTENT_URI, id);

                intent.setData(selectedMuscle);

                startActivity(intent);
            }
        });

        if (mCurrentProductUri == null) {
            setTitle("Muscle Groups");
            //   invalidateOptionsMenu();
            mCursorAdapter = new MuscleGroupCursorAdapter(this,null);
            muscleListView.setAdapter(mCursorAdapter);
            getSupportLoaderManager().initLoader(URL_LOADER, null, this);
        } else {
            setTitle("Activities");
            //TODO load activities
            mActivitiesAdapter = new ActivitiesCursorAdapter(this, null);
            muscleListView.setAdapter(mActivitiesAdapter);
            getSupportLoaderManager().initLoader(URL_LOADER, null, this);
        }

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
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
