package com.aimplatfarm.aimplatfarmdelivery.Models.RejectDto;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RejectDto implements Serializable
{

@SerializedName("code")
@Expose
private Integer code;
@SerializedName("err")
@Expose
private Boolean err;
@SerializedName("msg")
@Expose
private String msg;
@SerializedName("data")
@Expose
private RejectData data;
private final static long serialVersionUID = -1483741015351406991L;

public Integer getCode() {
return code;
}

public void setCode(Integer code) {
this.code = code;
}

public Boolean getErr() {
return err;
}

public void setErr(Boolean err) {
this.err = err;
}

public String getMsg() {
return msg;
}

public void setMsg(String msg) {
this.msg = msg;
}

public RejectData getData() {
return data;
}

public void setData(RejectData data) {
this.data = data;
}

}