package eazi.com.eazirentals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import eazi.com.eazirentals.helper.Constants;

public class PaymentSuccess extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initUI();
    }

    private void initUI() {
        ImageView back = (ImageView) findViewById(R.id.back);
        ImageView status_img = (ImageView) findViewById(R.id.status_img);
        Button done = (Button) findViewById(R.id.done);
        TextView total = (TextView) findViewById(R.id.total);
        TextView tvTotal = (TextView) findViewById(R.id.tvTotal);
        TextView status_result = (TextView) findViewById(R.id.status_result);
        TextView booking_id = (TextView) findViewById(R.id.booking_id);
        TextView status = (TextView) findViewById(R.id.status);
        total.setText(getIntent().getStringExtra(Constants.TOTAL));
        booking_id.setText(getIntent().getStringExtra(Constants.PAYMENTID));
        status.setText(getIntent().getStringExtra(Constants.STATUS));
        if(status.getText().toString().contains("fail")) {
            total.setVisibility(View.GONE);
            tvTotal.setVisibility(View.GONE);
            status_result.setText("Sorrry!!\n your booking is not completed");
            status_img.setImageDrawable(getResources().getDrawable(R.drawable.close_circle));
        }
        back.setOnClickListener(this);
        done.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.back) {
            Intent i =new Intent(PaymentSuccess.this,Home.class);
            startActivity(i);
        } else if(v.getId() == R.id.done) {
            Intent i =new Intent(PaymentSuccess.this,Home.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i =new Intent(PaymentSuccess.this,Home.class);
        startActivity(i);
    }
}
