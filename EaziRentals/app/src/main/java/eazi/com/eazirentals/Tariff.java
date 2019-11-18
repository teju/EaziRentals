package eazi.com.eazirentals;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Tariff extends AppCompatActivity {

    String [] bike_brands = {"Bajaj","TVS","Royal Enfield","Royal Enfield","Royal Enfield","Honda",
            "Bajaj","Yamaha","Bajaj","Honda"};
    String [] bike_names = {"Pulsar 200 NS","Apache RTR 160","Himalayan","Thunderbird 350","Classic 350","Navi",
            "Avenger 220","FZ","Pulsar 180","Activa 3G"};

    String [] tariffs_per_hour = {"42","35","25","70","45","43",
            "15","38","35","35"};
    String [] tariffs_per_day = {"499","349","899","599","589","589",
            "399","349","349","249  "};
    String [] tariffs_per_day_weekend = {"799","599","1299","999","899","249",
            "699","599","599","349  "};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tariff);
        setList();
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tariff.super.onBackPressed();
            }
        });
    }

    public void setList(){
        LinearLayout tariff = (LinearLayout)findViewById(R.id.tariff);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tariff.removeAllViews();
        for (int i = 0; i < bike_brands.length ; i++) {
            View child = inflater.inflate(R.layout.tariff_item, null);
            TextView bike_brand = (TextView)child.findViewById(R.id.bike_brand);
            TextView bike_name = (TextView)child.findViewById(R.id.bike_name);
            TextView tariff_per_hour = (TextView)child.findViewById(R.id.tariff_per_hour);
            TextView tariff_per_day = (TextView)child.findViewById(R.id.tariff_per_day);
            TextView tariff_per_day_weekend = (TextView)child.findViewById(R.id.tariff_per_day_weekend);

            bike_brand.setText(bike_brands[i]);
            bike_name.setText(bike_names[i]);
            tariff_per_hour.setText("\u20B9"+tariffs_per_hour[i]);
            tariff_per_day.setText("\u20B9"+tariffs_per_day[i ]);
            tariff_per_day_weekend.setText("\u20B9"+tariffs_per_day_weekend[i]);
            child.setBackgroundColor(getResources().getColor(R.color.white));

            tariff.addView(child);
        }
    }
}
