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
import com.svu.e_we_job.view.fragment.AddJobFragment;
import com.svu.e_we_job.view.fragment.CompanyJobsFragment;

public class CompanyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String[]activityTitles;

    NavigationView navigationView;
    DrawerLayout drawer;

    public static int navItemIndex=0;

    LoginManager loginManager;


    private static final String TAG_OUR_JOBS ="our jobs";
    private static final String TAG_ADD_JOB ="add job";

    public static String CURRENT_TAG= TAG_OUR_JOBS;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_company);
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

        activityTitles=getResources().getStringArray(R.array.nav_item_activity_company_title);

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
                    case R.id.nav_jobs:
                        navItemIndex=0;
                        CURRENT_TAG= TAG_OUR_JOBS;
                        break;

                    case R.id.nav_add_job:
                        navItemIndex=1;
                        CURRENT_TAG= TAG_ADD_JOB;
                        break;

                    case R.id.menu_item_logout:
                        loginManager.logout(CompanyActivity.this);
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
                CompanyJobsFragment fragment=new CompanyJobsFragment();
                return fragment;
            case 1:
                AddJobFragment fragment1=new AddJobFragment();
                return fragment1;
            default:
                CompanyJobsFragment fragment4=new CompanyJobsFragment();
                return fragment4;
        }
    }
}
