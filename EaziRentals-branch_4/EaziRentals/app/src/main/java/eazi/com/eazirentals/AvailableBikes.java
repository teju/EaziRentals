package eazi.com.eazirentals;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by tejaswini on 14/06/18.
 */

public class AvailableBikes extends AppCompatActivity {

    private LinearLayout listView;
    int[] bikes = {R.drawable.bike1,R.drawable.bike2,R.drawable.bike3,R.drawable.banner_3};

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_bikes);
        listView = (LinearLayout)findViewById(R.id.bike_list);
        Button book_now = (Button)findViewById(R.id.book_now);
        book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(AvailableBikes.this,Cart.class);
                startActivity(i);
            }
        });
        setListView();

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setListView() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listView.removeAllViews();

        for (int i = 0; i < bikes.length ; i++) {
            View child = inflater.inflate(R.layout.bike_item, null);
            ImageView bike_img = (ImageView)child.findViewById(R.id.bike_img);
            Button add_bikes_button = (Button)child.findViewById(R.id.add_bikes_button);
            add_bikes_button.setVisibility(View.VISIBLE);
            if( i == 2) {
                add_bikes_button.setBackgroundTintList(ContextCompat.getColorStateList(AvailableBikes.this, R.color.dark_gray));
                add_bikes_button.setText("ADDED TO CART");
            } else {
                add_bikes_button.setBackgroundTintList(ContextCompat.getColorStateList(AvailableBikes.this, R.color.yellow));
                add_bikes_button.setText("ADD TO CART");
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 10, 10);
            bike_img.setImageDrawable(getResources().getDrawable(bikes[i]));
            listView.addView(child,layoutParams);
        }
    }


}
