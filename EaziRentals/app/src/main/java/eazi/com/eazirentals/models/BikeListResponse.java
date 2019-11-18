package eazi.com.eazirentals.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tejaswini on 23/05/2019.
 */

public class BikeListResponse {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    @Expose
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    @Expose
    private String message;


    public List<BikeList> getBike_list() {
        return bike_list;
    }

    public void setBike_list(List<BikeList> bike_list) {
        this.bike_list = bike_list;
    }

    @SerializedName("bike_list")
    @Expose
    private List<BikeList> bike_list =new ArrayList<>();




}
