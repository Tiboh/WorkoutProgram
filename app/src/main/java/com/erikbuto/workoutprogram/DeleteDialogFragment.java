package com.erikbuto.workoutprogram;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Program;
import com.erikbuto.workoutprogram.DB.Set;
import com.erikbuto.workoutprogram.Drawer.MainActivity;

import java.util.ArrayList;

/**
 * Created by Utilisateur on 14/07/2015.
 */
public class DeleteDialogFragment extends DialogFragment {

    public static final String ARG_DATA_TYPE = "data_type";
    public static final String ARG_TYPE_EXERCISE = "type_exercise";
    public static final String ARG_TYPE_PROGRAM = "type_program";

    public static final String ARG_PROGRAM_ID = "program_id";
    public static final String ARG_EXERCISE_ID = "exercise_id";

    private Program mProgram;
    private Exercise mExercise;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final DatabaseHandler db = new DatabaseHandler(getActivity());
        if(getArguments().getString(DeleteDialogFragment.ARG_DATA_TYPE) == ARG_TYPE_PROGRAM) {
            mProgram = db.getProgram(getArguments().getLong(DeleteDialogFragment.ARG_PROGRAM_ID));
            builder.setTitle(getString(R.string.action_delete) + " " + mProgram.getName());
        } else {
            mExercise = db.getExercise(getArguments().getLong(DeleteDialogFragment.ARG_EXERCISE_ID));
            builder.setTitle(getString(R.string.action_delete) + " " + mExercise.getName());
        }

        builder.setMessage(R.string.message_delete)
                .setPositiveButton(R.string.abc_action_mode_done, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(getArguments().getString(DeleteDialogFragment.ARG_DATA_TYPE) == ARG_TYPE_PROGRAM) {
                            deleteProgram(mProgram, db);
                        } else {
                            deleteExercise(mExercise, db);
                        }
                        ((MainActivity) getActivity()).updateFrameLayoutView(db.getAllPrograms());
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void deleteProgram(Program program, DatabaseHandler db){
        ArrayList<Exercise> exercises = db.getAllExercisesProgram(mProgram.getId());
        for(int i = 0 ; i < exercises.size() ; i++){
            deleteExercise(exercises.get(i), db);
        }
        db.deleteProgram(program);
    }

    private void deleteExercise(Exercise exercise, DatabaseHandler db){
        ArrayList<Set> sets = db.getAllSetsExercise(exercise.getId());
        for(int i = 0 ; i < sets.size() ; i++){
            db.deleteSet(sets.get(i));
        }
        db.deleteExercise(exercise);
    }
}
