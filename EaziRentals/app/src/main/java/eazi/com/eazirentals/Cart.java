package eazi.com.eazirentals;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import eazi.com.eazirentals.api.APICalls;
import eazi.com.eazirentals.helper.ConstantStrings;
import eazi.com.eazirentals.helper.Constants;
import eazi.com.eazirentals.helper.CustomToast;
import eazi.com.eazirentals.helper.DataBaseHelper;
import eazi.com.eazirentals.helper.Helper;
import eazi.com.eazirentals.helper.Loader;
import eazi.com.eazirentals.helper.SharedPreference;
import eazi.com.eazirentals.models.BikeList;
import eazi.com.eazirentals.models.CouponCode;
import eazi.com.eazirentals.models.VerifyOtpResult;


public class Cart extends AppCompatActivity implements View.OnClickListener{
    private DataBaseHelper db;
    private EditText coupon_code;
    private TextView couponMessage;
    private Double discount = 0.0;
    private TextView total_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setList();
    }

    public void setList(){
        db = new DataBaseHelper(this);
        ImageView back = (ImageView) findViewById(R.id.back);
        coupon_code = (EditText) findViewById(R.id.coupon_code);
       // coupon_code.setFocusable(false);
        Button add_more = (Button) findViewById(R.id.add_more);
        Button view_more = (Button) findViewById(R.id.view_more);
        total_price = (TextView) findViewById(R.id.total_price);
        Button checkout = (Button) findViewById(R.id.checkout);
        Button apply_coup = (Button) findViewById(R.id.apply_coup);
        back.setOnClickListener(this);
        checkout.setOnClickListener(this);
        view_more.setOnClickListener(this);
        add_more.setOnClickListener(this);
        apply_coup.setOnClickListener(this);
        LinearLayout cartList = (LinearLayout)findViewById(R.id.cartList);
        TextView no_items = (TextView)findViewById(R.id.no_items);
        couponMessage = (TextView)findViewById(R.id.couponMessage);
        LinearLayout bottomView = (LinearLayout)findViewById(R.id.bottomView);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cartList.removeAllViews();
        List<BikeList> bikeListsTemp = db.getAllContacts("");

        double total_amt = calculateTotal()  - discount;
        total_amt = total_amt +  Helper.calculateGst(total_amt);
        total_price.setText("TOTAL : \u20B9 "+total_amt);
        if(bikeListsTemp.size() == 0) {
            bottomView.setVisibility(View.GONE);
            cartList.setVisibility(View.GONE);
            no_items.setVisibility(View.VISIBLE);

        } else {
            bottomView.setVisibility(View.VISIBLE);
            cartList.setVisibility(View.VISIBLE);
            no_items.setVisibility(View.GONE);
        }
        for (int i = 0; i < bikeListsTemp.size(); i++) {
            BikeList bikeList = bikeListsTemp.get(i);

            View child = inflater.inflate(R.layout.cart_item, null);
            ImageView bike_img = (ImageView)child.findViewById(R.id.bike_img);
            RelativeLayout delete_item = (RelativeLayout)child.findViewById(R.id.delete_item);
            TextView bike_name = (TextView)child.findViewById(R.id.bike_name);
            TextView from_date = (TextView)child.findViewById(R.id.from_date);
            TextView to_date = (TextView)child.findViewById(R.id.to_date);
            TextView price = (TextView)child.findViewById(R.id.price);
            Glide.with(this).load(bikeList.getImage()).into(bike_img);
            bike_name.setText(bikeList.getName());
            delete_item.setOnClickListener(this);
            delete_item.setTag(i);
            from_date.setText(Helper.convertDate(bikeList.getFrom(), Constants.format_6,Constants.format_7)+"\n"
                    +Helper.convertDate(bikeList.getFrom(), Constants.format_6,Constants.format_8)+"\n"
                    +Helper.convertDate(bikeList.getFrom(), Constants.format_6,Constants.format_4));
            to_date.setText(Helper.convertDate(bikeList.getTo(), Constants.format_6,Constants.format_7)+"\n"
                    +Helper.convertDate(bikeList.getTo(), Constants.format_6,Constants.format_8)+"\n"
                    +Helper.convertDate(bikeList.getTo(), Constants.format_6,Constants.format_4));

//            int days = (int) Daybetween(bikeList.getFrom(),bikeList.getTo(),Constants.format_6);
//            if(days > 20){
//                double gst = (Double.parseDouble(bikeList.getPrice()) / 100.0f) * 40;
//                discount = discount + gst;
//            } else if(days > 29) {
//                double gst = (Double.parseDouble(bikeList.getPrice()) / 100.0f) * 45;
//                discount = discount + gst;
//            }
//            System.out.println("NoOFDAYS "
//                    +Daybetween(bikeList.getFrom(),bikeList.getTo(),Constants.format_6) + "discount "+discount);

            price.setText("\u20B9 "+String.format("%.2f", Double.parseDouble(bikeList.getPrice())));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 10, 10);
            cartList.addView(child,layoutParams);
        }
    }

    public long Daybetween(String date1,String date2,String pattern)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        Date Date1 = null,Date2 = null;
        try{
            Date1 = sdf.parse(date1);
            Date2 = sdf.parse(date2);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return (Date2.getTime() - Date1.getTime())/(24*60*60*1000);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.delete_item) {
            int i = (Integer)v.getTag();
            List<BikeList> bikeListsTemp = db.getAllContacts("");
            db.deleteFromCart(bikeListsTemp.get(i).getId());
            setList();
        } else if(v.getId() == R.id.add_more) {
            Intent i = new Intent(Cart.this,SelectBike.class);
            i.putExtra(Constants.PICKUP_TIME,getIntent().getStringExtra(Constants.PICKUP_TIME));
            i.putExtra(Constants.PICKUP_DATE,getIntent().getStringExtra(Constants.PICKUP_DATE));
            i.putExtra(Constants.DROP_TIME,getIntent().getStringExtra(Constants.DROP_TIME));
            i.putExtra(Constants.DROP_DATE,getIntent().getStringExtra(Constants.DROP_DATE));
            i.putExtra(Constants.PICKUP_LOCATION,getIntent().getStringExtra(Constants.PICKUP_LOCATION));
            startActivity(i);
        }  else if(v.getId() == R.id.checkout) {
            Intent i =new Intent(Cart.this,CartDetails.class);
            i.putExtra(Constants.SUB_TOTAL,calculateTotal());
            i.putExtra(Constants.DISCOUNT,discount);
            if(discount != 0) {
                i.putExtra(Constants.COUPONCODE, coupon_code.getText().toString());
            }
            startActivity(i);
        }  else if(v.getId() == R.id.view_more) {
            priceDetails();
        } else if(v.getId() == R.id.apply_coup) {
            callApplyCoupon();
        } else {
            super.onBackPressed();

        }
    }

    public double calculateTotal(){
        double price =0;
        List<BikeList> bikeListsTemp = db.getAllContacts("");

        for(int i =0;i<bikeListsTemp.size();i++) {
            price = price + Double.parseDouble(bikeListsTemp.get(i).getPrice());
        }
        return price;
    }

    public void priceDetails() {
        try {
            final Dialog mBottomSheetDialog = new Dialog(this);
            mBottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mBottomSheetDialog.setContentView(R.layout.pricedeatils);
            mBottomSheetDialog.setCanceledOnTouchOutside(false);
            mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mBottomSheetDialog.show();
            mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
            Button submit = (Button)mBottomSheetDialog.findViewById(R.id.submit);
            TextView sub_total = (TextView)mBottomSheetDialog.findViewById(R.id.sub_total);
            TextView _discount = (TextView)mBottomSheetDialog.findViewById(R.id.discount);
            TextView gst = (TextView)mBottomSheetDialog.findViewById(R.id.gst);
            TextView total = (TextView)mBottomSheetDialog.findViewById(R.id.total);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i =new Intent(Cart.this,CartDetails.class);
                    i.putExtra(Constants.SUB_TOTAL,calculateTotal());
                    startActivity(i);

                }
            });
            sub_total.setText("\u20B9 " + calculateTotal());
            _discount.setText("\u20B9 " + discount);
            double total_amt = calculateTotal()  - discount;
            gst.setText("\u20B9 " + String.format("%.2f", Helper.calculateGst(total_amt)));
            total_amt =  total_amt + Helper.calculateGst(total_amt);
            total.setText("\u20B9 " +String.format("%.2f", total_amt) );
        } catch (Exception e){

        }
    }

    public void callApplyCoupon() {
        try {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();
            double total_amt = calculateTotal() ;

            params.add(new BasicNameValuePair("user_id", SharedPreference.getString(Cart.this, Constants.KEY_USER_ID)));
            params.add(new BasicNameValuePair("access_token",SharedPreference.getString(Cart.this,Constants.KEY_ACCESS_TOKEN)));
            params.add(new BasicNameValuePair("invoice_amount",String.valueOf(total_amt)));
            params.add(new BasicNameValuePair("coupon_code",coupon_code.getText().toString()));

            Loader.show(this);

            APICalls.invokeAPIEx(this,"Post",APICalls.SERVICE_URL+APICalls.APPLY_COUPON,params,new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    try {
                        if (message != null && message.obj != null) {
                            String result = (String) message.obj;
                            CouponCode data = new Gson().fromJson(result, CouponCode.class);
                            System.out.println("CartDetails2 callAPI Response " + result + " status " + data.getCoupon_details().getStatus());

                            if (data.getCoupon_details() != null && data.getCoupon_details().getStatus().equalsIgnoreCase("success")) {
                                if(Double.parseDouble(data.getCoupon_details().getDiscount_amount()) != 0) {
                                    couponMessage.setTextColor(getApplicationContext().getResources().getColor(R.color.green));
                                } else {
                                    couponMessage.setTextColor(Color.RED);
                                }
                                discount = Double.parseDouble(data.getCoupon_details().getDiscount_amount());
                                couponMessage.setText(data.getCoupon_details().getMessage());
                                double total_amt = calculateTotal()   - discount;
                                total_amt = total_amt + Helper.calculateGst(total_amt);
                                total_price.setText("TOTAL : \u20B9 "+String.format("%.2f", total_amt));
                            } else {
                                if (data.getCoupon_details().getMessage() != null) {
                                } else {
                                    new CustomToast().Show_Toast(Cart.this, ConstantStrings.common_msg, R.color.light_red2);
                                }
                            }
                        } else {
                            new CustomToast().Show_Toast(Cart.this, ConstantStrings.common_msg, R.color.light_red2);

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
