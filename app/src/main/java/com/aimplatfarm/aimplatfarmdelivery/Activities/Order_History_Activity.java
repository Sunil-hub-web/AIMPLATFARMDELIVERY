package com.aimplatfarm.aimplatfarmdelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.aimplatfarm.aimplatfarmdelivery.Adapter.Order_History_Adapter;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.RequestDto;
import com.aimplatfarm.aimplatfarmdelivery.Utility;
import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.Datum;
import com.aimplatfarm.aimplatfarmdelivery.R;
import com.aimplatfarm.aimplatfarmdelivery.session_manager.SessionManager;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_History_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView rv_order_history;
    private SwipeRefreshLayout swipeRefreshLayout;
    private com.aimplatfarm.aimplatfarmdelivery.Adapter.Order_History_Adapter Order_History_Adapter;
    ArrayList<Datum> rejectDtos;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        sessionManager = new SessionManager(this);
        init_Views();
    }

    private void init_Views() {
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);

        rv_order_history = findViewById(R.id.rv_order_history);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Order History");
        }

        if (Utility.isConnectingToInternet(this)) {
            api_history();
            swipeRefreshLayout.setOnRefreshListener(this::api_history);
        } else {
            Toast.makeText(this, "Please Connect Internet !", Toast.LENGTH_SHORT).show();
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
        rejectDtos = new ArrayList<>();
        Order_History_Adapter = new Order_History_Adapter(this);

        Call<RequestDto> call = new DeliveryApiToJson().api_history(sessionManager.getDeviceToken());
        call.enqueue(new Callback<RequestDto>() {
            @Override
            public void onResponse(Call<RequestDto> call, Response<RequestDto> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    RequestDto requestDto = response.body();
                    if (requestDto.getCode() == 200) {
                        if (requestDto.getData() != null) {
                            if (rejectDtos != null) {
                                rejectDtos.clear();
                                rejectDtos.addAll(requestDto.getData());
                            }
                            Collections.reverse(rejectDtos);
                            Order_History_Adapter.setrequestslist1(rejectDtos);
                            rv_order_history.setAdapter(Order_History_Adapter);
                            rv_order_history.setLayoutManager(new LinearLayoutManager(Order_History_Activity.this));
                            Order_History_Adapter.notifyDataSetChanged();
                        }


                    } else {
                        dialog.dismiss();
                        Toast.makeText(Order_History_Activity.this, requestDto.getMsg().toString(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Order_History_Activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RequestDto> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Order_History_Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}