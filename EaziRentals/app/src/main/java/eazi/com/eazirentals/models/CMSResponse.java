package eazi.com.eazirentals.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tejaswinin on 25/12/19.
 */

public class CMSResponse {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAbout_us() {
        return about_us;
    }

    public void setAbout_us(String about_us) {
        this.about_us = about_us;
    }

    @SerializedName("status")
    @Expose
    private String status;


    @SerializedName("about_us")
    @Expose
    private String about_us;

    public String getTerms_condition() {
        return terms_condition;
    }

    public void setTerms_condition(String terms_condition) {
        this.terms_condition = terms_condition;
    }

    @SerializedName("terms_condition")
    @Expose
    private String terms_condition;

}
