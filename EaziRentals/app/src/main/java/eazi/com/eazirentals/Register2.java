package eazi.com.eazirentals;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

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
import eazi.com.eazirentals.models.GeneralResult;
import eazi.com.eazirentals.models.StateResult;

public class Register2 extends AppCompatActivity implements View.OnClickListener{
    String[] city_list = {"Bengaluru" , "Mysore"};
    private EditText address,dl;
    int state_selected_pos = 0;
    int city_selected_pos = 0;
    private CheckBox agree;
    private StateResult stateResult;
    private boolean isRegister;
    private boolean isFromCart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_2);
        initUI();
    }

    public void initUI(){
        isRegister = getIntent().getBooleanExtra(Constants.ISRegister,true);
        isFromCart = getIntent().getBooleanExtra(Constants.ISFromCart,false);
        callStatesAPI();

        address = (EditText)findViewById(R.id.address);
        LinearLayout terms_conditions = (LinearLayout) findViewById(R.id.terms_conditions);
        dl = (EditText)findViewById(R.id.dl);
        agree = (CheckBox)findViewById(R.id.agree);
        Button submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(this);
        if(!isRegister) {
            setUSerDetails();
            terms_conditions.setVisibility(View.GONE);
            submit.setText("Update");
        }
    }

    public void setUSerDetails() {
        dl.setText(getIntent().getStringExtra(Constants.DRIVINGLICENCE));
        address.setText(getIntent().getStringExtra(Constants.ADDRESS));
    }

    public void pickTime(){
        Spinner state = (Spinner) findViewById(R.id.state);
        ArrayAdapter state_adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,stateResult.getStates());
        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        state.setAdapter(state_adapter);
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state_selected_pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner city = (Spinner) findViewById(R.id.city);
        ArrayAdapter drop_timeaa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,city_list);
        drop_timeaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        city.setAdapter(drop_timeaa);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_selected_pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (getIntent().getStringExtra(Constants.STATE) != null) {
            int spinnerPosition = state_adapter.getPosition(getIntent().getStringExtra(Constants.STATE));
            System.out.println("Register2 spinnerPosition "+spinnerPosition+" STATE "+getIntent().getStringExtra(Constants.STATE));
            state.setSelection(spinnerPosition);
        }
    }

    public boolean validate() {
        if(!Helper.isValidString(address.getText().toString())){
            new CustomToast().Show_Toast(this, ConstantStrings.invalid_address, R.color.light_red2);
            return false;
        }
        if(!Helper.isValidString(dl.getText().toString())){
            new CustomToast().Show_Toast(this, ConstantStrings.invalid_dl, R.color.light_red2);
            return false;
        }
        if(!agree.isChecked()){
            new CustomToast().Show_Toast(this, ConstantStrings.agree_terms_conditions, R.color.light_red2);
            return false;
        }



        return true;
    }

    public void callStatesAPI() {
        try {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();

            Loader.show(this);
            APICalls.invokeAPIEx(this,"Get",APICalls.SERVICE_URL+APICalls.STATE,params,new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    try {
                        if (message != null && message.obj != null) {
                            String result = (String) message.obj;
                            stateResult = new Gson().fromJson(result, StateResult.class);
                            System.out.println("Register2 callStatesAPI Response " + result + " status " + stateResult.getStatus());

                            if (stateResult.getStatus() != null && stateResult.getStatus().equalsIgnoreCase("true")) {
                                pickTime();
                            } else {
                                if (stateResult.getMessage() != null) {
                                    new CustomToast().Show_Toast(Register2.this, stateResult.getMessage(), R.color.light_red2);

                                } else {
                                    new CustomToast().Show_Toast(Register2.this, ConstantStrings.common_msg, R.color.light_red2);
                                }
                            }
                        } else {
                            new CustomToast().Show_Toast(Register2.this, ConstantStrings.common_msg, R.color.light_red2);

                        }
                    } catch (Exception e){
                        System.out.println("Register2 callStatesAPI Response Exception " + e.toString());

                    }
                    return false;
                }
            }));
        } catch (Exception e){
            System.out.println("Register2 callAPI Exception "+e.toString());
        }
    }

    public void callSignUPAPI() {
        try {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();
            String url = APICalls.SIGNUP;
            if(isRegister ) {
                params.add(new BasicNameValuePair("mobile_number", getIntent().getStringExtra(Constants.USER_PHONE)));
                params.add(new BasicNameValuePair("password", getIntent().getStringExtra(Constants.USER_PASSWORD)));
                params.add(new BasicNameValuePair("comfirm_password", getIntent().getStringExtra(Constants.USER_PASSWORD)));
                if (agree.isChecked()) {
                    params.add(new BasicNameValuePair("terms_condition", "1"));
                } else {
                    params.add(new BasicNameValuePair("terms_condition", "0"));
                }
            }  else {
                url = APICalls.USERPROFILEUPDATE;
                params.add(new BasicNameValuePair("user_id", SharedPreference.getString(Register2.this,Constants.KEY_USER_ID)));
                params.add(new BasicNameValuePair("access_token",SharedPreference.getString(Register2.this,Constants.KEY_ACCESS_TOKEN)));
            }

            params.add(new BasicNameValuePair("address", address.getText().toString()));
            params.add(new BasicNameValuePair("state", stateResult.getStates().get(state_selected_pos)));
            params.add(new BasicNameValuePair("city", city_list[city_selected_pos]));

            params.add(new BasicNameValuePair("driving_licence", dl.getText().toString()));
            params.add(new BasicNameValuePair("email_id", getIntent().getStringExtra(Constants.USER_EMAIL)));
            params.add(new BasicNameValuePair("full_name", getIntent().getStringExtra(Constants.USER_NAME)));
            Loader.show(this);

            APICalls.invokeAPIEx(this,"Post",APICalls.SERVICE_URL+url,params,new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    try {
                        if (message != null && message.obj != null) {
                            String result = (String) message.obj;
                            GeneralResult data = new Gson().fromJson(result, GeneralResult.class);
                            System.out.println("Register2callAPIResponse "
                                    + result + " status " + data.getStatus()+" isFromCart "+isFromCart);

                            if (data.getStatus() != null && data.getStatus().equalsIgnoreCase("true") ||
                                    data.getStatus() != null && data.getStatus().equalsIgnoreCase("success") ) {
                                if(isRegister) {
                                    new CustomToast().Show_Toast(Register2.this, data.getMessage(), R.color.green);
                                    Intent i = new Intent(Register2.this, LoginActivity.class);
                                    startActivity(i);
                                } else {
                                    System.out.println("Register2callAPIResponseisFromCart "+isFromCart);
                                    new CustomToast().Show_Toast(Register2.this, data.getMessage(), R.color.green);
                                    if(!isFromCart) {
                                        Intent i = new Intent(Register2.this, SelectCity.class);
                                        startActivity(i);
                                    } else {
                                        Intent i =new Intent(Register2.this,CartDetails.class);
                                        double subtotal = getIntent().getDoubleExtra(Constants.SUB_TOTAL, 0);
                                        i.putExtra(Constants.SUB_TOTAL,subtotal);
                                        startActivity(i);
                                    }
                                }
                            } else {
                                if (data.getMessage() != null) {
                                    new CustomToast().Show_Toast(Register2.this, data.getMessage(), R.color.light_red2);

                                } else {
                                    new CustomToast().Show_Toast(Register2.this, ConstantStrings.common_msg, R.color.light_red2);
                                }
                            }
                        } else {
                            new CustomToast().Show_Toast(Register2.this, ConstantStrings.common_msg, R.color.light_red2);

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

    @Override
    public void onClick(View v) {
        System.out.println("Register2 onClick "+isRegister);

        if(isRegister) {
            if(validate()) {
                callSignUPAPI();
            }
        } else {
            callSignUPAPI();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isFromCart) {
            Intent i =new Intent(Register2.this,CartDetails.class);
            startActivity(i);
        } else {
            super.onBackPressed();

        }
    }
}
