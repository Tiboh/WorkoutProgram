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

/**
 * Created by Utilisateur on 22/07/2015.
 */
public class CardViewAdapter extends RecyclerView.Adapter<CardViewHolder> {

    ArrayList<Exercise> list;
    private Context mContext;

    public static final String CENTER_CROP = "center_crop";
    public static final String CENTER_INSIDE = "center_inside";

    public CardViewAdapter(ArrayList<Exercise> list, Context context) {
        this.list = list;
        this.mContext = context;
    }

    public void replaceItems(ArrayList<Exercise> newItems){
        this.list = newItems;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_manage_program_item,viewGroup,false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder myViewHolder, int position) {
        Exercise myObject = list.get(position);
        DatabaseHandler db = new DatabaseHandler(mContext);
        ArrayList<Image> images = db.getAllImagesExercise(myObject.getId());
        if(!images.isEmpty()){
            myViewHolder.bind(myObject, MyUtils.getImageFromInternalStorage(images.get(0).getUrl(), mContext), CENTER_CROP);
        }else{
            final LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final TextView textView = (TextView) layoutInflater.inflate(R.layout.big_letter_image, null);
            textView.setText(myObject.getName().substring(0,1)); // Get first letter
            myViewHolder.bind(myObject, (Bitmap) MyUtils.convertViewToDrawable(textView), CENTER_INSIDE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}