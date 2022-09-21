package com.aimplatfarm.aimplatfarmdelivery.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.Product;
import com.aimplatfarm.aimplatfarmdelivery.session_manager.SessionManager;
import com.aimplatfarm.aimplatfarmdelivery.R;

import java.util.ArrayList;

public class AllProductListAdapter extends RecyclerView.Adapter<AllProductListAdapter.NotifiewHolder> {

    private Activity context;
    private ArrayList<Product> datumList;

    SessionManager sessionManager;

    public AllProductListAdapter(Activity context) {
        this.context = context;
    }

    public void setrequestslist1(ArrayList<Product> datumList) {
        this.datumList = datumList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotifiewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotifiewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotifiewHolder holder, int position) {
        int pos = position;
        holder.tv_order_name.setText("Product Id :- " + datumList.get(pos).getProductId());
    }

    @Override
    public int getItemCount() {
        return datumList.size();

    }

    public static class NotifiewHolder extends RecyclerView.ViewHolder {

        private TextView tv_order_name;

        public NotifiewHolder(@NonNull View itemView) {
            super(itemView);
            tv_order_name = itemView.findViewById(R.id.tv_order_name);
        }
    }


}
