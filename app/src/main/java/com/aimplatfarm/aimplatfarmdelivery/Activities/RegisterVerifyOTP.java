package com.aimplatfarm.aimplatfarmdelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;
import com.aimplatfarm.aimplatfarmdelivery.Helpers.Validator;
import com.aimplatfarm.aimplatfarmdelivery.Models.ApiResponse;
import com.aimplatfarm.aimplatfarmdelivery.Models.DriverDetails;
import com.aimplatfarm.aimplatfarmdelivery.R;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterVerifyOTP extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText otp;
    private TextView errorMsg, tv_resend_otp, tv_resend_otp_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_verify_o_t_p);

        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Verify OTP");
        }

        otp = findViewById(R.id.otp);
        errorMsg = findViewById(R.id.errorMsg);
        tv_resend_otp = findViewById(R.id.tv_resend_otp);
        tv_resend_otp_count = findViewById(R.id.tv_resend_otp_count);

        tv_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_resend_otp_count.setVisibility(View.VISIBLE);
                tv_resend_otp.setVisibility(View.GONE);
                Resend_Otp(v);

                new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        tv_resend_otp_count.setText("seconds remaining: " + millisUntilFinished / 1000);
                        //here you can have your logic to set text to edittext
                        tv_resend_otp.setVisibility(View.GONE);

                    }

                    public void onFinish() {
                        tv_resend_otp.setText("Resend OTP");
                        tv_resend_otp_count.setVisibility(View.GONE);
                        tv_resend_otp.setVisibility(View.VISIBLE);
                    }

                }.start();
            }
        });
    }

    // sign in to existing account
    public void signIn(View view) {
        Intent intent = new Intent(new Intent(this, LoginActivity.class));

        startActivity(intent);
        finish();
    }

    // open otp activity
    public void verifyOtp(View view) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Logging in");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

        errorMsg.setVisibility(View.GONE);
        // validate input
        Validator validator = new Validator();
        if (validator.validInput(otp)) {
            dialog.create();
            dialog.show();

            DriverDetails agent = new DriverDetails();
            agent.setOtp(validator.getInt(otp));
            //agent.setMobile(getIntent().getLongExtra("mobile", 1));
            agent.setMobile(getIntent().getStringExtra("mobile"));
            agent.setCountryCode(getIntent().getStringExtra("cc"));

            Call<ApiResponse> call = new DeliveryApiToJson().verifyRgisterOtp(agent);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        dialog.dismiss();

                     /*   // open main activity
                        startActivity(new Intent(RegisterVerifyOTP.this, LoginActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        RegisterVerifyOTP.this.finish();*/

                    } else {
                        dialog.dismiss();
                        Gson gson = new Gson();
                        ApiResponse message = gson.fromJson(response.errorBody().charStream(), ApiResponse.class);
                        showError(message.getMsg());
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    dialog.dismiss();
                    showError(t.getMessage());
                }
            });

        } else {
            showError("Fill the field");
        }
    }


    public void Resend_Otp(View view) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Logging in");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

        errorMsg.setVisibility(View.GONE);
        DriverDetails agent = new DriverDetails();
       // agent.setMobile(getIntent().getLongExtra("mobile", 1));
        agent.setMobile(getIntent().getStringExtra("mobile"));
        agent.setCountryCode(getIntent().getStringExtra("cc"));

        Call<ApiResponse> call = new DeliveryApiToJson().resendOtp(agent);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    Gson gson = new Gson();
                    ApiResponse message = gson.fromJson(response.errorBody().charStream(), ApiResponse.class);
                    showError(message.getMsg());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                dialog.dismiss();
                showError(t.getMessage());
            }
        });


    }


    private void showError(String message) {
        errorMsg.setText(message);
        errorMsg.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}