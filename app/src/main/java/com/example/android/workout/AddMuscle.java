package com.example.android.workout;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.android.workout.data.WorkoutContract;

/**
 * Created by Rob on 11/14/2016.
 */

public class AddMuscle  extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_PRODUCT_LOADER = 0;

    private boolean mProductHasChanged = false;

    private Uri mCurrentProductUri;

    private EditText mNameEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muscle_group_edit);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        mNameEditText = (EditText) findViewById(R.id.MuscleNameEditText);

        if (mCurrentProductUri == null) {
            setTitle("Add Muscle Group");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit Muscle Group");
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        //only show delete button if editing product
        if (mCurrentProductUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                //on selecting save, save the product
                saveMuscleGroup();
                finish();
                return true;
            case R.id.action_delete:
                deleteMuscleGroup();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteMuscleGroup() {
        if (mCurrentProductUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"Deleted", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    private void saveMuscleGroup() {
        //make sure all fields are filled out
        if (TextUtils.isEmpty(mNameEditText.getText())) {
            Toast.makeText(this, "Name Required", Toast.LENGTH_SHORT).show();
            return;
        }

        //get all data from fields
        String nameString = mNameEditText.getText().toString().trim();

        //put all values into ContentValues
        ContentValues values = new ContentValues();
        values.put(WorkoutContract.MuscleGroupEntry.COLUMN_MG_NAME, nameString);

        //if new product, insert values to new row and show Toast, otherwise, update product row
        if (mCurrentProductUri == null) {
            Uri newUri = getContentResolver().insert(WorkoutContract.MuscleGroupEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, "insert failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "insert successful", Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(
                    mCurrentProductUri,
                    values,
                    null,
                    null
            );

            if (rowsAffected == 0) {
                Toast.makeText(this, "update failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "update successful" , Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                WorkoutContract.MuscleGroupEntry._ID,
                WorkoutContract.MuscleGroupEntry.COLUMN_MG_NAME,
                WorkoutContract.MuscleGroupEntry.COLUMN_MG_COLOR,
                WorkoutContract.MuscleGroupEntry.COLUMN_MG_IMAGE
        };
        return new CursorLoader(this,
                mCurrentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }
        updateViews(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //on reset, clear all fields
        mNameEditText.setText("");

    }


    private void updateViews(Cursor data) {
        //if the cursor is not null, load data into views
        if (data.moveToFirst()) {
            int nameColumnIndext = data.getColumnIndex(WorkoutContract.MuscleGroupEntry.COLUMN_MG_NAME);

            String name = data.getString(nameColumnIndext);

            mNameEditText.setText(name);

        }
    }
}
