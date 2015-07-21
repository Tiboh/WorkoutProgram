package com.erikbuto.workoutprogram.Manage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Set;
import com.erikbuto.workoutprogram.R;
import com.nhaarman.listviewanimations.ArrayAdapter;

/**
 * Created by Utilisateur on 14/07/2015.
 */
public class EditSetDialogFragment extends DialogFragment {

    public static final String ARG_SET_ID = "set_id";

    private long mSetId;
    private Set mSet;

    private ArrayAdapter mAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final DatabaseHandler db = new DatabaseHandler(getActivity());
        mSetId = getArguments().getLong(EditSetDialogFragment.ARG_SET_ID);
        mSet = db.getSet(mSetId);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.set_dialog_view, null);
        builder.setView(view);

        final EditText valueRep = (EditText) view.findViewById(R.id.value_rep);
        final EditText valueWeight = (EditText) view.findViewById(R.id.value_weight);
        final EditText valueRestMinute = (EditText) view.findViewById(R.id.value_rest_minute);
        final EditText valueRestSecond = (EditText) view.findViewById(R.id.value_rest_second);

        valueRep.setText(Integer.toString(mSet.getNbRep()));
        valueWeight.setText(Integer.toString(mSet.getWeight()));
        valueRestMinute.setText(Integer.toString(mSet.getRestTimeMinute()));
        valueRestSecond.setText(Integer.toString(mSet.getRestTimeSecond()));

        builder.setTitle(R.string.edit_set)
                .setPositiveButton(R.string.action_edit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mSet.setNbRep(Integer.valueOf(valueRep.getText().toString()));
                        mSet.setWeight(Integer.valueOf(valueWeight.getText().toString()));
                        mSet.setRestTimeMinute(Integer.valueOf(valueRestMinute.getText().toString()));
                        mSet.setRestTimeSecond(Integer.valueOf(valueRestSecond.getText().toString()));

                        db.updateSet(mSet);
                        ((DynamicListAdapter) mAdapter).setItem(mSet.getPosition(), mSet);
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
