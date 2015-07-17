package com.erikbuto.workoutprogram.Home;

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
import android.widget.EditText;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Program;
import com.erikbuto.workoutprogram.R;

import java.util.ArrayList;

/**
 * Created by Utilisateur on 14/07/2015.
 */
public class NewProgramNameDialogFragment extends DialogFragment {

    public static final String ARG_PROGRAM_ID = "program_id";

    private long mProgramId;
    private Program mProgram;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final DatabaseHandler db = new DatabaseHandler(getActivity());
        mProgramId = getArguments().getLong(NewProgramNameDialogFragment.ARG_PROGRAM_ID);
        mProgram = db.getProgram(mProgramId);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.edit_text_view, null);
        builder.setView(view);

        final EditText editText = (EditText) view.findViewById(R.id.set_name_view);

        builder.setTitle(R.string.new_program)
                .setPositiveButton(R.string.abc_action_mode_done, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String fixed = editText.getText().toString().replaceAll("(\\s)+", " ");
                        mProgram.setName(fixed);
                        db.updateProgram(mProgram);
                        getActivity().setTitle(fixed);
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cancelNewProgramCreation();
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

    /*TO DO
    public void OnBackPressed(){
        cancelNewProgramCreation();
    }*/

    public void cancelNewProgramCreation(){
        DatabaseHandler db = new DatabaseHandler(getActivity());
        db.deleteProgram(mProgram);
        ArrayList<Program> programs = db.getAllPrograms();
        ((MainActivity) getActivity()).getmDrawer().removeItem(programs.size()); // Delete New program
        ((MainActivity) getActivity()).updateFrameLayoutView(programs); // Go to previous go program
    }
}