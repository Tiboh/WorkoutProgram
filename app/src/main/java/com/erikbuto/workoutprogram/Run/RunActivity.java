package com.erikbuto.workoutprogram.Run;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Program;
import com.erikbuto.workoutprogram.Home.HomeCardViewAdapter;
import com.erikbuto.workoutprogram.Home.RecyclerItemClickListener;
import com.erikbuto.workoutprogram.Manage.ManageExerciseActivity;
import com.erikbuto.workoutprogram.R;

import java.util.ArrayList;
import java.util.Collections;


public class RunActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private TextView mExerciseNameView;
    private NumberPicker mRepPicker;
    private NumberPicker mWeightPicker;
    private Chronometer mChrono;
    private FloatingActionButton mFloatingActionButton;
    private RecyclerView mRecyclerView;
    private RunCardViewAdapter mCardViewAdapter;

    public static final String ARG_PROGRAM_ID = "program_id";

    private static final int RUNNING_STATE = 0; // User is performing exercise
    private static final int RESTING_STATE = 1; // User is resting (between each set and exercise)
    private static final int PAUSING_STATE = 2; // User pauses the resting state

    private Program mProgram;
    private ArrayList<Exercise> mExercises;
    private int currentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        DatabaseHandler db = new DatabaseHandler(this);
        mProgram = db.getProgram(getIntent().getExtras().getLong(RunActivity.ARG_PROGRAM_ID));
        mExercises = db.getAllExercisesProgram(mProgram.getId());
        Collections.sort(mExercises, new Exercise.ExerciseComparator());

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_run));
        getSupportActionBar().setSubtitle(mProgram.getName());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCardViewAdapter = new RunCardViewAdapter(mExercises, this);
        mRecyclerView.setAdapter(mCardViewAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        /*Intent intent = new Intent(getActivity(), ManageExerciseActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.putExtra(ManageExerciseActivity.ARG_EXERCISE_ID, mExercises.get(position).getId());
                        startActivity(intent);*/
                    }
                })
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_run, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
