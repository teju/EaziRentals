package eazi.com.eazirentals;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import eazi.com.eazirentals.helper.Loader;
import eazi.com.eazirentals.helper.SharedPreference;
import eazi.com.eazirentals.models.BikeListResponse;
import eazi.com.eazirentals.models.Branch;
import eazi.com.eazirentals.models.CityListResponse;

public class SelectCity extends AppCompatActivity {
    Integer selected_position = -1;
    private CityListResponse data;
    private ListView branch_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_branch);
        initUI();
    }

    private void initUI() {
        branch_list = (ListView)findViewById(R.id.branch_list);
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setVisibility(View.GONE);
        callGetBikeList();
    }


    public void proceed(View v){
        if(selected_position == -1) {
            new CustomToast().Show_Toast(SelectCity.this, ConstantStrings.common_msg, R.color.light_red2);
        } else {
            SharedPreference.setString(SelectCity.this, Constants.KEY_CITY_ID,data.getBike_list().get(selected_position).getId());

            Intent i = new Intent(this,Home.class);
            startActivity(i);
        }

    }

    public void callGetBikeList() {
        try {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("user_id", SharedPreference.getString(SelectCity.this, Constants.KEY_USER_ID)));
            params.add(new BasicNameValuePair("access_token",SharedPreference.getString(SelectCity.this,Constants.KEY_ACCESS_TOKEN)));

            Loader.show(this);

            APICalls.invokeAPIEx(this,"Post",APICalls.SERVICE_URL+APICalls.CITY,params,new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    try {
                        if (message != null && message.obj != null) {
                            String result = (String) message.obj;
                            data = new Gson().fromJson(result, CityListResponse.class);
                            System.out.println("SelectBike callAPI Response " + result + " status " + data.getStatus());

                            if (data.getStatus() != null && data.getStatus().equalsIgnoreCase("success")) {
                                branch_list.setAdapter(new CityAdapter(SelectCity.this));

                            }

                            System.out.println("SelectBike callAPI Response data " +data.getBike_list().size() );

                        } else {
                            new CustomToast().Show_Toast(SelectCity.this, ConstantStrings.common_msg, R.color.light_red2);

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

    public class CityAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener{

        private final Context mContext;

        // 1
        public CityAdapter(Context context) {
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
                    mContext).inflate(R.layout.select_branch_item, parent, false);
            RelativeLayout container = (RelativeLayout)view.findViewById(R.id.container);
            final CheckBox select_bike_checkbox = (CheckBox)view.findViewById(R.id.select_bike_checkbox);
            select_bike_checkbox.setTag(position);
            select_bike_checkbox.setChecked(position==selected_position);

            TextView city_name = (TextView)view.findViewById(R.id.city_name);

            container.setTag(position);
            city_name.setText(data.getBike_list().get(position).getName());
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

}
