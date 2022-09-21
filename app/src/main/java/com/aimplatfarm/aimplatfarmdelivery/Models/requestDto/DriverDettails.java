package com.aimplatfarm.aimplatfarmdelivery.Models.requestDto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DriverDettails  implements Serializable {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("_id")
    @Expose
    private String _id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
