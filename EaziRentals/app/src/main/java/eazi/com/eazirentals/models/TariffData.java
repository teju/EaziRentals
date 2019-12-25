package eazi.com.eazirentals.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tejaswini on 23/05/2019.
 */

public class TariffData {


    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public List<Tariff> getTariff_data() {
        return tariff_data;
    }

    public void setTariff_data(List<Tariff> tariff_data) {
        this.tariff_data = tariff_data;
    }

    @SerializedName("tariff-data")
    @Expose
    private List<Tariff> tariff_data;




}
