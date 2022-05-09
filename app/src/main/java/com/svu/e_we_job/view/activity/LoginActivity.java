package com.svu.e_we_job.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.svu.e_we_job.util.controller.model.Login;
import com.svu.e_we_job.util.session.LoginManager;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG=LoginActivity.class.getSimpleName();

    LoginManager sessionManager;

    EditText edtLogin,edtPassword;
    Button btnLogin;
    ProgressBar progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager= LoginManager.getInstance(getApplicationContext());
        sessionManager.checkLogin(this);

        progressDialog=findViewById(R.id.progressBar);

        edtLogin=findViewById(R.id.edtLogin);
        edtPassword=findViewById(R.id.edtPassword);
        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(! WeJobUtil.isEmpty(edtLogin,edtPassword))
                    login();
                else
                    WeJobUtil.showToast(LoginActivity.this,getResources().getString(R.string.empty));
            }
        });
    }

    private void login(){
            AsyncHttpClient httpClient=new AsyncHttpClient();

            RequestParams params=new RequestParams();
            params.add(Candidate.LOGIN,edtLogin.getText().toString().trim());
            params.add(Candidate.PASSWORD,edtPassword.getText().toString());

            httpClient.post(getApplicationContext(), Http.POST_LOGIN,params, new  AsyncHttpResponseHandler() {

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
                            if(success) {
                                String role = object.getString(Login.ROLE);
                                int id = object.getInt(Login.ID);
                                String name=object.getString(Login.NAME);

                                sessionManager.login(id, role,name, LoginActivity.this);
                            }else{
                                WeJobUtil.showToast(LoginActivity.this,"Authentication Error");
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

        public void register(View v){
            Intent i=new Intent(this,RegisterCandidateActivity.class);
            startActivity(i);
//            finish();
        }

        public void registerCompany(View v){
        Intent i=new Intent(this,RegisterCompanyActivity.class);
        startActivity(i);
        }

}
