package com.aimplatfarm.aimplatfarmdelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.aimplatfarm.aimplatfarmdelivery.Utility;
import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;
import com.aimplatfarm.aimplatfarmdelivery.Helpers.Validator;
import com.aimplatfarm.aimplatfarmdelivery.Models.ApiResponse;

import com.aimplatfarm.aimplatfarmdelivery.Models.DriverDetails;
import com.aimplatfarm.aimplatfarmdelivery.R;
import com.aimplatfarm.aimplatfarmdelivery.place_picker.LocationPickerActivity;
import com.aimplatfarm.aimplatfarmdelivery.place_picker.MapUtility;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText name, mobile, password, email, zip;
    private TextView errorMsg;
    private CountryCodePicker ccp;
    private final static int ADDRESS_PICKER_REQUEST = 999;
    double latitude = 0.0, longitude = 0.0;
    private CheckBox termsCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        checkAndRequestPermissions();
        init();
    }

    private boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarsePermision = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (coarsePermision != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), Utility.REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }

        //getSettingsLocation();
        return true;

    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Register Account");
        }

        // initializing edit texts
        name = findViewById(R.id.userName);
        mobile = findViewById(R.id.userPhone);
        password = findViewById(R.id.userPassword);
        email = findViewById(R.id.userEmail);
       /* driversLicense = findViewById(R.id.driveLicence);
        bankName = findViewById(R.id.bankName);
        accountNum = findViewById(R.id.bankNum);
        ifsc = findViewById(R.id.ifscNum);
        adharcard = findViewById(R.id.adharcard);
        city = findViewById(R.id.cityName);
        state = findViewById(R.id.stateName);
        locality = findViewById(R.id.locality);*/
        zip = findViewById(R.id.zipcode);

        ccp = findViewById(R.id.countryCodePicker);
        termsCheckBox = findViewById(R.id.termsCheckBox);

        // initializing error
        errorMsg = findViewById(R.id.errorMsg);

//        email.setText("deep@gmail.com");
//        name.setText("deep");
//        mobile.setText("9691878748");
//        driversLicense.setText("1234567890");
//        bankName.setText("bank");
//        accountNum.setText("12345678901");
//        ifsc.setText("SBIN000444");
//        adharcard.setText("1234567890");
//        password.setText("12345678");


       /* state.setOnClickListener(this);
        city.setOnClickListener(this);
        locality.setOnClickListener(this);*/
       // zip.setOnClickListener(this);

    }

   /* @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cityName:
                showPlacePicker();
                break;
            case R.id.locality:
                showPlacePicker();
                break;
            case R.id.stateName:
                showPlacePicker();
                break;
            case R.id.zipcode:
                showPlacePicker();
                break;

        }
    }*/

    private void showPlacePicker() {
        Intent i = new Intent(RegistrationActivity.this, LocationPickerActivity.class);
        startActivityForResult(i, ADDRESS_PICKER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    // String address = data.getStringExtra(MapUtility.ADDRESS);double currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                    longitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
                    latitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
                    Bundle completeAddress = data.getBundleExtra("fullAddress");

//                    Toast.makeText(this, new StringBuilder().append("addressline2: ").append
//                            (completeAddress.getString("addressline2")).append("\ncity: ").append
//                            (completeAddress.getString("city")).append("\npostalcode: ").append
//                            (completeAddress.getString("postalcode")).append("\nstate: ").append
//                            (completeAddress.getString("state")).toString(), Toast.LENGTH_SHORT).show();

                   /* state.setText((completeAddress.getString("state")));
                    city.setText((completeAddress.getString("city")));
                    locality.setText((completeAddress.getString("addressline2")));*/
                    // zip.setText((completeAddress.getString("postalcode")));
                    //   vAddress = completeAddress.getString("addressline2");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // open otp activity
    public void registerUser(View view) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Logging in");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

        errorMsg.setVisibility(View.GONE);
        // validate inputs
        Validator validator = new Validator();
        if (!name.getText().toString().trim().equals("") && name.getText().toString().trim().length() >= 3) {
            if (!email.getText().toString().trim().equals("") && validator.validEmail(email)) {
                if (!mobile.getText().toString().trim().equals("") && mobile.getText().toString().trim().length() == 10) {

                    if (!zip.getText().toString().trim().equals("")) {
                        if (!password.getText().toString().trim().equals("")) {
                            if (termsCheckBox.isChecked()) {
                                dialog.create();
                                dialog.show();

                                // convert it to agent
                                DriverDetails agent = getAgent(validator);

                                agent.setDrivingLisence(name.getText().toString().trim());
                                agent.setMobile(mobile.getText().toString().trim());
                                agent.setCountryCode(ccp.getSelectedCountryCode());
                                agent.setPassword(password.getText().toString().trim());
                                agent.setDrivingLisence(zip.getText().toString().trim());
                                agent.setDrivingLisence(email.getText().toString().trim());


                                                              /*  agent.setDrivingLisence(driversLicense.getText().toString().trim());
                                                                agent.setDrivingLisence(bankName.getText().toString().trim());
                                                                agent.setDrivingLisence(accountNum.getText().toString().trim());
                                                                agent.setDrivingLisence(ifsc.getText().toString().trim());
                                                                agent.setDrivingLisence(adharcard.getText().toString().trim());
                                                                agent.setDrivingLisence(state.getText().toString().trim());
                                                                agent.setDrivingLisence(city.getText().toString().trim());
                                                                agent.setDrivingLisence(locality.getText().toString().trim());*/




                                Log.d("sunilvalue", agent.toString());
                                Call<ApiResponse> call = new DeliveryApiToJson().registerUser(agent);


                                call.enqueue(new Callback<ApiResponse>() {
                                    @Override
                                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                        if (response.isSuccessful()) {
                                            dialog.dismiss();
                                            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                        } else {
                                            dialog.dismiss();
                                            Gson gson = new Gson();
                                            ApiResponse message = gson.fromJson(response.errorBody().charStream(), ApiResponse.class);
                                            showError(message.getMsg());

                                            Log.d("sunilerror", message.getMsg());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                                        dialog.dismiss();
                                        showError(""+t.getMessage());

                                        Log.d("sunilerror1", t.getMessage());
                                    }
                                });
                            } else {
                                showError("Please allow terms & conditions !");
                            }
                        } else {
                            showError("Enter valid password & should be 6 characters !");
                        }
                    } else {
                        // invalid
                        showError("Enter valid zip code !");
                    }
                } else {
                    // invalid
                    showError("Enter valid phone number !");
                }
            } else {
                // invalid
                showError("Enter valid email address !");
            }
        } else {
            // invalid
            showError("Enter valid name or minimum 3 characters !");
        }
    }

    private DriverDetails getAgent(Validator validator) {

        DriverDetails agent = new DriverDetails();
        agent.setName(validator.getString(name));
       /* agent.setLocality(validator.getString(locality));
        agent.setState(validator.getString(state));
        agent.setCity(validator.getString(city));
        agent.setPanNum(validator.getString(adharcard));
        agent.setDrivingLisence(validator.getString(driversLicense));
        agent.setBankName(validator.getString(bankName));*/
        agent.setMobile(validator.getString(mobile));
        agent.setCountryCode(ccp.getSelectedCountryCode());
        agent.setPassword(validator.getString(password));

        // agent.setAccountNum(validator.getLong(accountNum));
        agent.setZip(validator.getInt(zip));

        //agent.setIfsc(validator.getString(ifsc));

        agent.setEmailID(validator.getString(email));

        return agent;

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