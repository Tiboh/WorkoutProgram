package com.erikbuto.workoutprogram.Run;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Set;
import com.erikbuto.workoutprogram.R;
import com.erikbuto.workoutprogram.Utils.MyUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Utilisateur on 22/07/2015.
 */
public class RunCardViewHolder extends RecyclerView.ViewHolder {

    private LinearLayout mLayout;

    public RunCardViewHolder(View itemView) {
        super(itemView);

        mLayout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
    }

    public void bind(ArrayList<Set> sets, Context context) {
        for(int i = 0 ; i < sets.size() ; i++){
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View child = inflater.inflate(R.layout.activity_run_card_item, null);
            final TextView previousRep = (TextView) child.findViewById(R.id.previous_rep);
            previousRep.setText(MyUtils.stringifySet(sets.get(i), context));
            mLayout.addView(child);
        }
    }
}