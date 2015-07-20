package com.erikbuto.workoutprogram.Manage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Program;
import com.erikbuto.workoutprogram.DB.Set;
import com.erikbuto.workoutprogram.R;
import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Utilisateur on 14/07/2015.
 */
public class NewSetDialogFragment extends DialogFragment {

    public static final String ARG_EXERCISE_ID = "exercise_id";
    public static final String ARG_POSITION = "position";

    private long mExerciseId;

    private ArrayAdapter mAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final DatabaseHandler db = new DatabaseHandler(getActivity());
        mExerciseId = getArguments().getLong(NewSetDialogFragment.ARG_EXERCISE_ID);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.set_dialog_view, null);
        builder.setView(view);

        final EditText valueRep = (EditText) view.findViewById(R.id.value_rep);
        final EditText valueWeight = (EditText) view.findViewById(R.id.value_weight);
        final EditText valueRest = (EditText) view.findViewById(R.id.value_rest);

        builder.setTitle(R.string.new_set)
                .setPositiveButton(R.string.abc_action_mode_done, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int position = getArguments().getInt(NewSetDialogFragment.ARG_POSITION);
                        DatabaseHandler db = new DatabaseHandler(getActivity());
                        Set set = new Set(Integer.valueOf(valueRep.getText().toString()), Integer.valueOf(valueWeight.getText().toString()), Integer.valueOf(valueRest.getText().toString()), Integer.valueOf(valueRest.getText().toString()), mExerciseId, position);
                        long setId = db.updateSet(set);
                        set.setId(setId);
                        mAdapter.add(set);
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        // Create the AlertDialog object and return it
        final AlertDialog dialog = builder.create();
        return dialog;
    }

    public void setmAdapter(ArrayAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }
}
