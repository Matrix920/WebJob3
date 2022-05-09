package com.svu.e_we_job.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.svu.e_we_job.R;
import com.svu.e_we_job.util.session.LoginManager;
import com.svu.e_we_job.view.fragment.AddDiplomaFragment;
import com.svu.e_we_job.view.fragment.DiplomasFragment;
import com.svu.e_we_job.view.fragment.OfferedJobsFragment;
import com.svu.e_we_job.view.fragment.SuitableJobsFragment;

public class CandidateActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG_DRIVER = "driver";
    private String[]activityTitles;

    NavigationView navigationView;
    DrawerLayout drawer;

    public static int navItemIndex=0;

    LoginManager loginManager;


    private static final String TAG_DIPLOMAS ="diplomas";
    private static final String TAG_OFFERED_JOBS ="offered jobs";
    private static final String TAG_SUITABLE_JOBS ="suitable jobs";
    private static final String TAG_ADD_DIPLOMA ="add diploma";
    private static final String LOGOUT ="logout";
    public static String CURRENT_TAG= TAG_DIPLOMAS;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginManager=LoginManager.getInstance(getApplicationContext());

        loginManager.checkLogout(this);

        mHandler=new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        activityTitles=getResources().getStringArray(R.array.nav_item_activity_title);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUpNavigationView();

        selectNavMenu();

    }

    @Override
    protected void onStart() {
        super.onStart();
//        loginManager.ifUserLoggedOut();
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_diplomas:
                        navItemIndex=0;
                        CURRENT_TAG= TAG_DIPLOMAS;
                        break;
                    case R.id.nav_offered_jobs:
                        navItemIndex=1;
                        CURRENT_TAG= TAG_OFFERED_JOBS;
                        break;
                    case R.id.nav_suitable_jobs:
                        navItemIndex=2;
                        CURRENT_TAG= TAG_SUITABLE_JOBS;
                        break;
                    case  R.id.nav_add_diploma:
                        navItemIndex=3;
                        CURRENT_TAG= TAG_ADD_DIPLOMA;
                        break;
                    case R.id.menu_item_logout:
                        loginManager.logout(CandidateActivity.this);
                        break;
                }

                if(item.isChecked()){
                    item.setChecked(false);
                }else{
                    item.setChecked(true);
                }

                item.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });
    }



    private void loadHomeFragment() {

        setToolbarTitle();

        Runnable mPendingRunnable=new Runnable() {
            @Override
            public void run() {
                Fragment fragment=getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame,fragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if(mPendingRunnable!=null){
            mHandler.post(mPendingRunnable);
        }

        drawer.closeDrawers();
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }


    private void selectNavMenu(){
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    public Fragment getHomeFragment() {
        switch (navItemIndex){
            case 0:
                DiplomasFragment fragment=new DiplomasFragment();
                return fragment;
            case 1:
                OfferedJobsFragment fragment1=new OfferedJobsFragment();
                return fragment1;
            case 2:
                SuitableJobsFragment fragment2=new SuitableJobsFragment();
                return fragment2;
            case 3:
                AddDiplomaFragment fragment3=new AddDiplomaFragment();
                return fragment3;
            default:
                DiplomasFragment fragment4=new DiplomasFragment();
                return fragment4;
        }
    }
}
