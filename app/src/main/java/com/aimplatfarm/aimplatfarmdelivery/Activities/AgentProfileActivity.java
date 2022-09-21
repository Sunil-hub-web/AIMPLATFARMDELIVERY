package com.aimplatfarm.aimplatfarmdelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;
import com.aimplatfarm.aimplatfarmdelivery.Models.ApiResponse;
import com.aimplatfarm.aimplatfarmdelivery.Models.DriverDetails;
import com.aimplatfarm.aimplatfarmdelivery.R;
import com.aimplatfarm.aimplatfarmdelivery.session_manager.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    String token;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_profile);
        sessionManager = new SessionManager(this);
        init();
    }

    private void init() {
//        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        token = sessionManager.getDeviceToken();
        toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Agent Profile");
        }

        getProfile();
    }

    // get agent profile
    private void getProfile() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait..");
        dialog.create();
        dialog.show();

        Call<ApiResponse> call = new DeliveryApiToJson().getProfile(token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    fillProfileInfo(response.body().getData());
                } else {
                    Toast.makeText(AgentProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(AgentProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


    private void fillProfileInfo(DriverDetails user) {
        // state, street, zip, city
        // bank, acount, ifsc
        // name, phone, email


        TextView stateTv, streetTv, zipTv, cityTv, bankTv, accountNumTv, ifscTv, nameTv, name2Tv, phoneTv, emailTv;

        nameTv = findViewById(R.id.nameTv);
        name2Tv = findViewById(R.id.name2Tv);
        phoneTv = findViewById(R.id.phoneTv);
        emailTv = findViewById(R.id.emailTv);

        nameTv.setText(user.getName());
        name2Tv.setText("Name : " + user.getName());
        phoneTv.setText("Mobile : +91-" + user.getMobile() + "");
        emailTv.setText("License : " + user.getDrivingLisence());

        stateTv = findViewById(R.id.stateTv);
        streetTv = findViewById(R.id.streetTv);
        cityTv = findViewById(R.id.cityTv);
        zipTv = findViewById(R.id.zipTv);

        stateTv.setText(user.getLocation().getState());
        streetTv.setText(user.getLocation().getLocality());
        zipTv.setText(user.getLocation().getZip() + "");
        cityTv.setText(user.getLocation().getCity());

        bankTv = findViewById(R.id.bankTv);
        accountNumTv = findViewById(R.id.accountNumTv);
        ifscTv = findViewById(R.id.ifscTv);

        bankTv.setText("Bank Name : " + user.getAccountDetails().getBankName());
        accountNumTv.setText("Account Num : " + user.getAccountDetails().getAccountNumber());
        ifscTv.setText("IFSC : " + user.getAccountDetails().getIfscCode());
    }

    // logout the user
    public void logout(View view1) {
        Dialog dialog = new Dialog(AgentProfileActivity.this);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.setCancelable(true);
        dialog.findViewById(R.id.logoutBtn).setOnClickListener(view -> {
            // logout
            clearSharedPrefs();
            dialog.dismiss();
            overridePendingTransition(0, 0);
            startActivity(new Intent(AgentProfileActivity.this, LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
            overridePendingTransition(0, 0);

        });
        dialog.findViewById(R.id.cancelBtn).setOnClickListener(view -> {
            // logout
            dialog.dismiss();
        });
        dialog.create();
        dialog.show();
    }

    // clearing prefs
    private void clearSharedPrefs() {
        sessionManager.Logout();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}