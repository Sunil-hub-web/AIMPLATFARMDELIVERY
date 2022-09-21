package com.aimplatfarm.aimplatfarmdelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;
import com.aimplatfarm.aimplatfarmdelivery.Adapter.NotificationsAdapter;
import com.aimplatfarm.aimplatfarmdelivery.Models.ApiResponse;
import com.aimplatfarm.aimplatfarmdelivery.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private String token;
    private RecyclerView notificationsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        init();
    }

    private void init() {
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this::getNotifications);
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

         notificationsView = findViewById(R.id.notifications);
        toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Notifications");
        }

        getNotifications();
    }

    // getting the delivery drivers notification
    private void getNotifications(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();

        Call<ApiResponse> call = new DeliveryApiToJson().getNotifications(token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()){
                    // fill the notification
                    NotificationsAdapter adapter = new NotificationsAdapter(NotificationsActivity.this);
                    adapter.setNotifications(response.body().getNotification());
                    notificationsView.setAdapter(adapter);
                    notificationsView.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this));

                }else{
                    Toast.makeText(NotificationsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(NotificationsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
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