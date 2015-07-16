package com.erikbuto.workoutprogram;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.Exercise;

import java.util.List;

/**
 * Created by Utilisateur on 13/07/2015.
 */

public class TestExerciseCardAdapter extends RecyclerView.Adapter<TestExerciseCardAdapter.ViewHolder> {
    private List<Exercise> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView mRootView;
        public TextView mNameView;
        public TextView mDescriptionView;

        public ViewHolder(CardView v) {
            super(v);
            mRootView = v;
            mNameView = (TextView) mRootView.findViewById(R.id.exercise_name);
            mDescriptionView = (TextView) mRootView.findViewById(R.id.exercise_description);
        }
    }

    public List<Exercise> getmDataset() {
        return mDataset;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TestExerciseCardAdapter(List<Exercise> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TestExerciseCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        CardView rootView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_manage_program_item, parent, false);
        ViewHolder vh = new ViewHolder(rootView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mNameView.setText(mDataset.get(position).getName());
        holder.mDescriptionView.setText(mDataset.get(position).getDescription());

    }

    @Override
    public long getItemId(int position) {
        return mDataset.get(position).getId();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
