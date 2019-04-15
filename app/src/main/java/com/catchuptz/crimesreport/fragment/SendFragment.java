package com.catchuptz.crimesreport.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
public class SendFragment extends Fragment {
    EditText message;
    Button btnSend;

    SharedPrefManager sharedPrefManager;

    Context mContext;
    BaseApiService mApiService;

    ProgressDialog loading;

    public SendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_send, container, false);

        ButterKnife.bind(getActivity());
        mContext = getContext();
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(getContext());

        final String sender_id = sharedPrefManager.getSpUserid();
        final String sender_name = sharedPrefManager.getSPNama();
        final String email = sharedPrefManager.getSPEmail();

        message = view.findViewById(R.id.message);
        btnSend = view.findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Sending ...", true, false);
                send_message(sender_id, sender_name, email);
            }
        });

        return view;
    }

    private void send_message(String sender_id, String sender_name, final String email){
        mApiService.savemessage(
                message.getText().toString(),
                sender_id,
                sender_name,
                email
        )
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: Martin");
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                Log.e("DATA: ", jsonRESULTS.toString());
                                if (jsonRESULTS.getString("status").equals("true")){
                                    Toast.makeText(mContext, jsonRESULTS.getString("message"), Toast.LENGTH_SHORT).show();
                                    String userId = jsonRESULTS.getString("data");
                                    AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getContext())
                                            //set message, title, and icon
                                            .setTitle("Ok")
                                            .setMessage("Message Is sent, Any reply will be made to your email "+email)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    message.setText("");
                                                }
                                            })
                                            .create();
                                    myQuittingDialogBox.show();
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.i("debug", "onResponse: GA Some Errors");
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(mContext, "Internet Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
