package eazi.com.eazirentals;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import eazi.com.eazirentals.models.VerifyOtpResult;

/**
 * Created by tejaswini on 03/06/18.
 */

public class Register extends AppCompatActivity implements View.OnClickListener{

    private EditText name,phone,email,password,rpassword;
    private TextView title;
    private boolean isRegister,ISFromCart;
    private VerifyOtpResult data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_1);
        initUI();
    }

    public void initUI(){

        title = (TextView)findViewById(R.id.title);
        name = (EditText)findViewById(R.id.name);
        phone = (EditText)findViewById(R.id.phone);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        rpassword = (EditText)findViewById(R.id.rpassword);
        isRegister = getIntent().getBooleanExtra(Constants.ISRegister,true);
        ISFromCart = getIntent().getBooleanExtra(Constants.ISFromCart,false);
        if(isRegister) {
            title.setVisibility(View.VISIBLE);
            phone.setVisibility(View.VISIBLE);
            password.setVisibility(View.VISIBLE);
            rpassword.setVisibility(View.VISIBLE);
        } else {
            callGetUSerprofileAPI();
            title.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            rpassword.setVisibility(View.GONE);
        }
        Button signup =(Button)findViewById(R.id.signup);
        signup.setOnClickListener(this);
    }

    public void setUSerDetails() {
        name.setText(data.getUser_details().getName());
        email.setText(data.getUser_details().getEmail_id());
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.signup) {
            Intent intent = new Intent(this, Register2.class);

            if(isRegister) {
                if (validate()) {
                    intent.putExtra(Constants.USER_NAME, name.getText().toString());
                    intent.putExtra(Constants.USER_PHONE, phone.getText().toString());
                    intent.putExtra(Constants.USER_EMAIL, email.getText().toString());
                    intent.putExtra(Constants.USER_PASSWORD, password.getText().toString());
                    startActivity(intent);

                }
            } else {
                intent.putExtra(Constants.ISRegister,false);
                intent.putExtra(Constants.ISFromCart,ISFromCart);
                double subtotal = getIntent().getDoubleExtra(Constants.SUB_TOTAL, 0);
                intent.putExtra(Constants.SUB_TOTAL,subtotal);
                intent.putExtra(Constants.USER_NAME, name.getText().toString());
                intent.putExtra(Constants.USER_EMAIL, email.getText().toString());
                intent.putExtra(Constants.DRIVINGLICENCE, data.getUser_details().getDriving_no());
                intent.putExtra(Constants.ADDRESS, data.getUser_details().getAddress());
                intent.putExtra(Constants.STATE, data.getUser_details().getState());
                intent.putExtra(Constants.CITY, data.getUser_details().getCity());
                startActivity(intent);
            }
        }
    }

    public boolean validate() {
        if(!Helper.isValidString(name.getText().toString())){
            new CustomToast().Show_Toast(this, ConstantStrings.invalid_name, R.color.light_red2);
            return false;
        }
        if(!Helper.isValidMobile(phone.getText().toString())){
            new CustomToast().Show_Toast(this, ConstantStrings.invalid_phone, R.color.light_red2);
            return false;
        }
        if(!Helper.isValidEmail(email.getText().toString())){
            new CustomToast().Show_Toast(this, ConstantStrings.invalid_email, R.color.light_red2);
            return false;
        }
        if(!Helper.isValidPassword(password.getText().toString())){
            new CustomToast().Show_Toast(this, ConstantStrings.invalid_password, R.color.light_red2);
            return false;
        }
        if(!password.getText().toString().equals(rpassword.getText().toString())){
            new CustomToast().Show_Toast(this, ConstantStrings.invalid_re_password, R.color.light_red2);
            return false;
        }

        return true;
    }

    public void callGetUSerprofileAPI() {
        try {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("user_id", SharedPreference.getString(Register.this,Constants.KEY_USER_ID)));
            params.add(new BasicNameValuePair("access_token",SharedPreference.getString(Register.this,Constants.KEY_ACCESS_TOKEN)));

            Loader.show(this);

            APICalls.invokeAPIEx(this,"Post",APICalls.SERVICE_URL+APICalls.USERPROFILE,params,new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    try {
                        if (message != null && message.obj != null) {
                            String result = (String) message.obj;
                            data = new Gson().fromJson(result, VerifyOtpResult.class);
                            System.out.println("Register2 callAPI Response " + result + " status " + data.getStatus());

                            if (data.getStatus() != null && data.getStatus().equalsIgnoreCase("success")) {
                                setUSerDetails();
                            } else {
                                if (data.getMessage() != null) {
                                    new CustomToast().Show_Toast(Register.this, data.getMessage(), R.color.light_red2);

                                } else {
                                    new CustomToast().Show_Toast(Register.this, ConstantStrings.common_msg, R.color.light_red2);
                                }
                            }
                        } else {
                            new CustomToast().Show_Toast(Register.this, ConstantStrings.common_msg, R.color.light_red2);

                        }
                    } catch (Exception e){
                        System.out.println("Register2 callAPI Response Exception " + e.toString());

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("Register2 callAPI Exception "+e.toString());
        }

    }


}
