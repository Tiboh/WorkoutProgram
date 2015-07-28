package com.erikbuto.workoutprogram.Home;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Image;
import com.erikbuto.workoutprogram.R;
import com.nostra13.universalimageloader.core.ImageLoader;

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

    public void bind(Exercise myObject, String url, String scaleType, Context context) {
        mNameView.setText(myObject.getName());
        ImageLoader imageLoader = ImageLoader.getInstance();
        switch(scaleType){
            case CardViewAdapter.FROM_INTERNAL_STORAGE:
                imageLoader.displayImage("file://" + context.getFilesDir() + "/" + url, mImageView);
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
            case CardViewAdapter.FROM_CACHE:
                imageLoader.displayImage("file://" + url, mImageView);
                mImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                break;
        }
    }
}