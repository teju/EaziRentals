package eazi.com.eazirentals.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tejaswini on 23/05/2019.
 */

public class Tariff {


    @SerializedName("bike_brand")
    @Expose
    private String bike_brand;

    @SerializedName("bike_name")
    @Expose
    private String bike_name;


    @SerializedName("tariff_per_hour_mon_fri")
    @Expose
    private String tariff_per_hour_mon_fri;


    @SerializedName("tariff_per_day_mon_fri")
    @Expose
    private String tariff_per_day_mon_fri;


    public String getBike_brand() {
        return bike_brand;
    }

    public void setBike_brand(String bike_brand) {
        this.bike_brand = bike_brand;
    }

    public String getBike_name() {
        return bike_name;
    }

    public void setBike_name(String bike_name) {
        this.bike_name = bike_name;
    }

    public String getTariff_per_hour_mon_fri() {
        return tariff_per_hour_mon_fri;
    }

    public void setTariff_per_hour_mon_fri(String tariff_per_hour_mon_fri) {
        this.tariff_per_hour_mon_fri = tariff_per_hour_mon_fri;
    }

    public String getTariff_per_day_mon_fri() {
        return tariff_per_day_mon_fri;
    }

    public void setTariff_per_day_mon_fri(String tariff_per_day_mon_fri) {
        this.tariff_per_day_mon_fri = tariff_per_day_mon_fri;
    }

    public String getTariff_per_day_sat_sun() {
        return tariff_per_day_sat_sun;
    }

    public void setTariff_per_day_sat_sun(String tariff_per_day_sat_sun) {
        this.tariff_per_day_sat_sun = tariff_per_day_sat_sun;
    }

    @SerializedName("tariff_per_day_sat_sun")
    @Expose
    private String tariff_per_day_sat_sun;



}
