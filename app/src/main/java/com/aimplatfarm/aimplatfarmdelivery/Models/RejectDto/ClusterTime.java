package com.aimplatfarm.aimplatfarmdelivery.Models.RejectDto;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClusterTime implements Serializable
{

@SerializedName("clusterTime")
@Expose
private String clusterTime;
@SerializedName("signature")
@Expose
private Signature signature;
private final static long serialVersionUID = -2323628752574916102L;

public String getClusterTime() {
return clusterTime;
}

public void setClusterTime(String clusterTime) {
this.clusterTime = clusterTime;
}

public Signature getSignature() {
return signature;
}

public void setSignature(Signature signature) {
this.signature = signature;
}

}