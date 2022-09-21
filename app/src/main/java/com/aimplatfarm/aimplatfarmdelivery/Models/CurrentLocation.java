package com.aimplatfarm.aimplatfarmdelivery.Models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentLocation implements Serializable
{

@SerializedName("longitude")
@Expose
private String longitude;
@SerializedName("latitude")
@Expose
private String latitude;
private final static long serialVersionUID = -7355219355176255709L;

public String getLongitude() {
return longitude;
}

public void setLongitude(String longitude) {
this.longitude = longitude;
}

public String getLatitude() {
return latitude;
}

public void setLatitude(String latitude) {
this.latitude = latitude;
}

}
