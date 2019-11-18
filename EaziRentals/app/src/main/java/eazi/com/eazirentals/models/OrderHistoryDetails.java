package eazi.com.eazirentals.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tejaswini on 23/05/2019.
 */

public class OrderHistoryDetails {


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<OrderList> getOrder_list() {
        return order_list;
    }

    public void setOrder_list(List<OrderList> order_list) {
        this.order_list = order_list;
    }

    @SerializedName("order_list")
    @Expose
    private List<OrderList> order_list;
}
