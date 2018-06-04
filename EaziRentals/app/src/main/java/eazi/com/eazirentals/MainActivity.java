package eazi.com.eazirentals;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login_button =(Button)findViewById(R.id.login_button);
        TextView sign_up =(TextView)findViewById(R.id.sign_up);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpDialog();
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Register.class);
                startActivity(i);
                finish();
            }
        });
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
                    Intent i = new Intent(MainActivity.this,Home.class);
                    startActivity(i);
                    finish();
                }
            });
        } catch (Exception e){

        }

    }
}
