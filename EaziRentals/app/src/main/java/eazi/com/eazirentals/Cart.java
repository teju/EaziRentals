package eazi.com.eazirentals;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


public class Cart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setList();
    }

    public void setList(){
        LinearLayout cartList = (LinearLayout)findViewById(R.id.cartList);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cartList.removeAllViews();
        for (int i = 0; i < 5; i++) {
            View child = inflater.inflate(R.layout.bike_item, null);
            cartList.addView(child);
        }
    }
}
