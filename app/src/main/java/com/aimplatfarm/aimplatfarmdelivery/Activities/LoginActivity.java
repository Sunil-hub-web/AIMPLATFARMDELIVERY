package com.aimplatfarm.aimplatfarmdelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aimplatfarm.aimplatfarmdelivery.Helpers.Validator;
import com.aimplatfarm.aimplatfarmdelivery.Models.DriverDetails;
import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;
import com.aimplatfarm.aimplatfarmdelivery.session_manager.SessionManager;
import com.aimplatfarm.aimplatfarmdelivery.Models.ApiResponse;
import com.aimplatfarm.aimplatfarmdelivery.R;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private TextView errorMsg;
    private SessionManager sessionManager;
    String device_token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(this);


        try {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (task.isComplete()) {
                        final String[] token = {""};
                        token[0] = task.getResult();
                        device_token = token[0];
                        sessionManager.setFCM_TOKEN(device_token);
                        Log.e("AppConstants", "onComplete: new Token got: " + token[0]);

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();

    }

    private void init() {
        email = findViewById(R.id.driveLicence);
        password = findViewById(R.id.userPassword);

        errorMsg = findViewById(R.id.loginError);
    }

    public void forgotPassword(View view) {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }

    // login user
    public void login(View view) {
//        startActivity(new Intent(LoginActivity.this, MainActivity.class)
//                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
//                        LoginActivity.this.finish();

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Logging in");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

        // check inputs
        Validator validator = new Validator();
        if (!email.getText().toString().equals("") && validator.validEmail(email)) {
            if (!password.getText().toString().equals("") && password.getText().toString().trim().length() >= 6) {
                dialog.create();
                dialog.show();

                DriverDetails agent = new DriverDetails();
                agent.setEmailID(email.getText().toString().trim());
                agent.setPassword(password.getText().toString().trim());
                agent.setFcm_id(device_token);

                // connect to the route here
                Call<ApiResponse> call = new DeliveryApiToJson().loginUser(agent);
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            dialog.dismiss();
                            // save token to shared prefs
                            ApiResponse apiResponse = response.body();
                            // open main activity

                            DriverDetails userData = apiResponse.getData();
                            sessionManager.setUserDetail(userData);
                            sessionManager.setDeviceToken(apiResponse.getToken());
                            sessionManager.setName(apiResponse.getData().getName());
                            sessionManager.login();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            LoginActivity.this.finish();

                          /*  if (response.body().getData().getMobileVerified() == true) {


                            } else {
                              *//*  startActivity(new Intent(LoginActivity.this, RegisterVerifyOTP.class)
                                        .putExtra("cc", response.body().getData().getCountryCode())
                                        .putExtra("mobile", response.body().getData().getMobile())
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));*//*
                                //  LoginActivity.this.finish();

                                Toast.makeText(LoginActivity.this, "Login Not SuccessFully", Toast.LENGTH_SHORT).show();
                            }*/


                        } else {
                            dialog.dismiss();
                            Gson gson = new Gson();
                            ApiResponse message = gson.fromJson(response.errorBody().charStream(), ApiResponse.class);
                              showError(message.getMsg());
                              // showError("Account not Exists !");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        dialog.dismiss();
                        showError(t.getMessage());
                    }
                });

            } else {
                // invalid
                showError("Enter valid password");
            }
        } else {
            // invalid

            showError("Enter valid email address");
        }
    }


    private void showError(String Message) {
        errorMsg.setVisibility(View.VISIBLE);
        errorMsg.setText(Message);
    }

    // open otp activity
    public void register(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}