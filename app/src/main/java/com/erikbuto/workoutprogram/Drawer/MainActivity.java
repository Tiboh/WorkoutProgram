package com.erikbuto.workoutprogram.Drawer;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.erikbuto.workoutprogram.DB.DatabaseHandler;
import com.erikbuto.workoutprogram.DB.Exercise;
import com.erikbuto.workoutprogram.DB.Program;
import com.erikbuto.workoutprogram.DB.Set;
import com.erikbuto.workoutprogram.DeleteDialogFragment;
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
    private int mSelectedItemDrawerPosition;
    public static final int ID_ADD_ITEM_DRAWER = 0;
    public static final int ID_SETTINGS_ITEM_DRAWER = 1;

    private Toolbar mToolbar;
    private TextView mTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // populateDB();

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        final int PROGRAM_GROUP_ID = 0;
        final int OTHER_GROUP_ID = 0;
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        Menu menu = navigationView.getMenu();

        SubMenu parent1 = menu.addSubMenu("SubMenu1");
        SubMenu parent2 = menu.addSubMenu("SubMenu2");

        parent1.add("Item1-1");
        parent1.add("Item1-2");
        parent1.add("Item1-3");

        parent2.add("Item2-1");
        parent2.add("Item2-2");

        for (int i = 0, count = navigationView.getChildCount(); i < count; i++) {
            final View child = navigationView.getChildAt(i);
            if (child != null && child instanceof ListView) {
                final ListView menuView = (ListView) child;
                final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
                final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
                wrapped.notifyDataSetChanged();
            }
        }

        // buildDrawer();
    }

    public void buildDrawer() {
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
        addLastItemsDrawer(mDrawer);

        updateFragmentView(mPrograms);

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
                        removeLastItemsDrawer(mDrawer);
                        mDrawer.addItem(new PrimaryDrawerItem().withName(newProgram.getName()), mPrograms.size() - 1);
                        addLastItemsDrawer(mDrawer);
                        updateProgramFragment(mPrograms.size() - 1);
                        break;
                    case ID_SETTINGS_ITEM_DRAWER:
                        // Intent intent = new Intent(this, SettingsActivity.class);
                        break;
                    default:
                        updateProgramFragment(position);
                        mDrawer.closeDrawer();
                }
                return true;
            }
        });
    }

    public void removeLastItemsDrawer(Drawer drawer){
        int drawerSize = drawer.getDrawerItems().size();
        drawer.removeItem(drawerSize-1);
        drawer.removeItem(drawerSize);
        drawer.removeItem(drawerSize+1);
    }

    public void addLastItemsDrawer(Drawer drawer){
        drawer.addItem(new PrimaryDrawerItem()
                        .withName(R.string.action_add)
                        .withIcon(R.drawable.ic_add_black)
                        .withIdentifier(ID_ADD_ITEM_DRAWER)
        );

        drawer.addItem(new DividerDrawerItem());

        drawer.addItem(new PrimaryDrawerItem()
                        .withName(R.string.action_settings)
                        .withIcon(R.drawable.ic_settings_black_48dp)
                        .withIdentifier(ID_SETTINGS_ITEM_DRAWER)
        );
    }

    public void onProgramNameChanged(String newName) {
        mTitleView.setText(newName);
        mDrawer.removeItem(mSelectedItemDrawerPosition);
        mDrawer.addItem(new PrimaryDrawerItem().withName(newName), mSelectedItemDrawerPosition);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // mDrawer.resetDrawerContent();
    }

    public Drawer getmDrawer() {
        return mDrawer;
    }

    public void updateFragmentView(ArrayList<Program> programs) {
        if (!programs.isEmpty()) {
            updateProgramFragment(0); // TO DO change hard code to updateProgramFragment(last user's choice)
        } else {
            mCurrent = null;
            ///// TO DO inflate NoPrograms layout
        }
    }

    public void updateProgramFragment(int position) {
        DatabaseHandler db = new DatabaseHandler(this);
        mCurrent = mPrograms.get(position);
        mSelectedItemDrawerPosition = position;
        mExercises = db.getAllExercisesProgram(mCurrent.getId());
        buildProgramActivity();
    }

    public void buildProgramActivity() {
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
        mTitleView = (TextView) findViewById(R.id.toolbar_title);
        mTitleView.setText(mCurrent.getName());
        mTitleView.setFocusableInTouchMode(true);
        mTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arg = new Bundle();
                SetNameDialogFragment dialogSetName = new SetNameDialogFragment();
                arg.putString(SetNameDialogFragment.ARG_DATA_TYPE, SetNameDialogFragment.ARG_TYPE_PROGRAM);
                arg.putLong(SetNameDialogFragment.ARG_PROGRAM_ID, mCurrent.getId());
                dialogSetName.setArguments(arg);
                dialogSetName.show(getSupportFragmentManager(), "TAG");
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        TabsAdapter tabsAdapter = new TabsAdapter(this, pager);
        pager.setAdapter(tabsAdapter);

        FloatingActionButton runProgramButton = (FloatingActionButton) findViewById(R.id.button_run_program);
        if(!mExercises.isEmpty()) {
            runProgramButton.setEnabled(true);
            runProgramButton.setBackgroundTintList(getResources().getColorStateList(R.color.accent));

            Bundle argManageProgramFragment = new Bundle();
            argManageProgramFragment.putLong(ManageProgramFragment.ARG_PROGRAM_ID, mCurrent.getId());
            tabsAdapter.addTab(tabLayout.newTab(), ManageProgramFragment.class, argManageProgramFragment, getString(R.string.tab_title_exercises));

            Bundle argNoExercisesFragment = new Bundle();
            argNoExercisesFragment.putLong(NoExercisesFragment.ARG_PROGRAM_ID, mCurrent.getId());
            tabsAdapter.addTab(tabLayout.newTab(), NoExercisesFragment.class, argNoExercisesFragment, getString(R.string.tab_title_stats));
        }else{
            runProgramButton.setEnabled(false);
            runProgramButton.setBackgroundTintList(getResources().getColorStateList(R.color.divider));

            Bundle argNoExercisesFragment = new Bundle();
            argNoExercisesFragment.putLong(NoExercisesFragment.ARG_PROGRAM_ID, mCurrent.getId());
            tabsAdapter.addTab(tabLayout.newTab(), NoExercisesFragment.class, argNoExercisesFragment, getString(R.string.tab_title_exercises));
            tabsAdapter.addTab(tabLayout.newTab(), NoExercisesFragment.class, argNoExercisesFragment, getString(R.string.tab_title_stats));
        }

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

        if (mCurrent != null) {
            if (!mExercises.isEmpty()) {
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

    private void populateDB() {
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