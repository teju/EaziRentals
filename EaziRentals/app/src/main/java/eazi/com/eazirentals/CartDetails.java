package eazi.com.eazirentals;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

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
import eazi.com.eazirentals.models.BikeList;
import eazi.com.eazirentals.models.CheckOutResultResult;
import eazi.com.eazirentals.models.GeneralResult;
import eazi.com.eazirentals.models.PaymentConfirmation;
import eazi.com.eazirentals.models.VerifyOtpResult;

public class CartDetails extends AppCompatActivity implements View.OnClickListener,PaymentResultListener {
    private DataBaseHelper db;
    private VerifyOtpResult data;
    private TextView name,email_id,mobile_number,address,city,state,dl,sub_total,discount,gst,total,terms;
    private Button edit,continue_to_pay,view_maps;
    private CheckBox agree;
    private double subtotal = 0;
    private CheckOutResultResult generalResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_details);
        initUI();
    }

    private void initUI() {
        subtotal = getIntent().getDoubleExtra(Constants.SUB_TOTAL,0);

        db = new DataBaseHelper(this);
        ImageView back = (ImageView) findViewById(R.id.back);
        name = (TextView) findViewById(R.id.name);
        terms = (TextView) findViewById(R.id.terms);
        email_id = (TextView) findViewById(R.id.email_id);
        mobile_number = (TextView) findViewById(R.id.mobile_number);
        address = (TextView) findViewById(R.id.address);
        city = (TextView) findViewById(R.id.city);
        state = (TextView) findViewById(R.id.state);
        dl = (TextView) findViewById(R.id.dl);
        edit = (Button) findViewById(R.id.edit);
        continue_to_pay = (Button) findViewById(R.id.continue_to_pay);
        view_maps = (Button) findViewById(R.id.view_maps);
        view_maps = (Button) findViewById(R.id.view_maps);
        agree = (CheckBox)findViewById(R.id.agree);
        sub_total = (TextView)findViewById(R.id.sub_total);
        discount = (TextView)findViewById(R.id.discount);
        gst = (TextView)findViewById(R.id.gst);
        total = (TextView)findViewById(R.id.total);
        edit.setOnClickListener(this);
        continue_to_pay.setOnClickListener(this);
        view_maps.setOnClickListener(this);
        terms.setOnClickListener(this);
        back.setOnClickListener(this);
        callGetUSerprofileAPI();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i =new Intent(CartDetails.this,Cart.class);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back) {
            super.onBackPressed();
        } else  if(v.getId() == R.id.edit) {
            Intent i = new Intent(CartDetails.this,Register.class);
            i.putExtra(Constants.ISRegister,false);
            i.putExtra(Constants.SUB_TOTAL,subtotal);

            startActivity(i);
        } else  if(v.getId() == R.id.view_maps) {
            String map = "http://maps.google.co.in/maps?q=" + getResources().getString(R.string.pick_address);

            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(map));
            startActivity(intent);
        } else  if(v.getId() == R.id.terms) {

            Intent intent = new Intent(this,TermsConditions.class);
            startActivity(intent);
        } else  if(v.getId() == R.id.continue_to_pay) {
            if(!agree.isChecked()){
                new CustomToast().Show_Toast(this, ConstantStrings.agree_terms_conditions, R.color.light_red2);
            } else {
               callCheckoutAPI();
            }
        }
    }

    public void callGetUSerprofileAPI() {
        try {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("user_id", SharedPreference.getString(CartDetails.this, Constants.KEY_USER_ID)));
            params.add(new BasicNameValuePair("access_token",SharedPreference.getString(CartDetails.this,Constants.KEY_ACCESS_TOKEN)));

            Loader.show(this);

            APICalls.invokeAPIEx(this,"Post",APICalls.SERVICE_URL+APICalls.USERPROFILE,params,new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    try {
                        if (message != null && message.obj != null) {
                            String result = (String) message.obj;
                            data = new Gson().fromJson(result, VerifyOtpResult.class);
                            System.out.println("CartDetails2 callAPI Response " + result + " status " + data.getStatus());

                            if (data.getStatus() != null && data.getStatus().equalsIgnoreCase("success")) {
                                setUSerDetails();
                            } else {
                                if (data.getMessage() != null) {
                                    new CustomToast().Show_Toast(CartDetails.this, data.getMessage(), R.color.light_red2);

                                } else {
                                    new CustomToast().Show_Toast(CartDetails.this, ConstantStrings.common_msg, R.color.light_red2);
                                }
                            }
                        } else {
                            new CustomToast().Show_Toast(CartDetails.this, ConstantStrings.common_msg, R.color.light_red2);

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

    public void callCheckoutAPI() {
        try {

            List<BikeList> bikeListsTemp = db.getAllContacts("");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", SharedPreference.getString(CartDetails.this, Constants.KEY_USER_ID));
            jsonObject.put("access_token", SharedPreference.getString(CartDetails.this, Constants.KEY_ACCESS_TOKEN));
            jsonObject.put("coupon_code", getIntent().getStringExtra(Constants.COUPONCODE));
            JSONArray jsonArray = new JSONArray();
            for (int i =0;i<bikeListsTemp.size();i++){
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id",bikeListsTemp.get(i).getId());
                jsonObject1.put("name",bikeListsTemp.get(i).getName());
                jsonObject1.put("image",bikeListsTemp.get(i).getImage());
                jsonObject1.put("price",bikeListsTemp.get(i).getPrice());
                jsonObject1.put("branch",bikeListsTemp.get(i).getBranch());
                jsonObject1.put("from",bikeListsTemp.get(i).getFrom());
                jsonObject1.put("to",bikeListsTemp.get(i).getTo());
                jsonObject1.put("km",bikeListsTemp.get(i).getKm());
                jsonArray.put(jsonObject1);
            }
            jsonObject.put("booking_details", jsonArray);
            System.out.println("callCheckoutAPI callAPI Response bikeListsTemp " + jsonArray.toString());

            Loader.show(this);

            APICalls.invokeAPIEx(this,"Post",APICalls.SERVICE_URL+APICalls.CHECKOUT,jsonObject.toString(),new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    try {
                        if (message != null && message.obj != null) {
                            String result = (String) message.obj;
                            generalResult = new Gson().fromJson(result, CheckOutResultResult.class);

                            if (generalResult.getStatus() != null && generalResult.getStatus().equalsIgnoreCase("success")) {
                                Checkout.preload(getApplicationContext());
                                startPayment();
                            } else {
                                if (generalResult.getMessage() != null) {
                                    new CustomToast().Show_Toast(CartDetails.this, generalResult.getMessage(), R.color.light_red2);

                                } else {
                                    new CustomToast().Show_Toast(CartDetails.this, ConstantStrings.common_msg, R.color.light_red2);
                                }
                            }
                        } else {
                            new CustomToast().Show_Toast(CartDetails.this, ConstantStrings.common_msg, R.color.light_red2);

                        }
                    } catch (Exception e){
                        System.out.println("callCheckoutAPI callAPI Response Exception " + e.toString());

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("CartDetails2 callAPI Exception "+e.toString());
        }

    }

    private void setUSerDetails() {
        name.setText(data.getUser_details().getName());
        email_id.setText(data.getUser_details().getEmail_id());
        mobile_number.setText(SharedPreference.getString(CartDetails.this, Constants.KEY_MOBILE_NO));
        address.setText(data.getUser_details().getAddress());
        city.setText(data.getUser_details().getCity());
        state.setText(data.getUser_details().getState());
        dl.setText(data.getUser_details().getDriving_no());
        sub_total.setText("\u20B9 " + (subtotal));
        discount.setText("\u20B9 " + getIntent().getDoubleExtra(Constants.DISCOUNT,0.0));
        gst.setText("\u20B9 " + String.format("%.2f", Helper.calculateGst(subtotal)));
        double total_amt = subtotal + Helper.calculateGst(subtotal) - getIntent().getDoubleExtra(Constants.DISCOUNT,0.0);
        total.setText("\u20B9 " + total_amt);
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", data.getUser_details().getName());
            options.put("description", "Demoing Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("currency", "INR");
            options.put("amount", "100");

            JSONObject preFill = new JSONObject();
            preFill.put("email", data.getUser_details().getEmail_id());
            preFill.put("contact", "9964062237");

            options.put("prefill", preFill);

            co.open(activity, options);
            System.out.println("Payment1234 preFill "+options.toString());

        } catch (Exception e) {
            System.out.println("Payment1234 Exception "+e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        System.out.println("Payment1234 onPaymentSuccess "+s);
        callPaymentConfirmationAPI(s, "success");
    }

    public void callPaymentConfirmationAPI(final String razorpay_order_id, final String status) {
        try {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("user_id", SharedPreference.getString(CartDetails.this, Constants.KEY_USER_ID)));
            params.add(new BasicNameValuePair("access_token",SharedPreference.getString(CartDetails.this,Constants.KEY_ACCESS_TOKEN)));
            params.add(new BasicNameValuePair("invoice_id",generalResult.getInvoice_id()));
            params.add(new BasicNameValuePair("payment_status",status));
            params.add(new BasicNameValuePair("razorpay_order_id",razorpay_order_id));

            Loader.show(this);

            APICalls.invokeAPIEx(this,"Post",APICalls.SERVICE_URL+APICalls.PAYMENT_CONFIRMATION,params,new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    try {
                        if (message != null && message.obj != null) {
                            String result = (String) message.obj;
                            PaymentConfirmation generalResult = new Gson().fromJson(result, PaymentConfirmation.class);
                            System.out.println("CartDetails2 callAPI Response " + result + " status " + data.getStatus());

                            if (generalResult.getPayment_info().getStatus() != null ) {
                                if(!status.equals("fail")) {
                                    db.deleteFromCart("");
                                }
                                Intent intent = new Intent(CartDetails.this,PaymentSuccess.class);
                                intent.putExtra(Constants.TOTAL,total.getText().toString());
                                intent.putExtra(Constants.PAYMENTID,razorpay_order_id);
                                intent.putExtra(Constants.STATUS,status);
                                startActivity(intent);

                            } else {
                                if (generalResult.getPayment_info().getMessage() != null) {
                                    new CustomToast().Show_Toast(CartDetails.this, generalResult.getPayment_info().getMessage(), R.color.light_red2);

                                } else {
                                    new CustomToast().Show_Toast(CartDetails.this, ConstantStrings.common_msg, R.color.light_red2);
                                }
                            }
                        } else {
                            new CustomToast().Show_Toast(CartDetails.this, ConstantStrings.common_msg, R.color.light_red2);

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

    @Override
    public void onPaymentError(int i, String s) {
        System.out.println("Payment1234 onPaymentError "+s);
        callPaymentConfirmationAPI(s,"fail");

    }
}
