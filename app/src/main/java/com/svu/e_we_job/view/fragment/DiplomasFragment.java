package com.svu.e_we_job.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.svu.e_we_job.util.adapter.DiplomaAdapter;
import com.svu.e_we_job.util.controller.provider.Http;
import com.svu.e_we_job.util.controller.model.Candidate;
import com.svu.e_we_job.util.controller.model.Diploma;
import com.svu.e_we_job.util.session.LoginManager;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class DiplomasFragment extends MainFragment {

    LoginManager sessionManager;

    List<Diploma> diplomaList;

    public DiplomaAdapter adapter;

    int id;

    public static final String TAG= DiplomasFragment.class.getSimpleName();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager= LoginManager.getInstance(getContext());
        sessionManager.checkLogout(getActivity());
        id=sessionManager.getID();

        diplomaList=new ArrayList<>();
        adapter=new DiplomaAdapter(diplomaList,getContext());
        list.setAdapter(adapter);

        getDiplomas();
    }

    private void getDiplomas(){
        AsyncHttpClient httpClient=new AsyncHttpClient();

        RequestParams params=new RequestParams();
        params.put(Candidate.CANDIDATE_ID,String.valueOf(id));

        httpClient.post(getContext(), Http.POST_DIPLOMAS,params, new  AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                progressDialog.setVisibility(View.VISIBLE);
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(responseBody!=null) {
                    Log.e(TAG, responseBody.toString());
                    diplomaList = new Gson().fromJson(new String(responseBody), new TypeToken<ArrayList<Diploma>>() {}.getType());

                    adapter.updateList(diplomaList);

                    if(diplomaList.isEmpty())
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


    @Override
    public void onStart() {
        super.onStart();
        getDiplomas();
    }
}
