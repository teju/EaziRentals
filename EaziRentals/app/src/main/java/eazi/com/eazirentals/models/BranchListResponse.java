package eazi.com.eazirentals.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tejaswini on 23/05/2019.
 */

public class BranchListResponse {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    @Expose
    private String status;

    public List<Branch> getBike_list() {
        return branch_list;
    }

    public void setBike_list(List<Branch> bike_list) {
        this.branch_list = bike_list;
    }

    @SerializedName("branch_list")
    @Expose
    private List<Branch> branch_list =new ArrayList<>();




}
