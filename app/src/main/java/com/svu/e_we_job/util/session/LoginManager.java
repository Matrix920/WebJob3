package com.svu.e_we_job.util.session;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.svu.e_we_job.util.controller.model.Candidate;
import com.svu.e_we_job.util.controller.model.Company;
import com.svu.e_we_job.util.controller.model.Login;
import com.svu.e_we_job.view.activity.CandidateActivity;
import com.svu.e_we_job.view.activity.CompanyActivity;
import com.svu.e_we_job.view.activity.LoginActivity;


public class LoginManager {
    private Context mContext;
    private SharedPreferences sharedPref;
    public static final String SHARED_PREF_NAME="e_we_job";
    private static final int PRIVATE_MODE=0;

    private static final String IS_LOGIN="login";
    SharedPreferences.Editor editor;

    private static LoginManager sessionManager;

    private LoginManager(Context context){
        this.mContext=context;
        sharedPref=mContext.getSharedPreferences(SHARED_PREF_NAME,PRIVATE_MODE);
        editor=sharedPref.edit();
    }

    public static LoginManager getInstance(Context context){
        if(sessionManager==null)
            sessionManager=new LoginManager(context);
        return sessionManager;
    }

    public void logout(Activity activity){
        //clear session
        editor.clear();
        editor.commit();

        StartLoginActivity(activity);
    }

    public String getName(){
        return sharedPref.getString(Login.NAME,"");
    }

    public int getID(){
        return sharedPref.getInt(Login.ID,0);
    }


    public void login(int id, String role,String name, Activity activity){
        editor.putBoolean(IS_LOGIN,true);
        editor.putInt(Login.ID,id);
        editor.putString(Login.ROLE,role);
        editor.putString(Login.NAME,name);

        editor.commit();

        StartHomeActivity(activity);
    }

    private void StartLoginActivity(Activity activity){
        Intent i=new Intent(mContext, LoginActivity.class);

        //closing all activities
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //start new activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(i);
        activity.finish();
    }

    public String getRole(){
        return sharedPref.getString(Login.ROLE,"");
    }

    private void StartHomeActivity(Activity activity){

        Intent i=new Intent();
        switch (getRole()){
            case Company.COMPANY:{
                i=new Intent(mContext, CompanyActivity.class);
                break;
            }
            case Candidate.CANDIDATE:{
                i=new Intent(mContext, CandidateActivity.class);
                break;
            }

        }

        //closing all activities
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //start new activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        mContext.startActivity(i);
        activity.finish();
    }

    public void checkLogin(Activity activity){
        boolean isLogin= sharedPref.getBoolean(IS_LOGIN,false);

        if(isLogin){
            StartHomeActivity(activity);
        }
    }

    public void checkLogout(Activity activity){
        boolean isLogin= sharedPref.getBoolean(IS_LOGIN,false);

        if(! isLogin){
            StartLoginActivity(activity);
        }
    }

}
