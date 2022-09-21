package com.aimplatfarm.aimplatfarmdelivery.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.Product;
import com.aimplatfarm.aimplatfarmdelivery.Activities.Order_History_Details_Activity;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.Datum;
import com.aimplatfarm.aimplatfarmdelivery.R;
import com.aimplatfarm.aimplatfarmdelivery.session_manager.SessionManager;

import java.util.ArrayList;

public class Order_History_Adapter extends RecyclerView.Adapter<Order_History_Adapter.NotifiewHolder> {

    private Activity context;
    private ArrayList<Datum> datumList;
    private ArrayList<Product> products_list;
    private AllProductListAdapter allProductListAdapter;
    SessionManager sessionManager;

    public Order_History_Adapter(Activity context) {
        this.context = context;
    }

    public void setrequestslist1(ArrayList<Datum> datumList) {
        this.datumList = datumList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotifiewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotifiewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotifiewHolder holder, int position) {
        int pos = position;

        sessionManager = new SessionManager(context);
        holder.tv_order_name.setText("Order Name :- " + datumList.get(pos).getShippingDetails().getName());
        holder.tv_order_id.setText("Order Id :- " + datumList.get(pos).getId());
//        holder.tv_driver_status.setText(datumList.get(pos).getDriverDettails().get(0).getStatus() != null ? datumList.get(0).getDriverDettails().get(pos).getStatus() : "");
        holder.tv_order_status.setText(datumList.get(pos).getOrderStatus() != null ? datumList.get(pos).getOrderStatus() : "");


        //  holder.tv_order_id.setText(datumList.get(position).getDescription());
//        Random rnd = new Random();
//        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//        holder.cardv_main.setCardBackgroundColor(color);

        try {
            products_list = new ArrayList<>();
            allProductListAdapter = new AllProductListAdapter(context);
            products_list.addAll(datumList.get(pos).getProducts());
            allProductListAdapter.setrequestslist1(products_list);
            holder.rv_product_list.setAdapter(allProductListAdapter);
            holder.rv_product_list.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            holder.rv_product_list.setHasFixedSize(false);
            holder.rv_product_list.setNestedScrollingEnabled(false);
            allProductListAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Order_History_Details_Activity.class);
                intent.putExtra("order_id", datumList.get(pos).getId() != null ? datumList.get(pos).getId() : "");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datumList.size();

    }

    public static class NotifiewHolder extends RecyclerView.ViewHolder {

        private TextView tv_order_name, tv_order_id, tv_driver_status, tv_order_status;
        private CardView cardv_main;
        private RecyclerView rv_product_list;

        public NotifiewHolder(@NonNull View itemView) {
            super(itemView);

            tv_order_name = itemView.findViewById(R.id.tv_order_name);
            tv_order_id = itemView.findViewById(R.id.tv_order_id);
            cardv_main = itemView.findViewById(R.id.cardv_main);
            tv_driver_status = itemView.findViewById(R.id.tv_driver_status);
            tv_order_status = itemView.findViewById(R.id.tv_order_status);
            rv_product_list = itemView.findViewById(R.id.rv_product_list);
            tv_order_id = itemView.findViewById(R.id.tv_order_id);
        }
    }


}
