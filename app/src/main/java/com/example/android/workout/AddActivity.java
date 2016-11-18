package com.example.android.workout;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.workout.data.WorkoutContract.ActivityEntry;

/**
 * Created by Rob on 11/14/2016.
 */

public class AddActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_PRODUCT_LOADER = 0;

    private boolean mProductHasChanged = false;

    private Uri mCurrentActivityUri;

    private String muscleId;

    private EditText mActivityNameEditText;
    private EditText mActivityImageText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muscle_group_edit);

        Intent intent = getIntent();
        muscleId = intent.getExtras().getString("MuscleId");
//        mCurrentActivityUri = intent.getData();

        mActivityNameEditText = (EditText) findViewById(R.id.MuscleNameEditText);
        mActivityImageText = (EditText) findViewById(R.id.MuscleImage);

        if (mCurrentActivityUri == null) {
            setTitle("Add Activity");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit Activity");
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
        if (mCurrentActivityUri == null) {
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
                saveActivity();
                finish();
                return true;
            case R.id.action_delete:
                deleteActivity();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteActivity() {
        if (mCurrentActivityUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentActivityUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"Deleted", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    private void saveActivity() {
        //make sure all fields are filled out
        if (TextUtils.isEmpty(mActivityNameEditText.getText())) {
            Toast.makeText(this, "Name Required", Toast.LENGTH_SHORT).show();
            return;
        }

        //get all data from fields
        String nameString = mActivityNameEditText.getText().toString().trim();
        String imageString = mActivityImageText.getText().toString().trim();

        //put all values into ContentValues
        ContentValues values = new ContentValues();
        values.put(ActivityEntry.COLUMN_ACTIVITY_NAME, nameString);
        values.put(ActivityEntry.COLUMN_ACTIVITY_IMAGE, imageString);
        values.put(ActivityEntry.COLUMN_ACTIVITY_MG_ID, muscleId);

        //if new product, insert values to new row and show Toast, otherwise, update product row
        if (mCurrentActivityUri == null) {
            Uri newUri = getContentResolver().insert(ActivityEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, "insert failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "insert successful", Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(
                    mCurrentActivityUri,
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
                ActivityEntry._ID,
                ActivityEntry.COLUMN_ACTIVITY_NAME,
                ActivityEntry.COLUMN_ACTIVITY_DESCRIPTION,
                ActivityEntry.COLUMN_ACTIVITY_IMAGE,
                ActivityEntry.COLUMN_ACTIVITY_VIDEO,
                ActivityEntry.COLUMN_ACTIVITY_MG_ID

        };
        return new CursorLoader(this,
                mCurrentActivityUri,
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
        mActivityNameEditText.setText("");
        mActivityImageText.setText("");
    }


    private void updateViews(Cursor data) {
        //if the cursor is not null, load data into views
        if (data.moveToFirst()) {
            int nameColumnIndext = data.getColumnIndex(ActivityEntry.COLUMN_ACTIVITY_NAME);
            int imageColumnIndext = data.getColumnIndex(ActivityEntry.COLUMN_ACTIVITY_IMAGE);

            String name = data.getString(nameColumnIndext);
            String image = data.getString(imageColumnIndext);

            mActivityNameEditText.setText(name);
            mActivityImageText.setText(image);
        }
    }
}
