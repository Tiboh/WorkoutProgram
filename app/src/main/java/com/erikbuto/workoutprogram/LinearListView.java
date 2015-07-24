package com.erikbuto.workoutprogram;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Utilisateur on 25/07/2015.
 */
public class LinearListView extends LinearLayout {
    Adapter adapter;
    Observer observer = new Observer(this);

    public LinearListView(Context context) {
        super(context);
    }

    public LinearListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAdapter(Adapter adapter) {
        if (this.adapter != null)
            this.adapter.unregisterDataSetObserver(observer);

        this.adapter = adapter;
        adapter.registerDataSetObserver(observer);
        observer.onChanged();
    }

    private class Observer extends DataSetObserver {
        LinearListView context;

        public Observer(LinearListView context) {
            this.context = context;
        }

        @Override
        public void onChanged() {
            List<View> oldViews = new ArrayList<View>(context.getChildCount());

            for (int i = 0; i < context.getChildCount(); i++)
                oldViews.add(context.getChildAt(i));

            Iterator<View> iter = oldViews.iterator();

            context.removeAllViews();

            for (int i = 0; i < context.adapter.getCount(); i++) {
                View convertView = iter.hasNext() ? iter.next() : null;
                context.addView(context.adapter.getView(i, convertView, context));
            }
            super.onChanged();
        }

        @Override
        public void onInvalidated() {
            context.removeAllViews();
            super.onInvalidated();
        }
    }
}
