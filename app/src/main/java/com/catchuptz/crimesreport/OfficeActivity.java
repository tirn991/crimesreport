package com.catchuptz.crimesreport;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.catchuptz.crimesreport.fragment.AboutFragment;
import com.catchuptz.crimesreport.fragment.OfficeCaseFragment;
import com.catchuptz.crimesreport.fragment.ProfileFragment;
import com.catchuptz.crimesreport.fragment.SendFragment;
import com.catchuptz.crimesreport.util.SharedPrefManager;
import com.catchuptz.crimesreport.util.api.BaseApiService;
import com.catchuptz.crimesreport.util.api.UtilsApi;

import butterknife.ButterKnife;

public class OfficeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPrefManager sharedPrefManager;

    Context mContext;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Cases");

        ButterKnife.bind(this);
        mContext = this;
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_header_name);
        navUsername.setText(sharedPrefManager.getSPNama());
        TextView navUserEmail = (TextView) headerView.findViewById(R.id.textViewEmail);
        navUserEmail.setText(sharedPrefManager.getSPEmail());
        TextView textViewType = (TextView) headerView.findViewById(R.id.textViewType);
        textViewType.setText(sharedPrefManager.getSpActorType());

        OfficeCaseFragment details = new OfficeCaseFragment();
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.content_frame, details);
        tx.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        Bundle bundle = new Bundle();
        bundle.putString("name", "User");
        switch (id){
            case R.id.nav_manage:
                getSupportActionBar().setTitle("Profile");
                fragment = new ProfileFragment();
                fragment.setArguments(bundle);
                break;

            case R.id.nav_cases:
                getSupportActionBar().setTitle("Cases");
                fragment = new OfficeCaseFragment();
                fragment.setArguments(bundle);
                break;

            case R.id.nav_contact:
                getSupportActionBar().setTitle("About App");
                fragment = new AboutFragment();
                fragment.setArguments(bundle);
                break;

            case R.id.nav_send:
                getSupportActionBar().setTitle("Send to Us");
                fragment = new SendFragment();
                fragment.setArguments(bundle);
                break;

            case R.id.signout:
                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
                startActivity(new Intent(this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
