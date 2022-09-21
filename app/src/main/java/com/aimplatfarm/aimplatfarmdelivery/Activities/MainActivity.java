package com.aimplatfarm.aimplatfarmdelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.aimplatfarm.aimplatfarmdelivery.Fragments.Home_Fragment;
import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;
import com.aimplatfarm.aimplatfarmdelivery.session_manager.SessionManager;
import com.aimplatfarm.aimplatfarmdelivery.Models.ApiResponse;

import com.aimplatfarm.aimplatfarmdelivery.R;
import com.google.android.material.navigation.NavigationView;

import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    ProgressDialog dialog;
    String token;
    private boolean active = false;
    Fragment fragment = null;
    Timer timer;
    public static MainActivity mainActivity;
    public static TextView toolbar_title;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        sessionManager = new SessionManager(this);

        init();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            open_Permission_dailoge();
        }




//        timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                LocationUpdater.getCurrentLocation(MainActivity.this);
//            }
//        }, 1000, 10000);

        statusCheck();
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void open_Permission_dailoge() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Kissan And Factory collects location data to show your live location on map for tracking even when the app is closed or not in use.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        // check permisions
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION

                            }, 100);


                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        open_Permission_dailoge();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    protected void onDestroy() {
        //   timer.cancel();
        super.onDestroy();
    }


    private void init() {
        token = sessionManager.getDeviceToken();
        Log.i("MainActivity", "token: " + token);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        fragment = new Home_Fragment();

        //  deliveredTab = findViewById(R.id.deliveredTab);

        toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

            toolbar_title = toolbar.findViewById(R.id.tb_title);
            toolbar_title.setText("Dashboard");
        }

        getProfile();
        setUpNavView();


        setUpDialog();


    }

    // setting up dialog
    private void setUpDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait");
        dialog.setCancelable(false);
        dialog.create();
    }


    // setting up the drawer layout
    private void setUpNavView() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        startActivity(new Intent(MainActivity.this, AgentProfileActivity.class));
                        break;
                    case R.id.Home:
                        fragment = new Home_Fragment();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            int backCount = fragmentManager.getBackStackEntryCount();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
                            if (backCount == 1) {
                                fragmentManager.popBackStack();
                            }
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            fragmentTransaction.commit();
                        }
                        break;
                    case R.id.notification:
                        startActivity(new Intent(MainActivity.this, NotificationsActivity.class));
                        break;
                    case R.id.support:

                        startActivity(new Intent(MainActivity.this, Support_Activity.class));

                        break;
                    case R.id.order_history:
                        startActivity(new Intent(MainActivity.this, Order_History_Activity.class));
                        break;
                    case R.id.invite_others:

                        break;

                    case R.id.logout:
                        // alert logout
                        Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setContentView(R.layout.logout_dialog);
                        dialog.setCancelable(true);
                        dialog.findViewById(R.id.logoutBtn).setOnClickListener(view -> {
                            // logout
                            clearSharedPrefs();
                            dialog.dismiss();
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(new Intent(MainActivity.this, LoginActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            overridePendingTransition(0, 0);

                        });
                        dialog.findViewById(R.id.cancelBtn).setOnClickListener(view -> {
                            // logout
                            dialog.dismiss();
                        });
                        dialog.create();
                        dialog.show();
                        break;

                    case R.id.wallet:
                        startActivity(new Intent(MainActivity.this, WalletActivity.class));
                        break;
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            int backCount = fragmentManager.getBackStackEntryCount();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
            if (backCount == 1) {
                fragmentManager.popBackStack();
            }
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }

    }

    // clearing prefs
    private void clearSharedPrefs() {
        sessionManager.Logout();
    }

    // get delivery profile
    private void getProfile() {

        Call<ApiResponse> call = new DeliveryApiToJson().getProfile(token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    active = TextUtils.equals(response.body().getData().getAvailabilityStatus(), "1");
                } else {
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.db_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.db_notification) {
            startActivity(new Intent(this, NotificationsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        int backCount = fragmentManager.getBackStackEntryCount();
//        if (MapsFragment.mapsFragment.isVisible()) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        } else
//            if (backCount > 1) {
//            super.onBackPressed();
//            finishAffinity();
//        } else {
//            finish();
//        }
        super.onBackPressed();
        finishAffinity();
    }

}