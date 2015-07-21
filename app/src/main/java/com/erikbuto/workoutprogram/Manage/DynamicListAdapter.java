package com.erikbuto.workoutprogram.Manage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Set;
import com.erikbuto.workoutprogram.MyUtils;
import com.erikbuto.workoutprogram.R;
import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Utilisateur on 17/07/2015.
 */
public class DynamicListAdapter extends ArrayAdapter<Set> {

    private final Context mContext;

    private ArrayList<Set> mItems;
    private int mResourceLayoutId;


    public DynamicListAdapter(final Context context, int resourceLayoutId, ArrayList<Set> items) {
        mContext = context;
        mItems = items;
        mResourceLayoutId = resourceLayoutId;
        for (int i = 0; i < items.size(); i++) {
            add(mItems.get(i));
        }
    }

    public ArrayList<Set> getmItems() {
        return mItems;
    }

    public void setItem(int position, Set item) {
        mItems.add(position, item);
        mItems.remove(position+1);
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
        rootView.setTag(SetListFragment.TAG_LIST_VIEW_SET);
        TextView setSummary = (TextView) rootView.findViewById(R.id.set_summary);
        setSummary.setText(MyUtils.stringifySet(mItems.get(position), mContext.getString(R.string.set_summary_divider), mContext.getString(R.string.weight_unit), mContext.getString(R.string.label_rep)));

        ImageView deleteButton = (ImageView) rootView.findViewById(R.id.action_delete_set);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseHandler db = new DatabaseHandler(mContext);
                Set set = mItems.get(position);
                db.deleteSet(set);
                mItems.remove(set);
                DynamicListAdapter.this.remove(position);
            }
        });

        return rootView;
    }
}