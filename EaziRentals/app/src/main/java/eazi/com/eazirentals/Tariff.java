package eazi.com.eazirentals;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import eazi.com.eazirentals.api.APICalls;
import eazi.com.eazirentals.helper.ConstantStrings;
import eazi.com.eazirentals.helper.Constants;
import eazi.com.eazirentals.helper.CustomToast;
import eazi.com.eazirentals.helper.Loader;
import eazi.com.eazirentals.helper.SharedPreference;
import eazi.com.eazirentals.models.GeneralResult;
import eazi.com.eazirentals.models.TariffData;

public class Tariff extends AppCompatActivity {

    private TariffData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tariff);
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tariff.super.onBackPressed();
            }
        });
        sendContactUS();
    }

    public void setList(){
        LinearLayout tariff = (LinearLayout)findViewById(R.id.tariff);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tariff.removeAllViews();
        for (int i = 0; i < data.getTariff_data().size() ; i++) {
            View child = inflater.inflate(R.layout.tariff_item, null);
            eazi.com.eazirentals.models.Tariff tariff1 = data.getTariff_data().get(i);
            TextView bike_brand = (TextView)child.findViewById(R.id.bike_brand);
            TextView bike_name = (TextView)child.findViewById(R.id.bike_name);
            TextView tariff_per_hour = (TextView)child.findViewById(R.id.tariff_per_hour);
            TextView tariff_per_day = (TextView)child.findViewById(R.id.tariff_per_day);
            TextView tariff_per_day_weekend = (TextView)child.findViewById(R.id.tariff_per_day_weekend);

            bike_brand.setText(tariff1.getBike_brand());
            bike_name.setText(tariff1.getBike_name());
            tariff_per_hour.setText("\u20B9"+tariff1.getTariff_per_hour_mon_fri());
            tariff_per_day.setText("\u20B9"+tariff1.getTariff_per_day_mon_fri());
            tariff_per_day_weekend.setText("\u20B9"+tariff1.getTariff_per_day_sat_sun());
            child.setBackgroundColor(getResources().getColor(R.color.white));

            tariff.addView(child);
        }
    }

    public void sendContactUS() {
        try {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("user_id", SharedPreference.getString(Tariff.this, Constants.KEY_USER_ID)));
            params.add(new BasicNameValuePair("access_token",SharedPreference.getString(Tariff.this,Constants.KEY_ACCESS_TOKEN)));


            Loader.show(this);

            APICalls.invokeAPIEx(this,"Post",APICalls.SERVICE_URL+APICalls.TARIFF,params,new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    try {
                        if (message != null && message.obj != null) {
                            String result = (String) message.obj;
                            data = new Gson().fromJson(result, TariffData.class);
                            System.out.println("SelectBike callAPI Response " + result + " status " + data.getStatus());

                            if (data.getStatus() != null && data.getStatus().equalsIgnoreCase("success")) {
                                setList();
                            }

                        } else {
                            new CustomToast().Show_Toast(Tariff.this, ConstantStrings.common_msg, R.color.light_red2);
                        }
                    } catch (Exception e){
                        System.out.println("SelectBike callAPI Response Exception " + e.toString());

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("SelectBike callAPI Exception "+e.toString());
        }

    }

}
