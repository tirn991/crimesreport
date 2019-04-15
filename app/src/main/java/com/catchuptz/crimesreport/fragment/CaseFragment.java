package com.catchuptz.crimesreport.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.catchuptz.crimesreport.AddCase;
import com.catchuptz.crimesreport.R;
import com.catchuptz.crimesreport.adapter.CaseAdapter;
import com.catchuptz.crimesreport.model.CaseItem;
import com.catchuptz.crimesreport.model.ResponseCase;
import com.catchuptz.crimesreport.util.SharedPrefManager;
import com.catchuptz.crimesreport.util.api.BaseApiService;
import com.catchuptz.crimesreport.util.api.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaseFragment extends Fragment {

    private CaseAdapter caseAdapter;
    ArrayList<CaseItem> cases = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    Context mContext;

    BaseApiService mApiService;

    SharedPrefManager sharedPrefManager;

    FrameLayout emptyView;

    SwipeRefreshLayout mSwipeRefreshLayout;


    public CaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_case, container, false);

        ButterKnife.bind(getActivity());
        sharedPrefManager = new SharedPrefManager(getContext());
        String userid = sharedPrefManager.getSpUserid();
        final String actor = sharedPrefManager.getSpActorType();


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actor.equals("doctor")) {
                    Snackbar.make(view, "Doctor Can not Add A case", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }else {
                    Intent veh = new Intent(getContext(), AddCase.class);
                    veh.putExtra("name", "Hurray");
                    veh.putExtra("case_id", "1");
                    getContext().startActivity(veh);
                }
            }
        });

        ButterKnife.bind(getActivity());
        mContext = getContext();
        mApiService = UtilsApi.getAPIService();

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        emptyView = (FrameLayout) view.findViewById(R.id.emptyView);

        TextView textStatus = view.findViewById(R.id.status);
        textStatus.setText("No Case Found, Add Now");

        mProgressBar=(ProgressBar) view.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView=(RecyclerView)view. findViewById(R.id.cars_list);

        caseAdapter = new CaseAdapter(cases, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        fetchUserCases(userid);

        return view;
    }

    public void fetchUserCases(String userid){
        mApiService.getUserCases(userid).enqueue(new Callback<ResponseCase>() {
            @Override
            public void onResponse(Call<ResponseCase> call, final Response<ResponseCase> response) {
                mProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body()!=null) {
                    final ArrayList<CaseItem> allcases = response.body().getCaseitem();
                    Log.e("CHENGA: ", allcases.toString());
                    mRecyclerView.setAdapter(new CaseAdapter(allcases, mContext));
                    caseAdapter.notifyDataSetChanged();

                    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            final ArrayList<CaseItem> allcases = response.body().getCaseitem();
                            Log.e("CHENGA: ", allcases.toString());
                            mRecyclerView.setAdapter(new CaseAdapter(allcases, mContext));
                            caseAdapter.notifyDataSetChanged();
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }else{
                    mProgressBar.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseCase> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(),"Oops! Something went wrong! Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
