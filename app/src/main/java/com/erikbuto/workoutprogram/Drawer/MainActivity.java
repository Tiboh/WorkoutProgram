/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.erikbuto.workoutprogram.Drawer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Program;
import com.erikbuto.workoutprogram.DB.Set;
import com.erikbuto.workoutprogram.DeleteDialogFragment;
import com.erikbuto.workoutprogram.Home.CardViewAdapter;
import com.erikbuto.workoutprogram.Home.ManageProgramFragment;
import com.erikbuto.workoutprogram.Home.NewExerciseNameDialogFragment;
import com.erikbuto.workoutprogram.Home.NoExercisesFragment;
import com.erikbuto.workoutprogram.Home.TabsAdapter;
import com.erikbuto.workoutprogram.SetNameDialogFragment;
import com.erikbuto.workoutprogram.R;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends ActionBarActivity {
    private ArrayList<Program> mPrograms;
    private Program mCurrent;
    private ArrayList<Exercise> mExercises = new ArrayList<>();

    private Drawer mDrawer;

    private Toolbar mToolbar;
    public static final int ID_ADD_ITEM_DRAWER = 0;
    public static final int ID_SETTINGS_ITEM_DRAWER = 1;

    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // populateDB();

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.noprogram)
                .withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER_CROP)
                .build();

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withAccountHeader(headerResult)
                .withDisplayBelowToolbar(false)
                        // .withFooter(R.id.footer)
                .build();

        final DatabaseHandler db = new DatabaseHandler(this);
        mPrograms = db.getAllPrograms();
        List<String> programsName = new ArrayList<String>();
        int i = 0;
        for (Program cn : mPrograms) {
            programsName.add(cn.getName());
            mDrawer.addItem(new PrimaryDrawerItem().withName(cn.getName()), i);
            i++;
        }

        mDrawer.addItem(new PrimaryDrawerItem()
                        .withName(R.string.action_add)
                        .withIcon(R.drawable.ic_add_black)
                        .withIdentifier(ID_ADD_ITEM_DRAWER)
        );

        mDrawer.addItem(new DividerDrawerItem());

        mDrawer.addItem(new PrimaryDrawerItem()
                        .withName(R.string.action_settings)
                        .withIcon(R.drawable.ic_settings_black_48dp)
                        .withIdentifier(ID_SETTINGS_ITEM_DRAWER)
        );

        updateFrameLayoutView(mPrograms);

        mDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                switch (drawerItem.getIdentifier()) {
                    case ID_ADD_ITEM_DRAWER:
                        mDrawer.closeDrawer();
                        Program newProgram = new Program(getString(R.string.new_program));
                        long newProgramId = db.addProgram(newProgram);
                        newProgram.setId(newProgramId);
                        mPrograms.add(newProgram);
                        mDrawer.addItem(new PrimaryDrawerItem().withName(newProgram.getName()), mPrograms.size() - 1);
                        updateFragment(mPrograms.size() - 1);
                        break;
                    case ID_SETTINGS_ITEM_DRAWER:
                        // Intent intent = new Intent(this, SettingsActivity.class);
                        break;
                    default:
                        updateFragment(position);
                        mDrawer.closeDrawer();
                }
                return true;
            }
        });

        if(mCurrent != null) {
            FloatingActionButton buttonAddExercise = (FloatingActionButton) findViewById(R.id.button_add_exercise);
            buttonAddExercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewExerciseNameDialogFragment dialogAddExercise = new NewExerciseNameDialogFragment();
                    Bundle arg = new Bundle();
                    arg.putLong(NewExerciseNameDialogFragment.ARG_PROGRAM_ID, mCurrent.getId());
                    dialogAddExercise.setArguments(arg);
                    dialogAddExercise.show(getSupportFragmentManager(), "TAG");
                }
            });
            // ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar)).setTitle(mCurrent.getName());
            ((TextView) findViewById(R.id.toolbar_title)).setText(mCurrent.getName());

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

            final ViewPager pager = (ViewPager) findViewById(R.id.pager);
            final TabsAdapter tabsAdapter = new TabsAdapter(this, pager);
            pager.setAdapter(tabsAdapter);

            Bundle argManageProgramFragment = new Bundle();
            argManageProgramFragment.putLong(ManageProgramFragment.ARG_PROGRAM_ID, mCurrent.getId());
            tabsAdapter.addTab(tabLayout.newTab(), ManageProgramFragment.class, argManageProgramFragment, ManageProgramFragment.TAB_TITLE);
            tabsAdapter.addTab(tabLayout.newTab(), NoExercisesFragment.class, argManageProgramFragment, NoExercisesFragment.TAB_TITLE);

            tabLayout.setTabsFromPagerAdapter(tabsAdapter);
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDrawer.resetDrawerContent();
    }

    public Drawer getmDrawer() {
        return mDrawer;
    }

    public void updateFrameLayoutView(ArrayList<Program> programs){
        if(!programs.isEmpty()){
            updateFragment(0); // TO DO change hard code to updateFragment(last user's choice)
        }else{
            mCurrent = null;
            ///// TO DO
            /*FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new NoProgramsFragment();
            fragmentManager.beginTransaction().replace(R.id.content_frame_program, fragment).commit();*/
        }
    }


    public void updateFragment(int position){
        DatabaseHandler db = new DatabaseHandler(this);
        mCurrent = mPrograms.get(position);
        //mToolbar.setTitle(mCurrent.getName());
        mExercises = db.getAllExercisesProgram(mCurrent.getId());
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (mExercises.size() != 0) {
            /*Fragment fragment = new ManageProgramFragment();
            Bundle arg = new Bundle();
            arg.putLong(ManageProgramFragment.ARG_PROGRAM_ID, mCurrent.getId());
            fragment.setArguments(arg);
            fragmentManager.beginTransaction().replace(R.id.content_frame_program, fragment).commit();*/
        } else {
            /*Fragment fragment = new NoExercisesFragment();
            Bundle arg = new Bundle();
            arg.putLong(NoExercisesFragment.ARG_PROGRAM_ID, mCurrent.getId());
            fragment.setArguments(arg);
            fragmentManager.beginTransaction().replace(R.id.content_frame_program, fragment).commit();*/
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer != null && mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if(mCurrent != null) {
            if(!mExercises.isEmpty()) {
                inflater.inflate(R.menu.menu_manage_program, menu);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    // Called whenever we call invalidateOptionsMenu()
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
        Bundle arg = new Bundle();
        switch (item.getItemId()) {
            case R.id.action_delete:
                DeleteDialogFragment dialogDelete = new DeleteDialogFragment();
                arg.putString(DeleteDialogFragment.ARG_DATA_TYPE, DeleteDialogFragment.ARG_TYPE_PROGRAM);
                arg.putLong(DeleteDialogFragment.ARG_PROGRAM_ID, mCurrent.getId());
                dialogDelete.setArguments(arg);
                dialogDelete.show(getSupportFragmentManager(), "TAG");
                return true;
            case R.id.action_set_name:
                SetNameDialogFragment dialogSetName = new SetNameDialogFragment();
                arg.putString(SetNameDialogFragment.ARG_DATA_TYPE, SetNameDialogFragment.ARG_TYPE_PROGRAM);
                arg.putLong(SetNameDialogFragment.ARG_PROGRAM_ID, mCurrent.getId());
                dialogSetName.setArguments(arg);
                dialogSetName.show(getSupportFragmentManager(), "TAG");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateDB(){
        DatabaseHandler db = new DatabaseHandler(this);
        db.deleteAllPrograms();
        long idRavi = db.addProgram(new Program("Half-body"));
        long idPushups = db.addExercise(new Exercise("Barbell Rear Delt Row c'est la fête au camping", null, idRavi, 0, Exercise.MODE_SIMPLE, "While keeping the upper arms perpendicular to the torso, " +
                "pull the barbell up towards your upper chest as you squeeze the " +
                "rear delts and you breathe out"));
        long idBarbell = db.addExercise(new Exercise("Barbell Bench Press", null, idRavi, 1, Exercise.MODE_SIMPLE, "From the starting position, breathe in and begin " +
                "coming down slowly until the bar touches your middle chest"));
        long idFlyes = db.addExercise(new Exercise("Bodyweight Flyes", null, idRavi, 2, Exercise.MODE_SIMPLE, "Alternating your left and right arms, whipping " +
                "the ropes up and down as fast as you can"));
        long idButterfly = db.addExercise(new Exercise("Butterfly", null, idRavi, 3, Exercise.MODE_SIMPLE, null));
        long idPress = db.addExercise(new Exercise("Cable Chest Press ", null, idRavi, 4, Exercise.MODE_SIMPLE, null));
        long idFlyPress = db.addExercise(new Exercise("Cable Fly Press ", null, idRavi, 5, Exercise.MODE_SIMPLE, null));

        db.addSet(new Set(13, 40, 1, 30, idPushups, 0));
        db.addSet(new Set(15, 40, 1, 30, idPushups, 1));
        db.addSet(new Set(13, 40, 1, 30, idPushups, 2));

        db.addSet(new Set(12, 40, 1, 0, idBarbell, 0));
        db.addSet(new Set(12, 40, 1, 0, idBarbell, 1));
        db.addSet(new Set(12, 40, 1, 0, idBarbell, 2));
        db.addSet(new Set(12, 40, 1, 0, idBarbell, 3));
        db.addSet(new Set(10, 40, 1, 0, idBarbell, 4));

        db.addSet(new Set(5, 50, 0, 0, idFlyes, 0));
        db.addSet(new Set(10, 20, 0, 0, idFlyes, 1));

        db.addSet(new Set(5, 0, 1, 0, idButterfly, 0));
        db.addSet(new Set(5, 0, 1, 30, idButterfly, 1));
        db.addSet(new Set(5, 0, 1, 30, idButterfly, 2));
        db.addSet(new Set(5, 0, 1, 0, idButterfly, 3));

        db.addSet(new Set(13, 40, 1, 30, idPress, 0));
        db.addSet(new Set(15, 40, 1, 30, idPress, 1));
        db.addSet(new Set(13, 40, 1, 30, idPress, 2));

        db.addProgram(new Program("Full Body"));
        db.addProgram(new Program("Monday training"));
    }
}