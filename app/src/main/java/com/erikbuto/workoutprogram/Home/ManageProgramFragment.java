package com.erikbuto.workoutprogram.Home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ScrollView;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.Manage.ManageExerciseActivity;
import com.erikbuto.workoutprogram.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Utilisateur on 14/07/2015.
 */
public class ManageProgramFragment extends Fragment {
    private GridLayout mGrid;
    private ScrollView mScrollView;
    private ValueAnimator mAnimator;
    private AtomicBoolean mIsScrolling = new AtomicBoolean(false);

    private LayoutInflater mLayoutInflater;

    private int mIndexStart;

    private long mProgramId;
    private ArrayList<Exercise> mExercises;

    public static final String ARG_PROGRAM_ID = "program_id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_program, container, false);
        mScrollView = (ScrollView) rootView.findViewById(R.id.scroll_view);
        mScrollView.setSmoothScrollingEnabled(true);

        mLayoutInflater = LayoutInflater.from(getActivity());
        mGrid = (GridLayout) rootView.findViewById(R.id.grid_layout);
        mGrid.setOnDragListener(new CardDragListener());

        mProgramId = getArguments().getLong(ManageProgramFragment.ARG_PROGRAM_ID);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        DatabaseHandler db = new DatabaseHandler(getActivity());

        mExercises = db.getAllExercisesProgram(mProgramId);
        Collections.sort(mExercises, new Exercise.ExerciseComparator());

        for (int i = 0; i < mExercises.size(); i++) {
            View itemView = mLayoutInflater.inflate(R.layout.fragment_manage_program_item, mGrid, false);
            final TextView exName = (TextView) itemView.findViewById(R.id.exercise_name);
            exName.setText(mExercises.get(i).getName());

            itemView.setOnLongClickListener(new LongPressListener());
            itemView.setOnClickListener(new ShortPressListener());

            mGrid.addView(itemView);

        }
    }


    private class CardDragListener implements View.OnDragListener {
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

                    if (event.getY() - scrollY > mScrollView.getBottom() - 250) {
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

                    if (mIndexStart != index) {
                        swapExercises(mIndexStart, index);
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
                    updateDBExercise(mExercises);
                    break;
            }
            return true;
        }
    }

    private void swapExercises(int indexOne, int indexTwo) {
        Exercise temp = mExercises.get(indexOne);

        temp.setPosition(indexTwo);
        mExercises.get(indexTwo).setPosition(indexOne);

        mExercises.set(indexOne, mExercises.get(indexTwo));
        mExercises.set(indexTwo, temp);
    }

    private void updateDBExercise(ArrayList<Exercise> exercises) {
        DatabaseHandler db = new DatabaseHandler(getActivity());
        for (int i = 0; i < exercises.size(); i++) {
            db.updateExercise(exercises.get(i));
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

    private class ShortPressListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int mIndexClicked = calculateNewIndex(view.getX(), view.getY());
            Intent intent = new Intent(getActivity(), ManageExerciseActivity.class);
            intent.putExtra(ManageExerciseActivity.ARG_EXERCISE_ID, mExercises.get(mIndexClicked).getId());
            startActivity(intent);
        }
    }

    private int calculateNewIndex(float x, float y) {
        // calculate which column to move to
        final float cellWidth = mGrid.getWidth() / mGrid.getColumnCount();
        final int column = (int) (x / cellWidth);

        // calculate which row to move to
        final float cellHeight = mGrid.getHeight() / mGrid.getRowCount();
        final int row = (int) Math.floor(y / cellHeight);

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
