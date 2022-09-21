package com.aimplatfarm.aimplatfarmdelivery.WebServices;

import android.util.Log;

import com.aimplatfarm.aimplatfarmdelivery.Models.Password;
import com.aimplatfarm.aimplatfarmdelivery.Models.ApiResponse;

import com.aimplatfarm.aimplatfarmdelivery.Models.DriverDetails;
import com.aimplatfarm.aimplatfarmdelivery.Models.ImageResponse;
import com.aimplatfarm.aimplatfarmdelivery.Models.Location;

import com.aimplatfarm.aimplatfarmdelivery.Models.RejectDto.RejectDto;
import com.aimplatfarm.aimplatfarmdelivery.Models.requestDto.RequestDto;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeliveryApiToJson {
    private static Retrofit retrofit1 = null;
    public static String BASE_URL = "https://kisaanandfactory.com/api/v1/deliveryapp/";

    public static OkHttpClient.Builder getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<LatLng> polylinelist;
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            // .baseUrl("http://kisaanandfactory.com/api/v1/deliveryapp/")
            //  .baseUrl("http://kisan.aimplatfarm.com/api/v1/deliveryapp/")
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getUnsafeOkHttpClient().build())
            .build();

    DeliveryApi deliveryApi = retrofit.create(DeliveryApi.class);

    public static Retrofit getClient() {
        if (retrofit1 == null) {
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.connectTimeout(15, TimeUnit.SECONDS);
            client.readTimeout(15, TimeUnit.SECONDS);
            client.writeTimeout(15, TimeUnit.SECONDS);
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit1 = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client.build())
                    .build()
            ;
        }
        return retrofit1;
    }

    private List<LatLng> decodePoly(String encoded) {

        Log.i("Location", "String received: " + encoded);
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((int) (((double) lat / 1E5) * 1E6), (int) (((double) lng / 1E5 * 1E6)));
            poly.add(p);
        }

        for (int i = 0; i < poly.size(); i++) {
            Log.i("Location", "Point sent: Latitude: " + poly.get(i).latitude + " Longitude: " + poly.get(i).longitude);
        }
        return poly;
    }


    public Call<ApiResponse> api_home_statistics(String token) {
        return deliveryApi.api_home_statistics(token);
    }

    public Call<ApiResponse> api_check_status(String token) {
        return deliveryApi.api_check_status(token);
    }

    public Call<ApiResponse> registerUser(DriverDetails agent) {
        return deliveryApi.registerUser(agent);
    }

    public Call<ApiResponse> loginUser(DriverDetails agent) {
        return deliveryApi.loginUser(agent);
    }

    // forgot password request otp
    public Call<ApiResponse> forgotPasswordOtp(DriverDetails driverDetails){
        return deliveryApi.forgotPasswordOtp(driverDetails);
    }
    // verify forgot password otp
    public Call<ApiResponse> verifyForgotPassword(DriverDetails customer){
        return deliveryApi.verifyForgetPasswordOtp(customer);
    }
    // reset customer otp
    public Call<ApiResponse> resendPasswordOtp(DriverDetails customer){
        return deliveryApi.resendPasswordOtp(customer);
    }
    // change the password
    public Call<ApiResponse> changePassword(Password password){
        return deliveryApi.changePassword(password);
    }
    public Call<ApiResponse> verifyRgisterOtp(DriverDetails agent) {
        return deliveryApi.verifyRegisterOtp(agent);
    }

    public Call<ApiResponse> resendOtp(DriverDetails agent) {
        return deliveryApi.resendOtp(agent);
    }


    // get notifications
    public Call<ApiResponse> getNotifications(String token) {
        return deliveryApi.getNotifications(token);
    }

    // upload Image
    public Call<ApiResponse> api_upload_photo_order_pickup(String token, MultipartBody.Part photo) {
        return deliveryApi.api_upload_photo_order_pickup(token, photo);
    }

    // get all requests
    public Call<RequestDto> getAllRequest(String token) {
        return deliveryApi.getAllRequest(token);
    }

    ////get all accept requests
    public Call<RequestDto> getAll_Accept_Request(String token) {
        return deliveryApi.getAll_Accept_Request(token);
    }

    ////reject requests
    public Call<RejectDto> api_reject_request(String token, String id, String status, JsonObject body) {
        return deliveryApi.api_reject_request(token, id, status, body);
    }

    ////cancel requests
    public Call<RejectDto> api_cancel_request(String token, String id, String status, JsonObject body) {
        return deliveryApi.api_cancel_request(token, id, status, body);
    }

    ////pickup requests
    public Call<RequestDto> api_order_picked(String token, String id, String status, JsonObject body) {
        return deliveryApi.api_order_picked(token, id, status, body);
    }

    ////get  delivered requests
    public Call<RequestDto> api_delivered_request(String token, JsonObject body) {
        return deliveryApi.api_delivered_request(token, body);
    }

    ////get all history
    public Call<RequestDto> api_history(String token) {
        return deliveryApi.api_history(token);
    }

    ////get  history details
    public Call<ApiResponse> api_order_history_details(String token, String order_id) {
        return deliveryApi.api_order_history_details(token, order_id);
    }


    // get profile
    public Call<ApiResponse> getProfile(String token) {
        return deliveryApi.getProfile(token);
    }

    // change stauts
    public Call<ApiResponse> changeStatus(String token, DriverDetails agent) {
        return deliveryApi.changeStatus(token, agent);
    }


    public Call<ImageResponse> uploadImage(String token, MultipartBody.Part photo) {
        return new Retrofit.Builder()
                .baseUrl("http://kisaanandfactory.com/api/v1/vendorapp/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient().build())
                .build().create(DeliveryApi.class).uploadImage(token, photo);
    }


    // update location
    public Call<ApiResponse> updateLocation(String token, Location location) {
        return deliveryApi.updateLocation(token, location);
    }


}
