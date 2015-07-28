package com.erikbuto.workoutprogram.Manage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.erikbuto.workoutprogram.DB.Program;
import com.erikbuto.workoutprogram.DeleteDialogFragment;
import com.erikbuto.workoutprogram.Utils.MyUtils;
import com.erikbuto.workoutprogram.R;
import com.erikbuto.workoutprogram.Utils.SquareImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tokenautocomplete.TokenCompleteTextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class EditOverviewActivity extends ActionBarActivity implements TokenCompleteTextView.TokenListener {

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
        Collections.sort(mImages, new Image.ImageComparator());
        mPrimaryMuscles = db.getAllPrimaryMuscleExercise(mExercise.getId());
        mSecondaryMuscles = db.getAllSecondaryMuscleExercise(mExercise.getId());
        Collections.sort(mImages, new Image.ImageComparator());

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_edit_overview));
        getSupportActionBar().setSubtitle(mExercise.getName());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDescriptionView = (EditText) findViewById(R.id.edit_overview_description);
        mDescriptionView.setText(mExercise.getDescription());


        TextView addImageTextView = (TextView) findViewById(R.id.add_image_edit_overview);
        addImageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 16) {
                    startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true).setType("image/*"), SELECT_PHOTO);
                } else {
                    startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).putExtra(Intent.ACTION_PICK, true).setType("image/*"), SELECT_PHOTO);
                }
            }
        });

        mGrid = (GridLayout) findViewById(R.id.grid_layout);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mScrollView.setSmoothScrollingEnabled(true);

        for (int i = 0; i < mImages.size(); i++) {
            addImageToGridView(mImages.get(i).getId());
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
        completionViewPrimary.setTokenListener(this);
        for (int i = 0; i < mPrimaryMuscles.size(); i++) {
            completionViewPrimary.addObject(mPrimaryMuscles.get(i));
        }

        ArrayAdapter<Muscle> adapterSecondary = new ArrayAdapter<Muscle>(this, android.R.layout.simple_list_item_1, allMuscles);
        MuscleCompletionView completionViewSecondary = (MuscleCompletionView) findViewById(R.id.edit_secondary_muscle_content);
        completionViewSecondary.setmExerciseId(mExercise.getId());
        completionViewSecondary.setmType(Muscle.MUSCLE_TYPE_SECONDARY);
        completionViewSecondary.setAdapter(adapterSecondary);
        completionViewSecondary.setTokenListener(this);
        for (int i = 0; i < mSecondaryMuscles.size(); i++) {
            completionViewSecondary.addObject(mSecondaryMuscles.get(i));
        }
        char[] splitChar = {',', ';', ' '};
        completionViewPrimary.allowDuplicates(false);
        completionViewPrimary.setSplitChar(splitChar);
        completionViewSecondary.allowDuplicates(false);
        completionViewSecondary.setSplitChar(splitChar);

    }

    @Override
    public void onTokenAdded(Object token) {
        // The adding is done in MuscleCompletionView.class cause of redundant on adding
    }

    @Override
    public void onTokenRemoved(Object token) {
        DatabaseHandler db = new DatabaseHandler(this);
        db.deleteMuscleFromId(((Muscle) token).getId());
        if(((Muscle) token).getType() == Muscle.MUSCLE_TYPE_PRIMARY) {
            mPrimaryMuscles.remove((Muscle) token);
        }else{
            mSecondaryMuscles.remove((Muscle) token);
        }    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseHandler db = new DatabaseHandler(this);
        mExercise.setDescription(mDescriptionView.getText().toString());
        db.updateExercise(mExercise);
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
                    updateImagesInDB();
                    break;
            }
            return true;
        }
    }

    public void updateImagesInDB(){
        DatabaseHandler db = new DatabaseHandler(this);
        for(int i = 0 ; i < mImages.size() ; i++){
            db.updateImage(mImages.get(i));
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
                onBackPressed();
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

                    if (Build.VERSION.SDK_INT >= 16 && imageReturnedIntent.getClipData() != null) {
                        ClipData clipData = imageReturnedIntent.getClipData();
                        new LoadingNewImages().execute(clipData);
                    } else {
                        Uri selectedImage = imageReturnedIntent.getData();
                        new LoadingNewImage().execute(selectedImage);
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ManageExerciseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(ManageExerciseActivity.ARG_EXERCISE_ID, mExercise.getId());
        intent.putExtra(ManageExerciseActivity.FROM_EDIT_OVERVIEW_ACTIVITY, true);
        startActivity(intent);
    }

    public void deleteImage(Image image) {
        int indice = -1;
        for(int i = 0 ; i < mImages.size() ; i++){
            if(mImages.get(i).getId() == image.getId()){
                indice = i;
            }
        }
        mImages.remove(indice);
        mGrid.removeViewAt(indice);
        for(int i = 0 ; i < mImages.size() ; i++){
            mImages.get(i).setPosition(i);
        }
        updateImagesInDB();
    }

    public void addImageToGridView(final long id) {
        final LayoutInflater inflater = LayoutInflater.from(this);
        final View itemView = inflater.inflate(R.layout.overview_edit_square_image_item, mGrid, false);
        final SquareImageView imageView = (SquareImageView) itemView.findViewById(R.id.overview_edit_image_item);
        ImageLoader imageLoader = ImageLoader.getInstance();
        DatabaseHandler db = new DatabaseHandler(this);
        final Image myNewImage = db.getImage(id);
        imageLoader.displayImage("file://" + this.getFilesDir() + "/" + myNewImage.getUrl(), imageView);
        itemView.setOnLongClickListener(new LongPressListener());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDialogFragment dialogDelete = new DeleteDialogFragment();
                Bundle arg = new Bundle();
                arg.putString(DeleteDialogFragment.ARG_DATA_TYPE, DeleteDialogFragment.ARG_TYPE_IMAGE);
                arg.putLong(DeleteDialogFragment.ARG_IMAGE_ID, myNewImage.getId());
                dialogDelete.setArguments(arg);
                dialogDelete.show(getSupportFragmentManager(), "TAG");
            }
        });
        mGrid.addView(itemView);
    }

    public long addNewImageToDB(Bitmap image) {
        String url = MyUtils.generateImageUrl();
        MyUtils.saveImageToInternalStorage(image, url, EditOverviewActivity.this);
        DatabaseHandler db = new DatabaseHandler(EditOverviewActivity.this);
        Image newImage = new Image(url, mImages.size(), mExercise.getId());
        long id = db.addImage(newImage);
        mImages.add(newImage);
        sortImages();
        return id;
    }

    public void sortImages() {
        for (int i = 0; i < mImages.size(); i++) {
            mImages.get(i).setPosition(i);
        }
    }

    private class LoadingNewImage extends AsyncTask<Uri, Integer, Long> {
        private ProgressDialog mDialog;

        protected void onPreExecute() {
            mDialog = new ProgressDialog(EditOverviewActivity.this);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mDialog.setMessage(getString(R.string.image_loading));
            mDialog.setIndeterminate(true);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(false);
            mDialog.show();
        }

        protected Long doInBackground(Uri... uri) {
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(uri[0]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
            return addNewImageToDB(yourSelectedImage);
        }

        protected void onProgressUpdate(Integer... a) {
        }

        protected void onPostExecute(Long id) {
            addImageToGridView(id);
            mDialog.dismiss();
        }
    }


    private class LoadingNewImages extends AsyncTask<ClipData, Integer, ArrayList<Long>> {
        private ProgressDialog mDialog;

        protected void onPreExecute() {
            mDialog = new ProgressDialog(EditOverviewActivity.this);
            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDialog.setMessage(getString(R.string.image_loading));
            mDialog.setIndeterminate(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
        }

        protected ArrayList<Long> doInBackground(ClipData... clipData) {
            ArrayList<Long> ids = new ArrayList<>();
            mDialog.setMax(clipData[0].getItemCount());
            for (int i = 0; i < clipData[0].getItemCount(); i++) {
                Uri selectedImage = clipData[0].getItemAt(i).getUri();
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                long id = addNewImageToDB(yourSelectedImage);
                ids.add(id);
                publishProgress(i);
            }
            return ids;
        }

        protected void onProgressUpdate(Integer... a) {
            mDialog.setProgress(a[0]);
        }

        protected void onPostExecute(ArrayList<Long> ids) {
            for (int i = 0; i < ids.size(); i++) {
                addImageToGridView(ids.get(i));
            }
            mDialog.dismiss();
        }
    }
}
