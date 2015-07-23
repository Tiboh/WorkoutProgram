package com.erikbuto.workoutprogram.Manage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Set;
import com.erikbuto.workoutprogram.R;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Utilisateur on 23/07/2015.
 */
public class OverviewFragment extends Fragment {

    public static final String ARG_EXERCISE_ID = "exercise_id";
    private Exercise mExercise;

    public OverviewFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_overview, container, false);

        DatabaseHandler db = new DatabaseHandler(getActivity());
        mExercise = db.getExercise(getArguments().getLong(OverviewFragment.ARG_EXERCISE_ID));

        if(!mExercise.getImageUrl().isEmpty()){
            LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.overview_image_view, null);

            // Insert into main view
            ViewGroup insertPoint = (ViewGroup) rootView.findViewById(R.id.overview_content);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        }

        if (!mExercise.getDescription().isEmpty()) {
            LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.overview_description_view, null);

            // Fill in any details dynamically here
            TextView textDescription = (TextView) v.findViewById(R.id.overview_description);
            textDescription.setText(mExercise.getDescription());

            // Insert into main view
            ViewGroup insertPoint = (ViewGroup) rootView.findViewById(R.id.overview_content);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        }

        return rootView;
    }
}
