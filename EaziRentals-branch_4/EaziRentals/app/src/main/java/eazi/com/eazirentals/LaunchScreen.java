package eazi.com.eazirentals;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import eazi.com.eazirentals.helper.Constants;
import eazi.com.eazirentals.helper.SharedPreference;

public class LaunchScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);
        initUI();
    }

    public void initUI(){

        if(SharedPreference.getBool(this, Constants.KEY_IS_LOGGEDIN)) {
            CountDownTimer c = new CountDownTimer(2000, 1000) {
                public void onFinish() {
                    //Display activity_no internet xml
                    Intent intent = new Intent(LaunchScreen.this, Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);
                }
                public void onTick(long millisUntilFinished) {
                }
            }.start();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }
    }
}
