package com.aimplatfarm.aimplatfarmdelivery.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aimplatfarm.aimplatfarmdelivery.Fragments.Home_Fragment;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.Datum;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.Product;
import com.aimplatfarm.aimplatfarmdelivery.R;
import com.aimplatfarm.aimplatfarmdelivery.session_manager.SessionManager;

import java.util.ArrayList;

public class AllAcceptOrdersListAdapter extends RecyclerView.Adapter<AllAcceptOrdersListAdapter.NotifiewHolder> {

    private Activity context;
    private ArrayList<Datum> datumList;
    private ArrayList<Product> products_list;
    private AllProductListAdapter allProductListAdapter;
    SessionManager sessionManager;

    public AllAcceptOrdersListAdapter(Activity context) {
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
                .inflate(R.layout.requests_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotifiewHolder holder, int position) {
        int pos = position;

        sessionManager = new SessionManager(context);
        holder.tv_order_name.setText("Order Id :- " + datumList.get(pos).getId());
        //  holder.tv_order_id.setText(datumList.get(position).getDescription());
     //   Random rnd = new Random();
       // int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        //holder.cardv_main.setCardBackgroundColor(color);

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
                (Home_Fragment.home_fragment).Adapter_redirection(datumList.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        return datumList.size();

    }

    public static class NotifiewHolder extends RecyclerView.ViewHolder {

        private TextView tv_order_name, tv_order_id;
        private CardView cardv_main;
        private RecyclerView rv_product_list;

        public NotifiewHolder(@NonNull View itemView) {
            super(itemView);

            tv_order_name = itemView.findViewById(R.id.tv_order_name);
            tv_order_id = itemView.findViewById(R.id.tv_order_id);
            cardv_main = itemView.findViewById(R.id.cardv_main);
            rv_product_list = itemView.findViewById(R.id.rv_product_list);
        }
    }


}
