package com.aimplatfarm.aimplatfarmdelivery.Models.requestDto;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackingDetails implements Serializable
{

@SerializedName("reject")
@Expose
private Boolean reject;
@SerializedName("reject_reason")
@Expose
private String rejectReason;
@SerializedName("ordered")
@Expose
private String ordered;
@SerializedName("packed")
@Expose
private Boolean packed;
@SerializedName("shipped")
@Expose
private Boolean shipped;
@SerializedName("delivered")
@Expose
private Boolean delivered;
@SerializedName("return")
@Expose
private Boolean _return;
@SerializedName("replaced")
@Expose
private Boolean replaced;
@SerializedName("cancel")
@Expose
private Boolean cancel;
@SerializedName("cancel_reason")
@Expose
private String cancelReason;
private final static long serialVersionUID = 4244318524234523099L;

public Boolean getReject() {
return reject;
}

public void setReject(Boolean reject) {
this.reject = reject;
}

public String getRejectReason() {
return rejectReason;
}

public void setRejectReason(String rejectReason) {
this.rejectReason = rejectReason;
}

public String getOrdered() {
return ordered;
}

public void setOrdered(String ordered) {
this.ordered = ordered;
}

public Boolean getPacked() {
return packed;
}

public void setPacked(Boolean packed) {
this.packed = packed;
}

public Boolean getShipped() {
return shipped;
}

public void setShipped(Boolean shipped) {
this.shipped = shipped;
}

public Boolean getDelivered() {
return delivered;
}

public void setDelivered(Boolean delivered) {
this.delivered = delivered;
}

public Boolean getReturn() {
return _return;
}

public void setReturn(Boolean _return) {
this._return = _return;
}

public Boolean getReplaced() {
return replaced;
}

public void setReplaced(Boolean replaced) {
this.replaced = replaced;
}

public Boolean getCancel() {
return cancel;
}

public void setCancel(Boolean cancel) {
this.cancel = cancel;
}

public String getCancelReason() {
return cancelReason;
}

public void setCancelReason(String cancelReason) {
this.cancelReason = cancelReason;
}

}