package eazi.com.eazirentals;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import eazi.com.eazirentals.helper.DataBaseHelper;
import eazi.com.eazirentals.helper.Helper;
import eazi.com.eazirentals.helper.Loader;
import eazi.com.eazirentals.helper.SharedPreference;
import eazi.com.eazirentals.models.BikeListResponse;

public class SelectBike extends AppCompatActivity implements View.OnClickListener{

    private BikeListResponse data;
    private GridView gridview;
    Integer selected_position = -1;
    private TextView dropoff_up_time,pick_up_time;
    private ImageView back;
    private DataBaseHelper db;
    private TextView cart_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bike);
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(db.getAllContacts("").size() == 0) {
            cart_count.setVisibility(View.GONE);
        } else {
            cart_count.setVisibility(View.VISIBLE);
            cart_count.setText(String.valueOf(db.getAllContacts("").size()));
        }
    }

    private void initUI() {
        db = new DataBaseHelper(this);

        gridview = (GridView)findViewById(R.id.gridview);
        RelativeLayout cart_button = (RelativeLayout) findViewById(R.id.cart_button);
        cart_button.setVisibility(View.VISIBLE);
        cart_button.setOnClickListener(this);
        cart_count = (TextView) findViewById(R.id.cart_count);

        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        dropoff_up_time = (TextView)findViewById(R.id.dropoff_up_time);
        pick_up_time = (TextView)findViewById(R.id.pick_up_time);
        pick_up_time.setText(Helper.convertDate(getIntent().
                getStringExtra(Constants.PICKUP_DATE),Constants.format_3,Constants.format_2)+", "+getIntent().getStringExtra(Constants.PICKUP_TIME));
        dropoff_up_time.setText(Helper.convertDate(getIntent().getStringExtra(Constants.DROP_DATE),Constants.format_3,Constants.format_2)+
                ", "+getIntent().getStringExtra(Constants.DROP_TIME));
        callGetBikeList();
    }


    public void selectNow() {
        if(selected_position == -1) {
            //new CustomToast().Show_Toast(this,ConstantStrings.select_one_bike,R.color.light_red2);
        } else {
            Intent i = new Intent(SelectBike.this, AvailableBikes.class);
            i.putExtra(Constants.BIKE_NAME,data.getBike_list().get(selected_position).getName());
            i.putExtra(Constants.PICKUP_TIME,getIntent().getStringExtra(Constants.PICKUP_TIME));
            i.putExtra(Constants.PICKUP_DATE,getIntent().getStringExtra(Constants.PICKUP_DATE));
            i.putExtra(Constants.DROP_TIME,getIntent().getStringExtra(Constants.DROP_TIME));
            i.putExtra(Constants.DROP_DATE,getIntent().getStringExtra(Constants.DROP_DATE));
            i.putExtra(Constants.PICKUP_LOCATION,getIntent().getStringExtra(Constants.PICKUP_LOCATION));
            startActivity(i);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cart_button) {
            if(db.getAllContacts("").size() == 0) {
                new CustomToast().Show_Toast(SelectBike.this, ConstantStrings.noitems_in_cart, R.color.light_red2);
            } else {
                Intent i =new Intent(SelectBike.this,Cart.class);
                startActivity(i);
            }

        } else {
            super.onBackPressed();

        }
    }


    public class BikesAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener{

        private final Context mContext;

        // 1
        public BikesAdapter(Context context) {
            this.mContext = context;
        }

        // 2
        @Override
        public int getCount() {
            return data.getBike_list().size();
        }

        // 3
        @Override
        public long getItemId(int position) {
            return position;
        }

        // 4
        @Override
        public Object getItem(int position) {
            return position;
        }

        // 5
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(
                    mContext).inflate(R.layout.select_bike_item, parent, false);
            ImageView bike_image = (ImageView)view.findViewById(R.id.bike_image);
            RelativeLayout container = (RelativeLayout)view.findViewById(R.id.container);
            final CheckBox select_bike_checkbox = (CheckBox)view.findViewById(R.id.select_bike_checkbox);
            select_bike_checkbox.setTag(position);
            select_bike_checkbox.setChecked(position==selected_position);

            TextView bike_name = (TextView)view.findViewById(R.id.bike_name);
            Glide.with(mContext).load(data.getBike_list().get(position).getImage())
                    .into(bike_image);
            bike_image.setTag(position);
            container.setTag(position);
            bike_name.setText(data.getBike_list().get(position).getName());
            select_bike_checkbox.setOnCheckedChangeListener(this);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = select_bike_checkbox.isChecked();
                    if(isChecked)
                    {
                        select_bike_checkbox.setChecked(false);
                        selected_position =  -1;
                    }
                    else{
                        select_bike_checkbox.setChecked(true);
                        selected_position = (int)v.getTag();
                    }
                    notifyDataSetChanged();
                    selectNow();
                }
            });
            return view;
        }



        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
            {
                selected_position =  (int)buttonView.getTag();
            }
            else{
                selected_position = -1;
            }
            notifyDataSetChanged();
        }
    }

    public void callGetBikeList() {
        try {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("user_id", SharedPreference.getString(SelectBike.this, Constants.KEY_USER_ID)));
            params.add(new BasicNameValuePair("access_token",SharedPreference.getString(SelectBike.this,Constants.KEY_ACCESS_TOKEN)));

            Loader.show(this);

            APICalls.invokeAPIEx(this,"Post",APICalls.SERVICE_URL+APICalls.REQUEST,params,new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    try {
                        if (message != null && message.obj != null) {
                            String result = (String) message.obj;
                            data = new Gson().fromJson(result, BikeListResponse.class);
                            System.out.println("SelectBike callAPI Response " + result + " status " + data.getStatus());

                            if (data.getStatus() != null && data.getStatus().equalsIgnoreCase("success")) {
                                gridview.setAdapter(new BikesAdapter(SelectBike.this));

                            } else {
                                if (data.getMessage() != null) {
                                    new CustomToast().Show_Toast(SelectBike.this, data.getMessage(), R.color.light_red2);

                                } else {
                                    new CustomToast().Show_Toast(SelectBike.this, ConstantStrings.common_msg, R.color.light_red2);
                                }
                            }
                        } else {
                            new CustomToast().Show_Toast(SelectBike.this, ConstantStrings.common_msg, R.color.light_red2);

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
