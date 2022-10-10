package com.aimplatfarm.aimplatfarmdelivery.WebServices;

import com.aimplatfarm.aimplatfarmdelivery.Models.EditProfile;
import com.aimplatfarm.aimplatfarmdelivery.Models.Password;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.Example_ex;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.RequestDto;
import com.aimplatfarm.aimplatfarmdelivery.Models.ApiResponse;

import com.aimplatfarm.aimplatfarmdelivery.Models.DriverDetails;
import com.aimplatfarm.aimplatfarmdelivery.Models.ImageResponse;
import com.aimplatfarm.aimplatfarmdelivery.Models.Location;
import com.aimplatfarm.aimplatfarmdelivery.Models.RejectDto.RejectDto;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface DeliveryApi {

    @POST("auth/register")
    Call<ApiResponse> registerUser(@Body DriverDetails agent);

    @PUT("auth/register/verify-otp")
    Call<ApiResponse> verifyRegisterOtp(@Body DriverDetails agent);

    @POST("auth/register/resend-otp")
    Call<ApiResponse> resendOtp(@Body DriverDetails agent);

    @POST("auth/login")
    Call<ApiResponse> loginUser(@Body DriverDetails agent);

    @POST("auth/forgot-password")
    Call<ApiResponse> forgotPasswordOtp(@Body DriverDetails agent);

    @PUT("auth/register/verify-otp")
    Call<ApiResponse> verifyForgetPasswordOtp(@Body DriverDetails agent);

    @POST("auth/set-password")
    Call<ApiResponse> changePassword(@Body Password password);

    @POST("auth/register/resend-otp")
    Call<ApiResponse> resendPasswordOtp(@Body DriverDetails agent);


    @GET("order/delivery/home-statistics")
    Call<ApiResponse> api_home_statistics(@Header("auth-token") String token);

    @POST("check_status")
    Call<ApiResponse> api_check_status(@Header("auth-token") String token);

    // getting th delivery notifications
    @GET("notifications")
    Call<ApiResponse> getNotifications(@Header("auth-token") String token);


    // get profile
    @GET("profile")
    Call<ApiResponse> getProfile(@Header("auth-token") String token);

    // edit profile
    @Headers("Content-Type: application/json")
    @PUT("auth/edit")
    Call<Example_ex> editProfile(@Header("auth-token") String token, @Body JSONObject editProfile);

    // toggle status
    @PUT("status/update")
    Call<ApiResponse> changeStatus(@Header("auth-token") String token, @Body DriverDetails statusHolder);


    // image upload
    @Multipart
    @POST("vendor/product/image/upload")
    Call<ImageResponse> uploadImage(@Header("auth-token") String token, @Part MultipartBody.Part photo);

    // set current location
    @PUT("order/delivery/set-agent-current-location")
    Call<ApiResponse> updateLocation(@Header("auth-token") String token, @Body Location location);

    ////get all requests
    @GET("order/delivery/order/all")
    Call<RequestDto> getAllRequest(@Header("auth-token") String token);

    // order Reject
    @POST("order/delivery/order/reject/{id}/{status}")
    Call<RejectDto> api_reject_request(@Header("auth-token") String token, @Path("id") String id, @Path("status") String status, @Body JsonObject body);

    // order photo upload
    @Multipart
    @POST("order/delivery/upload/photo")
    Call<ApiResponse> api_upload_photo_order_pickup(@Header("auth-token") String token, @Part MultipartBody.Part part);

    // order cancel
    @POST("order/delivery/order/cancel/{id}/{status}")
    Call<RejectDto> api_cancel_request(@Header("auth-token") String token, @Path("id") String id, @Path("status") String status, @Body JsonObject body);

    // order accept
    @POST("order/delivery/order/accept/{id}/{status}")
    Call<RejectDto> api_accept_request(@Header("auth-token") String token, @Path("id") String id, @Path("status") String status);

    ////get all accept requests
    @GET("order/delivery/order/accpet_order")
    Call<RequestDto> getAll_Accept_Request(@Header("auth-token") String token);

    // order picked
    @POST("order/delivery/order/picked/{id}/{status}")
    Call<RequestDto> api_order_picked(@Header("auth-token") String token, @Path("id") String id, @Path("status") String status, @Body JsonObject body);

    ////get all delivered requests
    @PUT("order/delivery/order/status/delivered")
    Call<RequestDto> api_delivered_request(@Header("auth-token") String token, @Body JsonObject body);


    ////get all history
    @GET("order/delivery/order/history")
    Call<RequestDto> api_history(@Header("auth-token") String token);

    ////get all history details
    @GET("order/delivery/details/{orderId}")
    Call<ApiResponse> api_order_history_details(@Header("auth-token") String token, @Path("orderId") String orderId);
}
