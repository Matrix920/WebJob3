package com.svu.e_we_job.view.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.svu.e_we_job.R;
import com.svu.e_we_job.util.tool.WeJobUtil;
import com.svu.e_we_job.util.controller.provider.Http;
import com.svu.e_we_job.util.controller.model.Job;
import com.svu.e_we_job.util.session.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddJobFragment extends Fragment {

    LoginManager sessionManager;

    public static final String TAG=AddJobFragment.class.getSimpleName();

    EditText edtTitle,edtSalary,edtExperience,edtEduaction;

    Button btnAddJob;

    int id;

    ProgressBar progressDialog;
    public AddJobFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_add_job, container, false);

        sessionManager= LoginManager.getInstance(getContext());
        sessionManager.checkLogout(getActivity());
        id=sessionManager.getID();

        progressDialog=v.findViewById(R.id.progressBar);

        btnAddJob=v.findViewById(R.id.btnAddJob);
        btnAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addJob();
            }
        });

        edtTitle=v.findViewById(R.id.edtTitle);
        edtSalary=v.findViewById(R.id.edtSalary);
        edtExperience=v.findViewById(R.id.edtRequiredExperienceLevel);
        edtEduaction=v.findViewById(R.id.edtRequiredEducationLevel);

        return v;
    }

    public void addJob(){
        if(! WeJobUtil.isEmpty(edtTitle,edtSalary,edtEduaction,edtExperience))
            save();
        else
            WeJobUtil.showToast(getContext(), getResources().getString(R.string.empty));
    }

    private void save(){
        AsyncHttpClient httpClient=new AsyncHttpClient();

        RequestParams params=new RequestParams();
        params.add(Job.TITLE,edtTitle.getText().toString().trim());
        params.add(Job.SALARY,edtSalary.getText().toString().trim());
        params.add(Job.REQUIRED_EDUCATION_LEVEL,edtEduaction.getText().toString().trim());
        params.add(Job.REQUIRED_EXPERIENCE_LEVEL,edtExperience.getText().toString().trim());
        params.add(Job.COMPANY_ID,String.valueOf(id));

        httpClient.post(getContext(), Http.POST_ADD_JOB,params, new  AsyncHttpResponseHandler() {

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
                            WeJobUtil.showToast(getContext(),getResources().getString(R.string.success));
                        }else{
                            WeJobUtil.showToast(getContext(),getResources().getString(R.string.error));
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

}
