package com.erikbuto.workoutprogram.Home;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.R;

/**
 * Created by Utilisateur on 22/07/2015.
 */
public class CardViewHolder extends RecyclerView.ViewHolder {

    private TextView mNameView;
    private ImageView mImageView;

    public CardViewHolder(View itemView) {
        super(itemView);

        mNameView = (TextView) itemView.findViewById(R.id.exercise_name);
        mImageView = (ImageView) itemView.findViewById(R.id.exercise_image);
    }

    public void bind(Exercise myObject) {
        mNameView.setText(myObject.getName());
        // Picasso.with(imageView.getContext()).load(myObject.getImageUrl()).centerCrop().fit().into(imageView);
    }
}