package eazi.com.eazirentals;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class BookingHistory extends AppCompatActivity {
    int[] bikes = {R.drawable.bike1,R.drawable.bike2,R.drawable.bike3,R.drawable.bike3,R.drawable.bike1};
    String[] names = {"Honda CB 1000R","Honda CB Unicorn 160 Price","Honda CB Unicorn 160 Price",
            "Honda CB Unicorn 160 Price","Honda CB Unicorn 160 Price"};
    private LinearLayout cartList;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        cartList = (LinearLayout)findViewById(R.id.cartList);

        setList();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setList() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cartList.removeAllViews();
        for (int i = 0; i < bikes.length ; i++) {
            View child = inflater.inflate(R.layout.bike_item, null);
            ImageView bike_img = (ImageView)child.findViewById(R.id.bike_img);
            Button add_bikes_button = (Button)child.findViewById(R.id.add_bikes_button);
            add_bikes_button.setVisibility(View.GONE);
            bike_img.setImageDrawable(getResources().getDrawable(bikes[i]));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 10, 10);
            cartList.addView(child,layoutParams);
        }
    }
}