package com.aimplatfarm.aimplatfarmdelivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.aimplatfarm.aimplatfarmdelivery.R;
import com.aimplatfarm.aimplatfarmdelivery.databinding.ActivityRejectRequestBinding;
import com.aimplatfarm.aimplatfarmdelivery.databinding.ActivitySupportBinding;

public class Support_Activity extends AppCompatActivity {
    private Toolbar toolbar;
    ActivitySupportBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_support);

        init_Views();
    }

    private void init_Views() {
        toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Support");
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}