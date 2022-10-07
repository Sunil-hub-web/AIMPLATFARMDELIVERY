package com.aimplatfarm.aimplatfarmdelivery.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.aimplatfarm.aimplatfarmdelivery.Adapter.PaymentDetailsAdapter;
import com.aimplatfarm.aimplatfarmdelivery.Models.PaymentDetails;
import com.aimplatfarm.aimplatfarmdelivery.R;

import java.util.ArrayList;

public class WalletActivity extends AppCompatActivity {

    RecyclerView payment_recycler;
    LinearLayoutManager linearLayoutManager;
    PaymentDetailsAdapter paymentDetailsAdapter;
    ArrayList<PaymentDetails> paymentDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        payment_recycler = findViewById(R.id.payment_recycler);

        paymentDetails.add(new PaymentDetails("123456789654","Apple","Delivered","Rs 250","Refundable","12 sep 22"));
        paymentDetails.add(new PaymentDetails("123456789654","Apple","Returned","Rs 630","Refundable","13 sep 22"));
        paymentDetails.add(new PaymentDetails("123456789654","Apple","Delivered","Rs 500","Refundable","14 sep 22"));
        paymentDetails.add(new PaymentDetails("123456789654","Apple","Returned","Rs 810","Refundable","15 sep 22"));

        linearLayoutManager = new LinearLayoutManager(WalletActivity.this,LinearLayoutManager.VERTICAL,false);
        paymentDetailsAdapter = new PaymentDetailsAdapter(paymentDetails,WalletActivity.this);
        payment_recycler.setLayoutManager(linearLayoutManager);
        payment_recycler.setHasFixedSize(true);
        payment_recycler.setAdapter(paymentDetailsAdapter);

    }
}