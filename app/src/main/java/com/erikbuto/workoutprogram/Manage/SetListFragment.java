package com.erikbuto.workoutprogram.Manage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Set;
import com.erikbuto.workoutprogram.R;
import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.OnItemMovedListener;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Utilisateur on 21/07/2015.
 */
public class SetListFragment extends Fragment {

    public static final String ARG_EXERCISE_ID = "exercise_id";

    private DynamicListAdapter mAdapter;

    // Default value for a new Set
    public static final int NB_REP_DEFAULT = 10;
    public static final int WEIGHT_DEFAULT = 50;
    public static final int REST_MINUTE_DEFAULT = 1;
    public static final int REST_SECONDS_DEFAULT = 30;

    private long mExerciseId;
    private ArrayList<Set> mSets;

    public SetListFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_set_list, container, false);
        mExerciseId = getArguments().getLong(SetListFragment.ARG_EXERCISE_ID);

        DatabaseHandler db = new DatabaseHandler(getActivity());
        mSets = db.getAllSetsExercise(mExerciseId);
        Collections.sort(mSets, new Set.SetComparator());
        DynamicListView dynamicListView = (DynamicListView) rootView.findViewById(R.id.dynamic_list_view);
        dynamicListView.enableDragAndDrop();
        dynamicListView.setDraggableManager(new TouchViewDraggableManager(R.id.grip_view));
        mAdapter = new DynamicListAdapter(getActivity(), R.layout.fragment_set_list_item, mSets, this);
        dynamicListView.setAdapter(mAdapter);
        dynamicListView.setOnItemMovedListener(new DynamicListViewOnItemMovedListener(mAdapter));
        dynamicListView.setOnItemClickListener(new DynamicListViewOnItemClickListener(dynamicListView, mExerciseId, mAdapter));

        return rootView;
    }

    public void removeSet(Set set) {
        ((ManageExerciseActivity) getActivity()).removeSet(set);
    }

    public void addNewSet() {
        DatabaseHandler db = new DatabaseHandler(getActivity());
        if (mSets == null) {
            mSets = db.getAllSetsExercise(mExerciseId);
        }
        int position = mSets.size();
        Set newSet;
        if (position != 0) { // Adapter get already items in the list
            Set previous = mSets.get(position - 1);
            newSet = new Set(previous.getNbRep(), previous.getWeight(), previous.getRestTimeMinute(), previous.getRestTimeSecond(), mExerciseId, position); // Get Set previous values
        } else {
            newSet = new Set(NB_REP_DEFAULT, WEIGHT_DEFAULT, REST_MINUTE_DEFAULT, REST_SECONDS_DEFAULT, mExerciseId, position);
        }
        db.addSet(newSet);
        mSets.add(position, newSet);
        mAdapter.add(newSet);
    }

    private class DynamicListViewOnItemMovedListener implements OnItemMovedListener {
        private ArrayAdapter<Set> mAdapter;

        DynamicListViewOnItemMovedListener(ArrayAdapter<Set> adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onItemMoved(final int originalPosition, final int newPosition) {
            ArrayList<Set> swapped = swapSets((ArrayList<Set>) mAdapter.getItems(), originalPosition, newPosition);
            updateDBSet(swapped);
        }
    }

    public ArrayList<Set> swapSets(ArrayList<Set> sets, int indexOne, int indexTwo) {
        for (int i = 0; i < sets.size(); i++) {
            sets.get(i).setPosition(i);
        }
        return sets;
    }

    private void updateDBSet(ArrayList<Set> sets) {
        DatabaseHandler db = new DatabaseHandler(getActivity());
        for (int i = 0; i < sets.size(); i++) {
            db.updateSet(sets.get(i));
        }
    }


    private class DynamicListViewOnItemClickListener implements AdapterView.OnItemClickListener {

        private final DynamicListView mListView;
        private long mExerciseId;
        private ArrayAdapter mAdapter;

        DynamicListViewOnItemClickListener(final DynamicListView listView, long exerciseId, final ArrayAdapter adapter) {
            mListView = listView;
            mExerciseId = exerciseId;
            mAdapter = adapter;
        }

        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
            EditSetDialogFragment dialog = new EditSetDialogFragment();
            Bundle arg = new Bundle();
            arg.putLong(EditSetDialogFragment.ARG_SET_ID, ((Set) mAdapter.getItem(position)).getId());
            dialog.setArguments(arg);
            dialog.setmAdapter(mAdapter);
            dialog.show(getActivity().getSupportFragmentManager(), "TAG");
        }
    }
}
