package com.aimplatfarm.aimplatfarmdelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aimplatfarm.aimplatfarmdelivery.Models.EditProfile;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.Example_ex;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.RequestDto;
import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;
import com.aimplatfarm.aimplatfarmdelivery.Models.ApiResponse;
import com.aimplatfarm.aimplatfarmdelivery.Models.DriverDetails;
import com.aimplatfarm.aimplatfarmdelivery.R;
import com.aimplatfarm.aimplatfarmdelivery.session_manager.SessionManager;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    String token;
    private SessionManager sessionManager;

    EditText mobilenumber_ed, name_ed, email_ed, drivingLisence_ed, accountnumber_ed, ifsccode_ed,
            bankname_ed, address_ed, locality_ed, state_ed, city_ed, street_ed, zipcode_ed;

    String mobilenumber_str, name_str, email_str, drivingLisence_str, accountnumber_str, ifsccode_str,
            bankname_str, address_str, locality_str, state_str, city_str, street_str, zipcode_str, driver_id;

    ImageView edit_image, save_image;

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
                long mobilenumber_long = Long.valueOf(mobilenumber_str);
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
                int zipcode_int = Integer.parseInt(zipcode_str);


                editProfile1(email_str, name_str, mobilenumber_long, zipcode_int, driver_id, drivingLisence_str, "+91",
                        "", "", "", locality_str, city_str, state_str, accountnumber_str, bankname_str,
                        ifsccode_str);

            }
        });

    }

    private void init() {
//        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        token = sessionManager.getDeviceToken();
        driver_id = sessionManager.getUSERID();
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
        email_ed.setText(user.getEmailID());
        drivingLisence_ed.setText(user.getDrivingLisence());

        state_ed = findViewById(R.id.state_ed);
        street_ed = findViewById(R.id.street_ed);
        city_ed = findViewById(R.id.city_ed);
        zipcode_ed = findViewById(R.id.zipcode_ed);
        address_ed = findViewById(R.id.address_ed);
        locality_ed = findViewById(R.id.locality_ed);


        state_ed.setText(user.getLocation().getState());
        street_ed.setText(user.getLocation().getLocality());
        zipcode_ed.setText(user.getLocation().getZip() + "");
        city_ed.setText(user.getLocation().getCity());
        locality_ed.setText(user.getLocation().getLocality());
        address_ed.setText(user.getLocation().getLocality());

        bankname_ed = findViewById(R.id.bankname_ed);
        accountnumber_ed = findViewById(R.id.accountnumber_ed);
        ifsccode_ed = findViewById(R.id.ifsccode_ed);

        bankname_ed.setText(user.getAccountDetails().getBankName());
        accountnumber_ed.setText(user.getAccountDetails().getAccountNumber());
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

    public void editProfile(String emailID, String name, long mobile, int zip, String driver_id,
                            String drivingLisence, String countryCode, String profilePhoto, String DlPhoto,
                            String AddressProof, String locality, String state, String city, String accountNumber,
                            String bankName, String ifscCode) {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait..");
        dialog.create();
        dialog.show();

        JSONObject editProfile = new JSONObject();

        //Map<String,Object> editProfile = new HashMap<>();

        try {

            editProfile.put("emailID", emailID);
            editProfile.put("name", name);
            editProfile.put("mobile", mobile);
            editProfile.put("driver_id", driver_id);
            editProfile.put("drivingLisence", drivingLisence);
            editProfile.put("countryCode", countryCode);
            editProfile.put("profilePhoto", "");
            editProfile.put("DlPhoto", "");
            editProfile.put("AddressProof", "");

            // Map<String,String> jsonObject_location = new HashMap<>();

            JSONObject jsonObject_location = new JSONObject();

            jsonObject_location.put("locality", locality);
            jsonObject_location.put("city", city);
            jsonObject_location.put("state", state);
            jsonObject_location.put("zip", zip);

            editProfile.put("location", jsonObject_location);

            // Map<String,String> jsonObject_account = new HashMap<>();

            JSONObject jsonObject_account = new JSONObject();

            jsonObject_account.put("accountNumber", accountNumber);
            jsonObject_account.put("bankName", bankName);
            jsonObject_account.put("ifscCode", ifscCode);

            editProfile.put("accountDetails", jsonObject_account);


        } catch (JSONException e) {

            e.printStackTrace();
        }

        Log.d("hxdgsubs", editProfile.toString());
        Log.d("hxdgsubsss", token);

        Call<Example_ex> call_editProfile = new DeliveryApiToJson().editProfile(token, editProfile);

        call_editProfile.enqueue(new Callback<Example_ex>() {
            @Override
            public void onResponse(Call<Example_ex> call, Response<Example_ex> response) {

                dialog.dismiss();

                //   Log.d("gsgjha",response.body().toString());

                if (response.body() != null) {

                    if (response.isSuccessful()) {

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

                        name_ed.setText(response.body().getDeliveryProfile().getName());
                        mobilenumber_ed.setText(response.body().getDeliveryProfile().getMobile() + "");
                        email_ed.setText(response.body().getDeliveryProfile().getEmailID());

                        bankname_ed.setText(response.body().getDeliveryProfile().getAccountDetails().getBankName());
                        accountnumber_ed.setText(response.body().getDeliveryProfile().getAccountDetails().getAccountNumber() + "");
                        ifsccode_ed.setText(response.body().getDeliveryProfile().getAccountDetails().getIfscCode());

                        state_ed.setText(response.body().getDeliveryProfile().getLocation().getState());
                        city_ed.setText(response.body().getDeliveryProfile().getLocation().getCity());
                        zipcode_ed.setText(response.body().getDeliveryProfile().getLocation().getZip());
                        address_ed.setText(response.body().getDeliveryProfile().getLocation().getLocality());
                        locality_ed.setText(response.body().getDeliveryProfile().getLocation().getLocality());

                    }
                } else {

                    Toast.makeText(AgentProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Example_ex> call, Throwable t) {

                dialog.dismiss();
                Toast.makeText(AgentProfileActivity.this, "" + t, Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void editProfile1(String emailID, String name, long mobile, int zip, String driver_id,
                             String drivingLisence, String countryCode, String profilePhoto, String DlPhoto,
                             String AddressProof, String locality, String state, String city, String accountNumber,
                             String bankName, String ifscCode) {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait..");
        dialog.create();
        dialog.show();

        JSONObject editProfile = new JSONObject();

        //Map<String,Object> editProfile = new HashMap<>();

        try {

            editProfile.put("emailID", emailID);
            editProfile.put("name", name);
            editProfile.put("mobile", mobile);
            editProfile.put("driver_id", driver_id);
            editProfile.put("drivingLisence", drivingLisence);
            editProfile.put("countryCode", countryCode);
            editProfile.put("profilePhoto", "");
            editProfile.put("DlPhoto", "");
            editProfile.put("AddressProof", "");

            // Map<String,String> jsonObject_location = new HashMap<>();

            JSONObject jsonObject_location = new JSONObject();

            jsonObject_location.put("locality", locality);
            jsonObject_location.put("city", city);
            jsonObject_location.put("state", state);
            jsonObject_location.put("zip", zip);

            editProfile.put("location", jsonObject_location);

            // Map<String,String> jsonObject_account = new HashMap<>();

            JSONObject jsonObject_account = new JSONObject();

            jsonObject_account.put("accountNumber", accountNumber);
            jsonObject_account.put("bankName", bankName);
            jsonObject_account.put("ifscCode", ifscCode);

            editProfile.put("accountDetails", jsonObject_account);


        } catch (JSONException e) {

            e.printStackTrace();
        }

        Log.d("hxdgsubs", editProfile.toString());
        Log.d("hxdgsubsss", token);

        String Url = "https://kisaanandfactory.com/api/v1/deliveryapp/auth/edit";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, Url, editProfile, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                dialog.dismiss();

                try {
                    String code = response.getString("code");
                    String msg = response.getString("msg");
                    String deliveryProfile = response.getString("deliveryProfile");
                    JSONObject jsonObject_profile = new JSONObject(deliveryProfile);

                    String location = jsonObject_profile.getString("location");
                    JSONObject jsonObject_location = new JSONObject(location);

                    String locality = jsonObject_location.getString("locality");
                    String city = jsonObject_location.getString("city");
                    String state = jsonObject_location.getString("state");
                    String zip = jsonObject_location.getString("zip");

                    String accountDetails = jsonObject_profile.getString("accountDetails");
                    JSONObject jsonObject_account = new JSONObject(accountDetails);

                    String accountNumber = jsonObject_account.getString("accountNumber");
                    String bankName = jsonObject_account.getString("bankName");
                    String ifscCode = jsonObject_account.getString("ifscCode");


                    String drivingLisence = jsonObject_profile.getString("drivingLisence");
                    String _id = jsonObject_profile.getString("_id");
                    String name = jsonObject_profile.getString("name");
                    String password = jsonObject_profile.getString("password");
                    String countryCode = jsonObject_profile.getString("countryCode");
                    String driver_id = jsonObject_profile.getString("driver_id");
                    String mobile = jsonObject_profile.getString("mobile");
                    String emailID = jsonObject_profile.getString("emailID");

                    if (code.equals("200")) {

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

                        Toast.makeText(AgentProfileActivity.this, msg, Toast.LENGTH_SHORT).show();

                        name_ed.setText(name);
                        mobilenumber_ed.setText(mobile+ "");
                        email_ed.setText(emailID);

                        bankname_ed.setText(bankName);
                        accountnumber_ed.setText(accountNumber);
                        ifsccode_ed.setText(ifscCode);

                        state_ed.setText(state);
                        city_ed.setText(city);
                        zipcode_ed.setText(zip+"");
                        address_ed.setText(locality);
                        locality_ed.setText(locality);

                    } else {

                        Toast.makeText(AgentProfileActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(AgentProfileActivity.this, "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(AgentProfileActivity.this, data, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                        }

                    }

                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                //String auth = sessionManager.getToken();
                headers.put("auth-token", token);
                Log.d("fvsDevbf", "" + token);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("fvsDevbf", "" + params);
                return params;
            }

        };

        jsonObjectRequest.setRetryPolicy(new
                DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(AgentProfileActivity.this);
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjectRequest);

    }
}