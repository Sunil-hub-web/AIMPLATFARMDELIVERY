package com.aimplatfarm.aimplatfarmdelivery.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aimplatfarm.aimplatfarmdelivery.Fragments.Home_Fragment;
import com.aimplatfarm.aimplatfarmdelivery.Utility;
import com.aimplatfarm.aimplatfarmdelivery.Activities.Reject_Request_Activity;
import com.aimplatfarm.aimplatfarmdelivery.Models.RejectDto.RejectDto;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.Datum;

import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.Product;
import com.aimplatfarm.aimplatfarmdelivery.R;
import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApi;
import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;
import com.aimplatfarm.aimplatfarmdelivery.session_manager.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersListAdapter extends RecyclerView.Adapter<OrdersListAdapter.NotifiewHolder> {

    private Activity context;
    private List<Datum> datumList;
    private DeliveryApi deliveryApi;
    SessionManager sessionManager;
    private ArrayList<Product> products_list;
    private AllProductListAdapter allProductListAdapter;
    String vOrder_id = "", vUser_Name = "",
            vOrder_Satus = "", vDelivery_Address = "", vUser_Contact = "", vPickupAddress = "";


    public OrdersListAdapter(Activity context) {
        this.context = context;
    }

    public void setrequestslist(List<Datum> datumList) {
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
        deliveryApi = DeliveryApiToJson.getClient().create(DeliveryApi.class);
        sessionManager = new SessionManager(context);
        holder.tv_order_name.setText("Order Id :- " + datumList.get(position).getId());
        //  holder.tv_order_id.setText(datumList.get(position).getDescription());
       // Random rnd = new Random();
       // int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
       // holder.cardv_main.setCardBackgroundColor(color);
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
                open_Dialog(pos);
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

    public void open_Dialog(int pos) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog_request_datails);
        AppCompatButton btn_allow = dialog.findViewById(R.id.btn_allow);
        AppCompatButton btn_dont_allow = dialog.findViewById(R.id.btn_dont_allow);

        TextView tv_order_id = dialog.findViewById(R.id.tv_order_id);
        TextView tv_order_name = dialog.findViewById(R.id.tv_order_name);
        TextView tv_name = dialog.findViewById(R.id.tv_name);
        TextView tv_Mobile = dialog.findViewById(R.id.tv_Mobile);
        TextView tv_Address = dialog.findViewById(R.id.tv_Address);

        String order_id = "<b>" + "Order Id :- " + "</b> " + datumList.get(pos).getId();
        String order_name = "<b>" + "Order Name :- " + "</b> " + datumList.get(pos).getProducts().get(0).getProduct().getTitle();
        String name = "<b>" + "Customer Name :- " + "</b> " + datumList.get(pos).getShippingDetails().getName();
        String Mobile = "<b>" + "Mobile No :- " + "</b> " + datumList.get(pos).getShippingDetails().getContacts().toString();
        String Address = "<b>" + "Address :- " + "</b> " + datumList.get(pos).getShippingDetails().getAddressDetails().getHouse() + ", "
                + datumList.get(pos).getShippingDetails().getAddressDetails().getStreet() + ", " +
                datumList.get(pos).getShippingDetails().getAddressDetails().getCity() + ", " +
                datumList.get(pos).getShippingDetails().getAddressDetails().getZip();

        tv_order_id.setText(Html.fromHtml(order_id));
        tv_order_name.setText(Html.fromHtml(order_name));
        tv_name.setText(Html.fromHtml(name));
        tv_Mobile.setText(Html.fromHtml(Mobile));
        tv_Address.setText(Html.fromHtml(Address));

        btn_allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.isConnectingToInternet(context)) {
                    accept_request_api(datumList.get(pos).getId(), dialog, pos);
                } else {
                    Toast.makeText(context, "Please Connect Internet !", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        btn_dont_allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vDelivery_Address = datumList.get(pos).getShippingDetails().getAddressDetails().getHouse() + " " +
                        datumList.get(pos).getShippingDetails().getAddressDetails().getStreet() + " " +
                        datumList.get(pos).getShippingDetails().getAddressDetails().getCity() + " " +
                        datumList.get(pos).getShippingDetails().getAddressDetails().getLocality() + " " +
                        datumList.get(pos).getShippingDetails().getAddressDetails().getState() + " " +
                        datumList.get(pos).getShippingDetails().getAddressDetails().getCountry() + " " +
                        datumList.get(pos).getShippingDetails().getAddressDetails().getZip();
                vPickupAddress = datumList.get(pos).getWarehouseId().getLocation().getAddress() + " " +
                        datumList.get(pos).getWarehouseId().getLocation().getCity() + " " +
                        datumList.get(pos).getWarehouseId().getLocation().getLocality() + " " +
                        datumList.get(pos).getWarehouseId().getLocation().getState() + " " +
                        datumList.get(pos).getWarehouseId().getLocation().getZip();

                if (Utility.isConnectingToInternet(context)) {
                    Intent intent2 = new Intent(context, Reject_Request_Activity.class);
                    intent2.putExtra("order_id", datumList.get(pos).getId());
                    intent2.putExtra("delivery_address", vDelivery_Address);
                    intent2.putExtra("user_contact", datumList.get(pos).getShippingDetails().getContacts().toString());
                    intent2.putExtra("user_name", datumList.get(pos).getShippingDetails().getName());
                    intent2.putExtra("pickup_address", vPickupAddress);
                    context.startActivity(intent2);


                   // reject_request_api(datumList.get(pos).getId(), dialog, pos);
                } else {
                    Toast.makeText(context, "Please Connect Internet !", Toast.LENGTH_SHORT).show();
                }


            }
        });
        dialog.show();
    }

    //////request reject by driver
