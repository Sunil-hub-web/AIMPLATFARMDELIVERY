package com.aimplatfarm.aimplatfarmdelivery.Models.RejectDto;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RejectData implements Serializable {

    @SerializedName("n")
    @Expose
    private Integer n;
    @SerializedName("nModified")
    @Expose
    private Integer nModified;
    @SerializedName("opTime")
    @Expose
    private OpTime opTime;
    @SerializedName("electionId")
    @Expose
    private String electionId;
    @SerializedName("ok")
    @Expose
    private Integer ok;
    @SerializedName("$clusterTime")
    @Expose
    private ClusterTime $clusterTime;
    @SerializedName("operationTime")
    @Expose
    private String operationTime;
    private final static long serialVersionUID = -7067238356862367762L;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Integer getnModified() {
        return nModified;
    }

    public void setnModified(Integer nModified) {
        this.nModified = nModified;
    }

    public OpTime getOpTime() {
        return opTime;
    }

    public void setOpTime(OpTime opTime) {
        this.opTime = opTime;
    }

    public String getElectionId() {
        return electionId;
    }

    public void setElectionId(String electionId) {
        this.electionId = electionId;
    }

    public Integer getOk() {
        return ok;
    }

    public void setOk(Integer ok) {
        this.ok = ok;
    }

    public ClusterTime get$clusterTime() {
        return $clusterTime;
    }

    public void set$clusterTime(ClusterTime $clusterTime) {
        this.$clusterTime = $clusterTime;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

}