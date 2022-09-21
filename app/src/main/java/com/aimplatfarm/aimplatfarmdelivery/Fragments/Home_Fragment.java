package com.aimplatfarm.aimplatfarmdelivery.Fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aimplatfarm.aimplatfarmdelivery.Utility;
import com.aimplatfarm.aimplatfarmdelivery.map.UpdateLocationService;
import com.aimplatfarm.aimplatfarmdelivery.Adapter.AllAcceptOrdersListAdapter;
import com.aimplatfarm.aimplatfarmdelivery.Adapter.OrdersListAdapter;
import com.aimplatfarm.aimplatfarmdelivery.Models.ApiResponse;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.Datum;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.RequestDto;
import com.aimplatfarm.aimplatfarmdelivery.R;

import com.aimplatfarm.aimplatfarmdelivery.WebServices.DeliveryApiToJson;
import com.aimplatfarm.aimplatfarmdelivery.session_manager.SessionManager;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home_Fragment extends Fragment {

    View rootView;
    RelativeLayout rl_3;
    LocationManager locationManager;
    Fragment map_fragment;
    ScrollView home_view;
    LinearLayout ll_fragment;
    public double lat = 0.0;
    public double longi = 0.0;
    private RecyclerView orderslist;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SessionManager sessionManager;
    private ArrayList<Datum> datumArrayList;
    public static Home_Fragment home_fragment;
    private OrdersListAdapter adapter;
    AllAcceptOrdersListAdapter allAcceptOrdersListAdapter;
    ArrayList<Datum> rejectDtos;
    private RelativeLayout rl_accept_requests_list;
    Dialog acceptdialog;
    private TextView tv_driver_name, tv_total_earning, tv_total_shipment, tv_total_success, tv_total_declined, tv_total_pickup;
    MapsFragment mapsFragment;
    private LinearLayout ll_pickup_request;
    CircleImageView profile_image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        home_fragment = this;

        rootView = inflater.inflate(R.layout.fragment_home_, container, false);
        sessionManager = new SessionManager(getActivity());
        startServiceForLocationUpdateNew();

        rl_3 = rootView.findViewById(R.id.rl_3);
        home_view = rootView.findViewById(R.id.home_view);
        ll_fragment = rootView.findViewById(R.id.ll_fragment);
        orderslist = rootView.findViewById(R.id.orderslist);
        rl_accept_requests_list = rootView.findViewById(R.id.rl_accept_requests_list);
        tv_driver_name = rootView.findViewById(R.id.tv_driver_name);
        tv_total_earning = rootView.findViewById(R.id.tv_total_earning);
        tv_total_shipment = rootView.findViewById(R.id.tv_total_shipment);
        tv_total_success = rootView.findViewById(R.id.tv_total_success);
        tv_total_declined = rootView.findViewById(R.id.tv_total_declined);
        tv_total_pickup = rootView.findViewById(R.id.tv_total_pickup);
        ll_pickup_request = rootView.findViewById(R.id.ll_pickup_request);
        profile_image = rootView.findViewById(R.id.profile_image);

        tv_driver_name.setText(sessionManager.getName() != null ? sessionManager.getName().toString() : "");
        //  swipeRefreshLayout = rootView.findViewById(R.id.swipeLayout);
        //  swipeRefreshLayout.setOnRefreshListener(this::getordersfromApi);
        rl_accept_requests_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                All_accept_request_Dialog();
            }
        });

        if (Utility.isConnectingToInternet(getActivity())) {
            api_home_statistics();
            getordersfromApi();

        } else {
            Toast.makeText(getActivity(), "Please Connect Internet !", Toast.LENGTH_SHORT).show();
        }

