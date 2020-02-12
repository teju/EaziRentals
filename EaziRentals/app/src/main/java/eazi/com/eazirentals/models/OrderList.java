package eazi.com.eazirentals.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tejaswini on 23/05/2019.
 */

public class OrderList {




    @SerializedName("booking_date")
    @Expose
    private String booking_date;

    @SerializedName("pickup_date")
    @Expose
    private String pickup_date;

    @SerializedName("dropup_date")
    @Expose
    private String dropup_date;

    @SerializedName("rider_name")
    @Expose
    private String rider_name;

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    @SerializedName("booking_id")
    @Expose
    private String booking_id;

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    @SerializedName("invoice_id")
    @Expose
    private String invoice_id;

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getPickup_date() {
        return pickup_date;
    }

    public void setPickup_date(String pickup_date) {
        this.pickup_date = pickup_date;
    }

    public String getDropup_date() {
        return dropup_date;
    }

    public void setDropup_date(String dropup_date) {
        this.dropup_date = dropup_date;
    }

    public String getRider_name() {
        return rider_name;
    }

    public void setRider_name(String rider_name) {
        this.rider_name = rider_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    @Expose
    private String status;
}
