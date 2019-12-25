package eazi.com.eazirentals;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import eazi.com.eazirentals.api.APICalls;
import eazi.com.eazirentals.helper.ConstantStrings;
import eazi.com.eazirentals.helper.Constants;
import eazi.com.eazirentals.helper.CustomToast;
import eazi.com.eazirentals.helper.DataBaseHelper;
import eazi.com.eazirentals.helper.Loader;
import eazi.com.eazirentals.helper.Notify;
import eazi.com.eazirentals.helper.SharedPreference;
import eazi.com.eazirentals.library.CustomViewPager;
import eazi.com.eazirentals.models.BikeListResponse;
import eazi.com.eazirentals.models.BranchListResponse;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemSelectedListener,View.OnClickListener {

    private Handler handler;
    private int delay = 5000; //milliseconds
    private ViewPager viewPager;
    private int page = 0;
    private Runnable runnable;
    String type = "";
    String branch_id = "";
    int pick_time_pos = 0;
    int drop_time_pos = 0;
    int select_location_pos = 0;
    List<String> pickuptime = new ArrayList<>();
    List<String> dropoffptime = new ArrayList<>();
    List<String> location = new ArrayList<>();
    private Button pick_up_date,drop_Off_Date;
    private Spinner pickup_time,drop_time,select_location;
    private DataBaseHelper db;
    private TextView cart_count;
    private static DecimalFormat df = new DecimalFormat("0.00");
    private ArrayAdapter pickup_timeaa;
    private ArrayAdapter drop_timeaa;
    private BranchListResponse data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();

    }

    private void initUI() {
        callGetBranchList();
        pick_up_date = (Button)findViewById(R.id.pick_up_date);
        drop_Off_Date = (Button)findViewById(R.id.drop_Off_Date);
        pickup_time = (Spinner) findViewById(R.id.pickup_time);
        select_location = (Spinner) findViewById(R.id.select_location);
        drop_time = (Spinner) findViewById(R.id.drop_time);
        RelativeLayout cart_button = (RelativeLayout) findViewById(R.id.cart_button);
        cart_count = (TextView) findViewById(R.id.cart_count);
        pickup_time.setOnItemSelectedListener(this);
        select_location.setOnItemSelectedListener(this);
        drop_time.setOnItemSelectedListener(this);
        System.out.println("Home124 pick_time_pos getId initUI "+pickup_time.getId());
        handler = new Handler();

        final CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.view_pager);

        final ImagePagerAdapter adapter = new ImagePagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                System.out.println("onPageScrollStateChanged "+state);
            }
        });

        runnable = new Runnable() {
            public void run() {
                if (adapter.getCount() == page) {
                    page = 0;
                } else {
                    page++;
                }
                viewPager.setCurrentItem(page, true);
                handler.postDelayed(this, delay);
            }
        };
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView profile_name = (TextView)header.findViewById(R.id.profile_name);
        profile_name.setText( SharedPreference.getString(Home.this, Constants.KEY_USER_NAME));
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this,Register.class);
                i.putExtra(Constants.ISRegister,false);
                startActivity(i);
                finish();
            }
        });

        pick_up_date.setText(sdf.format(new Date()));
        drop_Off_Date.setText(sdf.format(new Date()));
        cart_button.setOnClickListener(this);

        db = new DataBaseHelper(this);
        makeTimeList();
        pickTime();

    }

    static Date toNearestWholeMinute(Date d,int add) {
        final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

        long curTimeInMs = d.getTime();
        Date afterAddingMins = new Date(curTimeInMs + (add * ONE_MINUTE_IN_MILLIS));
        return afterAddingMins;
    }

    private String secondsToString(int hr, int min) {
        return String.format("%02d:%d", hr , min);
    }



    public String calculateStartTime() {
        Date current_date = null;
        try {
            current_date = sdf.parse(pick_up_date.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String day_of_week = new SimpleDateFormat("EE", Locale.ENGLISH).format(current_date.getTime());
        Date now_date = new Date();
        String str_now_date = sdf.format(now_date);
        try {
            now_date = sdf.parse(str_now_date);
        } catch (ParseException e) {
            System.out.println("toNearestWholeMinutecurrent_date " +
                    " ParseException "+e.toString());
            e.printStackTrace();
        }
        System.out.println("toNearestWholeMinutecurrent_date " +
                " "+now_date.compareTo(current_date)+" now_date "+now_date+" current_date "+current_date);

        if(current_date.compareTo(now_date) == 0) {
            int minutes_to_add = 0;
            if (myCalendar.getTime().getMinutes() > 30) {
                minutes_to_add = 60 - myCalendar.getTime().getMinutes();
            } else {
                minutes_to_add = 30 - myCalendar.getTime().getMinutes();
            }

            Date toNearest = toNearestWholeMinute(myCalendar.getTime(), minutes_to_add);

            int start_hr = Integer.parseInt(new SimpleDateFormat("HH", Locale.ENGLISH).format(toNearest));
            int start_min = Integer.parseInt(new SimpleDateFormat("mm", Locale.ENGLISH).format(toNearest));
            String am_pm = new SimpleDateFormat("aa", Locale.ENGLISH).format(toNearest);
            String start_time = secondsToString(start_hr, start_min);
            System.out.println("toNearestWholeMinute toNearest " + toNearest + " start_time " + start_time +
                    " minutes_to_add " + minutes_to_add + " day_of_week " +
                    " " + day_of_week + " current_date " + current_date);

            if (am_pm.equalsIgnoreCase("pm")) {
                if (start_hr > 21) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(current_date); // Now use today date.
                    c.add(Calendar.DATE, 1); // Adding 5 days
                    String output = sdf.format(c.getTime());
                    pick_up_date.setText(output);
                    drop_Off_Date.setText(output);
                    System.out.println("toNearestWholeMinuteday_of_week "+day_of_week+" "+day_of_week.contains("Sat"));
                    if (day_of_week.contains("Sat") || day_of_week.contains("Sun")) {
                        return "8:30";
                    } else {
                        System.out.println("toNearestWholeMinuteday_of_week 7:30 one");

                        return "7:30";
                    }
                } else {
                    return start_time;
                }
            } else {
                if (day_of_week.contains("Sat") || day_of_week.contains("Sun")) {
                    if (start_hr < 9.00) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(current_date); // Now use today date.
                        c.add(Calendar.DATE, 1); // Adding 5 days
                        String output = sdf.format(c.getTime());
                        pick_up_date.setText(output);
                        drop_Off_Date.setText(output);
                        return "8:30";
                    } else {
                        return start_time;
                    }
                } else {
                    if (start_hr < 8.00) {
                        Calendar c = Calendar.getInstance();
                        c.setTime(current_date); // Now use today date.
                        c.add(Calendar.DATE, 1); // Adding 5 days
                        String output = sdf.format(c.getTime());
                        pick_up_date.setText(output);
                        drop_Off_Date.setText(output);
                        return "7:30";
                    } else {
                        return start_time;
                    }
                }
            }
        } else {

            if(type.equals("pickUp")) {
                drop_Off_Date.setText(pick_up_date.getText().toString());
            }
            if (day_of_week.contains("Sat") || day_of_week.contains("Sun")) {
                return "8:30";
            } else {
                return "7:30";
            }
        }
    }

    public void makeTimeList() {
        pickuptime.clear();
        try {
            String date_add = calculateStartTime();
            System.out.println("toNearestWholeMinutemakeTimeList date_add "+date_add);

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date date = null;
            try {
                date = formatter.parse(date_add);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Double i = Double.parseDouble(new SimpleDateFormat("HH.mm", Locale.ENGLISH).format(date));
            System.out.println("toNearestWholeMinutemakeTimeList "+i);
            int j = 0;
            while (i <= 21.30 && i >= 7.30) {

                String ss = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(date);

                pickuptime.add(ss);
                Date new_date = toNearestWholeMinute(date, 30);
                int start_hr = Integer.parseInt(new SimpleDateFormat("HH", Locale.ENGLISH).format(new_date));
                int start_min = Integer.parseInt(new SimpleDateFormat("mm", Locale.ENGLISH).format(new_date));
                date_add = secondsToString(start_hr, start_min);
                try {
                    date = formatter.parse(date_add);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                i = Double.parseDouble(new SimpleDateFormat("HH.mm", Locale.ENGLISH).format(date));
                System.out.println("toNearestWholeMinute intValue new_date " + new_date + " date_add "
                        + date_add + " date " + date + " i " + i);

                j = j + 1;
            }
            pickup_timeaa.notifyDataSetChanged();
            reverseList(pickuptime.get(pick_time_pos),true);

        } catch (Exception e){
            System.out.println("toNearestWholeMinutemakeTimeList Exception "+e.toString() +" "+pickuptime.size());

        }
    }


    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    String myFormat = "dd/MM/yy"; //In which you need put here
    String myFormat2 = "hh:mm a"; //In which you need put here
    String myFormat3 = "dd/MM/yy hh:mm a"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.US);
    SimpleDateFormat sdf3 = new SimpleDateFormat(myFormat3, Locale.US);

    private void updateLabel() {

        if(type.equals("pickUp")) {
            pick_up_date.setText(sdf.format(myCalendar.getTime()));
        } else {
            drop_Off_Date.setText(sdf.format(myCalendar.getTime()));
        }
        pick_time_pos =0;
        drop_time_pos =0;

        makeTimeList();
        pickup_time.setSelection(pick_time_pos);
        drop_time.setSelection(drop_time_pos);
    }

    public void pickupDate(View view){
        type = "pickUp";
        new DatePickerDialog(Home.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void pickTime(){
        pickup_timeaa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,pickuptime);
        pickup_timeaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        pickup_time.setAdapter(pickup_timeaa);

        drop_timeaa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,dropoffptime);
        drop_timeaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        drop_time.setAdapter(drop_timeaa);

    }

    public void selectLocation(){
        ArrayAdapter selectLocation = new ArrayAdapter(this,android.R.layout.simple_spinner_item,location);
        selectLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        select_location.setAdapter(selectLocation);

    }


    public  void reverseList(String _add,boolean isAdd) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat sd = new SimpleDateFormat("HH:mm");
        dropoffptime.clear();
        Date _date = null;
        try {
            _date = sdf.parse(_add);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(_date); // Now use today date.
        if(isAdd) {
            c.add(Calendar.HOUR, 1); // Adding 5 days
        }
        String output = sd.format(c.getTime());
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date date = null;
            try {
                date = formatter.parse(output);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Double i = Double.parseDouble(new SimpleDateFormat("HH.mm", Locale.ENGLISH).format(date));
            System.out.println("toNearestWholeMinutemakeTimeList reverseList "+i);
            if(i > 21.30) {
                setDropDate();
                reverseList("7:30 AM",false);
            }
            int j = 0;
            while (i <= 21.30 && i >= 7.30) {

                String ss = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(date);

                dropoffptime.add(ss);
                Date new_date = toNearestWholeMinute(date, 30);
                int start_hr = Integer.parseInt(new SimpleDateFormat("HH", Locale.ENGLISH).format(new_date));
                int start_min = Integer.parseInt(new SimpleDateFormat("mm", Locale.ENGLISH).format(new_date));
                output = secondsToString(start_hr, start_min);
                try {
                    date = formatter.parse(output);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                i = Double.parseDouble(new SimpleDateFormat("HH.mm", Locale.ENGLISH).format(date));
                System.out.println("toNearestWholeMinute intValue new_date " + new_date + " date_add "
                        + output + " date " + date + " i " + i);

                j = j + 1;
            }

            drop_timeaa.notifyDataSetChanged();
        } catch (Exception e){
            System.out.println("toNearestWholeMinutemakeTimeList Exception reverseList "+e.toString());

        }

    }

    public void setDropDate(){
        Date current_date = null;
        try {
            current_date = sdf.parse(pick_up_date.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(current_date); // Now use today date.
        c.add(Calendar.DATE, 1); // Adding 5 days
        String output = sdf.format(c.getTime());
        drop_Off_Date.setText(output);
    }
    public boolean validateDate() {

        if(!CheckDates(sdf.format(new Date())+" "+sdf2.format(new Date()),pick_up_date.getText().toString()+" 0"+pickuptime.get(pick_time_pos))) {
            new CustomToast().Show_Toast(this,ConstantStrings.pickupDate,R.color.light_red2);
            return false;
        }
        else if(!CheckDates(pick_up_date.getText().toString()+" "+pickuptime.get(pick_time_pos),
                drop_Off_Date.getText().toString()+" "+dropoffptime.get(drop_time_pos))){
            new CustomToast().Show_Toast(this,ConstantStrings.dropDate,R.color.light_red2);
            return false;
        }
        return true;
    }

    public  boolean CheckDates(String startDate, String endDate) {
        System.out.println("Home124 startDate "+startDate+" endDate "+endDate);

        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yy hh:mm a");

        boolean b = false;

        try {
            if (dfDate.parse(startDate).before(dfDate.parse(endDate))) {
                b = true;  // If start date is before end date.
            } else if (dfDate.parse(startDate).equals(dfDate.parse(endDate))) {
                b = true;  // If two dates are equal.
            } else {
                b = false; // If start date is after the end date.
            }
        } catch (ParseException e) {
            System.out.println("Home124 ParseException "+e.toString());
            e.printStackTrace();
        }

        return b;
    }
    public void bookNow(View view){
        if(validateDate()) {
            Intent i = new Intent(Home.this, SelectBike.class);
            i.putExtra(Constants.PICKUP_DATE, pick_up_date.getText().toString());
            i.putExtra(Constants.DROP_DATE, drop_Off_Date.getText().toString());
            i.putExtra(Constants.PICKUP_TIME, pickuptime.get(pick_time_pos));
            i.putExtra(Constants.DROP_TIME, dropoffptime.get(drop_time_pos));
            i.putExtra(Constants.PICKUP_LOCATION, data.getBike_list().get(select_location_pos).getId());
            startActivity(i);
        }
    }

    public void dropOffDate(View view){
        type = "drop";
        new DatePickerDialog(Home.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
        if(db.getAllContacts("").size() == 0) {
            cart_count.setVisibility(View.GONE);

        } else {
            cart_count.setVisibility(View.VISIBLE);
            cart_count.setText(String.valueOf(db.getAllContacts("").size()));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {


        } else if (id == R.id.booking_history) {
            Intent i = new Intent(Home.this,BookingHistory.class);
            startActivity(i);
        } else if (id == R.id.home) {

        } else if (id == R.id.tariff) {
            Intent i = new Intent(Home.this,Tariff.class);
            startActivity(i);
        }
// else if (id == R.id.refer_earn) {
//            Intent i = new Intent(Home.this,ReferAndEarn.class);
//            startActivity(i);
//        } else if (id == R.id.offers) {
//            Intent i = new Intent(Home.this,Offers.class);
//            startActivity(i);
//        }
         else if (id == R.id.contact_us) {
            Intent i = new Intent(Home.this,ContactUs.class);
            startActivity(i);
        } else if (id == R.id.about_us) {
            Intent i = new Intent(Home.this,AboutUs.class);
            startActivity(i);
        } else if (id == R.id.faq) {
            Intent i = new Intent(Home.this,Faq.class);
            startActivity(i);
        } else if (id == R.id.logout) {
            Notify.show(Home.this, ConstantStrings.logout, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == dialog.BUTTON_POSITIVE) {
                        SharedPreference.clear(Home.this);
                        Intent i = new Intent(Home.this,LoginActivity.class);
                        startActivity(i);
                    }

                }
            },"Ok","Cancel");
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("Home124 pick_time_pos getId "+(parent.getId() == R.id.drop_time));

        if(parent.getId() == R.id.pickup_time) {
            pick_time_pos = position;
            System.out.println("Home124 pick_time_pos");
            makeTimeList();
        } else if(parent.getId() == R.id.select_location) {
            select_location_pos = position;
            System.out.println("Home124 pick_time_pos");

        } else {
            System.out.println("Home124 drop_time_pos");

            drop_time_pos = position;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cart_button) {
            if(db.getAllContacts("").size() == 0) {
                new CustomToast().Show_Toast(Home.this, ConstantStrings.noitems_in_cart, R.color.light_red2);
            } else {
                Intent i =new Intent(Home.this,Cart.class);
                startActivity(i);
            }

        }
    }

    private class ImagePagerAdapter extends PagerAdapter {
        private int[] mImages = new int[] {
                R.drawable.banner_1,
                R.drawable.banner_2,
                R.drawable.banner_3,
                R.drawable.banner_4
        };

        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = Home.this;
            View view = LayoutInflater.from(
                    Home.this).inflate(R.layout.home_banners, container, false);
            ImageView imageView = (ImageView)view.findViewById(R.id.banner_image);
            imageView.setImageResource(mImages[position]);

            ((ViewPager) container).addView(view, 0);

            return view;
        }

        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);
        }

    }

    @Override
    public void onBackPressed() {
        Notify.show(this, "Are yoy sure you want to exit this app ?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == dialog.BUTTON_POSITIVE) {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    finish();
                }
            }
        },"OK","Cancel");
    }

    public void callGetBranchList() {
        try {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("user_id", SharedPreference.getString(Home.this, Constants.KEY_USER_ID)));
            params.add(new BasicNameValuePair("access_token",SharedPreference.getString(Home.this,Constants.KEY_ACCESS_TOKEN)));
            params.add(new BasicNameValuePair("city_id",SharedPreference.getString(Home.this,Constants.KEY_CITY_ID)));

            Loader.show(this);

            APICalls.invokeAPIEx(this,"Post",APICalls.SERVICE_URL+APICalls.BRANCH,params,new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    try {
                        if (message != null && message.obj != null) {
                            String result = (String) message.obj;
                            data = new Gson().fromJson(result, BranchListResponse.class);
                            System.out.println("callGetBranchList callAPI Response " + result + " status " + data.getStatus());
                            if (data.getStatus() != null && data.getStatus().equalsIgnoreCase("success")) {
                                for (int i = 0; i < data.getBike_list().size(); i++) {
                                    location.add(data.getBike_list().get(i).getName());
                                }
                                System.out.println("callGetBranchList callAPI Response " + result + " status " + data.getStatus());

                                selectLocation();
                            }
                        } else {
                            new CustomToast().Show_Toast(Home.this, ConstantStrings.common_msg, R.color.light_red2);

                        }
                    } catch (Exception e){
                        System.out.println("Home callAPI Response Exception " + e.toString());

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("Home callAPI Exception "+e.toString());
        }

    }

}