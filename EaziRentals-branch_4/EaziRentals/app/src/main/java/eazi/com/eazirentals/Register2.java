package eazi.com.eazirentals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Register2 extends AppCompatActivity {
    String[] time = {"Bengaluru" , "Mysore"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_2);
        pickTime();

    }
    public void pickTime(){
        Spinner state = (Spinner) findViewById(R.id.state);
        ArrayAdapter pickup_timeaa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,time);
        pickup_timeaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        state.setAdapter(pickup_timeaa);

        Spinner city = (Spinner) findViewById(R.id.city);
        ArrayAdapter drop_timeaa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,time);
        drop_timeaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        city.setAdapter(drop_timeaa);

    }

}
