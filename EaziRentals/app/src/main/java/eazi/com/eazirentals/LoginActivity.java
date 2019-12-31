package eazi.com.eazirentals;

import android.app.Dialog;
import android.content.ComponentCallbacks2;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import eazi.com.eazirentals.api.APICalls;
import eazi.com.eazirentals.helper.AppSignatureHelper;
import eazi.com.eazirentals.helper.ConstantStrings;
import eazi.com.eazirentals.helper.Constants;
import eazi.com.eazirentals.helper.CustomToast;
import eazi.com.eazirentals.helper.Loader;
import eazi.com.eazirentals.helper.Notify;
import eazi.com.eazirentals.helper.SharedPreference;
import eazi.com.eazirentals.helper.SmsListener;
import eazi.com.eazirentals.helper.SmsReceiver;
import eazi.com.eazirentals.models.GeneralResult;
import eazi.com.eazirentals.models.VerifyOtpResult;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1002;
    private EditText phone;
    private EditText otp;

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
        if(Build.VERSION.SDK_INT < 23){
            //your code here
        } else {
            try {
                requestHint();
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
            requestContactPermission();
        }
        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                String msgArr[] = messageText.split("\\s");
                messageText = msgArr[1];
                //Toast.makeText(Login.this, "Message: " + messageText, Toast.LENGTH_LONG).show();
                otp.setText(messageText);
            }
        });
        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(LoginActivity.this);
        Log.v("KeyHash1234 ", appSignatureHelper.getAppSignatures().get(0));

    }

    @Override
    public void onBackPressed() {
        finish();
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
            otp = (EditText) mBottomSheetDialog.findViewById(R.id.otp);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callVerifyOtpAPI(otp.getText().toString());

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

    private void requestHint() throws IntentSender.SendIntentException {
        requestContactPermission();

        GoogleApiClient apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

    /*    PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
                apiClient, hintRequest);
        startIntentSenderForResult(intent.getIntentSender(),
                RESOLVE_HINT, null, 0, 0, 0);*/
    }
    private void requestContactPermission() {

        SmsRetrieverClient client = SmsRetriever.getClient(this);

// Starts SmsRetriever, waits for ONE matching SMS message until timeout
// (5 minutes).
        Task<Void> task = client.startSmsRetriever();

// Listen for success/failure of the start Task.
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "SMS Permission Failed", Toast.LENGTH_LONG).show();

            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(new SmsReceiver(), intentFilter);
//        int hasContactPermission =ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECEIVE_SMS);
//
//        if(hasContactPermission != PackageManager.PERMISSION_GRANTED ) {
//            ActivityCompat.requestPermissions(this, new String[]
//                    {android.Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_ASK_PERMISSIONS);
//        }else {
//            //Toast.makeText(AddContactsActivity.this, "Contact Permission is already granted", Toast.LENGTH_LONG).show();
//        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                // Check if the only required permission has been granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Contact Permission is Granted",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Contact permission was NOT granted.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public void callVerifyOtpAPI(String s) {
        try {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("mobile_number",phone.getText().toString()));
            params.add(new BasicNameValuePair("otp_code",s));
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
                                SharedPreference.setString(LoginActivity.this, Constants.KEY_USER_NAME,data.getUser_details().getName());
                                SharedPreference.setString(LoginActivity.this, Constants.KEY_ACCESS_TOKEN,data.getUser_details().getAccess_token());
                                Intent i = new Intent(LoginActivity.this,SelectCity.class);
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


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
