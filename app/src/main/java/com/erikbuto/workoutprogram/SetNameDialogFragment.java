package com.erikbuto.workoutprogram;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Program;
import com.erikbuto.workoutprogram.Drawer.MainActivity;

/**
 * Created by Utilisateur on 14/07/2015.
 */
public class SetNameDialogFragment extends DialogFragment {

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
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.edit_text_view, null);
        builder.setView(view);
        final EditText editText = (EditText) view.findViewById(R.id.set_name_view);

        final DatabaseHandler db = new DatabaseHandler(getActivity());
        if(getArguments().getString(SetNameDialogFragment.ARG_DATA_TYPE) == ARG_TYPE_PROGRAM) {
            mProgram = db.getProgram(getArguments().getLong(SetNameDialogFragment.ARG_PROGRAM_ID));
            editText.setText(mProgram.getName());
        } else {
            mExercise = db.getExercise(getArguments().getLong(SetNameDialogFragment.ARG_EXERCISE_ID));
            editText.setText(mExercise.getName());
        }

        builder.setTitle(R.string.edit_name)
                .setPositiveButton(R.string.abc_action_mode_done, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String fixed = editText.getText().toString().replaceAll("(\\s)+"," ");

                        if(getArguments().getString(SetNameDialogFragment.ARG_DATA_TYPE) == ARG_TYPE_PROGRAM) {
                            mProgram.setName(fixed);
                            db.updateProgram(mProgram);
                            ((MainActivity) getActivity()).onProgramNameChanged(mProgram, fixed);
                        } else {
                            mExercise.setName(fixed);
                            db.updateExercise(mExercise);
                            getActivity().setTitle(fixed);
                        }

                        SetNameDialogFragment.this.getDialog().dismiss();
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        SetNameDialogFragment.this.getDialog().cancel();
                    }
                });

        // Create the AlertDialog object and return it
        final AlertDialog dialog = builder.create();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isGraphic(s)) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });

        return dialog;
    }
}
