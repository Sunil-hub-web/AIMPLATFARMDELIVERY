package com.aimplatfarm.aimplatfarmdelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aimplatfarm.aimplatfarmdelivery.Utility;
import com.aimplatfarm.aimplatfarmdelivery.Models.RejectDto.RejectDto;
import com.aimplatfarm.aimplatfarmdelivery.R;
import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;
import com.aimplatfarm.aimplatfarmdelivery.databinding.ActivityRejectRequestBinding;
import com.aimplatfarm.aimplatfarmdelivery.session_manager.SessionManager;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Reject_Request_Activity extends AppCompatActivity implements View.OnClickListener {
    ActivityRejectRequestBinding binding;
    private SessionManager sessionManager;
    String vOrder_id = "", vDevice_Token = "", vSpinner_value = "", vUser_Name = "", vReasonTxt = "",
            vOrder_Satus = "", vDelivery_Address = "", vUser_Contact = "", vPickupAddress = "",
            vReceiver_name = "";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reject_request);
        sessionManager = new SessionManager(this);
        vDevice_Token = sessionManager.getDeviceToken() != null ? sessionManager.getDeviceToken() : "";


        toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Reject Order");
        }


        if (getIntent() != null) {
            try {
                vOrder_id = getIntent().getStringExtra("order_id") != null ? getIntent().getStringExtra("order_id") : "";
                vDelivery_Address = getIntent().getStringExtra("delivery_address") != null ? getIntent().getStringExtra("delivery_address") : "";
                vUser_Name = getIntent().getStringExtra("user_name") != null ? getIntent().getStringExtra("user_name") : "";
                vUser_Contact = getIntent().getStringExtra("user_contact") != null ? getIntent().getStringExtra("user_contact") : "";
                vPickupAddress = getIntent().getStringExtra("pickup_address") != null ? getIntent().getStringExtra("pickup_address") : "";


                binding.tvNameReject.setText(vUser_Name);
                binding.tvContactNumberReject.setText(vUser_Contact);
                binding.tvOrderIdReject.setText(vOrder_id);
                binding.tvPickupReject.setText(vPickupAddress);
                binding.tvDeliveryReject.setText(vDelivery_Address);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        ArrayAdapter<CharSequence> adp3 = ArrayAdapter.createFromResource(this,
                R.array.reject_reason, android.R.layout.simple_list_item_1);

        adp3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerReason.setAdapter(adp3);
        binding.spinnerReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                vSpinner_value = binding.spinnerReason.getSelectedItem().toString();
                //  Toast.makeText(getActivity(), vSpinner_value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        binding.txtBtnSubmitRejected.setOnClickListener(this);
        binding.txtBackRejected.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_btn_submit_rejected:
                if (binding.edtNameOfReceiverReject.getText().toString().isEmpty()) {
                    Toast.makeText(Reject_Request_Activity.this, "Please Enter Reciever Name !", Toast.LENGTH_SHORT).show();
                } else if (binding.edtReasonReject.getText().toString().isEmpty()) {
                    Toast.makeText(Reject_Request_Activity.this, "Please Enter Reject Reason", Toast.LENGTH_SHORT).show();
                } else {
                    if (Utility.isConnectingToInternet(Reject_Request_Activity.this)) {
                        reject_request_api(vSpinner_value, binding.edtNameOfReceiverReject.getText().toString(), binding.edtReasonReject.getText().toString());
                    } else {
                        Toast.makeText(Reject_Request_Activity.this, "Please Connect Internet !", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.txt_back_rejected:
                finish();
                break;

        }
    }


    ////request reject by driver
    private void reject_request_api(String sp_value, String R_name, String R_reason) {
        ProgressDialog dialog = new ProgressDialog(Reject_Request_Activity.this);
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("reject_reason", R_reason + " " + sp_value);
            jsonObject.addProperty("reciver_name", R_name);
            System.out.println("jsonObject=request fast boat at home=" + jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        final Call<RejectDto> call = new DeliveryApiToJson().api_reject_request(sessionManager.getDeviceToken(), vOrder_id, "reject", jsonObject);
        call.enqueue(new Callback<RejectDto>() {
            @Override
            public void onResponse(Call<RejectDto> call, Response<RejectDto> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    RejectDto requestDto = response.body();
                    if (requestDto.getCode() == 200) {
                        Toast.makeText(Reject_Request_Activity.this, requestDto.getMsg().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(Reject_Request_Activity.this, MainActivity.class);
                        startActivity(intent1);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Reject_Request_Activity.this.finish();

                    } else {
                        dialog.dismiss();
                        Toast.makeText(Reject_Request_Activity.this, requestDto.getMsg().toString(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    dialog.dismiss();
                    Toast.makeText(Reject_Request_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                }
                //  swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RejectDto> call, Throwable t) {
                dialog.dismiss();

                Toast.makeText(Reject_Request_Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                //  swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}