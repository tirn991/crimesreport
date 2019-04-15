package com.catchuptz.crimesreport;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class AddCase extends Activity {

    SharedPrefManager sharedPrefManager;

    Context mContext;
    BaseApiService mApiService;

    ProgressDialog loading;

    @BindView(R.id.case_title)
    EditText case_title;
    @BindView(R.id.victim_email)
    EditText victim_email;
    @BindView(R.id.victim_name)
    EditText victim_name;
    @BindView(R.id.victim_phone)
    EditText victim_phone;
    @BindView(R.id.case_details)
    EditText case_details;
    @BindView(R.id.tribe)
    EditText tribe;
    @BindView(R.id.religion)
    Spinner religion;
    @BindView(R.id.age)
    EditText age;
    @BindView(R.id.gender)
    Spinner gender;
    @BindView(R.id.residence)
    EditText residence;
    @BindView(R.id.btnSave)
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_case);

        ButterKnife.bind(this);
        mContext = this;
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(this);

        Spinner spinner = (Spinner) findViewById(R.id.gender);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, R.layout.spinner_row);
        spinner.setAdapter(adapter);

        Spinner religz = (Spinner) findViewById(R.id.religion);

        ArrayAdapter<CharSequence> adapterreligion = ArrayAdapter.createFromResource(this,
                R.array.religion, R.layout.spinner_row);
        religz.setAdapter(adapterreligion);

        String user_id = sharedPrefManager.getSpUserid();
        String actor = sharedPrefManager.getSpActorType();
        if (actor.equals("police")){
            victim_email.setVisibility(View.VISIBLE);
            victim_phone.setVisibility(View.VISIBLE);
            victim_name.setVisibility(View.VISIBLE);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loading = ProgressDialog.show(mContext, null, "Registering and Saving Case ... ", true, false);
                    requestRegister();
                }
            });

        }else if (actor.equals("user")){
            getUserData(user_id);
        }
    }

    private void getUserData(String user_id){
        mApiService.getuserdata(user_id)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                Log.e("Data Received: ", jsonRESULTS.toString());
                                final String username = jsonRESULTS.getString("name");
                                final String user_id = jsonRESULTS.getString("id");
                                final String phone = jsonRESULTS.getString("phone");
                                Log.e("User NAme", username);
                                btnSave.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        loading = ProgressDialog.show(mContext, null, "Registering ... ", true, false);
                                        save_case(user_id, username, phone);
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }


    private void save_case(String user_id, String name, String phone){
        mApiService.savecase(
                user_id,
                case_title.getText().toString(),
                case_details.getText().toString(),
                name,
                tribe.getText().toString(),
                religion.getSelectedItem().toString(),
                age.getText().toString(),
                gender.getSelectedItem().toString(),
                residence.getText().toString(),
                phone
                )
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
                                    String actor = sharedPrefManager.getSpActorType();
                                    if (actor.equals("user")){
                                        startActivity(new Intent(mContext, UserActivity.class));
                                    }else{
                                        startActivity(new Intent(mContext, OfficeActivity.class));
                                    }
//                                    finish();
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


    private void requestRegister(){
        mApiService.registerRequest(victim_name.getText().toString(),
                victim_email.getText().toString(), victim_phone.getText().toString(), "user",
                "12345678")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: ....");
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                Log.e("DATA: ", jsonRESULTS.toString());
                                if (jsonRESULTS.getString("status").equals("true")){
                                    Toast.makeText(mContext, jsonRESULTS.getString("message"), Toast.LENGTH_SHORT).show();
                                    String userId = jsonRESULTS.getString("data");
                                    save_case(userId, victim_name.getText().toString(), victim_phone.getText().toString());
                                } else {
                                    loading.dismiss();
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.dismiss();
                            Log.i("debug", "onResponse: GA BERHASIL");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(mContext, "Internet Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
