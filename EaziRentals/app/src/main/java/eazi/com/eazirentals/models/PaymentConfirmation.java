package eazi.com.eazirentals.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tejaswini on 23/05/2019.
 */

public class PaymentConfirmation {


    public PaymentConfirmationDetails getPayment_info() {
        return payment_info;
    }

    public void setPayment_info(PaymentConfirmationDetails payment_info) {
        this.payment_info = payment_info;
    }

    @SerializedName("payment_info")
    @Expose
    private PaymentConfirmationDetails payment_info;
}
