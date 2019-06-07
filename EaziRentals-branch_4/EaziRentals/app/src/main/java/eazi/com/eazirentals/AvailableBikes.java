package eazi.com.eazirentals;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import eazi.com.eazirentals.models.BikeList;
import eazi.com.eazirentals.models.BikeListResponse;

/**
 * Created by tejaswini on 14/06/18.
 */

public class AvailableBikes extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout listView;
    int[] bikes = {R.drawable.bike1,R.drawable.bike2,R.drawable.bike3,R.drawable.banner_3};
    private BikeListResponse data;
    private TextView dropoff_up_time,pick_up_time;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.available_bikes);
        initUI();


    }

    private void initUI() {
        listView = (LinearLayout)findViewById(R.id.bike_list);
        Button book_now = (Button)findViewById(R.id.book_now);
        book_now.setOnClickListener(this);
        dropoff_up_time = (TextView)findViewById(R.id.dropoff_up_time);
        pick_up_time = (TextView)findViewById(R.id.pick_up_time);
        pick_up_time.setText(Helper.convertDate(getIntent().
                getStringExtra(Constants.PICKUP_DATE),Constants.format_2)+", "+getIntent().getStringExtra(Constants.PICKUP_TIME));
        dropoff_up_time.setText(Helper.convertDate(getIntent().getStringExtra(Constants.DROP_DATE),Constants.format_2)+
                ", "+getIntent().getStringExtra(Constants.DROP_TIME));
        callGetBikeList();

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setListView() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listView.removeAllViews();
        List<BikeList> bikeLists = data.getBike_list();
        for (int i = 0; i < bikeLists.size() ; i++) {
            BikeList bikeList = bikeLists.get(i);
            View child = inflater.inflate(R.layout.bike_item, null);
            ImageView bike_img = (ImageView)child.findViewById(R.id.bike_img);
            Button add_bikes_button = (Button)child.findViewById(R.id.add_bikes_button);
            add_bikes_button.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 10, 10);
            Glide.with(this).load(bikeList.getImage())
                    .into(bike_img);
            listView.addView(child,layoutParams);
        }
    }

    public void callGetBikeList() {
        try {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("user_id", SharedPreference.getString(AvailableBikes.this, Constants.KEY_USER_ID)));
            params.add(new BasicNameValuePair("access_token",SharedPreference.getString(AvailableBikes.this,Constants.KEY_ACCESS_TOKEN)));
            params.add(new BasicNameValuePair("pickup_date", Helper.convertDate(getIntent().getStringExtra(Constants.PICKUP_DATE),Constants.format_1)));
            params.add(new BasicNameValuePair("pickup_time",getIntent().getStringExtra(Constants.PICKUP_TIME)));
            params.add(new BasicNameValuePair("dropoff_date", Helper.convertDate(getIntent().getStringExtra(Constants.DROP_DATE),Constants.format_1)));
            params.add(new BasicNameValuePair("dropoff_time",getIntent().getStringExtra(Constants.DROP_TIME)));
            params.add(new BasicNameValuePair("bike_name",getIntent().getStringExtra(Constants.BIKE_NAME)));

            Loader.show(this);

            APICalls.invokeAPIEx(this,"Post",APICalls.SERVICE_URL+APICalls.BIKELIST,params,new Handler(new Handler.Callback() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    try {
                        if (message != null && message.obj != null) {
                            String result = (String) message.obj;
                            data = new Gson().fromJson(result, BikeListResponse.class);
                            System.out.println("AvailableBikes callAPI Response " + result + " status " + data.getStatus());

                            if (data.getStatus() != null && data.getStatus().equalsIgnoreCase("success")) {
                                setListView();

                            } else {
                                if (data.getMessage() != null) {
                                    new CustomToast().Show_Toast(AvailableBikes.this, data.getMessage(), R.color.light_red2);

                                } else {
                                    new CustomToast().Show_Toast(AvailableBikes.this, ConstantStrings.common_msg, R.color.light_red2);
                                }
                            }
                        } else {
                            new CustomToast().Show_Toast(AvailableBikes.this, ConstantStrings.common_msg, R.color.light_red2);

                        }
                    } catch (Exception e){
                        System.out.println("AvailableBikes callAPI Response Exception " + e.toString());

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("AvailableBikes callAPI Exception "+e.toString());
        }

    }

    @Override
    public void onClick(View v) {
        Intent i =new Intent(AvailableBikes.this,Cart.class);
        startActivity(i);
    }
}
