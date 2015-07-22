package com.erikbuto.workoutprogram.Home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.R;

import java.util.ArrayList;

/**
 * Created by Utilisateur on 22/07/2015.
 */
public class CardViewAdapter extends RecyclerView.Adapter<CardViewHolder> {

    ArrayList<Exercise> list;

    public CardViewAdapter(ArrayList<Exercise> list) {
        this.list = list;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_manage_program_item,viewGroup,false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder myViewHolder, int position) {
        Exercise myObject = list.get(position);
        myViewHolder.bind(myObject);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}