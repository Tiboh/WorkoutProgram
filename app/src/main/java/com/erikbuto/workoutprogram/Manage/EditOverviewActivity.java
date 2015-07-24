package com.erikbuto.workoutprogram.Manage;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Image;
import com.erikbuto.workoutprogram.DB.Muscle;
import com.erikbuto.workoutprogram.R;

import java.util.ArrayList;

public class EditOverviewActivity extends ActionBarActivity {

    public static final String ARG_EXERCISE_ID = "exercise_id";
    private Toolbar mToolbar;

    private Exercise mExercise;
    private ArrayList<Image> mImages;
    private ArrayList<Muscle> mPrimaryMuscles;
    private ArrayList<Muscle> mSecondaryMuscles;

    private EditText mDescriptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_overview);

        DatabaseHandler db = new DatabaseHandler(this);
        mExercise = db.getExercise(getIntent().getExtras().getLong(OverviewFragment.ARG_EXERCISE_ID));
        mImages = db.getAllImagesExercise(mExercise.getId());
        mPrimaryMuscles = db.getAllPrimaryMuscleExercise(mExercise.getId());
        mSecondaryMuscles = db.getAllSecondaryMuscleExercise(mExercise.getId());

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_edit_overview));
        getSupportActionBar().setSubtitle(mExercise.getName());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText mDescriptionView = (EditText) findViewById(R.id.edit_overview_description);
        mDescriptionView.setText(mExercise.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                //// RETURN ACTIVITY WITH RESULT
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
