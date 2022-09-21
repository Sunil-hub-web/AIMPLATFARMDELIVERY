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
import android.widget.Toast;

import com.aimplatfarm.aimplatfarmdelivery.Helpers.Validator;
import com.aimplatfarm.aimplatfarmdelivery.Models.ApiResponse;
import com.aimplatfarm.aimplatfarmdelivery.Models.Password;
import com.aimplatfarm.aimplatfarmdelivery.R;
import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText password1, password2;
    private TextView err;
    private String vOTP = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();
    }

    // initializing the view
    private void init() {
        // initializing the custom toolbar
        toolbar = findViewById(R.id.toolbar);

        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // setting up the toolbar title
            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Change Password");
            if (getIntent() != null) {
                vOTP = String.valueOf(getIntent().getIntExtra("vOtp", 1));
                System.out.println("otp...." + vOTP);
            }
        }

        err = findViewById(R.id.errorMsg);
        password1 = findViewById(R.id.userPassword);
        password2 = findViewById(R.id.passwordConf);
    }

    // change the password using the route
    public void changePassword(View view) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.setTitle("Updating Password");
        dialog.setCancelable(false);

        err.setVisibility(View.GONE);
        if (new Validator().validInputs(password1, password2)) {
            if (new Validator().passwordConfirmation(password1, password2)) {
                dialog.create();
                dialog.show();

                String pass1 = password1.getText().toString().trim();
                //  String pass2 = password2.getText().toString().trim();
                String token = getIntent().getStringExtra("token");

                Password newPassword = new Password();
                newPassword.setPassword(pass1);
                // newPassword.setPassword2(pass2);
                newPassword.setMobile(getIntent().getLongExtra("phone", 1));
                newPassword.setCountry_code(getIntent().getStringExtra("countryCode"));
                newPassword.setOtp(vOTP);

                Call<ApiResponse> call = new DeliveryApiToJson().changePassword(newPassword);
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            dialog.dismiss();
                            // show some confirmation here
                            Toast.makeText(ChangePasswordActivity.this, response.message().toString().trim(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                            ChangePasswordActivity.this.finish();
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
                showError("Passwords do not match");
            }
        } else {
            showError("Fill all fields");
        }

    }

    private void showError(String error) {
        err.setText(error);
        err.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}