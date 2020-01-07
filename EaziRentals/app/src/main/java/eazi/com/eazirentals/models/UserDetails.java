package eazi.com.eazirentals.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tejaswini on 23/05/2019.
 */

public class UserDetails {


    @SerializedName("user_id")
    @Expose
    private String user_id;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    @SerializedName("access_token")
    @Expose
    private String access_token;


    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email_id")
    @Expose
    private String email_id;

    @SerializedName("driving_no")
    @Expose
    private String driving_no;


    @SerializedName("address")
    @Expose
    private String address;


    @SerializedName("city")
    @Expose
    private String city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriving_no() {
        return driving_no;
    }

    public void setDriving_no(String driving_no) {
        this.driving_no = driving_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @SerializedName("state")
    @Expose
    private String state;

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    @SerializedName("mobile_no")
    @Expose
    private String mobile_no;


}
