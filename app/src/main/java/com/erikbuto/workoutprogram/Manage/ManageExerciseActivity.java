package com.erikbuto.workoutprogram.Manage;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DeleteDialogFragment;
import com.erikbuto.workoutprogram.R;
import com.erikbuto.workoutprogram.SetNameDialogFragment;

import io.karim.MaterialTabs;

public class ManageExerciseActivity extends ActionBarActivity {

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    public final static String ARG_EXERCISE_ID = "exercise_id";
    private long mExerciseId;
    private Exercise mExercise;

    private Toolbar mToolbar;
    private ArrayList<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_exercise);

        Intent intent = getIntent();
        mExerciseId = intent.getExtras().getLong(ARG_EXERCISE_ID);

        DatabaseHandler db = new DatabaseHandler(this);
        mExercise = db.getExercise(mExerciseId);

        mFragments = new ArrayList<>();
        Fragment fragment1 = new SetListFragment();
        Bundle arg = new Bundle();
        arg.putLong(SetListFragment.ARG_EXERCISE_ID, mExerciseId);
        fragment1.setArguments(arg);
        mFragments.add(fragment1);


        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mExercise.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize the ViewPager and set an adapter
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), mFragments));

        // Bind the tabs to the ViewPager
        MaterialTabs tabs = (MaterialTabs) findViewById(R.id.material_tabs);
        tabs.setViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_program, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Bundle arg = new Bundle();
        switch (item.getItemId()) {
            case R.id.action_delete:
                DeleteDialogFragment dialogDelete = new DeleteDialogFragment();
                arg.putString(DeleteDialogFragment.ARG_DATA_TYPE, DeleteDialogFragment.ARG_TYPE_EXERCISE);
                arg.putLong(DeleteDialogFragment.ARG_EXERCISE_ID, mExerciseId);
                dialogDelete.setArguments(arg);
                dialogDelete.show(getSupportFragmentManager(), "TAG");
                return true;
            case R.id.action_set_name:
                SetNameDialogFragment dialogSetName = new SetNameDialogFragment();
                arg.putString(SetNameDialogFragment.ARG_DATA_TYPE, SetNameDialogFragment.ARG_TYPE_EXERCISE);
                arg.putLong(SetNameDialogFragment.ARG_EXERCISE_ID, mExerciseId);
                dialogSetName.setArguments(arg);
                dialogSetName.show(getSupportFragmentManager(), "TAG");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> mFragments;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }
}
