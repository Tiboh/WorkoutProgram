package com.erikbuto.workoutprogram.Drawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.erikbuto.workoutprogram.R;

/**
 * Created by Utilisateur on 14/07/2015.
 */

public class NoProgramsFragment extends Fragment {

    public NoProgramsFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_noprograms, container, false);

        ((ImageView) rootView.findViewById(R.id.image)).setImageResource(R.drawable.noprogram);
        getActivity().setTitle(R.string.no_program);
        return rootView;
    }
}

