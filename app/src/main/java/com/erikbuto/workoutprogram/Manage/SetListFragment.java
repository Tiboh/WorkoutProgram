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
    public static final String TAG_LIST_VIEW_FOOTER = "tag_list_view_footer";
    public static final String TAG_LIST_VIEW_SET = "tag_list_view_set";

    private long mExerciseId;

    public SetListFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_set_list, container, false);

        mExerciseId = getArguments().getLong(SetListFragment.ARG_EXERCISE_ID);

        DatabaseHandler db = new DatabaseHandler(getActivity());
        ArrayList<Set> sets = db.getAllSetsExercise(mExerciseId);
        Collections.sort(sets, new Set.SetComparator());

        DynamicListView dynamicListView = (DynamicListView) rootView.findViewById(R.id.dynamic_list_view);
        dynamicListView.enableDragAndDrop();
        dynamicListView.setDraggableManager(new TouchViewDraggableManager(R.id.grip_view));
        DynamicListAdapter adapter = new DynamicListAdapter(getActivity(), R.layout.fragment_set_list_item, sets);
        dynamicListView.setAdapter(adapter);

        View footerView = inflater.inflate(R.layout.add_footer_view, dynamicListView, false);
        footerView.setTag(TAG_LIST_VIEW_FOOTER);
        dynamicListView.addFooterView(footerView);
        dynamicListView.setOnItemMovedListener(new DynamicListViewOnItemMovedListener(adapter));
        dynamicListView.setOnItemClickListener(new DynamicListViewOnItemClickListener(dynamicListView, mExerciseId, adapter));

        return rootView;
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
            if (view.getTag() == SetListFragment.TAG_LIST_VIEW_FOOTER) {
                NewSetDialogFragment dialog = new NewSetDialogFragment();
                Bundle arg = new Bundle();
                arg.putLong(NewSetDialogFragment.ARG_EXERCISE_ID, mExerciseId);
                arg.putInt(NewSetDialogFragment.ARG_POSITION, position);
                if (position > 0) {
                    arg.putLong(NewSetDialogFragment.ARG_PREVIOUS_SET_ID, ((Set) mAdapter.getItem(position - 1)).getId());
                }
                dialog.setArguments(arg);
                dialog.setmAdapter(mAdapter);
                dialog.show(getActivity().getSupportFragmentManager(), "TAG");
            } else {
                EditSetDialogFragment dialog = new EditSetDialogFragment();
                Bundle arg = new Bundle();
                arg.putLong(EditSetDialogFragment.ARG_SET_ID, ((Set) mAdapter.getItem(position)).getId());
                dialog.setArguments(arg);
                dialog.setmAdapter(mAdapter);
                dialog.show(getActivity().getSupportFragmentManager(), "TAG");
            }
        }
    }
}
