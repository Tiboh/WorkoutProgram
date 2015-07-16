package com.erikbuto.workoutprogram.Manage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Program;
import com.erikbuto.workoutprogram.DB.Set;
import com.erikbuto.workoutprogram.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public class ManageExerciseActivity extends ActionBarActivity {

    public static final String ARG_EXERCISE_ID = "exercise_id";

    private long mExerciseId;
    private Exercise mExercise;
    private List<Set> mSets = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_exercise);

        mExerciseId = getIntent().getExtras().getLong(ManageExerciseActivity.ARG_EXERCISE_ID);

        DatabaseHandler db = new DatabaseHandler(this);

        mSets = db.getAllSetsExercise(mExerciseId);
        mExercise = db.getExercise(mExerciseId);

        if (mSets.size() != 0) {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = new ManageExerciseFragment();
            fragmentManager.beginTransaction().replace(R.id.content_frame_program, fragment).commit();

        } else {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = new NoSetsFragment();
            fragmentManager.beginTransaction().replace(R.id.content_frame_program, fragment).commit();
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        setTitle(mExercise.getName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        DatabaseHandler db = new DatabaseHandler(this);
        db.updateExercise(mExercise);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_exercise, menu);
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
                arg.putString(DeleteDialogFragment.ARG_DATA_TYPE, DeleteDialogFragment.ARG_TYPE_EXERCISE);
                arg.putLong(DeleteDialogFragment.ARG_EXERCISE_ID, mExerciseId);
                dialogDelete.setArguments(arg);
                dialogDelete.show(getSupportFragmentManager(), "TAG");
                return true;
            case R.id.action_set_name:
                SetNameDialogFragment dialogSetName = new SetNameDialogFragment();
                arg.putString(SetNameDialogFragment.ARG_DATA_TYPE, SetNameDialogFragment.ARG_TYPE_EXERCISE);
                arg.putLong(SetNameDialogFragment.ARG_EXERCISE_ID, mExerciseId);
                dialogSetName.setArguments(arg);
                dialogSetName.show(getSupportFragmentManager(), "TAG");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
