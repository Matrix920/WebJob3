package com.svu.e_we_job.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.svu.e_we_job.R;
import com.svu.e_we_job.util.adapter.CandidateAdapter;
import com.svu.e_we_job.util.controller.provider.Http;
import com.svu.e_we_job.util.controller.model.Candidate;
import com.svu.e_we_job.util.controller.model.Job;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SuitableCandidatesActivity extends AppCompatActivity {

    public static final  String TAG=SuitableCandidatesActivity.class.getSimpleName();

    ListView listView;
    CandidateAdapter adapter;
    List<Candidate>list;
    int jobID;
    TextView txtNoData;

    ProgressBar progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suitable_candidates);

//        initTransition();

        txtNoData=findViewById(R.id.txtNodata);

        listView=findViewById(R.id.list);

        progressDialog=findViewById(R.id.progressBar);

        list=new ArrayList<>();
        adapter=new CandidateAdapter(this,list);
        listView.setAdapter(adapter);

        jobID=getIntent().getIntExtra(Job.JOB_ID,0);

        getCandidates();
    }

    private void getCandidates(){
        AsyncHttpClient httpClient=new AsyncHttpClient();

        RequestParams params=new RequestParams();
        params.put(Job.JOB_ID,String.valueOf(jobID));

        httpClient.post(this, Http.POST_GET_SUITABLE_CANDIDATES,params, new  AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                progressDialog.setVisibility(View.VISIBLE);
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(responseBody!=null) {
                    Log.e(TAG, responseBody.toString());
                    list = new Gson().fromJson(new String(responseBody), new TypeToken<ArrayList<Candidate>>() {}.getType());

                    adapter.updateList(list);

                    if(list.isEmpty())
                        viewText();
                    else
                        hideText();
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
    public void hideText(){
        txtNoData.setVisibility(View.GONE);
    }

    public void viewText(){
        txtNoData.setVisibility(View.VISIBLE);
    }

    void initTransition(){

        android.transition.Slide enterTransition=new android.transition.Slide();
        enterTransition.setSlideEdge(Gravity.LEFT);
        enterTransition.setDuration(500);
        enterTransition.setInterpolator(new LinearInterpolator());
        getWindow().setEnterTransition(enterTransition);

    }
}
