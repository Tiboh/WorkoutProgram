package com.erikbuto.workoutprogram.Drawer;

import java.net.URL;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Image;
import com.erikbuto.workoutprogram.DB.Muscle;
import com.erikbuto.workoutprogram.DB.Program;
import com.erikbuto.workoutprogram.DB.Set;
import com.erikbuto.workoutprogram.DeleteDialogFragment;
import com.erikbuto.workoutprogram.Home.ManageProgramFragment;
import com.erikbuto.workoutprogram.Home.NoExercisesFragment;
import com.erikbuto.workoutprogram.Home.TabsAdapter;
import com.erikbuto.workoutprogram.Manage.ManageExerciseActivity;
import com.erikbuto.workoutprogram.Utils.MyUtils;
import com.erikbuto.workoutprogram.SetNameDialogFragment;
import com.erikbuto.workoutprogram.R;

public class MainActivity extends ActionBarActivity {
    private ArrayList<Program> mPrograms;
    private Program mProgram;
    private ArrayList<Exercise> mExercises = new ArrayList<>();

    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private SubMenu mProgramSubMenu;
    private Menu mDrawerMenu;
    private int mSelectedItemDrawerPosition;
    public static final int ID_ADD_ITEM_DRAWER = -1;
    public static final int ID_SETTINGS_ITEM_DRAWER = -2;
    public static final int PROGRAM_DRAWER_GROUP_ID = 0;
    public static final int OTHER_DRAWER_GROUP_ID = 1;

    public static final int POSITION_EXERCISE_TAB = 0;
    public static final int POSITION_STATS_TAB = 1;

    public static final String ACTIVITY_RESULT = "activity_result";

