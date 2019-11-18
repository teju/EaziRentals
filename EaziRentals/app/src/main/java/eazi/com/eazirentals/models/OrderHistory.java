package eazi.com.eazirentals.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tejaswini on 23/05/2019.
 */

public class OrderHistory {


    public OrderHistoryDetails getOrder_list() {
        return order_list;
    }

    public void setOrder_list(OrderHistoryDetails order_list) {
        this.order_list = order_list;
    }

    @SerializedName("order_list")
    @Expose
    private OrderHistoryDetails order_list;
}
