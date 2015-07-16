package com.erikbuto.workoutprogram.Home;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.erikbuto.workoutprogram.DB.Program;
import com.erikbuto.workoutprogram.Manage.ManageProgramActivity;
import com.erikbuto.workoutprogram.R;

/**
 * Created by Utilisateur on 14/07/2015.
 */
public class NewProgramNameDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.edit_text_view, null);
        builder.setView(view);

        final EditText editText = (EditText) view.findViewById(R.id.set_name_view);

        builder.setTitle(R.string.new_program)
                .setPositiveButton(R.string.abc_action_mode_done, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String fixed = editText.getText().toString().replaceAll("(\\s)+"," ");
                        Program newP = new Program(fixed);
                        DatabaseHandler db = new DatabaseHandler(getActivity());
                        long programId = db.addProgram(newP);
                        Intent intent = new Intent(getActivity().getBaseContext(), ManageProgramActivity.class);
                        intent.putExtra(ManageProgramActivity.ARG_PROGRAM_ID, programId);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        NewProgramNameDialogFragment.this.getDialog().cancel();
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