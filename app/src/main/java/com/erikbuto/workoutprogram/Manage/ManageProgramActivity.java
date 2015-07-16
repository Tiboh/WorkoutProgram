package com.erikbuto.workoutprogram.Manage;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Program;
import com.erikbuto.workoutprogram.R;

import java.util.ArrayList;
import java.util.List;


public class ManageProgramActivity extends ActionBarActivity {

    public static final String ARG_PROGRAM_ID = "program_id";

    private long mProgramId;
    private Program mProgram;
    private List<Exercise> mExercises = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_program);

        mProgramId = getIntent().getExtras().getLong(ManageProgramActivity.ARG_PROGRAM_ID);

        DatabaseHandler db = new DatabaseHandler(this);

        mExercises = db.getAllExercisesProgram(mProgramId);
        mProgram = db.getProgram(mProgramId);

        FloatingActionButton buttonAddExercise = (FloatingActionButton) findViewById(R.id.button_add_exercise);
        buttonAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewExerciseNameDialogFragment dialogAddExercise = new NewExerciseNameDialogFragment();
                Bundle arg = new Bundle();
                arg.putLong(NewExerciseNameDialogFragment.ARG_PROGRAM_ID, mProgramId);
                dialogAddExercise.setArguments(arg);
                dialogAddExercise.show(getSupportFragmentManager(), "TAG");
            }
        });

        if (mExercises.size() != 0) {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = new ManageProgramFragment();
            fragmentManager.beginTransaction().replace(R.id.content_frame_program, fragment).commit();

        } else {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = new NoExercisesFragment();
            fragmentManager.beginTransaction().replace(R.id.content_frame_program, fragment).commit();
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        setTitle(mProgram.getName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        DatabaseHandler db = new DatabaseHandler(this);
        db.updateProgram(mProgram);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_program, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Bundle arg = new Bundle();
        switch (item.getItemId()) {
            case R.id.action_delete:
                DeleteDialogFragment dialogDelete = new DeleteDialogFragment();
                arg.putString(DeleteDialogFragment.ARG_DATA_TYPE, DeleteDialogFragment.ARG_TYPE_PROGRAM);
                arg.putLong(DeleteDialogFragment.ARG_PROGRAM_ID, mProgramId);
                dialogDelete.setArguments(arg);
                dialogDelete.show(getSupportFragmentManager(), "TAG");
                return true;
            case R.id.action_set_name:
                SetNameDialogFragment dialogSetName = new SetNameDialogFragment();
                arg.putString(SetNameDialogFragment.ARG_DATA_TYPE, SetNameDialogFragment.ARG_TYPE_PROGRAM);
                arg.putLong(SetNameDialogFragment.ARG_PROGRAM_ID, mProgramId);
                dialogSetName.setArguments(arg);
                dialogSetName.show(getSupportFragmentManager(), "TAG");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
