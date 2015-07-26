package com.erikbuto.workoutprogram.Manage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Image;
import com.erikbuto.workoutprogram.DB.Muscle;
import com.erikbuto.workoutprogram.Utils.MyUtils;
import com.erikbuto.workoutprogram.R;
import com.erikbuto.workoutprogram.Utils.SquareImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public class EditOverviewActivity extends ActionBarActivity {

    public static final String ARG_EXERCISE_ID = "exercise_id";
    private Toolbar mToolbar;

    private Exercise mExercise;
    private ArrayList<Image> mImages;
    private final int SELECT_PHOTO = 0;
    private ArrayList<Muscle> mPrimaryMuscles;
    private ArrayList<Muscle> mSecondaryMuscles;

    private EditText mDescriptionView;

    private GridLayout mGrid;
    private ScrollView mScrollView;
    private ValueAnimator mAnimator;
    private AtomicBoolean mIsScrolling = new AtomicBoolean(false);
    private int mIndexStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_overview);

        DatabaseHandler db = new DatabaseHandler(this);
        mExercise = db.getExercise(getIntent().getExtras().getLong(OverviewFragment.ARG_EXERCISE_ID));
        mImages = db.getAllImagesExercise(mExercise.getId());
        mPrimaryMuscles = db.getAllPrimaryMuscleExercise(mExercise.getId());
        mSecondaryMuscles = db.getAllSecondaryMuscleExercise(mExercise.getId());
        Collections.sort(mImages, new Image.ImageComparator());

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_edit_overview));
        getSupportActionBar().setSubtitle(mExercise.getName());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText mDescriptionView = (EditText) findViewById(R.id.edit_overview_description);
        mDescriptionView.setText(mExercise.getDescription());


        TextView addImageTextView = (TextView) findViewById(R.id.add_image_edit_overview);
        addImageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 16) {
                    startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true).setType("image/*"), SELECT_PHOTO);
                }else{
                    startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).putExtra(Intent.ACTION_PICK, true).setType("image/*"), SELECT_PHOTO);
                }
            }
        });

        if (!mImages.isEmpty()) {
            mGrid = (GridLayout) findViewById(R.id.grid_layout);
            mScrollView = (ScrollView) findViewById(R.id.scroll_view);
            mScrollView.setSmoothScrollingEnabled(true);

            for (int i = 0; i < mImages.size(); i++) {
                addImageToGridView(mImages.get(i).getUrl());
            }

            // Purely aesthetic
            switch (mImages.size()) {
                case 1:
                    mGrid.setColumnCount(1);
                    break;
                case 2:
                    mGrid.setColumnCount(2);
                    break;
                case 4:
                    mGrid.setColumnCount(2);
                    break;
                default:
                    mGrid.setColumnCount(3);
                    break;
            }
            mGrid.setOnDragListener(new CardDragListener());
            registerForContextMenu(mGrid);
        }

        String[] muscles = getResources().getStringArray(R.array.muscles_list);
        ArrayList<Muscle> allMuscles = new ArrayList<>();
        for (int i = 0; i < muscles.length; i++) {
            allMuscles.add(new Muscle(muscles[i], "", 0));
        }

        ArrayAdapter<Muscle> adapterPrimary = new ArrayAdapter<Muscle>(this, android.R.layout.simple_list_item_1, allMuscles);
        MuscleCompletionView completionViewPrimary = (MuscleCompletionView) findViewById(R.id.edit_primary_muscle_content);
        completionViewPrimary.setmExerciseId(mExercise.getId());
        completionViewPrimary.setmType(Muscle.MUSCLE_TYPE_PRIMARY);
        completionViewPrimary.setAdapter(adapterPrimary);
        for (int i = 0; i < mPrimaryMuscles.size(); i++) {
            completionViewPrimary.addObject(mPrimaryMuscles.get(i));
        }

        ArrayAdapter<Muscle> adapterSecondary = new ArrayAdapter<Muscle>(this, android.R.layout.simple_list_item_1, allMuscles);
        MuscleCompletionView completionViewSecondary = (MuscleCompletionView) findViewById(R.id.edit_secondary_muscle_content);
        completionViewSecondary.setmExerciseId(mExercise.getId());
        completionViewSecondary.setmType(Muscle.MUSCLE_TYPE_SECONDARY);
        completionViewSecondary.setAdapter(adapterSecondary);
        for (int i = 0; i < mSecondaryMuscles.size(); i++) {
            completionViewSecondary.addObject(mSecondaryMuscles.get(i));
        }
        char[] splitChar = {',', ';', ' '};
        completionViewPrimary.allowDuplicates(false);
        completionViewPrimary.setSplitChar(splitChar);
        completionViewSecondary.allowDuplicates(false);
        completionViewSecondary.setSplitChar(splitChar);
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
                        swapImages(mIndexStart, index);
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
                    break;
            }
            return true;
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

        }
    }

    private void swapImages(int indexOne, int indexTwo) {
        Image temp = mImages.get(indexOne);

        temp.setPosition(indexTwo);
        mImages.get(indexTwo).setPosition(indexOne);

        mImages.set(indexOne, mImages.get(indexTwo));
        mImages.set(indexTwo, temp);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                //// RETURN ACTIVITY WITH RESULT
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overview_edit_image_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_image:
                mGrid.removeViewAt(info.position);
                mGrid.notify();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {

                    if(Build.VERSION.SDK_INT >= 16) {
                        ClipData clipData = imageReturnedIntent.getClipData();
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            Uri selectedImage = clipData.getItemAt(i).getUri();
                            InputStream imageStream = null;
                            try {
                                imageStream = getContentResolver().openInputStream(selectedImage);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                            String url = addNewImageToDB(yourSelectedImage);
                            addImageToGridView(url);
                        }
                    }else{
                        Uri selectedImage = imageReturnedIntent.getData();
                        InputStream imageStream = null;
                        try {
                            imageStream = getContentResolver().openInputStream(selectedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                        String url = addNewImageToDB(yourSelectedImage);
                        addImageToGridView(url);
                    }
                }
                break;
        }
    }

    public void addImageToGridView(String url){
        final LayoutInflater inflater = LayoutInflater.from(this);
        final View itemView = inflater.inflate(R.layout.overview_edit_square_image_item, mGrid, false);
        final SquareImageView imageView = (SquareImageView) itemView.findViewById(R.id.overview_edit_image_item);
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.abc_spinner_mtrl_am_alpha) // resource or drawable
                .build();
        imageLoader.displayImage("file:///" + this.getFilesDir() +"/"+url, imageView);
        itemView.setOnLongClickListener(new LongPressListener());
        mGrid.addView(itemView);
    }

    public String addNewImageToDB(Bitmap image){
        String url = MyUtils.IMAGE_FOLDER_URL + System.currentTimeMillis();
        MyUtils.saveImageToInternalStorage(image, url, this);
        DatabaseHandler db = new DatabaseHandler(this);
        Image newImage = new Image(url, mImages.size(), mExercise.getId());
        db.addImage(newImage);
        mImages.add(newImage);
        return url;
    }
}
