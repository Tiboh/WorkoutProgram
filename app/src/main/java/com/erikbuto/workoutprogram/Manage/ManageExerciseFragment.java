package com.erikbuto.workoutprogram.Manage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Set;
import com.erikbuto.workoutprogram.MyUtils;
import com.erikbuto.workoutprogram.R;
import com.erikbuto.workoutprogram.TestExerciseCardAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Utilisateur on 14/07/2015.
 */
public class ManageExerciseFragment extends Fragment {
    private GridLayout mGrid;
    private ScrollView mScrollView;
    private ValueAnimator mAnimator;
    private AtomicBoolean mIsScrolling = new AtomicBoolean(false);

    private int mIndexStart;

    private ArrayList<Set> mSets;


    public static final String ARG_EXERCISE_ID = "exercise_id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_exercise, container, false);
        mScrollView = (ScrollView) rootView.findViewById(R.id.scroll_view);
        mScrollView.setSmoothScrollingEnabled(true);

        final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        mGrid = (GridLayout) rootView.findViewById(R.id.grid_layout);
        mGrid.setOnDragListener(new DragListener());
        DatabaseHandler db = new DatabaseHandler(getActivity());
        mSets = db.getAllSetsExercise(getActivity().getIntent().getExtras().getLong(ManageExerciseFragment.ARG_EXERCISE_ID));
        Collections.sort(mSets, new Set.SetComparator());

        for(int i = 0 ; i < mSets.size() ; i++){
            View itemView = layoutInflater.inflate(R.layout.fragment_manage_exercise_item, mGrid, false);

            TextView labelWeight = (TextView) itemView.findViewById(R.id.label_weight);
            labelWeight.setText(R.string.label_weight + "(" + R.string.weight_unit + ")");

            NumberPicker pickerRep = (NumberPicker) itemView.findViewById(R.id.picker_rep);
            NumberPicker pickerWeight = (NumberPicker) itemView.findViewById(R.id.picker_weight);
            TextView restValue = (TextView) itemView.findViewById(R.id.rest_value);

            pickerRep.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                }
            });

            pickerWeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                }
            });

            restValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // open dialog time picker
                }
            });

            pickerRep.setValue(mSets.get(i).getNbRep());
            pickerRep.setMinValue(0);
            pickerWeight.setValue(mSets.get(i).getNbRep());
            pickerWeight.setMinValue(0);
            restValue.setText(MyUtils.stringifyRestTime(mSets.get(i).getRestTimeMinute(), mSets.get(i).getRestTimeSecond()));

            itemView.setOnLongClickListener(new LongPressListener());
            mGrid.addView(itemView);
        }

        return rootView;
    }

    private class DragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            final View view = (View) event.getLocalState();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    // do nothing if hovering above own position
                    if (view == v) return true;
                    // get the new list index
                    final int index = calculateNewIndex(event.getX(), event.getY());

                    final int scrollY = mScrollView.getScrollY();
                    final Rect rect = new Rect();
                    mScrollView.getHitRect(rect);

                    if (event.getY() -  scrollY > mScrollView.getBottom() - 250) {
                        startScrolling(scrollY, mGrid.getHeight());
                    } else if (event.getY() - scrollY < mScrollView.getTop() + 250) {
                        startScrolling(scrollY, 0);
                    } else {
                        stopScrolling();
                    }

                    // remove the view from the old position
                    mGrid.removeView(view);

                    // and push to the new
                    mGrid.addView(view, index);

                    if(mIndexStart != index) {
                        swapElements(mIndexStart, index);
                        mIndexStart = index;
                    }

                    break;
                case DragEvent.ACTION_DROP:
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (!event.getResult()) {
                        view.setVisibility(View.VISIBLE);
                    }
                    updateDB(mSets);
                    break;
            }
            return true;
        }
    }

    private void swapElements(int indexOne, int indexTwo) {
        Set temp = mSets.get(indexOne);

        temp.setPosition(indexTwo);
        mSets.get(indexTwo).setPosition(indexOne);

        mSets.set(indexOne, mSets.get(indexTwo));
        mSets.set(indexTwo, temp);
    }

    private void updateDB(ArrayList<Set> sets){
        DatabaseHandler db = new DatabaseHandler(getActivity());
        for(int i = 0 ; i < sets.size() ; i++){
            db.updateSet(sets.get(i));
        }
    }

    private class LongPressListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            final ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);

            mIndexStart = calculateNewIndex(view.getX(), view.getY());

            return true;
        }
    }

    private int calculateNewIndex(float x, float y) {
        // calculate which column to move to
        final float cellWidth = mGrid.getWidth() / mGrid.getColumnCount();
        final int column = (int)(x / cellWidth);

        // calculate which row to move to
        final float cellHeight = mGrid.getHeight() / mGrid.getRowCount();
        final int row = (int)Math.floor(y / cellHeight);

        // the items in the GridLayout is organized as a wrapping list
        // and not as an actual grid, so this is how to get the new index
        int index = row * mGrid.getColumnCount() + column;
        if (index >= mGrid.getChildCount()) {
            index = mGrid.getChildCount() - 1;
        }

        return index;
    }

    private void startScrolling(int from, int to) {
        if (from != to && mAnimator == null) {
            mIsScrolling.set(true);
            mAnimator = new ValueAnimator();
            mAnimator.setInterpolator(new OvershootInterpolator());
            mAnimator.setDuration(Math.abs(to - from));
            mAnimator.setIntValues(from, to);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mScrollView.smoothScrollTo(0, (int) valueAnimator.getAnimatedValue());
                }
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mIsScrolling.set(false);
                    mAnimator = null;
                }
            });
            mAnimator.start();
        }
    }

    private void stopScrolling() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }
}
