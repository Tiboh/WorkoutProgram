package com.erikbuto.workoutprogram.Manage;

import java.util.ArrayList;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Set;
import com.erikbuto.workoutprogram.DeleteDialogFragment;
import com.erikbuto.workoutprogram.Home.TabsAdapter;
import com.erikbuto.workoutprogram.R;
import com.erikbuto.workoutprogram.SetNameDialogFragment;

public class ManageExerciseActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private TabsAdapter mTabsAdapter;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private FloatingActionButton mFloatingButton;
    private TextView mTitleView;

    public static final String ARG_EXERCISE_ID = "exercise_id";

    public static final int POSITION_SETS_TAB = 0;
    public static final int POSITION_OVERVIEW_TAB = 1;
    public static final int POSITION_STATS_TAB = 2;

    private Exercise mExercise;
    private ArrayList<Set> mSets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_exercise);

        DatabaseHandler db = new DatabaseHandler(this);
        mExercise = db.getExercise(this.getIntent().getExtras().getLong(ARG_EXERCISE_ID));
        mSets = db.getAllSetsExercise(mExercise.getId());

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitleView = (TextView) findViewById(R.id.toolbar_title);
        mTitleView.setText(mExercise.getName());
        mTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arg = new Bundle();
                SetNameDialogFragment dialogSetName = new SetNameDialogFragment();
                arg.putString(SetNameDialogFragment.ARG_DATA_TYPE, SetNameDialogFragment.ARG_TYPE_EXERCISE);
                arg.putLong(SetNameDialogFragment.ARG_EXERCISE_ID, mExercise.getId());
                dialogSetName.setArguments(arg);
                dialogSetName.show(getSupportFragmentManager(), "TAG");
            }
        });

        mFloatingButton = (FloatingActionButton) findViewById(R.id.floating_action);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        mTabsAdapter = new TabsAdapter(this, pager);
        pager.setAdapter(mTabsAdapter);

        if (mSets.isEmpty()) {
            Bundle argNoSetsFragment = new Bundle();
            argNoSetsFragment.putLong(NoSetFragment.ARG_EXERCISE_ID, mExercise.getId());
            mTabsAdapter.addTab(mTabLayout.newTab(), NoSetFragment.class, argNoSetsFragment, getString(R.string.tab_title_sets));
        }else{
            Bundle argSetListFragment = new Bundle();
            argSetListFragment.putLong(SetListFragment.ARG_EXERCISE_ID, mExercise.getId());
            mTabsAdapter.addTab(mTabLayout.newTab(), SetListFragment.class, argSetListFragment, getString(R.string.tab_title_sets));
        }

        Bundle argOverviewFragment = new Bundle();
        argOverviewFragment.putLong(OverviewFragment.ARG_EXERCISE_ID, mExercise.getId());
        mTabsAdapter.addTab(mTabLayout.newTab(), OverviewFragment.class, argOverviewFragment, getString(R.string.tab_title_overview));

        //// TO MODIFY
        Bundle argExerciseStatsFragment = new Bundle();
        argExerciseStatsFragment.putLong(NoSetFragment.ARG_EXERCISE_ID, mExercise.getId());
        mTabsAdapter.addTab(mTabLayout.newTab(), NoSetFragment.class, argOverviewFragment, getString(R.string.tab_title_stats));

        mTabLayout.setTabsFromPagerAdapter(mTabsAdapter);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case POSITION_SETS_TAB:
                        mFloatingButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
                        mFloatingButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((SetListFragment) mTabsAdapter.getItem(POSITION_SETS_TAB)).addNewSet();
                            }
                        });
                        break;
                    case POSITION_OVERVIEW_TAB:
                        mFloatingButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_pen));
                        mFloatingButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ManageExerciseActivity.this, EditOverviewActivity.class);
                                intent.putExtra(ImageFullScreenActivity.ARG_EXERCISE_ID, mExercise.getId());
                                startActivity(intent);
                            }
                        });
                        break;
                    case POSITION_STATS_TAB:
                        mFloatingButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_share));
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    public void removeSet(Set set){
        mSets.remove(set);
        if(mSets.isEmpty()){
            mTabLayout.removeTabAt(POSITION_SETS_TAB);
            Bundle argNoSetsFragment = new Bundle();
            argNoSetsFragment.putLong(NoSetFragment.ARG_EXERCISE_ID, mExercise.getId());
            mTabsAdapter.addTab(mTabLayout.newTab(), NoSetFragment.class, argNoSetsFragment, getString(R.string.tab_title_sets));
        }
    }

    public void onExerciseNameChanged(Exercise exercise, String newName) {
        mTitleView.setText(newName);
        mExercise.setName(exercise.getName());
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
                arg.putLong(DeleteDialogFragment.ARG_EXERCISE_ID, mExercise.getId());
                dialogDelete.setArguments(arg);
                dialogDelete.show(getSupportFragmentManager(), "TAG");
                return true;
            case R.id.action_set_name:
                SetNameDialogFragment dialogSetName = new SetNameDialogFragment();
                arg.putString(SetNameDialogFragment.ARG_DATA_TYPE, SetNameDialogFragment.ARG_TYPE_EXERCISE);
                arg.putLong(SetNameDialogFragment.ARG_EXERCISE_ID, mExercise.getId());
                dialogSetName.setArguments(arg);
                dialogSetName.show(getSupportFragmentManager(), "TAG");
                return true;
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
