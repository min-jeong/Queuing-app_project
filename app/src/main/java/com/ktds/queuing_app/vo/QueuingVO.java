package com.ktds.queuing_app.vo;

/**
 * Created by 206-020 on 2016-07-13.
 */
public class QueuingVO {

    private int _id;
    private String regId;
    private String branchId;
    private String token;
    private String regState;
    private boolean isCancel;
    private String doCancel;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRegState() {
        return regState;
    }

    public void setRegState(String regState) {
        this.regState = regState;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public String getDoCancel() {
        return doCancel;
    }

    public void setDoCancel(String doCancel) {
        this.doCancel = doCancel;
    }
}
