package com.catchuptz.crimesreport.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.catchuptz.crimesreport.LoginActivity;
import com.catchuptz.crimesreport.R;
import com.catchuptz.crimesreport.util.SharedPrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    SharedPrefManager sharedPrefManager;

    TextView account_name, account_email, account_type, account_phone;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(getActivity());
        sharedPrefManager = new SharedPrefManager(getContext());
        String username = sharedPrefManager.getSPNama();
        String email = sharedPrefManager.getSPEmail();
        String type = sharedPrefManager.getSpActorType();
        String phone = sharedPrefManager.getSpPhone();

        account_name = view.findViewById(R.id.account_name);
        account_email = view.findViewById(R.id.account_email);
        account_type = view.findViewById(R.id.account_type);
        account_phone = view.findViewById(R.id.account_phone);

        account_email.setText(email);
        account_name.setText(username);
        account_type.setText(type);
        account_phone.setText(phone);

        return view;
    }

}
