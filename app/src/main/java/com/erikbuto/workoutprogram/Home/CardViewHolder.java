package com.erikbuto.workoutprogram.Home;

import android.graphics.Bitmap;
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

    public void bind(Exercise myObject, Bitmap firstImage, String scaleType) {
        mNameView.setText(myObject.getName());
        if(firstImage != null) {
            mImageView.setImageBitmap(firstImage);
        }
        switch(scaleType){
            case CardViewAdapter.CENTER_CROP:
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
            case CardViewAdapter.CENTER_INSIDE:
                mImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                break;
        }
    }
}