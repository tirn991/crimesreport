package com.catchuptz.crimesreport;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.catchuptz.crimesreport.adapter.CaseAttendanceAdapter;
import com.catchuptz.crimesreport.model.CaseAttendance;
import com.catchuptz.crimesreport.model.ResponseCaseAttendance;
import com.catchuptz.crimesreport.util.SharedPrefManager;
import com.catchuptz.crimesreport.util.api.BaseApiService;
import com.catchuptz.crimesreport.util.api.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleCase extends AppCompatActivity {
    private CaseAttendanceAdapter caseAdapter;
    ArrayList<CaseAttendance> cases = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    Toolbar toolbar;
    Context mContext;
    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;

    @BindView(R.id.edittext_chatbox)
    EditText edittext_chatbox;

    Button button_chatbox_send;

    ProgressDialog loading;

    FrameLayout emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_case);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String file_number = getIntent().getStringExtra("file_number");
        String casetitle = getIntent().getStringExtra("casetitle");
        String casedetails = getIntent().getStringExtra("casedetails");
        String userage = getIntent().getStringExtra("userage");
        String residence = getIntent().getStringExtra("residence");
        String casestatus = getIntent().getStringExtra("casestatus");
        String phonenumber = getIntent().getStringExtra("phonenumber");
        String name = getIntent().getStringExtra("name");
        toolbar.setTitle(file_number);
        getSupportActionBar().setTitle("");

        TextView toolbartitle = findViewById(R.id.toolbartitle);
        toolbartitle.setText("FILE: "+file_number);


        TextView txcasetitle = findViewById(R.id.casetitle);
        txcasetitle.setText(casetitle);
        TextView txname = findViewById(R.id.name);
        txname.setText(name);
        TextView txphone = findViewById(R.id.phone);
        txphone.setText(phonenumber);
        TextView txage = findViewById(R.id.age);
        txage.setText(userage+" Years");
        TextView txcasedetails = findViewById(R.id.case_details);
        txcasedetails.setText(casedetails);

        ButterKnife.bind(this);
        mContext = this;
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(this);

        emptyView = (FrameLayout) findViewById(R.id.emptyView);

        TextView textStatus = findViewById(R.id.status);
        textStatus.setText("No Conversation About this Case for Now");

        mProgressBar=(ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView=(RecyclerView) findViewById(R.id.cars_list);

        caseAdapter = new CaseAttendanceAdapter(cases, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        button_chatbox_send = findViewById(R.id.button_chatbox_send);
        button_chatbox_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Sending Message", true, false);
                sendMessage();
            }
        });
        fetchUserCaseAttendance();
    }

    private void sendMessage(){
        sharedPrefManager = new SharedPrefManager(this);
        String attendername = sharedPrefManager.getSPNama();
        Log.e("name: ", attendername);
        String victim_id = getIntent().getStringExtra("case_user_id");
        String case_id = getIntent().getStringExtra("case_id");
        String attendertitle = sharedPrefManager.getSpActorType();
        String user_id = sharedPrefManager.getSpUserid();

        mApiService.savecaseattendance(case_id,
                victim_id,
                user_id,
                attendername,
                attendertitle,
                edittext_chatbox.getText().toString())
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
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
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
                        loading.dismiss();
                    }
                });
    }

    public void fetchUserCaseAttendance(){
        String name = getIntent().getStringExtra("name");
        String case_user_id = getIntent().getStringExtra("case_user_id");
        String casetitle = getIntent().getStringExtra("casetitle");
        String casedetails = getIntent().getStringExtra("casedetails");
        String userage = getIntent().getStringExtra("userage");
        String residence = getIntent().getStringExtra("residence");
        String file_number = getIntent().getStringExtra("file_number");
        String casestatus = getIntent().getStringExtra("casestatus");
        String case_id = getIntent().getStringExtra("case_id");

        mApiService.getUserCasesAttendance(case_id, case_user_id).enqueue(new Callback<ResponseCaseAttendance>() {
            @Override
            public void onResponse(Call<ResponseCaseAttendance> call, Response<ResponseCaseAttendance> response) {
                mProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body()!=null) {
                    final ArrayList<CaseAttendance> allcases = response.body().getCaseitem();
                    Log.e("CHENGA: ", allcases.toString());
                    mRecyclerView.setAdapter(new CaseAttendanceAdapter(allcases, mContext));
                    caseAdapter.notifyDataSetChanged();
                }else{
                    mProgressBar.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
//                    Toast.makeText(getApplicationContext(),"Oops No conversation Done!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCaseAttendance> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Oops! Something went wrong! Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.casemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.case_info:
//                Toast.makeText(getApplicationContext(),"Hurray!", Toast.LENGTH_SHORT).show();
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//                sendIntent.setType("text/plain");
//                this.startActivity(sendIntent);
                String casetitle = getIntent().getStringExtra("casetitle");
                String casedetails = getIntent().getStringExtra("casedetails");
                String file_number = getIntent().getStringExtra("file_number");
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.popup,null, false);
                PopupWindow pw = new PopupWindow(customView,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        true);
                pw.showAtLocation(this.findViewById(R.id.casesingle), Gravity.TOP, 0, 0);
                TextView caseT = customView.findViewById(R.id.case_title);
                TextView caseD = customView.findViewById(R.id.case_details);
                caseD.setText(casedetails);
                caseT.setText("File No: "+file_number+" - "+casetitle);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
