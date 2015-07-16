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

package com.erikbuto.workoutprogram.Home;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Program;
import com.erikbuto.workoutprogram.DB.Set;
import com.erikbuto.workoutprogram.R;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * This example illustrates a common usage of the DrawerLayout widget
 * in the Android support library.
 * <p/>
 * <p>When a navigation (left) drawer is present, the host activity should detect presses of
 * the action bar's Up affordance as a signal to open and close the navigation drawer. The
 * ActionBarDrawerToggle facilitates this behavior.
 * Items within the drawer should fall into one of two categories:</p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 * </ul>
 * <p/>
 * <p>Right side drawers should be used for actions, not navigation. This follows the pattern
 * established by the Action Bar that navigation should be to the left and actions to the right.
 * An action should be an operation performed on the current contents of the window,
 * for example enabling or disabling a data overlay on top of the current content.</p>
 */
public class MainActivity extends ActionBarActivity {
    private List<Program> mPrograms;
    private Program mCurrent;

    private Drawer mDrawer;

    private Toolbar mToolbar;
    public static final int ID_ADD_ITEM_DRAWER = 0;
    public static final int ID_SETTINGS_ITEM_DRAWER = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);

        populateDB();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        DatabaseHandler db = new DatabaseHandler(this);
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

        if(!mPrograms.isEmpty()){
            updateFragment(0);
        }else{
            mCurrent = null;
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = new NoProgramsFragment();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }

        mDrawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                switch(drawerItem.getIdentifier()){
                    case ID_ADD_ITEM_DRAWER :
                        mDrawer.closeDrawer();
                        NewProgramNameDialogFragment dialog = new NewProgramNameDialogFragment();
                        dialog.show(getSupportFragmentManager(), "TAG");
                        break;
                    case ID_SETTINGS_ITEM_DRAWER :
                        // Intent intent = new Intent(this, SettingsActivity.class);
                        break;
                    default :
                        updateFragment(position);
                        mDrawer.closeDrawer();
                }
                return true;
            }
        });
    }

    public void updateFragment(int position){
        mCurrent = mPrograms.get(position);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new HomeFragment();

        Bundle arg = new Bundle();
        arg.putLong(HomeFragment.ARG_PROGRAM_ID, mCurrent.getId());
        arg.putString(HomeFragment.ARG_PROGRAM_NAME, mCurrent.getName());
        fragment.setArguments(arg);
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
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
        // The action bar home/up action should open or close the drawer.
        // Handle action buttons
        switch(item.getItemId()) {
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