//    private void reject_request_api(String id, Dialog dialog1, int pos) {
//        ProgressDialog dialog = new ProgressDialog(context);
//        dialog.setMessage("Please wait..");
//        dialog.setCancelable(false);
//        dialog.create();
//        dialog.show();
//        JsonObject jsonObject = new JsonObject();
//        try {
//            jsonObject.addProperty("reject_reason", "reject request txt");
//            System.out.println("jsonObject=request fast boat at home=" + jsonObject);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        final Call<RejectDto> call = deliveryApi.api_reject_request(sessionManager.getDeviceToken(), id, "reject", jsonObject);
//        call.enqueue(new Callback<RejectDto>() {
//            @Override
//            public void onResponse(Call<RejectDto> call, Response<RejectDto> response) {
//                if (response.isSuccessful()) {
//                    dialog.dismiss();
//                    dialog1.dismiss();
//                    RejectDto requestDto = response.body();
//                    if (requestDto.getCode() == 200) {
//                        dialog1.dismiss();
//                        Toast.makeText(context, requestDto.getMsg().toString(), Toast.LENGTH_SHORT).show();
//
//                        if (home_fragment != null) {
//                            ///refresh api
//                            if (Utility.isConnectingToInternet(context)) {
//                                home_fragment.getordersfromApi();
//
//                            } else {
//                                Toast.makeText(context, "Please Connect Internet !", Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                        notifyDataSetChanged();
//                    } else {
//                        dialog.dismiss();
//                        dialog1.dismiss();
//                        Toast.makeText(context, requestDto.getMsg().toString(), Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    dialog.dismiss();
//                    dialog1.dismiss();
//                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
//
//                }
//                //  swipeRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onFailure(Call<RejectDto> call, Throwable t) {
//                dialog.dismiss();
//                dialog1.dismiss();
//                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
//                //  swipeRefreshLayout.setRefreshing(false);
//            }
//        });
//    }


    //////request accept by driver
    private void accept_request_api(String id, Dialog dialog1, int pos) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();

        final Call<RejectDto> call = deliveryApi.api_accept_request(sessionManager.getDeviceToken(), id, "reject");
        call.enqueue(new Callback<RejectDto>() {
            @Override
            public void onResponse(Call<RejectDto> call, Response<RejectDto> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    dialog1.dismiss();
                    RejectDto requestDto = response.body();
                    if (requestDto.getCode() == 200) {
                        dialog1.dismiss();
                        Toast.makeText(context, requestDto.getMsg().toString(), Toast.LENGTH_SHORT).show();
                        if (Home_Fragment.home_fragment != null) {
                            ///refresh api
                            if (Utility.isConnectingToInternet(context)) {
                                Home_Fragment.home_fragment.getordersfromApi();

                            } else {
                                Toast.makeText(context, "Please Connect Internet !", Toast.LENGTH_SHORT).show();
                            }

                        }

                    } else {
                        dialog.dismiss();
                        dialog1.dismiss();
                        Toast.makeText(context, requestDto.getMsg().toString(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    dialog.dismiss();
                    dialog1.dismiss();
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

                }
                //  swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RejectDto> call, Throwable t) {
                dialog.dismiss();
                dialog1.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                //  swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}