    private Toolbar mToolbar;
    private TextView mTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_and_drawer);

        // populateDB();

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerMenu = mNavigationView.getMenu();
        mProgramSubMenu = mDrawerMenu.addSubMenu(getString(R.string.drawer_title_programs));

        final DatabaseHandler db = new DatabaseHandler(this);
        mPrograms = db.getAllPrograms();

        if (!mPrograms.isEmpty()) {
            for (int i = 0; i < mPrograms.size(); i++) {
                mProgramSubMenu.add(PROGRAM_DRAWER_GROUP_ID, i, Menu.CATEGORY_SYSTEM, mPrograms.get(i).getName());
            }
        }
        mDrawerMenu.add(OTHER_DRAWER_GROUP_ID, ID_ADD_ITEM_DRAWER, Menu.CATEGORY_SECONDARY, R.string.drawer_item_add).setIcon(R.drawable.ic_add_black);
        mDrawerMenu.add(OTHER_DRAWER_GROUP_ID, ID_SETTINGS_ITEM_DRAWER, Menu.CATEGORY_SECONDARY, R.string.drawer_item_settings).setIcon(R.drawable.ic_settings_black_48dp);
        drawerNotifyDataSetChanged();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case MainActivity.ID_ADD_ITEM_DRAWER:
                        mDrawer.closeDrawers();
                        Program newProgram = new Program(getString(R.string.new_program));
                        long newProgramId = db.addProgram(newProgram);
                        newProgram.setId(newProgramId);
                        mPrograms.add(newProgram);
                        mProgramSubMenu.add(PROGRAM_DRAWER_GROUP_ID, mPrograms.size() - 1, Menu.CATEGORY_SYSTEM, newProgram.getName());
                        drawerNotifyDataSetChanged();
                        updateProgramFragment(mPrograms.size() - 1);
                        break;
                    case MainActivity.ID_SETTINGS_ITEM_DRAWER:

                        break;
                    default:
                        updateProgramFragment(menuItem.getItemId());
                        break;
                }
                // If an item from extras group is clicked,refresh NAV_ITEMS_MAIN to remove previously checked item
                /*if (menuItem.getGroupId() == PROGRAM_DRAWER_GROUP_ID) {
                    mNavigationView.getMenu().setGroupCheckable(OTHER_DRAWER_GROUP_ID, false, true);
                    mNavigationView.getMenu().setGroupCheckable(PROGRAM_DRAWER_GROUP_ID, true, true);
                }else{
                    mNavigationView.getMenu().setGroupCheckable(OTHER_DRAWER_GROUP_ID, true, true);
                    mNavigationView.getMenu().setGroupCheckable(PROGRAM_DRAWER_GROUP_ID, false, true);
                }*/
                // Update highlighted item in the navigation menu
                menuItem.setChecked(true);
                mDrawer.closeDrawers();
                return true;
            }
        });

        updateProgramFragment(0);
    }

    public void drawerNotifyDataSetChanged(){
        for (int i = 0, count = mNavigationView.getChildCount(); i < count; i++) {
            final View child = mNavigationView.getChildAt(i);
            if (child != null && child instanceof ListView) {
                final ListView menuView = (ListView) child;
                final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
                final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
                wrapped.notifyDataSetChanged();
            }
        }
    }

    public void deleteProgram(Program program) {
        int i = 0;
        Program p = mPrograms.get(0);
        while (p.getId() != program.getId()) {
            p = mPrograms.get(i);
            i++;
        }
        mProgramSubMenu.removeItem(i);
        drawerNotifyDataSetChanged();
        updateProgramFragment(0);
    }

    public void onProgramNameChanged(Program program, String newName) {
        mTitleView.setText(newName);
        mProgramSubMenu.getItem(mSelectedItemDrawerPosition).setTitle(newName);
        mProgram.setName(program.getName());
        drawerNotifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void updateProgramFragment(int position) {
        if (!mPrograms.isEmpty()) {
            DatabaseHandler db = new DatabaseHandler(this);
            mProgram = mPrograms.get(position);
            mSelectedItemDrawerPosition = position;
            mExercises = db.getAllExercisesProgram(mProgram.getId());
            buildProgramActivity();
        } else { // All programs have been deleted
            mProgram = null;
            ///// TO DO
        }
    }

    public void buildProgramActivity() {
        final FloatingActionButton bottomFloatingButton = (FloatingActionButton) findViewById(R.id.bottom_floating_button);
        bottomFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewExercise();
            }
        });
        // ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar)).setTitle(mProgram.getName());
        mTitleView = (TextView) findViewById(R.id.toolbar_title);
        mTitleView.setText(mProgram.getName());
        mTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arg = new Bundle();
                SetNameDialogFragment dialogSetName = new SetNameDialogFragment();
                arg.putString(SetNameDialogFragment.ARG_DATA_TYPE, SetNameDialogFragment.ARG_TYPE_PROGRAM);
                arg.putLong(SetNameDialogFragment.ARG_PROGRAM_ID, mProgram.getId());
                dialogSetName.setArguments(arg);
                dialogSetName.show(getSupportFragmentManager(), "TAG");
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        final TabsAdapter tabsAdapter = new TabsAdapter(this, pager);
        pager.setAdapter(tabsAdapter);

        FloatingActionButton runProgramButton = (FloatingActionButton) findViewById(R.id.button_run_program);
        if (!mExercises.isEmpty()) {
            runProgramButton.setEnabled(true);
            runProgramButton.setBackgroundTintList(getResources().getColorStateList(R.color.accent));

            Bundle argManageProgramFragment = new Bundle();
            argManageProgramFragment.putLong(ManageProgramFragment.ARG_PROGRAM_ID, mProgram.getId());
            tabsAdapter.addTab(tabLayout.newTab(), ManageProgramFragment.class, argManageProgramFragment, getString(R.string.tab_title_exercises));

            Bundle argNoExercisesFragment = new Bundle();
            argNoExercisesFragment.putLong(NoExercisesFragment.ARG_PROGRAM_ID, mProgram.getId());
            tabsAdapter.addTab(tabLayout.newTab(), NoExercisesFragment.class, argNoExercisesFragment, getString(R.string.tab_title_stats));
        } else {
            runProgramButton.setEnabled(false);
            runProgramButton.setBackgroundTintList(getResources().getColorStateList(R.color.divider));

            Bundle argNoExercisesFragment = new Bundle();
            argNoExercisesFragment.putLong(NoExercisesFragment.ARG_PROGRAM_ID, mProgram.getId());
            tabsAdapter.addTab(tabLayout.newTab(), NoExercisesFragment.class, argNoExercisesFragment, getString(R.string.tab_title_exercises));
            tabsAdapter.addTab(tabLayout.newTab(), NoExercisesFragment.class, argNoExercisesFragment, getString(R.string.tab_title_stats));
        }
        tabLayout.setTabsFromPagerAdapter(tabsAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case POSITION_EXERCISE_TAB:
                        bottomFloatingButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
                        bottomFloatingButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addNewExercise();
                            }
                        });
                        break;
                    case POSITION_STATS_TAB:
                        bottomFloatingButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_share));
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
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    public void addNewExercise(){
        DatabaseHandler db = new DatabaseHandler(MainActivity.this);
        Exercise newExercise = new Exercise(getString(R.string.new_exercise), mProgram.getId(), mExercises.size() + 1, "");
        long newExerciseId = db.addExercise(newExercise);
        newExercise.setId(newExerciseId);
        mExercises.add(newExercise);
        Intent intent = new Intent(MainActivity.this, ManageExerciseActivity.class);
        intent.putExtra(ManageExerciseActivity.ARG_EXERCISE_ID, newExerciseId);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer != null && mDrawer.isDrawerOpen(GravityCompat.END)) {
            mDrawer.closeDrawers();
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
        if (mProgram != null) {
            if (!mExercises.isEmpty()) {
                inflater.inflate(R.menu.menu_manage_program, menu);
            } else {
                // inflater.inflate(R.menu.menu_manage_program_no_sort, menu);
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
                arg.putLong(DeleteDialogFragment.ARG_PROGRAM_ID, mProgram.getId());
                dialogDelete.setArguments(arg);
                dialogDelete.show(getSupportFragmentManager(), "TAG");
                return true;
            case R.id.action_set_name:
                SetNameDialogFragment dialogSetName = new SetNameDialogFragment();
                arg.putString(SetNameDialogFragment.ARG_DATA_TYPE, SetNameDialogFragment.ARG_TYPE_PROGRAM);
                arg.putLong(SetNameDialogFragment.ARG_PROGRAM_ID, mProgram.getId());
                dialogSetName.setArguments(arg);
                dialogSetName.show(getSupportFragmentManager(), "TAG");
                return true;
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateDB() {
        DatabaseHandler db = new DatabaseHandler(this);
        db.deleteAllPrograms();
        db.deleteAllExercises();
        db.deleteAllSets();
        db.deleteAllImages();
        db.deleteAllMuscles();

        long idRavi = db.addProgram(new Program("Half-body"));
        long idPushups = db.addExercise(new Exercise("Barbell Rear Delt Row c'est la fête au camping", idRavi, 0, "While keeping the upper arms perpendicular to the torso, " +
                "pull the barbell up towards your upper chest as you squeeze the " +
                "rear delts and you breathe out"));
        long idBarbell = db.addExercise(new Exercise("Barbell Bench Press", idRavi, 1, "From the starting position, breathe in and begin " +
                "coming down slowly until the bar touches your middle chest"));
        long idFlyes = db.addExercise(new Exercise("Bodyweight Flyes", idRavi, 2, "Alternating your left and right arms, whipping " +
                "the ropes up and down as fast as you can"));
        long idButterfly = db.addExercise(new Exercise("Butterfly", idRavi, 3, new String()));
        long idPress = db.addExercise(new Exercise("Cable Chest Press ", idRavi, 4, new String()));
        long idFlyPress = db.addExercise(new Exercise("Cable Fly Press ", idRavi, 5, new String()));

        db.addMuscle(new Muscle("Ischio", Muscle.MUSCLE_TYPE_PRIMARY, idPushups));
        db.addMuscle(new Muscle("Quadriceps", Muscle.MUSCLE_TYPE_PRIMARY, idPushups));
        db.addMuscle(new Muscle("Trapez", Muscle.MUSCLE_TYPE_SECONDARY, idPushups));
        db.addMuscle(new Muscle("Biceps", Muscle.MUSCLE_TYPE_SECONDARY, idPushups));
        db.addMuscle(new Muscle("Tricpes", Muscle.MUSCLE_TYPE_SECONDARY, idPushups));

        db.addMuscle(new Muscle("Ischio", Muscle.MUSCLE_TYPE_PRIMARY, idBarbell));
        db.addMuscle(new Muscle("Quadriceps", Muscle.MUSCLE_TYPE_PRIMARY, idBarbell));

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