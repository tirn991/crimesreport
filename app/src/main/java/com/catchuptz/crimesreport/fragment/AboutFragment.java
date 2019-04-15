package com.catchuptz.crimesreport.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.catchuptz.crimesreport.R;
import com.catchuptz.crimesreport.util.SharedPrefManager;
import com.catchuptz.crimesreport.util.api.BaseApiService;
import com.catchuptz.crimesreport.util.api.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;

    TextView aboutus;


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_about, container, false);

        ButterKnife.bind(getActivity());
        mContext = getContext();
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(getContext());

        aboutus = view.findViewById(R.id.about_text);

        loading = ProgressDialog.show(mContext, null, "Loading ...", true, false);
        String id = "CRIMES";
        getAboutUs(id);

        return view;
    }

    private void getAboutUs(String id){
        mApiService.getaboutus(id)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                Log.e("Data About: ", jsonRESULTS.toString());
                                    String content = jsonRESULTS.getString("content");
                                    Log.e("actorType: ", content);
                                    aboutus.setText(content);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.dismiss();
                    }
                });
    }

}