//        Picasso.with(getActivity())
//                .load(R.drawable.kissan_logo_circle) //Load the image
//                .into(profile_image);

        return rootView;
    }

    ////////all request from server
    public void getordersfromApi() {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait..");
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
        datumArrayList = new ArrayList<>();
        adapter = new OrdersListAdapter(getActivity());

        Call<RequestDto> call = new DeliveryApiToJson().getAllRequest(sessionManager.getDeviceToken());
        call.enqueue(new Callback<RequestDto>() {
            @Override
            public void onResponse(Call<RequestDto> call, Response<RequestDto> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    RequestDto requestDto = response.body();
                    if (requestDto.getCode() == 200) {
                        ll_pickup_request.setVisibility(View.VISIBLE);

                        if (requestDto.getData() != null) {
                            if (datumArrayList != null) {
                                datumArrayList.clear();

                                datumArrayList.addAll(requestDto.getData());
                            }
                            Collections.reverse(datumArrayList);
                            adapter.setrequestslist(datumArrayList);
                            orderslist.setAdapter(adapter);
                            orderslist.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                            orderslist.setHasFixedSize(false);
                            orderslist.setNestedScrollingEnabled(true);
                            adapter.notifyDataSetChanged();
                        } else {
                            ll_pickup_request.setVisibility(View.GONE);
                            if (datumArrayList != null) {
                                datumArrayList.clear();
                                adapter.setrequestslist(datumArrayList);
                                orderslist.setAdapter(adapter);
                                orderslist.setLayoutManager(new LinearLayoutManager(getActivity()));
                                orderslist.setHasFixedSize(false);
                                adapter.notifyDataSetChanged();
                                //   Toast.makeText(getActivity(), requestDto.getMsg().toString(), Toast.LENGTH_SHORT).show();

                            }
                        }


                    } else {
                        ll_pickup_request.setVisibility(View.GONE);
                        dialog.dismiss();
                        //  Toast.makeText(getActivity(), requestDto.getMsg().toString(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();

                }
                //  swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RequestDto> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                //  swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    //////show all accepted requests
    public void All_accept_request_Dialog() {
        acceptdialog = new Dialog(getActivity());
        acceptdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(acceptdialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        acceptdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        acceptdialog.setCancelable(true);
        acceptdialog.setContentView(R.layout.custom_dialog_all_accept_requests);
        ImageView btn_dont_allow = acceptdialog.findViewById(R.id.btn_dont_allow);
        RecyclerView rv_acceptorderslist = acceptdialog.findViewById(R.id.rv_acceptorderslist);
        ProgressBar progressBar1 = acceptdialog.findViewById(R.id.progressBar1);
        if (Utility.isConnectingToInternet(getActivity())) {
            api_all_accept_reqiests(rv_acceptorderslist, progressBar1);
        } else {
            Toast.makeText(getActivity(), "Please Connect Internet !", Toast.LENGTH_SHORT).show();
        }
        btn_dont_allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptdialog.cancel();
            }
        });
        acceptdialog.show();
    }

    //////show all accept request after accept
    private void api_all_accept_reqiests(RecyclerView rv_acceptorderslist, ProgressBar progressBar1) {

        progressBar1.setVisibility(View.VISIBLE);
        rejectDtos = new ArrayList<>();
        allAcceptOrdersListAdapter = new AllAcceptOrdersListAdapter(getActivity());

        Call<RequestDto> call = new DeliveryApiToJson().getAll_Accept_Request(sessionManager.getDeviceToken());
        call.enqueue(new Callback<RequestDto>() {
            @Override
            public void onResponse(Call<RequestDto> call, Response<RequestDto> response) {
                if (response.isSuccessful()) {
                    progressBar1.setVisibility(View.GONE);
                    RequestDto requestDto = response.body();
                    if (requestDto.getCode() == 200) {
                        if (requestDto.getData() != null) {
                            if (rejectDtos != null) {
                                rejectDtos.clear();
                                rejectDtos.addAll(requestDto.getData());
                            }
                            Collections.reverse(rejectDtos);
                            allAcceptOrdersListAdapter.setrequestslist1(rejectDtos);
                            rv_acceptorderslist.setAdapter(allAcceptOrdersListAdapter);
                            rv_acceptorderslist.setLayoutManager(new LinearLayoutManager(getActivity()));
                            allAcceptOrdersListAdapter.notifyDataSetChanged();
                        } else {
                            if (rejectDtos != null) {
                                rejectDtos.clear();
                                allAcceptOrdersListAdapter.setrequestslist1(rejectDtos);
                                rv_acceptorderslist.setAdapter(allAcceptOrdersListAdapter);
                                rv_acceptorderslist.setLayoutManager(new LinearLayoutManager(getActivity()));
                                allAcceptOrdersListAdapter.notifyDataSetChanged();
                                //  Toast.makeText(getActivity(), requestDto.getMsg().toString(), Toast.LENGTH_SHORT).show();

                            }
                        }


                    } else {
                        progressBar1.setVisibility(View.GONE);
                        //  Toast.makeText(getActivity(), requestDto.getMsg().toString(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();

                }
                //  swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<RequestDto> call, Throwable t) {
                progressBar1.setVisibility(View.GONE);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                //  swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void Adapter_redirection(Datum datum) {
        home_view.setVisibility(View.GONE);
        ll_fragment.setVisibility(View.VISIBLE);
        acceptdialog.dismiss();
        Bundle args = new Bundle();
        args.putSerializable("order_bundal", datum);
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack("map_fragment");
        mapsFragment = new MapsFragment();
        mapsFragment.setArguments(args);
        fragmentTransaction.replace(R.id._fragment, mapsFragment).commit();

    }

    public void startServiceForLocationUpdateNew() {

        getActivity().startService(new Intent(getActivity(), UpdateLocationService.class));

    }

    public void stopServiceForLocationUpdate() {
        if (UpdateLocationService.updateLocationService != null) {
            getActivity().stopService(new Intent(getActivity(), UpdateLocationService.class));

            UpdateLocationService.updateLocationService.stopSelf();
        }

    }

    //////get driver information about work
    public void api_home_statistics() {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        String token = sessionManager.getDeviceToken();


        Call<ApiResponse> call = new DeliveryApiToJson().api_home_statistics(token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    ApiResponse apiResponse = response.body();
                    tv_total_earning.setText("$ " + apiResponse.getData().getEarrning().toString() != null ? apiResponse.getData().getEarrning().toString() : "0");
                    tv_total_shipment.setText(apiResponse.getData().getShipped().toString() != null ? apiResponse.getData().getShipped().toString() : "0");
                    tv_total_success.setText(apiResponse.getData().getDelivered().toString() != null ? apiResponse.getData().getDelivered().toString() : "0");
                    tv_total_declined.setText(apiResponse.getData().getDeclined().toString() != null ? apiResponse.getData().getDeclined().toString() : "0");
                    tv_total_pickup.setText("0");

                } else {
                    dialog.dismiss();
                    Gson gson = new Gson();
                    ApiResponse message = gson.fromJson(response.errorBody().charStream(), ApiResponse.class);

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                dialog.dismiss();
            }
        });


    }


}