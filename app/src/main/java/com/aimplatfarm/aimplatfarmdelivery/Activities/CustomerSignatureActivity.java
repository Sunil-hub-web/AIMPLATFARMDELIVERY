package com.aimplatfarm.aimplatfarmdelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;
import com.aimplatfarm.aimplatfarmdelivery.Models.ApiResponse;
import com.aimplatfarm.aimplatfarmdelivery.Models.DriverDetails;
import com.aimplatfarm.aimplatfarmdelivery.Models.ImageResponse;
import com.aimplatfarm.aimplatfarmdelivery.R;
import com.google.gson.Gson;
import com.raodevs.touchdraw.TouchDrawView;

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

public class CustomerSignatureActivity extends AppCompatActivity {

    private Toolbar toolbar;
    Button clearBtn, acceptBtn;
    TouchDrawView drawView;
    String token;
    OutputStream outputStream;
    File customerSignature;
    String id, userId;
    DriverDetails agent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_signature);

        init();
    }

    private void init(){
        id = getIntent().getStringExtra("id");
        userId = getIntent().getStringExtra("userId");
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        toolbar = findViewById(R.id.toolbar);
        getProfile();
        if (getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Get Signature");
        }
        drawView = findViewById(R.id.canvas);
        clearBtn = findViewById(R.id.clearBtn);
        acceptBtn = findViewById(R.id.acceptBtn);

        clearBtn.setOnClickListener(v -> {
            drawView.clear();
        });

        acceptBtn.setOnClickListener(v -> {
            Bitmap bmp = drawView.getFile();
            // display confirmation dialog
            confirmDialog(bmp);
        });
    }

    // display confirm dialog
    private void confirmDialog(Bitmap bitmap){
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.findViewById(R.id.cancelBtn).setOnClickListener(v1->dialog.dismiss());
        TextView confirmBtn = dialog.findViewById(R.id.logoutBtn);
        TextView titleText = dialog.findViewById(R.id.titleText);
        titleText.setText("Confirm Order");
        confirmBtn.setText("Confirm");
        confirmBtn.setOnClickListener(v1 -> {
            dialog.dismiss();
            if (ContextCompat.checkSelfPermission(CustomerSignatureActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                try {
                    saveImage(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new Handler().postDelayed(()->{
                    uploadImage(customerSignature);
                }, 2000);
            } else {
                askPermission();
            }
        });
        dialog.create();
        dialog.show();
    }

    private void saveImage(Bitmap bitmap){
        File dir = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            dir = new File(getExternalFilesDir(Environment.DIRECTORY_SCREENSHOTS), "saveImage");
        }

        if (!dir.exists()){
            dir.mkdir();
        }

        customerSignature = new File(dir, System.currentTimeMillis()+".jpg");
        try {
            outputStream = new FileOutputStream(customerSignature);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
            }else{
                Toast.makeText(this, "Please grant the permissions to complete this operation", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void askPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private void getProfile() {
        Call<ApiResponse> call = new DeliveryApiToJson().getProfile(token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CustomerSignatureActivity.this, "Got profile", Toast.LENGTH_SHORT).show();
                    agent = response.body().getData();
                } else {
                    Toast.makeText(CustomerSignatureActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(CustomerSignatureActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // upload image
    private void uploadImage(File file){
        RequestBody imageBode = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("photo", file.getName(), imageBode);
        Call<ImageResponse> call = new DeliveryApiToJson().uploadImage(token, partImage);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (response.isSuccessful()){
                  //  confirmOrderObjectCall(response.body().getMsg().getFilename());
                    Log.e("Image Response", response.body().toString());
                }else{
                    Gson gson = new Gson();
                    ApiResponse message = gson.fromJson(response.errorBody().charStream(),ApiResponse.class);
                    Log.e("Image Response", message.getMsg());
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Log.e("Image Response", t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}