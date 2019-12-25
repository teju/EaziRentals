package eazi.com.eazirentals;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import eazi.com.eazirentals.api.APICalls;
import eazi.com.eazirentals.helper.ConstantStrings;
import eazi.com.eazirentals.helper.Constants;
import eazi.com.eazirentals.helper.CustomToast;
import eazi.com.eazirentals.helper.Helper;
import eazi.com.eazirentals.helper.Loader;
import eazi.com.eazirentals.helper.SharedPreference;
import eazi.com.eazirentals.models.CMSResponse;
import eazi.com.eazirentals.models.GeneralResult;

public class ContactUs extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1001;
    private GeneralResult data;
    private EditText name,email,mobile,message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        initUI();
    }

    private void initUI() {
        ImageView back = (ImageView) findViewById(R.id.back);
        TextView phone = (TextView) findViewById(R.id.phone);
        TextView send_email = (TextView) findViewById(R.id.send_email);
        Button open_map = (Button) findViewById(R.id.open_map);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.mobile);
        message = (EditText) findViewById(R.id.message);
        Button send_message = (Button) findViewById(R.id.send_message);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactUs.super.onBackPressed();
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
            }
        });
        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email();
            }
        });
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    sendContactUS();
                }
            }
        });
        open_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMAps();
            }
        });
    }
    public boolean validate() {
        if(!Helper.isValidString(name.getText().toString())){
            new CustomToast().Show_Toast(this, ConstantStrings.invalid_name, R.color.light_red2);
            return false;
        }
        if(!Helper.isValidEmail(email.getText().toString())){
            new CustomToast().Show_Toast(this, ConstantStrings.invalid_email, R.color.light_red2);
            return false;
        }
        if(!Helper.isValidMobile(mobile.getText().toString())){
            new CustomToast().Show_Toast(this, ConstantStrings.invalid_phone, R.color.light_red2);
            return false;
        }

        if(!Helper.isValidString(message.getText().toString())){
            new CustomToast().Show_Toast(this, ConstantStrings.invalid_meaage, R.color.light_red2);
            return false;
        }
        return true;
    }

    public void goToMAps() {
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 12.365420, 76.613710);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);

    }
    public void call() {
        String number = ("tel:" + "91 8047091579");
        Intent mIntent = new Intent(Intent.ACTION_CALL);
        mIntent.setData(Uri.parse(number));
// Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);

            // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            //You already have permission
            try {
                startActivity(mIntent);
            } catch(SecurityException e) {
                e.printStackTrace();
            }
        }
    }
    public void email() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@eezeerentals.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }

    public void sendContactUS() {
        try {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("user_id", SharedPreference.getString(ContactUs.this, Constants.KEY_USER_ID)));
            params.add(new BasicNameValuePair("access_token",SharedPreference.getString(ContactUs.this,Constants.KEY_ACCESS_TOKEN)));
            params.add(new BasicNameValuePair("form_name",name.getText().toString()));
            params.add(new BasicNameValuePair("form_email",email.getText().toString()));
            params.add(new BasicNameValuePair("form_phone",mobile.getText().toString()));
            params.add(new BasicNameValuePair("form_subject","Contact Us"));
            params.add(new BasicNameValuePair("form_message",message.getText().toString()));

            Loader.show(this);

            APICalls.invokeAPIEx(this,"Post",APICalls.SERVICE_URL+APICalls.CONTACTUS,params,new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    try {
                        if (message != null && message.obj != null) {
                            String result = (String) message.obj;
                            data = new Gson().fromJson(result, GeneralResult.class);
                            System.out.println("SelectBike callAPI Response " + result + " status " + data.getStatus());

                            if (data.getStatus() != null && data.getStatus().equalsIgnoreCase("success")) {
                                new CustomToast().Show_Toast(ContactUs.this, data.getMessage(), R.color.light_red2);
                                ContactUs.super.onBackPressed();
                            }

                        } else {
                            new CustomToast().Show_Toast(ContactUs.this, ConstantStrings.common_msg, R.color.light_red2);
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
