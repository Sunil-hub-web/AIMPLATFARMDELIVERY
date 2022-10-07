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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aimplatfarm.aimplatfarmdelivery.Models.EditProfile;
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

    EditText mobilenumber_ed,name_ed,email_ed,drivingLisence_ed,accountnumber_ed,ifsccode_ed,
            bankname_ed,address_ed,locality_ed,state_ed,city_ed,street_ed,zipcode_ed;

    String mobilenumber_str,name_str,email_str,drivingLisence_str,accountnumber_str,ifsccode_str,
            bankname_str,address_str,locality_str,state_str,city_str,street_str,zipcode_str;

    ImageView edit_image,save_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_profile);
        sessionManager = new SessionManager(this);
        init();

        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name_ed.setEnabled(true);
                mobilenumber_ed.setEnabled(true);
                email_ed.setEnabled(true);
                bankname_ed.setEnabled(true);
                accountnumber_ed.setEnabled(true);
                ifsccode_ed.setEnabled(true);
                state_ed.setEnabled(true);
                city_ed.setEnabled(true);
                street_ed.setEnabled(true);
                zipcode_ed.setEnabled(true);
                address_ed.setEnabled(true);
                locality_ed.setEnabled(true);

                name_ed.requestFocus();

                edit_image.setVisibility(View.GONE);
                save_image.setVisibility(View.VISIBLE);
            }
        });

        save_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mobilenumber_str = mobilenumber_ed.getText().toString().trim();
                name_str = name_ed.getText().toString().trim();
                email_str = email_ed.getText().toString().trim();
                drivingLisence_str = drivingLisence_ed.getText().toString().trim();
                accountnumber_str = accountnumber_ed.getText().toString().trim();
                ifsccode_str = ifsccode_ed.getText().toString().trim();
                bankname_str = bankname_ed.getText().toString().trim();
                address_str = address_ed.getText().toString().trim();
                locality_str = locality_ed.getText().toString().trim();
                state_str = state_ed.getText().toString().trim();
                city_str = city_ed.getText().toString().trim();
                street_str = street_ed.getText().toString().trim();
                zipcode_str = zipcode_ed.getText().toString().trim();


                editProfile(email_str,name_str,mobilenumber_str,zipcode_str,"",drivingLisence_str,"+91",
                        "","","",locality_str,state_str,accountnumber_str,bankname_str,
                        ifsccode_str);

            }
        });

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

            edit_image = findViewById(R.id.edit_image);
            save_image = findViewById(R.id.save_image);

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


        name_ed = findViewById(R.id.name_ed);
        mobilenumber_ed = findViewById(R.id.mobilenumber_ed);
        email_ed = findViewById(R.id.email_ed);
        drivingLisence_ed = findViewById(R.id.drivingLisence_ed);

        name_ed.setText(user.getName());
        name_ed.setText(user.getName());
        mobilenumber_ed.setText(user.getMobile() + "");
        email_ed.setText(user.getDrivingLisence());

        state_ed = findViewById(R.id.state_ed);
        street_ed = findViewById(R.id.street_ed);
        city_ed = findViewById(R.id.city_ed);
        zipcode_ed = findViewById(R.id.zipcode_ed);

        state_ed.setText(user.getLocation().getState());
        street_ed.setText(user.getLocation().getLocality());
        zipcode_ed.setText(user.getLocation().getZip() + "");
        city_ed.setText(user.getLocation().getCity());

        bankname_ed = findViewById(R.id.bankname_ed);
        accountnumber_ed = findViewById(R.id.accountnumber_ed);
        ifsccode_ed = findViewById(R.id.ifsccode_ed);

        bankname_ed.setText(user.getAccountDetails().getBankName());
        accountnumber_ed.setText(user.getAccountDetails().getAccountNumber()+ "");
        ifsccode_ed.setText(user.getAccountDetails().getIfscCode());

        name_ed.setEnabled(false);
        mobilenumber_ed.setEnabled(false);
        email_ed.setEnabled(false);
        bankname_ed.setEnabled(false);
        accountnumber_ed.setEnabled(false);
        ifsccode_ed.setEnabled(false);
        state_ed.setEnabled(false);
        city_ed.setEnabled(false);
        street_ed.setEnabled(false);
        zipcode_ed.setEnabled(false);
        address_ed.setEnabled(false);
        locality_ed.setEnabled(false);
    }

   /* // logout the user
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

    }*/

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

    public void editProfile(String emailID,String name,String mobile,String zip,String driver_id,
                            String drivingLisence,String countryCode,String profilePhoto,String DlPhoto,
                            String AddressProof,String locality,String state,String accountNumber,String bankName,
                            String ifscCode){

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait..");
        dialog.create();
        dialog.show();

        EditProfile editProfile = new EditProfile();
        editProfile.setEmailID(emailID);
        editProfile.setName(name);
        editProfile.setMobile(mobile);
        editProfile.setZip(zip);
        editProfile.setDriver_id(driver_id);
        editProfile.setDrivingLisence(drivingLisence);
        editProfile.setCountryCode(countryCode);
        editProfile.setLocality(locality);
        editProfile.setState(state);
        editProfile.setAccountNumber(accountNumber);
        editProfile.setBankName(bankName);
        editProfile.setIfscCode(ifscCode);

        Call<ApiResponse> call_editProfile = new DeliveryApiToJson().editProfile(token,editProfile);
        call_editProfile.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                dialog.dismiss();

                if(response.isSuccessful()){

                    name_ed.setEnabled(false);
                    mobilenumber_ed.setEnabled(false);
                    email_ed.setEnabled(false);
                    bankname_ed.setEnabled(false);
                    accountnumber_ed.setEnabled(false);
                    ifsccode_ed.setEnabled(false);
                    state_ed.setEnabled(false);
                    city_ed.setEnabled(false);
                    street_ed.setEnabled(false);
                    zipcode_ed.setEnabled(false);
                    address_ed.setEnabled(false);
                    locality_ed.setEnabled(false);

                    name_ed.requestFocus();

                    edit_image.setVisibility(View.VISIBLE);
                    save_image.setVisibility(View.GONE);

                }else{


                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

                dialog.dismiss();
                Toast.makeText(AgentProfileActivity.this, ""+t, Toast.LENGTH_SHORT).show();

            }
        });

    }
}