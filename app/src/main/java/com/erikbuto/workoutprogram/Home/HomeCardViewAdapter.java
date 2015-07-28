package com.erikbuto.workoutprogram.Home;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Image;
import com.erikbuto.workoutprogram.Utils.MyUtils;
import com.erikbuto.workoutprogram.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Utilisateur on 22/07/2015.
 */
public class HomeCardViewAdapter extends RecyclerView.Adapter<HomeCardViewHolder> {

    ArrayList<Exercise> list;
    private Context mContext;

    public static final String FROM_INTERNAL_STORAGE = "internal";
    public static final String FROM_CACHE = "cache";

    public HomeCardViewAdapter(ArrayList<Exercise> list, Context context) {
        this.list = list;
        this.mContext = context;
    }

    public void replaceItems(ArrayList<Exercise> newItems){
        this.list = newItems;
    }

    @Override
    public HomeCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_manage_program_item,viewGroup,false);
        return new HomeCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeCardViewHolder myViewHolder, int position) {
        Exercise myObject = list.get(position);
        DatabaseHandler db = new DatabaseHandler(mContext);
        ArrayList<Image> images = db.getAllImagesExercise(myObject.getId());
        if(!images.isEmpty()){
            Collections.sort(images, new Image.ImageComparator());
            myViewHolder.bind(myObject, images.get(0).getUrl(), FROM_INTERNAL_STORAGE, mContext);
        }else{
            final LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final TextView textView = (TextView) layoutInflater.inflate(R.layout.big_letter_image, null);
            textView.setText(myObject.getName().substring(0, 1)); // Get first letter
            Bitmap letterBitmap = (Bitmap) MyUtils.convertViewToDrawable(textView);
            String url = MyUtils.saveImageToCache(mContext, letterBitmap, MyUtils.generateImageUrl());
            myViewHolder.bind(myObject, url, FROM_CACHE, mContext);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}