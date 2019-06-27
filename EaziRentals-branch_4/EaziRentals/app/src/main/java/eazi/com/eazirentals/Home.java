package eazi.com.eazirentals;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import eazi.com.eazirentals.helper.ConstantStrings;
import eazi.com.eazirentals.helper.Constants;
import eazi.com.eazirentals.helper.CustomToast;
import eazi.com.eazirentals.helper.DataBaseHelper;
import eazi.com.eazirentals.helper.Notify;
import eazi.com.eazirentals.helper.SharedPreference;
import eazi.com.eazirentals.library.CustomViewPager;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemSelectedListener,View.OnClickListener {

    private Handler handler;
    private int delay = 5000; //milliseconds
    private ViewPager viewPager;
    private int page = 0;
    private Runnable runnable;
    String type = "";
    int pick_time_pos = 0;
    int drop_time_pos = 0;
    List<String> pickuptime = new ArrayList<>();
    List<String> dropoffptime = new ArrayList<>();
    private Button pick_up_date,drop_Off_Date;
    private Spinner pickup_time,drop_time;
    private DataBaseHelper db;
    private TextView cart_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();

    }

    private void initUI() {
        pick_up_date = (Button)findViewById(R.id.pick_up_date);
        drop_Off_Date = (Button)findViewById(R.id.drop_Off_Date);
        pickup_time = (Spinner) findViewById(R.id.pickup_time);
        drop_time = (Spinner) findViewById(R.id.drop_time);
        RelativeLayout cart_button = (RelativeLayout) findViewById(R.id.cart_button);
        cart_count = (TextView) findViewById(R.id.cart_count);
        pickup_time.setOnItemSelectedListener(this);
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
    }


    public void makeTimeList() {
        double i = 7.00;
        int j =0;
        while (i <= 12.00){
            String pTime = String.format("%.2f", i) +" AM";
            pickuptime.add(pTime.replaceAll("\\.",":"));
            if(j % 2 == 0) {
                i = i+0.30;
            } else {
                i = i+0.70;
            }
            j = j + 1;
        }
        if(i> 12) {
            String pTime = String.format("%.2f", 12.30) +" PM";
            pickuptime.add(pTime.replaceAll("\\.",":"));
            i = 1.00;
           while (i < 10.00) {
               pTime = String.format("%.2f",i) +" PM";
               pickuptime.add(pTime.replaceAll("\\.",":"));
               if (j % 2 != 0) {
                   i = i + 0.30;
               } else {
                   i = i + 0.70;
               }
               j = j + 1;
            }
        }
        reverseList();
        pickTime();


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
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.US);

    private void updateLabel() {

        if(type.equals("pickUp")) {
            pick_up_date.setText(sdf.format(myCalendar.getTime()));
        } else {
            drop_Off_Date.setText(sdf.format(myCalendar.getTime()));
        }
    }

    public void pickupDate(View view){
        type = "pickUp";
        new DatePickerDialog(Home.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void pickTime(){
        ArrayAdapter pickup_timeaa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,pickuptime);
        pickup_timeaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        pickup_time.setAdapter(pickup_timeaa);

        ArrayAdapter drop_timeaa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,dropoffptime);
        drop_timeaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        drop_time.setAdapter(drop_timeaa);

    }

    public  void reverseList() {
        int size = pickuptime.size()-1;

        for(int i=size;i>=0;i--){
            dropoffptime.add(pickuptime.get(i));
        }
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
        } else if (id == R.id.refer_earn) {
            Intent i = new Intent(Home.this,ReferAndEarn.class);
            startActivity(i);
        } else if (id == R.id.offers) {
            Intent i = new Intent(Home.this,Offers.class);
            startActivity(i);
        }  else if (id == R.id.contact_us) {
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
}