package com.svu.e_we_job.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.svu.e_we_job.R;
import com.svu.e_we_job.util.adapter.JobAdapter;
import com.svu.e_we_job.util.controller.provider.Http;
import com.svu.e_we_job.util.controller.model.Job;
import com.svu.e_we_job.util.session.LoginManager;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class OfferedJobsFragment extends Fragment {

    LoginManager sessionManager;

    ProgressBar progressDialog;

    public JobAdapter adapter;

    List<Job>jobList;
    ListView list;

    Button btnSortExperience,btnSortEducation;

    public static final String TAG= OfferedJobsFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_offered_jobs,container,false);

        list=v.findViewById(R.id.list);

        btnSortEducation=v.findViewById(R.id.btnSortEducation);
        btnSortExperience=v.findViewById(R.id.btnSortExperience);

        btnSortExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadJobs(Job.SORT_EXPERIENCE);
            }
        });

        btnSortEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadJobs(Job.SORT_EDUCATION);
            }
        });

        sessionManager= LoginManager.getInstance(getContext());
        sessionManager.checkLogout(getActivity());

        progressDialog=v.findViewById(R.id.progressBar);

        jobList=new ArrayList<>();

        adapter=new JobAdapter(jobList,getContext());
        list.setAdapter(adapter);

        loadJobs(0);

        return v;
    }


    private void loadJobs(int type){
        AsyncHttpClient httpClient=new AsyncHttpClient();

        RequestParams params=new RequestParams();
        params.put(Job.TYPE,type);

        httpClient.post(getContext(), Http.POST_GET_OFFERED_JOBS,params, new  AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                progressDialog.setVisibility(View.VISIBLE);
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(responseBody!=null) {
                    Log.e(TAG, responseBody.toString());
                    List<Job> list = new Gson().fromJson(new String(responseBody), new TypeToken<ArrayList<Job>>() {}.getType());

                    adapter.updateList(list);

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
}
