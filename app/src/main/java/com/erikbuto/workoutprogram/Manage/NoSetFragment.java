package com.erikbuto.workoutprogram.Manage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erikbuto.workoutprogram.R;

/**
 * Created by Utilisateur on 23/07/2015.
 */
public class NoSetFragment extends Fragment {

    public static final String ARG_EXERCISE_ID = "exercise_id";

    public NoSetFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nosets, container, false);
        return rootView;
    }
}
