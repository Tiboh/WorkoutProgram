package com.erikbuto.workoutprogram.Manage;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Set;
import com.erikbuto.workoutprogram.MyUtils;
import com.erikbuto.workoutprogram.R;
import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Utilisateur on 17/07/2015.
 */
public class DynamicListAdapter extends ArrayAdapter<Set> {

    private final Context mContext;
    private SetListFragment mParentFragment;

    private ArrayList<Set> mItems;
    private int mResourceLayoutId;


    public DynamicListAdapter(final Context context, int resourceLayoutId, ArrayList<Set> items, SetListFragment parentFragment) {
        mContext = context;
        mItems = items;
        mParentFragment = parentFragment;
        mResourceLayoutId = resourceLayoutId;
        for (int i = 0; i < items.size(); i++) {
            add(mItems.get(i));
        }
    }

    public void setItem(int position, Set item) {
        mItems.add(position, item);
        mItems.remove(position + 1);
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
        setSummary.setText(MyUtils.stringifySet(mItems.get(position), mContext.getString(R.string.set_summary_divider), mContext.getString(R.string.weight_unit), mContext.getString(R.string.label_rep)));

        ImageView deleteButton = (ImageView) rootView.findViewById(R.id.action_delete_set);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(mContext);
                Set set = mItems.get(position);
                db.deleteSet(set);
                DynamicListAdapter.this.remove(position);
                mParentFragment.removeSet(set);
            }
        });

        return rootView;
    }
}