package com.erikbuto.workoutprogram.Home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.Drawer.MainActivity;
import com.erikbuto.workoutprogram.Drawer.NewProgramNameDialogFragment;
import com.erikbuto.workoutprogram.R;

/**
 * Created by Utilisateur on 14/07/2015.
 */
public class NoExercisesFragment extends Fragment {

    public static final String ARG_PROGRAM_ID = "program_id";

    public NoExercisesFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_noexercises, container, false);

        ((ImageView) rootView.findViewById(R.id.image)).setImageResource(R.drawable.noprogram);
        return rootView;
    }
}