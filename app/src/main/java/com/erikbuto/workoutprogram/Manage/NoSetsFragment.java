package com.erikbuto.workoutprogram.Manage;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.erikbuto.workoutprogram.R;

/**
 * Created by Utilisateur on 14/07/2015.
 */
public class NoSetsFragment extends Fragment {

    public NoSetsFragment() {
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
