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
import com.svu.e_we_job.util.controller.model.Diploma;
import com.svu.e_we_job.util.session.LoginManager;
import com.svu.e_we_job.view.activity.RegisterCompanyActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddDiplomaFragment extends Fragment {

    LoginManager sessionManager;

    public static final String TAG= RegisterCompanyActivity.class.getSimpleName();

    EditText edtDiplomaTitle;

    int id;

    Button btnAddDiploma;

    ProgressBar progressDialog;

    public AddDiplomaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_add_diploma, container, false);

        sessionManager= LoginManager.getInstance(getContext());
        sessionManager.checkLogout(getActivity());
        id=sessionManager.getID();

        btnAddDiploma=v.findViewById(R.id.btnAddDiploma);
        btnAddDiploma.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addDiploma();
            }
        });

        progressDialog=v.findViewById(R.id.progressBar);
        edtDiplomaTitle=v.findViewById(R.id.edtDiplomaTitle);

        return v;
    }

    private void addDiploma(){
        AsyncHttpClient httpClient=new AsyncHttpClient();

        RequestParams params=new RequestParams();
        params.add(Diploma.DIPLOMA_TITLE,edtDiplomaTitle.getText().toString().trim());
        params.add(Diploma.CANDIDATE_ID,String.valueOf(id));

        httpClient.post(getContext(), Http.POST_ADD_DIPLOMA,params, new  AsyncHttpResponseHandler() {

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
