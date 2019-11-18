package eazi.com.eazirentals;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import eazi.com.eazirentals.helper.Helper;
import eazi.com.eazirentals.helper.Loader;
import eazi.com.eazirentals.helper.SharedPreference;
import eazi.com.eazirentals.models.OrderHistory;
import eazi.com.eazirentals.models.OrderList;
import eazi.com.eazirentals.models.VerifyOtpResult;

public class BookingHistory extends AppCompatActivity {
    int[] bikes = {R.drawable.bike1,R.drawable.bike2,R.drawable.bike3,R.drawable.bike3,R.drawable.bike1};

    private LinearLayout cartList;
    private OrderHistory data;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        cartList = (LinearLayout)findViewById(R.id.cartList);
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookingHistory.super.onBackPressed();
            }
        });
        callgetBookingHistoryAPI();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setList() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cartList.removeAllViews();
        for (int i = 0; i < data.getOrder_list().getOrder_list().size() ; i++) {
            OrderList orderList =data.getOrder_list().getOrder_list().get(i);
            View child = inflater.inflate(R.layout.order_history_item, null);
            ImageView bike_img = (ImageView)child.findViewById(R.id.bike_img);
            TextView bike_name = (TextView)child.findViewById(R.id.bike_name);
            TextView pickup_date = (TextView)child.findViewById(R.id.pickup_date);
            TextView dropup_date = (TextView)child.findViewById(R.id.dropup_date);
            TextView km = (TextView)child.findViewById(R.id.km);
            TextView location = (TextView)child.findViewById(R.id.location);

            bike_name.setText(orderList.getRider_name());
            dropup_date.setText(orderList.getDropup_date());
            km.setText("Booking Date: "+Helper.convertDate(orderList.getBooking_date(),Constants.format_1,Constants.format_9));
            pickup_date.setText("Pick UP Date: "+Helper.convertDate(orderList.getPickup_date(),Constants.format_1,Constants.format_9));
            dropup_date.setText("Drop Date: "+Helper.convertDate(orderList.getDropup_date(),Constants.format_1,Constants.format_9));
           // bike_img.setImageDrawable(getResources().getDrawable(bikes[i]));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 10, 10);

            cartList.addView(child,layoutParams);
        }
    }

    public void callgetBookingHistoryAPI() {
        try {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("user_id", SharedPreference.getString(BookingHistory.this, Constants.KEY_USER_ID)));
            params.add(new BasicNameValuePair("access_token",SharedPreference.getString(BookingHistory.this,Constants.KEY_ACCESS_TOKEN)));

            Loader.show(this);

            APICalls.invokeAPIEx(this,"Post",APICalls.SERVICE_URL+APICalls.CUSTOMER_ORDERS,params,new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    try {
                        if (message != null && message.obj != null) {
                            String result = (String) message.obj;
                            data = new Gson().fromJson(result, OrderHistory.class);
                            System.out.println("CartDetails2 callAPI Response " + result + " status " + data.getOrder_list().getStatus());

                            if (data.getOrder_list().getStatus() != null && data.getOrder_list().getStatus().equalsIgnoreCase("success")) {
                                setList();
                            } else {
                                if (data.getOrder_list().getMessage() != null) {
                                    new CustomToast().Show_Toast(BookingHistory.this, data.getOrder_list().getMessage(), R.color.light_red2);

                                } else {
                                    new CustomToast().Show_Toast(BookingHistory.this, ConstantStrings.common_msg, R.color.light_red2);
                                }
                            }
                        } else {
                            new CustomToast().Show_Toast(BookingHistory.this, ConstantStrings.common_msg, R.color.light_red2);

                        }
                    } catch (Exception e){
                        System.out.println("CartDetails2 callAPI Response Exception " + e.toString());

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("CartDetails2 callAPI Exception "+e.toString());
        }

    }

}
