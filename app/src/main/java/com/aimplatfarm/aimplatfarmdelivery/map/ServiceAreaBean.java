package com.aimplatfarm.aimplatfarmdelivery.map;

import java.util.ArrayList;

public class ServiceAreaBean {

    String city = "";
    String country = "";
    String crated = "";
    String service_area_id = "";
    String service_area_name = "";
    String state = "";


    public ArrayList<ServiceAreaDetail> getService_area_detail() {
        return service_area_detail;
    }

    public void setService_area_detail(ArrayList<ServiceAreaDetail> service_area_detail) {
        this.service_area_detail = service_area_detail;
    }

    ArrayList<ServiceAreaDetail> service_area_detail = new ArrayList<>();

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCrated() {
        return crated;
    }

    public void setCrated(String crated) {
        this.crated = crated;
    }

    public String getService_area_id() {
        return service_area_id;
    }

    public void setService_area_id(String service_area_id) {
        this.service_area_id = service_area_id;
    }

    public String getService_area_name() {
        return service_area_name;
    }

    public void setService_area_name(String service_area_name) {
        this.service_area_name = service_area_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
