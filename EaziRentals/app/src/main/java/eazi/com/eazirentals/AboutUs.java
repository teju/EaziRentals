package eazi.com.eazirentals;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
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
import eazi.com.eazirentals.models.CMSResponse;

public class AboutUs extends AppCompatActivity {
    private CMSResponse data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        // GetCMS();
        initUI();
    }


    private void initUI() {
        ImageView back = (ImageView)findViewById(R.id.back);
        ImageView keerti_fb = (ImageView)findViewById(R.id.keerti_fb);
        ImageView youtube = (ImageView)findViewById(R.id.youtube);
        LinearLayout bijesh_fb = (LinearLayout) findViewById(R.id.bijesh_fb);
        LinearLayout champak_fb = (LinearLayout) findViewById(R.id.champak_fb);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutUs.super.onBackPressed();
            }
        });
        keerti_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reDirectToWebPAge("https://www.facebook.com/keerthi.ekambaram");
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reDirectToWebPAge("https://www.youtube.com/c/KeerthiEkambaram");

            }
        });
        bijesh_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reDirectToWebPAge("https://www.facebook.com/bijesh.karakat");

            }
        });
        champak_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reDirectToWebPAge("https://www.facebook.com/champak524232");

            }
        });
    }

    public void reDirectToWebPAge(String url) {
        Intent i = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
        );
        startActivity(i);
// Starts Implicit Activity
    }


/*
    public void GetCMS() {
        try {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("user_id", SharedPreference.getString(AboutUs.this, Constants.KEY_USER_ID)));
            params.add(new BasicNameValuePair("access_token",SharedPreference.getString(AboutUs.this,Constants.KEY_ACCESS_TOKEN)));
            params.add(new BasicNameValuePair("type","about_us"));

            Loader.show(this);

            APICalls.invokeAPIEx(this,"Post",APICalls.SERVICE_URL+APICalls.CMS,params,new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    Loader.hide();
                    try {
                        if (message != null && message.obj != null) {
                            String result = (String) message.obj;
                            data = new Gson().fromJson(result, CMSResponse.class);
                            System.out.println("SelectBike callAPI Response " + result + " status " + data.getStatus());

                            if (data.getStatus() != null && data.getStatus().equalsIgnoreCase("success")) {
                                TextView terms_conditions = (TextView) findViewById(R.id.terms_conditions);
                                terms_conditions.setText(Html.fromHtml(data.getAbout_us()));
                            }

                        } else {
                            new CustomToast().Show_Toast(AboutUs.this, ConstantStrings.common_msg, R.color.light_red2);
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
*/

}
