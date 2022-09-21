package com.aimplatfarm.aimplatfarmdelivery.Models.RejectDto;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Signature implements Serializable
{

@SerializedName("hash")
@Expose
private String hash;
@SerializedName("keyId")
@Expose
private String keyId;
private final static long serialVersionUID = -5135942174780886509L;

public String getHash() {
return hash;
}

public void setHash(String hash) {
this.hash = hash;
}

public String getKeyId() {
return keyId;
}

public void setKeyId(String keyId) {
this.keyId = keyId;
}

}