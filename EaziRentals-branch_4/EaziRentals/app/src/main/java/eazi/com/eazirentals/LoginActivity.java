package eazi.com.eazirentals;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import eazi.com.eazirentals.helper.Loader;
import eazi.com.eazirentals.helper.SharedPreference;
import eazi.com.eazirentals.models.GeneralResult;
import eazi.com.eazirentals.models.VerifyOtpResult;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    public void initUI (){
        phone = (EditText)findViewById(R.id.phone);
        Button login_button =(Button)findViewById(R.id.login_button);
        TextView sign_up =(TextView)findViewById(R.id.sign_up);
        login_button.setOnClickListener(this);
        sign_up.setOnClickListener(this);
    }

    public void otpDialog() {
        try {
            final Dialog mBottomSheetDialog = new Dialog(this);
            mBottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mBottomSheetDialog.setContentView(R.layout.otp);
            mBottomSheetDialog.setCanceledOnTouchOutside(false);
            mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mBottomSheetDialog.show();
            mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
            Button submit = (Button)mBottomSheetDialog.findViewById(R.id.submit);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callVerifyOtpAPI();

                }
            });
        } catch (Exception e){

        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sign_up) {
            Intent i = new Intent(LoginActivity.this,Register.class);
            startActivity(i);
            finish();
        } else if(v.getId() == R.id.login_button) {
            callLoginAPI();
        }
    }

    public void callLoginAPI() {
        try {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mobile_number",phone.getText().toString()));
            Loader.show(this);

            APICalls.invokeAPIEx(this,"Post",APICalls.SERVICE_URL+APICalls.OTP,params,new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    try {
                        if (message != null && message.obj != null) {
                            String result = (String) message.obj;
                            GeneralResult data = new Gson().fromJson(result, GeneralResult.class);
                            System.out.println("LoginActivity callAPI Response " + result + " status " + data.getStatus());

                            if (data.getStatus() != null && data.getStatus().equalsIgnoreCase("success")) {
                                otpDialog();
                            } else {
                                if (data.getMessage() != null) {
                                    new CustomToast().Show_Toast(LoginActivity.this, data.getMessage(), R.color.light_red2);

                                } else {
                                    new CustomToast().Show_Toast(LoginActivity.this, ConstantStrings.common_msg, R.color.light_red2);
                                }
                            }
                        } else {
                            new CustomToast().Show_Toast(LoginActivity.this, ConstantStrings.common_msg, R.color.light_red2);

                        }
                    } catch (Exception e){
                        System.out.println("LoginActivity callAPI Response Exception " + e.toString());

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("LoginActivity callAPI Exception "+e.toString());
        }

    }

    public void callVerifyOtpAPI() {
        try {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mobile_number",phone.getText().toString()));
            params.add(new BasicNameValuePair("otp_code","123456"));
            Loader.show(this);

            APICalls.invokeAPIEx(this,"Post",APICalls.SERVICE_URL+APICalls.OTPVERIFY,params,new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    try {
                        if (message != null && message.obj != null) {
                            String result = (String) message.obj;
                            VerifyOtpResult data = new Gson().fromJson(result, VerifyOtpResult.class);
                            System.out.println("LoginActivity callAPI Response " + result + " status " + data.getStatus());

                            if (data.getStatus() != null && data.getStatus().equalsIgnoreCase("success")) {
                                SharedPreference.setBool(LoginActivity.this,Constants.KEY_IS_LOGGEDIN,true);
                                SharedPreference.setString(LoginActivity.this, Constants.KEY_USER_ID,data.getUser_details().getUser_id());
                                SharedPreference.setString(LoginActivity.this, Constants.KEY_ACCESS_TOKEN,data.getUser_details().getAccess_token());
                                Intent i = new Intent(LoginActivity.this,Home.class);
                                startActivity(i);
                                finish();
                            } else {
                                if (data.getMessage() != null) {
                                    new CustomToast().Show_Toast(LoginActivity.this, data.getMessage(), R.color.light_red2);

                                } else {
                                    new CustomToast().Show_Toast(LoginActivity.this, ConstantStrings.common_msg, R.color.light_red2);
                                }
                            }
                        } else {
                            new CustomToast().Show_Toast(LoginActivity.this, ConstantStrings.common_msg, R.color.light_red2);

                        }
                    } catch (Exception e){
                        System.out.println("LoginActivity callAPI Response Exception " + e.toString());

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("LoginActivity callAPI Exception "+e.toString());
        }

    }


}
