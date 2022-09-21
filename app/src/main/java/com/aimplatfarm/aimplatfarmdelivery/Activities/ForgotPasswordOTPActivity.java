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
import android.widget.Toast;

import com.aimplatfarm.aimplatfarmdelivery.Helpers.Validator;
import com.aimplatfarm.aimplatfarmdelivery.Models.ApiResponse;
import com.aimplatfarm.aimplatfarmdelivery.Models.DriverDetails;
import com.aimplatfarm.aimplatfarmdelivery.R;
import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordOTPActivity extends AppCompatActivity {

    private TextView timerText, resendText, err;
    private Toolbar toolbar;
    //Declare timer
    CountDownTimer cTimer = null;
    private EditText otpView;
    int vOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_otpactivity);

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        otpView = findViewById(R.id.otp);

        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // setting up the toolbar title
            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Forget Password");
        }

        err = findViewById(R.id.errorMsg);
        timerText = findViewById(R.id.timerText);
        resendText = findViewById(R.id.resendText);
        startTimer();
    }

    public void changeNumber(View view) {
        super.onBackPressed();
    }

    public void changePassword(View view) {
        err.setVisibility(View.GONE);
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.setTitle("Verifying");
        dialog.setCancelable(false);

        if (new Validator().validInput(otpView)) {
            vOtp = Integer.parseInt(otpView.getText().toString().trim());

            DriverDetails agent = new DriverDetails();
            agent.setCountryCode(String.valueOf(getIntent().getIntExtra("countryCode", 1)));
            //agent.setMobile(getIntent().getLongExtra("mobile", 1));
            agent.setMobile(getIntent().getStringExtra("mobile"));
            agent.setOtp(vOtp);

            dialog.create();
            dialog.show();
            Call<ApiResponse> call = new DeliveryApiToJson().verifyForgotPassword(agent);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        startActivity(new Intent(ForgotPasswordOTPActivity.this, ChangePasswordActivity.class)
                                .putExtra("token", response.body().getToken())
                                .putExtra("vOtp", vOtp)
                                .putExtra("phone", ForgotPasswordOTPActivity.this.getIntent().getLongExtra("mobile", 1))
                                .putExtra("countryCode", agent.getCountryCode())
                        );
                        ForgotPasswordOTPActivity.this.finish();
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
            showError("Enter the OTP");
        }

    }

    private void showError(String error) {
        err.setText(error);
        err.setVisibility(View.VISIBLE);
    }

    // resending the otp
    private void resendOtp() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.setTitle("Verifying");
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();


        DriverDetails agent = new DriverDetails();
        agent.setCountryCode(String.valueOf(getIntent().getIntExtra("countryCode", 1)));
        agent.setMobile(getIntent().getStringExtra("mobile"));

        Call<ApiResponse> call = new DeliveryApiToJson().resendPasswordOtp(agent);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(ForgotPasswordOTPActivity.this, "New OTP sent", Toast.LENGTH_SHORT).show();
                    startTimer();
                    timerText.setOnClickListener(view -> {
                    });
                } else {
                    dialog.dismiss();
                    Gson gson = new Gson();
                    ApiResponse message = gson.fromJson(response.errorBody().charStream(), ApiResponse.class);
                    Toast.makeText(ForgotPasswordOTPActivity.this, message.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ForgotPasswordOTPActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //start timer function
    void startTimer() {
        cTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText("(" + (millisUntilFinished / 1000) + " secs)");
            }

            public void onFinish() {
                timerText.setText(" ");
                resendText.setOnClickListener(view -> {
                    // resend otp
                    resendOtp();
                });
            }
        };
        cTimer.start();
    }

    //cancel timer
    void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }

    // configuring the back arrow functionality
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}