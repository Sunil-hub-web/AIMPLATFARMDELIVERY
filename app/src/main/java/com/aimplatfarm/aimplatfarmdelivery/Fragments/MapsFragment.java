package com.aimplatfarm.aimplatfarmdelivery.Fragments;

import static com.aimplatfarm.aimplatfarmdelivery.Activities.MainActivity.toolbar_title;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.aimplatfarm.aimplatfarmdelivery.Activities.Order_Delivered_Activity;
import com.aimplatfarm.aimplatfarmdelivery.Models.RejectDto.RejectDto;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.Datum;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.RequestDto;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.ShippingDetails;
import com.aimplatfarm.aimplatfarmdelivery.Utility;
import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;
import com.aimplatfarm.aimplatfarmdelivery.map.HttpConnection;
import com.aimplatfarm.aimplatfarmdelivery.map.ParserTask;
import com.aimplatfarm.aimplatfarmdelivery.session_manager.SessionManager;
import com.aimplatfarm.aimplatfarmdelivery.Activities.MainActivity;
import com.aimplatfarm.aimplatfarmdelivery.Models.ApiResponse;
import com.aimplatfarm.aimplatfarmdelivery.Models.DriverDetails;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.Location_Dto;
import com.aimplatfarm.aimplatfarmdelivery.R;
import com.aimplatfarm.aimplatfarmdelivery.databinding.FragmentMapsBinding;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    public static MapsFragment mapsFragment;
    private SessionManager sessionManager;
    private FragmentMapsBinding binding;
    private OnMapReadyCallback callback;
    private View rootView;
    private final int interval = 1000; // 1 Second
    private Handler handler = new Handler();
    private Spinner spinner_reason;
    String profilImgPath = "";
    //map
    public static GoogleMap mGoogleMap;
    private ArrayList<Marker> markers = new ArrayList<>();
    public static FusedLocationProviderClient mFusedLocationClient;
    private Marker userMarker_Picker = null, drop_marker = null;
    private LatLng customerLatLng, drop_latlng;

    public static Polyline polyline;
    public static List<Polyline> polylines;
    SupportMapFragment mapFrag;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    private Marker userMarker = null;
    private LatLng userLatLng;
    private View mapView;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    double userlat = 0.0;
    double userlong = 0.0;
    double latitude = 0;
    double longitude = 0;
    private int vGoType = 0, vCancel_type = 0;
    private String Pickup_address = "";
    File currentPhotoPath;
    String imgUploadFrom;
    /////behavior of bottom sheets
    private BottomSheetBehavior behavior_bottomSheetDeliveredOne, behavior_bottom_sheet_pickedup,
            behavior_bottom_sheet_cancel_pickup, behavior_bottom_sheet_order_delivered, behavior_bottom_sheet_order_rejected;
    String vOrder_id = "", vDevice_Token = "", vSpinner_value = "", vUser_Name = "", vReasonTxt = "",
            vOrder_Satus = "", vDelivery_Address = "", vUser_Contact = "";
    //    public void setCallback(OnMapReadyCallback callback) {
