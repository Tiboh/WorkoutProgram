package com.erikbuto.workoutprogram.Manage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Image;
import com.erikbuto.workoutprogram.DB.Muscle;
import com.erikbuto.workoutprogram.DB.Set;
import com.erikbuto.workoutprogram.MyUtils;
import com.erikbuto.workoutprogram.R;
import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Utilisateur on 24/07/2015.
 */
public class EditMuscleListAdapter extends ArrayAdapter<Muscle> {

    private final Context mContext;
    private String mType;
    private long mExerciseId;
    private ArrayList<Muscle> mItems;
    private int mResourceLayoutId;


    public EditMuscleListAdapter(final Context context, int resourceLayoutId, ArrayList<Muscle> items, String type, long exerciseId) {
        mContext = context;
        mItems = items;
        mResourceLayoutId = resourceLayoutId;
        mType = type;
        mExerciseId = exerciseId;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View rootView = (View) convertView;
        if (rootView == null) {
            rootView = LayoutInflater.from(mContext).inflate(mResourceLayoutId, parent, false);
        }

        final EditText muscleEditText = (EditText) rootView.findViewById(R.id.edit_muscle_name_item);
        final ImageView deleteView = (ImageView) rootView.findViewById(R.id.edit_muscle_delete_item);

        if(position == mItems.size()-1){
            muscleEditText.setText("");
            deleteView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_black));
            deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItems.get(position).setName(muscleEditText.getText().toString());
                    mItems.add(position + 1, new Muscle("", mType, mExerciseId));
                    deleteView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_cross));
                    deleteView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mItems.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                    notifyDataSetChanged();
                }
            });
        }else {
            muscleEditText.setText(mItems.get(position).getName());
            deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItems.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        return rootView;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }
}

