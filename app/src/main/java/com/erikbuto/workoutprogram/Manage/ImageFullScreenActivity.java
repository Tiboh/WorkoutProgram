package com.erikbuto.workoutprogram.Manage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Image;
import com.erikbuto.workoutprogram.DeleteDialogFragment;
import com.erikbuto.workoutprogram.MyUtils;
import com.erikbuto.workoutprogram.R;
import com.erikbuto.workoutprogram.SetNameDialogFragment;

import java.util.ArrayList;

/**
 * Created by Utilisateur on 24/07/2015.
 */
public class ImageFullScreenActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {

    private long mExerciseId;
    private ArrayList<Image> mImages;
    private Toolbar mToolbar;

    public static final String ARG_EXERCISE_ID = "exercise_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_fullscreen);

        DatabaseHandler db = new DatabaseHandler(this);
        mExerciseId = this.getIntent().getExtras().getLong(ARG_EXERCISE_ID);

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(db.getExercise(mExerciseId).getName());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();

        mImages = db.getAllImagesExercise(mExerciseId);

        SliderLayout mSliderShow = (SliderLayout) findViewById(R.id.slider_fullscreen);

        for (int i = 0; i < mImages.size(); i++) {
            DefaultSliderView sliderView = new DefaultSliderView(this);
            sliderView.image(MyUtils.getFileFromInternalStorage(mImages.get(i).getUrl(), this));
            sliderView.setScaleType(BaseSliderView.ScaleType.CenterInside);
            sliderView.setOnSliderClickListener(this);
            mSliderShow.addSlider(sliderView);
        }
        mSliderShow.stopAutoCycle();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        if(getSupportActionBar().isShowing()){
            getSupportActionBar().hide();
        }else{
            getSupportActionBar().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
