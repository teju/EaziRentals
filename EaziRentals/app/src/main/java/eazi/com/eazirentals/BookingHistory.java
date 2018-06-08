package eazi.com.eazirentals;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BookingHistory extends AppCompatActivity {
    int[] bikes = {R.drawable.bike1,R.drawable.bike2,R.drawable.bike3,R.drawable.bike4,R.drawable.bike5};
    String[] names = {"Honda CB 1000R","Honda CB Unicorn 160 Price","Honda CB Unicorn 160 Price",
            "Honda CB Unicorn 160 Price","Honda CB Unicorn 160 Price"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        setList();
    }

    public void setList(){
        LinearLayout cartList = (LinearLayout)findViewById(R.id.cartList);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cartList.removeAllViews();
        for (int i = 0; i < bikes.length; i++) {
            View child = inflater.inflate(R.layout.bike_item, null);
            ImageView bike_img = (ImageView)child.findViewById(R.id.bike_img);
            bike_img.setImageDrawable(getResources().getDrawable(bikes[i]));
            TextView bike_name = (TextView)child.findViewById(R.id.bike_name);
            TextView booking_date = (TextView)child.findViewById(R.id.booking_date);
            LinearLayout edit = (LinearLayout) child.findViewById(R.id.edit);
            booking_date.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
            bike_name.setText(names[i]);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(5, 0, 5, 10);
            child.setLayoutParams(params);
            cartList.addView(child);
        }
    }

}
