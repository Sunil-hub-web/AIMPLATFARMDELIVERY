package com.aimplatfarm.aimplatfarmdelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.aimplatfarm.aimplatfarmdelivery.Models.ApiResponse;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.ShippingDetails;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.WarehouseId;
import com.aimplatfarm.aimplatfarmdelivery.Utility;
import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;
import com.aimplatfarm.aimplatfarmdelivery.session_manager.SessionManager;
import com.aimplatfarm.aimplatfarmdelivery.R;
import com.aimplatfarm.aimplatfarmdelivery.databinding.ActivityOrderHistoryDetailsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_History_Details_Activity extends AppCompatActivity implements OnMapReadyCallback {
    private Toolbar toolbar;
    private SessionManager sessionManager;
    String vOrder_id = "";
    private ActivityOrderHistoryDetailsBinding binding;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_history_details);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        init();
    }

    private void init() {
        sessionManager = new SessionManager(this);
        toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Order History");

            if (getIntent() != null) {
                vOrder_id = getIntent().getStringExtra("order_id") != null ? getIntent().getStringExtra("order_id") : "";
                if (Utility.isConnectingToInternet(this)) {
                    api_history();

                } else {
                    Toast.makeText(this, "Please Connect Internet !", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void api_history() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
//62095682f7b43c5df7111764
        // eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MWFmNzcyODU1ODlhNDUxYzAxNmNiMDgiLCJpYXQiOjE2NDQwNDc0MjF9.F8bxhagxiEbOJAcd_H1-zDlho51mU8XkzXBdC2jlO4I
        Call<ApiResponse> call = new DeliveryApiToJson().api_order_history_details(sessionManager.getDeviceToken(), vOrder_id);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.getCode() == 200) {
                        ShippingDetails shippingDetails = apiResponse.getOrderDetails().getShippingDetails() != null ? apiResponse.getOrderDetails().getShippingDetails() : null;
//                        Location_Dto pickup_location = apiResponse.getOrderDetails().getLocation() != null ? apiResponse.getOrderDetails().getWarehouseId().getLocation() : null;
                        WarehouseId warehouseId = apiResponse.getOrderDetails().getWarehouseId() != null ? apiResponse.getOrderDetails().getWarehouseId() : null;
                        String Pickup_address = null;
                        if (warehouseId != null) {
                            Pickup_address = warehouseId.getLocation().getAddress() + " " +
                                    warehouseId.getLocation().getCity() + " " +
                                    warehouseId.getLocation().getLocality() + " " +
                                    warehouseId.getLocation().getState() + " " +
                                    warehouseId.getLocation().getZip();
                            binding.tvFromAddPickup.setText(Pickup_address);
                        }
                        vOrder_id = apiResponse.getOrderDetails().getId() != null ? apiResponse.getOrderDetails().getId() : "";


                        String delivery_address = null;
                        if (shippingDetails != null) {
                            delivery_address = shippingDetails.getAddressDetails().getHouse() + " " +
                                    shippingDetails.getAddressDetails().getStreet() + " " +
                                    shippingDetails.getAddressDetails().getCity() + " " +
                                    shippingDetails.getAddressDetails().getLocality() + " " +
                                    shippingDetails.getAddressDetails().getState() + " " +
                                    shippingDetails.getAddressDetails().getCountry() + " " +
                                    shippingDetails.getAddressDetails().getZip();
                            binding.tvDeliveryToAddPickup.setText(delivery_address);
                        }


                        binding.tvOrderIdPickup.setText(vOrder_id);
                        binding.tvOrderStatus.setText(apiResponse.getOrderDetails().getOrderStatus());

                        binding.tvNamePickup.setText(apiResponse.getOrderDetails().getShippingDetails().getName() != null ? apiResponse.getOrderDetails().getShippingDetails().getName() : "");
                        binding.tvContactNumberPickup.setText(apiResponse.getOrderDetails().getShippingDetails().getContacts() != null ? apiResponse.getOrderDetails().getShippingDetails().getContacts().toString() : "");
                        binding.tvOrderIdPickup.setText(apiResponse.getOrderDetails().getId() != null ? apiResponse.getOrderDetails().getId() : "");
                        try {
                            double latitude = Double.parseDouble(shippingDetails.getAddressDetails().getLatitude());
                            double longitude = Double.parseDouble(shippingDetails.getAddressDetails().getLongitude());
                            LatLng TutorialsPoint = new LatLng(latitude, longitude);

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(TutorialsPoint);
                            markerOptions.title(delivery_address);
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker_red));
                            Marker userMarker = mMap.addMarker(markerOptions);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TutorialsPoint, 12));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(Order_History_Details_Activity.this, apiResponse.getMsg().toString(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Order_History_Details_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Order_History_Details_Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        mMap.getUiSettings().setScrollGesturesEnabled(false);
    }
}