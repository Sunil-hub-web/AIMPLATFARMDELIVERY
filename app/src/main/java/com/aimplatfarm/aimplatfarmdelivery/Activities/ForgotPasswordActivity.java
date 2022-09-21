package com.aimplatfarm.aimplatfarmdelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aimplatfarm.aimplatfarmdelivery.Helpers.Validator;
import com.aimplatfarm.aimplatfarmdelivery.Models.ApiResponse;
import com.aimplatfarm.aimplatfarmdelivery.Models.DriverDetails;
import com.aimplatfarm.aimplatfarmdelivery.R;
import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText mobile;
    private TextView err;
    private CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        init();
    }

    // initializing the toolbar
    private void init() {
        toolbar = findViewById(R.id.toolbar);

        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // setting up the toolbar title
            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Forget Password");
        }

        err = findViewById(R.id.errorMsg);
        mobile = findViewById(R.id.userPhone);
        countryCodePicker = findViewById(R.id.countryCodePicker);

    }

    // verify otp
    public void requestOtp(View view) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.setTitle("Verifying");
        dialog.setCancelable(false);

        err.setVisibility(View.GONE);
        if (new Validator().validInput(mobile)) {
            dialog.create();
            dialog.show();
            int cc = Integer.parseInt(countryCodePicker.getSelectedCountryCode());
            DriverDetails agent = new DriverDetails();
            //agent.setMobile(Long.parseLong(mobile.getText().toString().trim()));
            agent.setMobile(mobile.getText().toString().trim());
            agent.setCountryCode(String.valueOf(cc));

            Call<ApiResponse> call = new DeliveryApiToJson().forgotPasswordOtp(agent);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        startActivity(new Intent(ForgotPasswordActivity.this, ForgotPasswordOTPActivity.class)
                                .putExtra("countryCode", cc)
                                .putExtra("mobile", Long.parseLong(mobile.getText().toString().trim())));
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
            showError("Enter valid number");
        }

    }

    private void showError(String error) {
        err.setText(error);
        err.setVisibility(View.VISIBLE);
    }


    public void signUp(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
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