//        this.callback = callback;
//    }
    OutputStream outputStream;
    File vCustomerSignature;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false);
        rootView = binding.getRoot();
        mapsFragment = this;
        toolbar_title.setText("Pick Up Item");
        sessionManager = new SessionManager(getActivity());
        mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragm_mapss);
        mapFrag.getMapAsync(this);
        mapView = mapFrag.getView();
        checkLocationPermission();
        statusCheck();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        ////////customer location

        ////////bundal pass from home fragment and setup on map
        Bundle bundle = getArguments();
        try {
            if (bundle != null) {
                Datum datum = (Datum) bundle.getSerializable("order_bundal") != null ? (Datum) bundle.getSerializable("order_bundal") : null;
                ShippingDetails shippingDetails = datum.getShippingDetails() != null ? datum.getShippingDetails() : null;
                Location_Dto pickup_location = datum.getWarehouseId().getLocation() != null ? datum.getWarehouseId().getLocation() : null;
                userlat = Double.parseDouble(shippingDetails.getAddressDetails().getLatitude());
                userlong = Double.parseDouble(shippingDetails.getAddressDetails().getLongitude());

                vOrder_Satus = datum.getOrderStatus() != null ? datum.getOrderStatus() : "";
                Pickup_address = pickup_location.getAddress() + " " +
                        pickup_location.getCity() + " " +
                        pickup_location.getLocality() + " " +
                        pickup_location.getState() + " " +
                        pickup_location.getZip();
                Log.i("MapFrag", "latlong_user....." + userlat + " " + userlong);
                vOrder_id = datum.getId() != null ? datum.getId() : "";
                vDevice_Token = sessionManager.getDeviceToken() != null ? sessionManager.getDeviceToken() : "";
                vDelivery_Address = shippingDetails.getAddressDetails().getHouse() + " " +
                        shippingDetails.getAddressDetails().getStreet() + " " +
                        shippingDetails.getAddressDetails().getCity() + " " +
                        shippingDetails.getAddressDetails().getLocality() + " " +
                        shippingDetails.getAddressDetails().getState() + " " +
                        shippingDetails.getAddressDetails().getCountry() + " " +
                        shippingDetails.getAddressDetails().getZip();
                vUser_Name = datum.getShippingDetails().getName() != null ? datum.getShippingDetails().getName() : "";
                vUser_Contact = datum.getShippingDetails().getContacts() != null ? datum.getShippingDetails().getContacts().toString() : "";
                ////set text bottom sheet pickup
                binding.bottomSheetPickedup.tvFromAddPickup.setText(Pickup_address);
                binding.bottomSheetPickedup.tvDeliveryToAddPickup.setText(vDelivery_Address);
                binding.bottomSheetPickedup.tvNamePickup.setText(vUser_Name);
                binding.bottomSheetPickedup.tvContactNumberPickup.setText(vUser_Contact);
                binding.bottomSheetPickedup.tvOrderIdPickup.setText(datum.getId() != null ? datum.getId() : "");

                ////set text bottom sheet delivery
                binding.bottomSheetDeliveredOne.tvName.setText(vUser_Name);
                binding.bottomSheetDeliveredOne.txtContactNumber.setText(vUser_Contact);
                binding.bottomSheetDeliveredOne.tvOrderId.setText(datum.getId() != null ? datum.getId() : "");
                binding.bottomSheetDeliveredOne.tvPickupfromAdd.setText(Pickup_address);
                binding.bottomSheetDeliveredOne.tvDeliveryToAdd.setText(vDelivery_Address);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        drop_latlng = new LatLng(userlat, userlong);
        polylines = new ArrayList<>();
        initialization_views();

        ArrayAdapter<CharSequence> adp4 = ArrayAdapter.createFromResource(getActivity(),
                R.array.cancel_reason, android.R.layout.simple_list_item_1);

        adp4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.bottomSheetCancelPickup.spinnerCancel.setAdapter(adp4);
        binding.bottomSheetCancelPickup.spinnerCancel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                vSpinner_value = binding.bottomSheetCancelPickup.spinnerCancel.getSelectedItem().toString();
                //  Toast.makeText(getActivity(), vSpinner_value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        return rootView;
    }

    private void initialization_views() {


        ///////bottom sheet /////
        behavior_bottomSheetDeliveredOne = BottomSheetBehavior.from(binding.bottomSheetDeliveredOne.getRoot());
        behavior_bottom_sheet_pickedup = BottomSheetBehavior.from(binding.bottomSheetPickedup.getRoot());
        behavior_bottom_sheet_cancel_pickup = BottomSheetBehavior.from(binding.bottomSheetCancelPickup.getRoot());

        /////defult extended bottomsheet
        behavior_bottom_sheet_pickedup.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottom_sheet_click_events();

        if (vOrder_Satus.equals("picked")) {
            binding.bottomSheetDeliveredOne.getRoot().setVisibility(View.VISIBLE);
            binding.bottomSheetPickedup.getRoot().setVisibility(View.GONE);
            binding.bottomSheetCancelPickup.getRoot().setVisibility(View.GONE);

            /////set behaviour///
            behavior_bottomSheetDeliveredOne.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

        binding.bottomSheetPickedup.llImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgUploadFrom = "pickup";
                binding.bottomSheetPickedup.ivImageUpload.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getActivity(), ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true

                startActivityForResult(intent, 1213);
            }
        });
        binding.bottomSheetCancelPickup.llImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgUploadFrom = "cancel";
                binding.bottomSheetCancelPickup.ivImageUpload.setVisibility(View.VISIBLE);
                Intent intent = new Intent(getActivity(), ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, 1213);
            }
        });

    }

    private void bottom_sheet_click_events() {
        ////bottom sheets items cancel
        binding.bottomSheetDeliveredOne.txtCancelAccepted.setOnClickListener(this);
        binding.bottomSheetPickedup.txtCancelPickedup.setOnClickListener(this);
        binding.bottomSheetPickedup.imgUploadPickup.setOnClickListener(this);

        ////bottom sheets accept conditions
        binding.bottomSheetDeliveredOne.txtBtnDelivered.setOnClickListener(this);
        binding.bottomSheetPickedup.txtBtnPickedup.setOnClickListener(this);
        binding.bottomSheetCancelPickup.txtBtnCancelOrder.setOnClickListener(this);
        binding.bottomSheetCancelPickup.txtBack.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_btn_pickedup:
                if (Utility.isConnectingToInternet(getActivity())) {
                    imgUploadFrom = "pickup";
                    if (vCustomerSignature != null) {
                        uploadImage(vCustomerSignature, imgUploadFrom);
                    } else {
                        Toast.makeText(getActivity(), "Please Upload Image !", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getActivity(), "Please Connect Internet !", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.txt_btn_delivered:
                binding.bottomSheetPickedup.getRoot().setVisibility(View.GONE);
                binding.bottomSheetCancelPickup.getRoot().setVisibility(View.GONE);
                binding.bottomSheetDeliveredOne.getRoot().setVisibility(View.VISIBLE);
                Intent intent2 = new Intent(getActivity(), Order_Delivered_Activity.class);
                intent2.putExtra("order_id", vOrder_id);
                intent2.putExtra("delivery_address", vDelivery_Address);
                intent2.putExtra("user_contact", vUser_Contact);
                intent2.putExtra("user_name", vUser_Name);
                intent2.putExtra("pickup_address", Pickup_address);
                startActivity(intent2);
                break;

            case R.id.txt_cancel_accepted:
                vGoType = 3;
                binding.bottomSheetDeliveredOne.getRoot().setVisibility(View.GONE);
                binding.bottomSheetPickedup.getRoot().setVisibility(View.GONE);
                binding.bottomSheetCancelPickup.getRoot().setVisibility(View.VISIBLE);
                /////set behaviour///
                behavior_bottom_sheet_cancel_pickup.setState(BottomSheetBehavior.STATE_EXPANDED);

                break;
            case R.id.txt_cancel_pickedup:
                vGoType = 2;
                binding.bottomSheetDeliveredOne.getRoot().setVisibility(View.GONE);
                binding.bottomSheetPickedup.getRoot().setVisibility(View.GONE);
                binding.bottomSheetCancelPickup.getRoot().setVisibility(View.VISIBLE);
                /////set behaviour///
                behavior_bottom_sheet_cancel_pickup.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.txt_btn_cancel_order:


                if (binding.bottomSheetCancelPickup.edtReasonCancel.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Provide Valid Reason !", Toast.LENGTH_SHORT).show();
                } else {
                    if (Utility.isConnectingToInternet(getActivity())) {
                        imgUploadFrom = "cancel";
                        if (vCustomerSignature != null) {
                            uploadImage(vCustomerSignature, imgUploadFrom);
                        } else {
                            Toast.makeText(getActivity(), "Please Upload Image !", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Connect Internet !", Toast.LENGTH_SHORT).show();
                    }
                }
                break;


            case R.id.txt_back:

                if (vGoType == 2) {
                    binding.bottomSheetDeliveredOne.getRoot().setVisibility(View.GONE);
                    binding.bottomSheetPickedup.getRoot().setVisibility(View.VISIBLE);
                    binding.bottomSheetCancelPickup.getRoot().setVisibility(View.GONE);
                    /////set behaviour///
                    toolbar_title.setText("Pick Up Item");
                    behavior_bottom_sheet_pickedup.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (vGoType == 3) {
                    binding.bottomSheetDeliveredOne.getRoot().setVisibility(View.VISIBLE);
                    binding.bottomSheetPickedup.getRoot().setVisibility(View.GONE);
                    binding.bottomSheetCancelPickup.getRoot().setVisibility(View.GONE);
                    /////set behaviour///
                    toolbar_title.setText("Pick Up Item");
                    behavior_bottomSheetDeliveredOne.setState(BottomSheetBehavior.STATE_EXPANDED);
                }

        }
    }

    private String getUrl(LatLng User_location, LatLng Drop_location) {

        // Waypoints
        String waypoints = "";
        waypoints = "waypoints=";


        String str_user_location = "";
        // Origin of route
        if (User_location != null) {
            if (User_location.latitude != 0.0 || User_location.longitude != 0.0) {
                str_user_location = "origin=" + User_location.latitude + "," + User_location.longitude;
            }
        }


        String str_drop_location = "";
        if (Drop_location != null) {
            if (Drop_location.latitude != 0.0 || Drop_location.longitude != 0.0) {
                // Destination of route
                str_drop_location = "destination=" + Drop_location.latitude + "," + Drop_location.longitude;
            }
        }

        String mode = "mode=driving";

        // Sensor enabled
        String sensor = "sensor=false";
        //   String params = waypoints + "&" + sensor;


        String key = "key=" + "AIzaSyAjOrzdzs-dxm48pmGWGshTufkBjj94BZc";
        // Output format
        String output = "json";
        String parameters = "";

        // Building the parameters to the web service
        parameters = str_user_location + "&" + str_drop_location + "&" + sensor + "&" + mode + "&" + key;

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        System.out.println("mapapi......" + url);
        return url;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {

            if (imgUploadFrom.equals("pickup")) {
                binding.bottomSheetPickedup.txtUploadImage.setVisibility(View.GONE);
            } else if (imgUploadFrom.equals("cancel")) {
                binding.bottomSheetCancelPickup.txtUploadImage.setVisibility(View.GONE);
            }
            String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);

            if (ImageSelectActivity.click_type_camera){
                //create a file to write bitmap data
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    currentPhotoPath = new File(getActivity().getExternalFilesDir(Environment.getExternalStorageState()), "saveImage");
                }

                if (!currentPhotoPath.exists()) {
                    currentPhotoPath.mkdir();
                }

                vCustomerSignature = new File(currentPhotoPath, System.currentTimeMillis() + ".jpg");
                try {
                    outputStream = new FileOutputStream(vCustomerSignature);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                try {
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = BitmapFactory.decodeFile(vCustomerSignature.getPath());
                if (imgUploadFrom.equals("pickup")) {
                    binding.bottomSheetPickedup.ivImageUpload.setImageBitmap(bitmap);

                } else if (imgUploadFrom.equals("cancel")) {
                    binding.bottomSheetCancelPickup.ivImageUpload.setImageBitmap(bitmap);
                }

            }else if(ImageSelectActivity.click_type_gellery){
                vCustomerSignature= new File(filePath);
                Uri  uri = Uri.parse(vCustomerSignature.getPath());
                if (imgUploadFrom.equals("pickup")) {
                    binding.bottomSheetPickedup.ivImageUpload.setImageURI(uri);

                } else if (imgUploadFrom.equals("cancel")) {
                    binding.bottomSheetCancelPickup.ivImageUpload.setImageURI(uri);
                }
            }




            //   Toast.makeText(getActivity(), currentPhotoPath.getAbsolutePath().toString(), Toast.LENGTH_SHORT).show();



        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setBuildingsEnabled(true);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); // two minute interval
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mGoogleMap.setMyLocationEnabled(false);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(false);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(false);
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {

                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                System.out.println("Location: " + location.getLatitude() + " " + location.getLongitude());
//                Toast.makeText(getActivity(), location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_SHORT).show();

                mLastLocation = location;

                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();

                if (userMarker != null) {
                    userMarker.remove();
                }
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                map_current_location_set_starting(mGoogleMap, latLng);
            }
        }
    };

    private void map_current_location_set_starting(GoogleMap mGoogleMap, LatLng latLng) {
        try {
            String url = "";
            FetchUrl FetchUrl = new FetchUrl();
            ////driver current location///
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("driver Location");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker_red));
            userMarker = mGoogleMap.addMarker(markerOptions);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

            MarkerOptions markerOptions1 = new MarkerOptions();
            markerOptions1.position(drop_latlng);
            markerOptions1.title("Drop Location");
            markerOptions1.flat(true);
            markerOptions1.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker_green));
            drop_marker = mGoogleMap.addMarker(markerOptions1);

            //   boundMapOnCenter(userMarker, drop_marker);
            url = getUrl(latLng, drop_latlng);
            Log.d("onMapClick", url.toString());
            // Start downloading json data from Google Directions API
            FetchUrl.execute(url);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }


    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (result != null) {
                    new ParserTask().execute(result);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void boundMapOnCenter(Marker marker1, Marker marker2) {
        try {
            if (markers == null) {
                markers = new ArrayList<>();
            }
            if (markers != null) {
                markers.clear();
                // markers.add(currentLocationMarker);
                markers.add(marker1);
                markers.add(marker2);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : markers) {
                    builder.include(marker.getPosition());
                }
                LatLngBounds bounds = builder.build();
                int width = getResources().getDisplayMetrics().widthPixels;
                int padding = (int) (width * 0.20); //
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mGoogleMap.animateCamera(cu);
            }
        } catch (Exception e) {

        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);


                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(false);

                        Fragment frg = null;
                        frg = getChildFragmentManager().findFragmentByTag("Your_Fragment_TAG");
                        final FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                        ft.detach(frg);
                        ft.attach(frg);
                        ft.commit();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }


    //////Gps Enable////
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        statusCheck();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /////////pickup api
    private void api_pickup_order(String path) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("picked_image", path);
            System.out.println("jsonObject=request fast boat at home=" + jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<RequestDto> call = new DeliveryApiToJson().api_order_picked(vDevice_Token, vOrder_id, "picked", jsonObject);
        call.enqueue(new Callback<RequestDto>() {
            @Override
            public void onResponse(Call<RequestDto> call, Response<RequestDto> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    RequestDto requestDto = response.body();
                    //  System.out.println("request_dto==== " + requestDto.getData());
                    if (requestDto.getCode() == 200) {
                        // if (requestDto.getData() != null) {
                        Toast.makeText(getActivity(), requestDto.getMsg().toString(), Toast.LENGTH_SHORT).show();
                        binding.bottomSheetDeliveredOne.getRoot().setVisibility(View.VISIBLE);
                        binding.bottomSheetPickedup.getRoot().setVisibility(View.GONE);
                        binding.bottomSheetCancelPickup.getRoot().setVisibility(View.GONE);
                        /////set behaviour///
                        behavior_bottomSheetDeliveredOne.setState(BottomSheetBehavior.STATE_EXPANDED);
                        //    } else {
                        //       dialog.dismiss();
                        //       Toast.makeText(getActivity(), requestDto.getMsg().toString(), Toast.LENGTH_SHORT).show();
                        //   }

                    } else {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), requestDto.getMsg().toString(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();

                }
                //  swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RequestDto> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                //  swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    //////request cancel by driver

    private void cancel_order_api(String sp_value, String R_name, String R_reason) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("cancel_reason", R_reason + " " + sp_value);
            jsonObject.addProperty("cancel_image", "");
            System.out.println("jsonObject=request fast boat at home=" + jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        final Call<RejectDto> call = new DeliveryApiToJson().api_cancel_request(sessionManager.getDeviceToken(), vOrder_id, "cancel", jsonObject);
        call.enqueue(new Callback<RejectDto>() {
            @Override
            public void onResponse(Call<RejectDto> call, Response<RejectDto> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    RejectDto requestDto = response.body();
                    if (requestDto.getCode() == 200) {
                        Toast.makeText(getActivity(), requestDto.getMsg().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent1);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().finish();

                    } else {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), requestDto.getMsg().toString(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();

                }
                //  swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RejectDto> call, Throwable t) {
                dialog.dismiss();

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                //  swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /////photo upload
    private void uploadImage(File file, String imgUploadFrom) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
        RequestBody imageBode = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("photo", file.getPath(), imageBode);
        Call<ApiResponse> call = new DeliveryApiToJson().api_upload_photo_order_pickup(vDevice_Token, partImage);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println("res:----    " + response.body());
                    dialog.dismiss();
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.getCode() == 200) {
                        DriverDetails data = apiResponse.getData();
                        if (data != null) {
                            Toast.makeText(getActivity(), apiResponse.getMsg().toString(), Toast.LENGTH_SHORT).show();
                            String path = data.getPath() != null ? data.getPath() : "";
                            if (imgUploadFrom.equals("pickup")) {
                                if (Utility.isConnectingToInternet(getActivity())) {
                                    api_pickup_order(path);
                                } else {
                                    Toast.makeText(getActivity(), "Please Connect Internet !", Toast.LENGTH_SHORT).show();
                                }
                            } else if (imgUploadFrom.equals("cancel")) {
                                if (Utility.isConnectingToInternet(getActivity())) {
                                    cancel_order_api(vSpinner_value, binding.bottomSheetCancelPickup.edtReasonCancel.getText().toString(), vReasonTxt);
                                } else {
                                    Toast.makeText(getActivity(), "Please Connect Internet !", Toast.LENGTH_SHORT).show();
                                }
                            }


                        }

                    }
                    //  confirmOrderObjectCall(response.body().getMsg().getFilename());
                    Log.e("Image Response", response.body().toString());
                    Toast.makeText(getActivity(), response.body().getMsg().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Gson gson = new Gson();
                    ApiResponse message = gson.fromJson(response.errorBody().charStream(), ApiResponse.class);
                    Log.e("Image Response", message.getMsg());
                    Toast.makeText(getActivity(), message.getMsg().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                dialog.dismiss();
                Log.e("Image Response", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}