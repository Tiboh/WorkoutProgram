package com.erikbuto.workoutprogram.Manage;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Muscle;
import com.erikbuto.workoutprogram.R;
import com.tokenautocomplete.TokenCompleteTextView;

/**
 * Created by Utilisateur on 25/07/2015.
 */
public class MuscleCompletionView extends TokenCompleteTextView<Muscle> {

    private String mType;
    private long mExerciseId;
    private Context mContext;

    public MuscleCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected View getViewForObject(Muscle muscle) {

        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout)l.inflate(R.layout.muscle_token, (ViewGroup) MuscleCompletionView.this.getParent(), false);
        ((TextView)view.findViewById(R.id.muscle_name)).setText(muscle.getName());

        return view;
    }

    @Override
    protected Muscle defaultObject(String completionText) {
        //Stupid simple example of guessing if we have an email or not
        DatabaseHandler db = new DatabaseHandler(mContext);
        Muscle newMuscle = new Muscle(completionText, mType, mExerciseId);
        db.addMuscle(newMuscle);
        return newMuscle;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public void setmExerciseId(long mExerciseId) {
        this.mExerciseId = mExerciseId;
    }
}
