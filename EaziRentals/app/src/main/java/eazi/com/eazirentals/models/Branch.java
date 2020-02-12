package eazi.com.eazirentals.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tejaswini on 23/05/2019.
 */

public class Branch {


    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("name")
    @Expose
    private String name;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @SerializedName("address")
    @Expose
    private String address;

    public String getBranch_google_map() {
        return branch_google_map;
    }

    public void setBranch_google_map(String branch_google_map) {
        this.branch_google_map = branch_google_map;
    }

    @SerializedName("branch_google_map")
    @Expose
    private String branch_google_map;



}
