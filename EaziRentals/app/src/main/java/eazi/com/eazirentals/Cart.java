package eazi.com.eazirentals;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Cart extends AppCompatActivity {
    int[] bikes = {R.drawable.bike1,R.drawable.bike2,R.drawable.bike3,R.drawable.bike4};
    String type = "";
    String[] time = {"7.30 AM" , "8.00 AM"};
    TextView pick_up_date,drop_Off_Date;

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
        for (int i = 0; i < bikes.length; i++) {
            View child = inflater.inflate(R.layout.cart_item, null);
            ImageView bike_img = (ImageView)child.findViewById(R.id.bike_img);
            bike_img.setImageDrawable(getResources().getDrawable(bikes[i]));

            pick_up_date = (TextView)child.findViewById(R.id.pick_up_date);
            drop_Off_Date = (TextView)child.findViewById(R.id.drop_Off_Date);
            pick_up_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickupDate();
                }
            });
            drop_Off_Date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dropOffDate();
                }
            });
            if(type.equals("pickUp")) {
                pick_up_date.setText("Pickup Date : "+sdf.format(myCalendar.getTime()));
            } else if(type.equals("drop")){
                drop_Off_Date.setText("DropOff Date : "+sdf.format(myCalendar.getTime()));
            }

            Spinner pickup_time = (Spinner) child.findViewById(R.id.pickup_time);
            ArrayAdapter pickup_timeaa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,time);
            pickup_timeaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            pickup_time.setAdapter(pickup_timeaa);

            Spinner drop_time = (Spinner) child.findViewById(R.id.drop_time);
            ArrayAdapter drop_timeaa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,time);
            drop_timeaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            drop_time.setAdapter(drop_timeaa);

            cartList.addView(child);
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

    String myFormat = "MM/dd/yy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    public void pickupDate(){
        type = "pickUp";
        new DatePickerDialog(Cart.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void dropOffDate(){
        type = "drop";
        new DatePickerDialog(Cart.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        if(type.equals("pickUp")) {

            pick_up_date.setText(sdf.format(myCalendar.getTime()));
        } else {
            drop_Off_Date.setText(sdf.format(myCalendar.getTime()));

        }
    }

}
