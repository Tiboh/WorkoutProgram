package com.erikbuto.workoutprogram.Manage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erikbuto.workoutprogram.R;
import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.GripView;

import java.util.ArrayList;

/**
 * Created by Utilisateur on 17/07/2015.
 */
public class DynamicListAdapter extends ArrayAdapter<String> {

    private final Context mContext;
    private ArrayList<String> mItems;
    private int mResourceLayoutId;


    public DynamicListAdapter(final Context context, int resourceLayoutId, ArrayList<String> items) {
        mContext = context;
        mItems = items;
        mResourceLayoutId = resourceLayoutId;
        for (int i = 0; i < items.size(); i++) {
            add(mItems.get(i));
        }
    }

    @Override
    public long getItemId(final int position) {
        return getItem(position).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View rootView = (View) convertView;
        if (rootView == null) {
            rootView = LayoutInflater.from(mContext).inflate(mResourceLayoutId, parent, false);
        }
        TextView setSummary = (TextView) rootView.findViewById(R.id.set_summary);
        setSummary.setText(mItems.get(position));

        return rootView;
    }
}