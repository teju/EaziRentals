package eazi.com.eazirentals;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class Tariff extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tariff);
        setList();

    }

    public void setList(){
        LinearLayout tariff = (LinearLayout)findViewById(R.id.tariff);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tariff.removeAllViews();
        for (int i = 0; i < 5; i++) {
            View child = inflater.inflate(R.layout.tariff_item, null);
            if( i == 0) {
                child.setBackgroundColor(getResources().getColor(R.color.yellow));
            } else {
                child.setBackgroundColor(getResources().getColor(R.color.white));
            }
            tariff.addView(child);
        }
    }
}
