package eazi.com.eazirentals.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tejaswini on 23/05/2019.
 */

public class CouponCode {


    public CouponCodeDetails getCoupon_details() {
        return coupon_details;
    }

    public void setCoupon_details(CouponCodeDetails coupon_details) {
        this.coupon_details = coupon_details;
    }

    @SerializedName("coupon_details")
    @Expose
    private CouponCodeDetails coupon_details;
}
