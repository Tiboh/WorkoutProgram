package com.erikbuto.workoutprogram.Manage;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Image;
import com.erikbuto.workoutprogram.DB.Muscle;
import com.erikbuto.workoutprogram.Utils.MyUtils;
import com.erikbuto.workoutprogram.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Utilisateur on 23/07/2015.
 */
public class OverviewFragment extends Fragment implements BaseSliderView.OnSliderClickListener {

    private SliderLayout mSliderShow;
    public static final String ARG_BUNDLE_SLIDER = "image_id";

    public static final String ARG_EXERCISE_ID = "exercise_id";
    private Exercise mExercise;
    private ArrayList<Image> mImages;
    private ArrayList<Muscle> mPrimaryMuscles;
    private ArrayList<Muscle> mSecondaryMuscles;

    public OverviewFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DatabaseHandler db = new DatabaseHandler(getActivity());
        mExercise = db.getExercise(getArguments().getLong(OverviewFragment.ARG_EXERCISE_ID));
        mImages = db.getAllImagesExercise(mExercise.getId());
        mPrimaryMuscles = db.getAllPrimaryMuscleExercise(mExercise.getId());
        mSecondaryMuscles = db.getAllSecondaryMuscleExercise(mExercise.getId());

        View rootView;
        if (mImages.isEmpty() && mExercise.getDescription().isEmpty() && mPrimaryMuscles.isEmpty() && mSecondaryMuscles.isEmpty()) {
            rootView = inflater.inflate(R.layout.fragment_nooverview, container, false);
        } else {
            rootView = inflater.inflate(R.layout.fragment_overview, container, false);
            if (!mImages.isEmpty()) {
                FrameLayout rootLayout = (FrameLayout) rootView.findViewById(R.id.overview_frame_image);
                View v = rootView.inflate(getActivity(), R.layout.overview_image_view, rootLayout);

                mSliderShow = (SliderLayout) v.findViewById(R.id.image_slider);

                for (int i = 0; i < mImages.size(); i++) {
                    DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                    sliderView.image(MyUtils.getFileFromInternalStorage(mImages.get(i).getUrl(), getActivity()));
                    sliderView.setOnSliderClickListener(this);
                    sliderView.getBundle().putLong(ARG_BUNDLE_SLIDER, mImages.get(i).getId());
                    sliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
                    mSliderShow.addSlider(sliderView);
                }

                if(mImages.size() == 1){
                    mSliderShow.stopAutoCycle();
                }
            }

            if (!mExercise.getDescription().isEmpty()) {
                FrameLayout rootLayout = (FrameLayout) rootView.findViewById(R.id.overview_frame_description);
                View v = rootView.inflate(getActivity(), R.layout.overview_description_view, rootLayout);

                // Fill in any details dynamically here
                TextView textDescription = (TextView) v.findViewById(R.id.overview_description);
                textDescription.setText(mExercise.getDescription());
            }

            if (!mPrimaryMuscles.isEmpty() || !mSecondaryMuscles.isEmpty()) {
                FrameLayout rootLayout = (FrameLayout) rootView.findViewById(R.id.overview_frame_muscle);
                View v = rootView.inflate(getActivity(), R.layout.overview_muscle_view, rootLayout);

                if (!mPrimaryMuscles.isEmpty()) {
                    FrameLayout framePrimary = (FrameLayout) v.findViewById(R.id.muscle_primary_frame);
                    View primaryView = v.inflate(getActivity(), R.layout.overview_muscle_view_item, framePrimary);

                    String concatString = new String();
                    for (int i = 0; i < mPrimaryMuscles.size(); i++) {
                        concatString = concatString.concat(mPrimaryMuscles.get(i).getName() + " ");
                    }

                    TextView musclesHeader = (TextView) primaryView.findViewById(R.id.overview_muscle_item_header);
                    musclesHeader.setText(getString(R.string.overview_muscle_primary_header));

                    TextView musclesContent = (TextView) primaryView.findViewById(R.id.overview_muscle_item_content);
                    chipsifyMyTextView(musclesContent, concatString);
                }

                if (!mSecondaryMuscles.isEmpty()) {
                    FrameLayout frameSecondary = (FrameLayout) v.findViewById(R.id.muscle_secondary_frame);
                    View secondaryView = v.inflate(getActivity(), R.layout.overview_muscle_view_item, frameSecondary);

                    String concatString = new String();
                    for (int i = 0; i < mSecondaryMuscles.size(); i++) {
                        concatString = concatString.concat(mSecondaryMuscles.get(i).getName() + " ");
                    }

                    TextView musclesHeader = (TextView) secondaryView.findViewById(R.id.overview_muscle_item_header);
                    musclesHeader.setText(getString(R.string.overview_muscle_secondary_header));

                    TextView musclesContent = (TextView) secondaryView.findViewById(R.id.overview_muscle_item_content);
                    chipsifyMyTextView(musclesContent, concatString);
                }
            }
        }

        return rootView;
    }

    public void chipsifyMyTextView(TextView myTextView, String myText){
        String regex = "\\w+";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(myText);
        SpannableStringBuilder sb = new SpannableStringBuilder(myText);
        while (matcher.find()) {
            final LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View parent =  (View) layoutInflater.inflate(R.layout.muscle_token, null);
            final TextView oneWord =  (TextView) parent.findViewById(R.id.muscle_name);
            final int begin = matcher.start();
            final int end = matcher.end();
            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics());
            oneWord.setTextSize(pixels);
            oneWord.setText(myText.substring(begin, end).toString());
            BitmapDrawable bd = new BitmapDrawable(MyUtils.convertViewToDrawable(oneWord));
            bd.setBounds(0, 0, bd.getIntrinsicWidth(),bd.getIntrinsicHeight());
            sb.setSpan(new ImageSpan(bd), begin, end, 0); // Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        }
        myTextView.setText(sb);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Intent intent = new Intent(getActivity(), ImageFullScreenActivity.class);
        intent.putExtra(ImageFullScreenActivity.ARG_EXERCISE_ID, mExercise.getId());
        startActivity(intent);
    }

    @Override
    public void onStop() {
        if(mSliderShow != null) {
            mSliderShow.stopAutoCycle();
        }
        super.onStop();
    }
}
