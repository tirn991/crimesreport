package com.catchuptz.crimesreport;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.catchuptz.crimesreport.util.api.BaseApiService;
import com.catchuptz.crimesreport.util.api.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.etNama)
    EditText etNama;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etCfmPassword)
    EditText etCfmPassword;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        ButterKnife.bind(this);
        mContext = this;
        mApiService = UtilsApi.getAPIService();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCfmPassword.getText().toString().equals(etPassword.getText().toString())){
                    loading = ProgressDialog.show(mContext, null, "Registering ... ", true, false);
                    requestRegister();
                }else {
                    status.setText("Passwords Does not Match");
                    status.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void requestRegister(){
        mApiService.registerRequest(etNama.getText().toString(),
                etEmail.getText().toString(), etPhone.getText().toString(), "user",
                etPassword.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: BERHASIL");
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                Log.e("DATA: ", jsonRESULTS.toString());
                                if (jsonRESULTS.getString("status").equals("true")){
                                    Toast.makeText(mContext, jsonRESULTS.getString("message"), Toast.LENGTH_SHORT).show();
                                    String userId = jsonRESULTS.getString("data");
                                    startActivity(new Intent(mContext, LoginActivity.class));
                                } else {
                                    String error_message = jsonRESULTS.toString();
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.i("debug", "onResponse: GA BERHASIL");
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
