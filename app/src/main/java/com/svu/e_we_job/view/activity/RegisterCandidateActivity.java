package com.svu.e_we_job.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.svu.e_we_job.R;
import com.svu.e_we_job.util.tool.WeJobUtil;
import com.svu.e_we_job.util.controller.provider.Http;
import com.svu.e_we_job.util.controller.model.Candidate;
import com.svu.e_we_job.util.session.LoginManager;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegisterCandidateActivity extends AppCompatActivity {

    LoginManager sessionManager;

    public static final String TAG=RegisterCandidateActivity.class.getSimpleName();

    EditText edtLogin,edtPassword,edtFullname,edtExperience,edtTel;

    ProgressBar progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_candidate);


        sessionManager= LoginManager.getInstance(getApplicationContext());
        sessionManager.checkLogin(this);

        progressDialog=findViewById(R.id.progressBar);

        edtLogin=findViewById(R.id.edtLogin);
        edtPassword=findViewById(R.id.edtPassword);
        edtFullname=findViewById(R.id.edtFullName);
        edtExperience=findViewById(R.id.edtExperienceYears);
        edtTel=findViewById(R.id.edtTel);
    }

    void initTransition(){

        android.transition.Slide enterTransition=new android.transition.Slide();
        enterTransition.setSlideEdge(Gravity.LEFT);
        enterTransition.setDuration(500);
        enterTransition.setInterpolator(new LinearInterpolator());
        getWindow().setEnterTransition(enterTransition);

    }

    public void saveCandidate(View v){
        if(! WeJobUtil.isEmpty(edtLogin,edtPassword,edtFullname,edtTel,edtExperience))
            register();
        else
            WeJobUtil.showToast(RegisterCandidateActivity.this, getResources().getString(R.string.empty));
    }

    private void register(){
        AsyncHttpClient httpClient=new AsyncHttpClient();

        RequestParams params=new RequestParams();
        params.add(Candidate.LOGIN,edtLogin.getText().toString().trim());
        params.add(Candidate.PASSWORD,edtPassword.getText().toString());
        params.add(Candidate.FULL_NAME,edtFullname.getText().toString());
        params.add(Candidate.TEL,edtTel.getText().toString());
        params.add(Candidate.EXPERIENCE_YEARS,edtExperience.getText().toString());

        httpClient.post(getApplicationContext(), Http.POST_ADD_CANDIDATE,params, new  AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                progressDialog.setVisibility(View.VISIBLE);
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(responseBody!=null){
                    Log.e(TAG,responseBody.toString());
                    try {
                        JSONObject object = new JSONObject(new String(responseBody));

                        boolean success=object.getBoolean(Http.SUCCESS);

                        if(success){
                            WeJobUtil.showToast(RegisterCandidateActivity.this,getResources().getString(R.string.registered));
                        }else{
                            WeJobUtil.showToast(RegisterCandidateActivity.this,getResources().getString(R.string.registerd_error));
                        }

                    }catch (JSONException e){
                        Log.e(TAG,e.getMessage());
                    }
                }

                progressDialog.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.setVisibility(View.GONE);
                error.printStackTrace();
            }

        });
    }

    public void login(View v){
        Intent i=new Intent(this,LoginActivity.class);
        startActivity(i);
        finish();
    }
}
