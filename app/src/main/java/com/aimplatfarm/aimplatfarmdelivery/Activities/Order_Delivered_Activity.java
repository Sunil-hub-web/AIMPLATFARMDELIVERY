package com.aimplatfarm.aimplatfarmdelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aimplatfarm.aimplatfarmdelivery.RealPathUtil;
import com.aimplatfarm.aimplatfarmdelivery.Utility;
import com.aimplatfarm.aimplatfarmdelivery.Models.ApiResponse;
import com.aimplatfarm.aimplatfarmdelivery.Models.DriverDetails;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.RequestDto;
import com.aimplatfarm.aimplatfarmdelivery.R;
import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;


import com.aimplatfarm.aimplatfarmdelivery.databinding.ActivityOrderDeliveredBinding;
import com.aimplatfarm.aimplatfarmdelivery.session_manager.SessionManager;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_Delivered_Activity extends AppCompatActivity implements View.OnClickListener {
    ActivityOrderDeliveredBinding binding;
    OutputStream outputStream;
    File customerSignature;
    private SessionManager sessionManager;
    String vOrder_id = "", vDevice_Token = "", vSpinner_value = "", vUser_Name = "", vReasonTxt = "",
            vOrder_Satus = "", vDelivery_Address = "", vUser_Contact = "", vPickupAddress = "",
            currentPhotoPath = "";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_delivered);
        sessionManager = new SessionManager(this);
        init_views();
    }

    private void init_views() {

        toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Order Delivery");
        }
        vDevice_Token = sessionManager.getDeviceToken() != null ? sessionManager.getDeviceToken() : "";
        binding.txtClearSignature.setOnClickListener(this);
        binding.txtBtnDeliveryReject.setOnClickListener(this);
        binding.txtBtnSubmitDelivered.setOnClickListener(this);

        if (getIntent() != null) {
            try {
                vOrder_id = getIntent().getStringExtra("order_id") != null ? getIntent().getStringExtra("order_id") : "";
                vDelivery_Address = getIntent().getStringExtra("delivery_address") != null ? getIntent().getStringExtra("delivery_address") : "";
                vUser_Name = getIntent().getStringExtra("user_name") != null ? getIntent().getStringExtra("user_name") : "";
                vUser_Contact = getIntent().getStringExtra("user_contact") != null ? getIntent().getStringExtra("user_contact") : "";
                vPickupAddress = getIntent().getStringExtra("pickup_address") != null ? getIntent().getStringExtra("pickup_address") : "";


                binding.tvNameDelivery.setText(vUser_Name);
                binding.tvContactNumberDelivery.setText(vUser_Contact);
                binding.tvOrderIdDelivery.setText(vOrder_id);
                binding.tvPickupDelivery.setText(vPickupAddress);
                binding.tvDeliveryAddress.setText(vDelivery_Address);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        binding.signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                //  Toast.makeText(getActivity(), "OnStartSigning", Toast.LENGTH_SHORT).show();
                //https://github.com/gcacace/android-signaturepad/blob/master/SignaturePad-Example/src/main/java/com/github/gcacace/signaturepad/MainActivity.java
            }

            @Override
            public void onSigned() {
//                mSaveButton.setEnabled(true);
                binding.txtClearSignature.setEnabled(true);
            }

            @Override
            public void onClear() {
                //mSaveButton.setEnabled(false);
                binding.txtClearSignature.setEnabled(true);

            }
        });


    }

    //delivered order api
    private void api_delivered(String path) {
        ProgressDialog dialog = new ProgressDialog(Order_Delivered_Activity.this);
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.addProperty("orderId", vOrder_id);
            jsonObject.addProperty("signature", path);
            System.out.println("jsonObject=request fast boat at home=" + jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<RequestDto> call = new DeliveryApiToJson().api_delivered_request(vDevice_Token, jsonObject);
        call.enqueue(new Callback<RequestDto>() {
            @Override
            public void onResponse(Call<RequestDto> call, Response<RequestDto> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    RequestDto requestDto = response.body();
                    if (requestDto.getCode() == 200) {
                        Toast.makeText(Order_Delivered_Activity.this, requestDto.getMsg().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Order_Delivered_Activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(Order_Delivered_Activity.this, requestDto.getMsg().toString(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Order_Delivered_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                }
                //  swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RequestDto> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Order_Delivered_Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                //  swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    /////photo upload
    private void uploadImage(File file) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
        RequestBody imageBode = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("photo", file.getName(), imageBode);
        Call<ApiResponse> call = new DeliveryApiToJson().api_upload_photo_order_pickup(vDevice_Token, partImage);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.getCode() == 200) {
                        DriverDetails data = apiResponse.getData();
                        if (data != null) {
                            //  Toast.makeText(Order_Delivered_Activity.this, apiResponse.getMsg().toString(), Toast.LENGTH_SHORT).show();
                            String path = data.getPath() != null ? data.getPath() : "";
                            api_delivered(path);

                        }

                    }
                    //  confirmOrderObjectCall(response.body().getMsg().getFilename());
                    Log.e("Image Response", response.body().toString());
                } else {
                    dialog.dismiss();
                    Gson gson = new Gson();
                    ApiResponse message = gson.fromJson(response.errorBody().charStream(), ApiResponse.class);
                    Log.e("Image Response", message.getMsg());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                dialog.dismiss();
                Log.e("Image Response", t.getMessage());
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_clear_signature:
                binding.signaturePad.clear();
                break;
            case R.id.txt_btn_delivery_reject:

                Intent intent2 = new Intent(Order_Delivered_Activity.this, Reject_Request_Activity.class);
                intent2.putExtra("order_id", vOrder_id);
                intent2.putExtra("delivery_address", vDelivery_Address);
                intent2.putExtra("user_contact", vUser_Contact);
                intent2.putExtra("user_name", vUser_Name);
                intent2.putExtra("pickup_address", vPickupAddress);
                startActivity(intent2);
                break;

            case R.id.txt_btn_submit_delivered:

                try {
                    if (Utility.isConnectingToInternet(Order_Delivered_Activity.this)) {
                        if (!binding.edtName.getText().toString().isEmpty()) {
                            if (ContextCompat.checkSelfPermission(Order_Delivered_Activity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                Bitmap bitmap = binding.signaturePad.getSignatureBitmap();

                                try {

                                    if (!binding.signaturePad.isEmpty()) {
                                        Toast.makeText(this, "image"+bitmap, Toast.LENGTH_SHORT).show();

                                        //saveImage(bitmap);

                                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                                        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "Title", null);
                                        Log.d("vbrfxgfecs", "Pick from Camera::>>> "+bitmap);
                                        Uri photouri = Uri.parse(path);

                                        String path1 = RealPathUtil.getRealPath(this,photouri);
                                        File file = new File(path1);

                                        new Handler().postDelayed(() -> {
                                            uploadImage(file);
                                        }, 1000);
                                    } else {
                                        Toast.makeText(Order_Delivered_Activity.this, "Please Upload Signature !", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Utility.checkAndRequestPermissions(this);
                            }
                        } else {
                            Toast.makeText(Order_Delivered_Activity.this, "Please enter receiver name !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Order_Delivered_Activity.this, "Please Connect Internet !", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // Toast.makeText(Order_Delivered_Activity.this, "Please Upload Signature !", Toast.LENGTH_SHORT).show();

                }


                break;

        }

    }

    private void saveImage(Bitmap bitmap) {
        File dir = null;
        customerSignature = new File("");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            dir = new File(this.getExternalFilesDir(Environment.DIRECTORY_SCREENSHOTS), "signature_image");
        }

        if (!dir.exists()) {
            dir.mkdir();
        }

        customerSignature = new File(dir, System.currentTimeMillis() + ".jpg");
        try {
            outputStream = new FileOutputStream(customerSignature);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        //////storage/emulated/0/Android/data/com.aimplatfarm.aimplatfarmdelivery/files/Screenshots/signature_image/1645373559616.jpg
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
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}