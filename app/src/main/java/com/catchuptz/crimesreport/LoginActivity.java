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
import android.widget.Toast;


import com.catchuptz.crimesreport.util.SharedPrefManager;
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

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        ButterKnife.bind(this);
        mContext = this;
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Login In...", true, false);
                requestLogin();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RegisterActivity.class));
            }
        });

        if (sharedPrefManager.getSPSudahLogin()){
            if (sharedPrefManager.getSpActorType().equals("user")){
                startActivity(new Intent(mContext, UserActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            }else if(sharedPrefManager.getSpActorType().equals("police")) {
                startActivity(new Intent(LoginActivity.this, OfficeActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            }else {
                startActivity(new Intent(LoginActivity.this, OfficeActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
            finish();
        }
    }

    private void requestLogin(){
        mApiService.loginRequest(etPhone.getText().toString(), etPassword.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                Log.e("Data Login: ", jsonRESULTS.toString());
                                if (jsonRESULTS.getString("status").equals("true")){
                                    String name = jsonRESULTS.getJSONObject("data").getString("name");
                                    String id = jsonRESULTS.getJSONObject("data").getString("id");
                                    String actorType = jsonRESULTS.getJSONObject("data").getString("actorType");
                                    String email = jsonRESULTS.getJSONObject("data").getString("email");
                                    String phone = jsonRESULTS.getJSONObject("data").getString("phone");
                                    Log.e("actorType: ", actorType);
                                    Toast.makeText(mContext, "SUCCESSFUL LOGIN: "+name, Toast.LENGTH_SHORT).show();
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, name);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_USERID, id);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_EMAIL, email);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_PHONE, phone);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_ACTOR_TYPE, actorType);
                                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
                                    if (actorType.equals("user")) {
                                        startActivity(new Intent(mContext, UserActivity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    }else if(actorType.equals("police")) {
                                    startActivity(new Intent(LoginActivity.this, OfficeActivity.class)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    }else{
                                            startActivity(new Intent(mContext, OfficeActivity.class)
                                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                        }
                                    finish();
                                } else {
                                    // Jika login gagal
                                    String error_message = jsonRESULTS.toString();
                                    loading.dismiss();
                                    Toast.makeText(mContext, "Wrong Password Or Phone Number, Try Again", Toast.LENGTH_SHORT).show();
                                }
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
