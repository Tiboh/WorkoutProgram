package com.erikbuto.workoutprogram.Manage;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.Home.MainActivity;
import com.erikbuto.workoutprogram.Home.NewProgramNameDialogFragment;
import com.erikbuto.workoutprogram.R;

/**
 * Created by Utilisateur on 14/07/2015.
 */
public class NoExercisesFragment extends Fragment {

    public static final String ARG_PROGRAM_ID = "program_id";
    private long mProgramId;

    public NoExercisesFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_noexercises, container, false);

        mProgramId = getArguments().getLong(NoExercisesFragment.ARG_PROGRAM_ID);
        DatabaseHandler db = new DatabaseHandler(getActivity());
        if(db.getProgram(mProgramId).getName().equals(getString(R.string.new_program))){
            Bundle arg = new Bundle();
            NewProgramNameDialogFragment dialogSetName = new NewProgramNameDialogFragment();
            arg.putLong(NewProgramNameDialogFragment.ARG_PROGRAM_ID, mProgramId);
            dialogSetName.setArguments(arg);
            dialogSetName.show(((MainActivity) getActivity()).getSupportFragmentManager(), "TAG");
        }

        ((ImageView) rootView.findViewById(R.id.image)).setImageResource(R.drawable.noprogram);
        return rootView;
    }
}