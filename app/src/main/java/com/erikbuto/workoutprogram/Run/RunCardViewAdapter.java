package com.erikbuto.workoutprogram.Run;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Set;
import com.erikbuto.workoutprogram.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Utilisateur on 22/07/2015.
 */
public class RunCardViewAdapter extends RecyclerView.Adapter<RunCardViewHolder> {

    ArrayList<Exercise> mExercises;
    private Context mContext;

    public RunCardViewAdapter(ArrayList<Exercise> list, Context context) {
        this.mExercises = list;
        this.mContext = context;
    }

    @Override
    public RunCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_run_card, viewGroup, false);
        return new RunCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RunCardViewHolder myViewHolder, int position) {
        Exercise myObject = mExercises.get(position);
        DatabaseHandler db = new DatabaseHandler(mContext);
        ArrayList<Set> sets = db.getAllSetsExercise(myObject.getId());
        Collections.sort(sets, new Set.SetComparator());
        myViewHolder.bind(sets, mContext);
    }

    @Override
    public int getItemCount() {
        return mExercises.size();
    }

}