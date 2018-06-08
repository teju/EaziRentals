package eazi.com.eazirentals;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import eazi.com.eazirentals.library.CustomViewPager;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Handler handler;
    private int delay = 5000; //milliseconds
    private ViewPager viewPager;
    private int page = 0;
    private Runnable runnable;
    String type = "";

    String[] time = {"7.30 AM" , "8.00 AM"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

    String myFormat = "MM/dd/yy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    private void updateLabel() {
        Button pick_up_date = (Button)findViewById(R.id.pick_up_date);
        Button drop_Off_Date = (Button)findViewById(R.id.drop_Off_Date);
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
        Spinner pickup_time = (Spinner) findViewById(R.id.pickup_time);
        ArrayAdapter pickup_timeaa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,time);
        pickup_timeaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        pickup_time.setAdapter(pickup_timeaa);

        Spinner drop_time = (Spinner) findViewById(R.id.drop_time);
        ArrayAdapter drop_timeaa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,time);
        drop_timeaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        drop_time.setAdapter(drop_timeaa);

    }

    public void bookNow(View view){
        Intent i = new Intent(Home.this,SelectBike.class);
        startActivity(i);
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

        }
        return true;
    }

    private class ImagePagerAdapter extends PagerAdapter {
        private int[] mImages = new int[] {
                R.drawable.chiang_mai,
                R.drawable.himeji,
                R.drawable.petronas_twin_tower,
                R.drawable.ulm
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
}