package eazi.com.eazirentals;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectBike extends AppCompatActivity {

    int[] bikes = {R.drawable.bike1,R.drawable.bike2,R.drawable.bike3,R.drawable.bike4,R.drawable.bike5};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bike);
        GridView gridview = (GridView)findViewById(R.id.gridview);
        gridview.setAdapter(new BikesAdapter(this,bikes));

    }

    public void selectNow(View v) {
        Intent i = new Intent(SelectBike.this,Cart.class);
        startActivity(i);
    }

    public class BikesAdapter extends BaseAdapter implements AdapterView.OnClickListener{

        private final Context mContext;
        private final int[] bikes;

        // 1
        public BikesAdapter(Context context, int[] bikes) {
            this.mContext = context;
            this.bikes = bikes;
        }

        // 2
        @Override
        public int getCount() {
            return bikes.length;
        }

        // 3
        @Override
        public long getItemId(int position) {
            return position;
        }

        // 4
        @Override
        public Object getItem(int position) {
            return position;
        }

        // 5
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(
                    mContext).inflate(R.layout.select_bike_item, parent, false);
            LinearLayout bike_image = (LinearLayout)view.findViewById(R.id.bike_image);
            bike_image.setBackground(mContext.getResources().getDrawable(bikes[position]));
            bike_image.setOnClickListener(this);
            bike_image.setTag(position);
            return view;
        }

        @Override
        public void onClick(View v) {
            try {
                Integer i = (int)v.getTag();
                final Dialog mBottomSheetDialog = new Dialog(mContext);
                mBottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mBottomSheetDialog.setContentView(R.layout.bike_details);
                mBottomSheetDialog.setCanceledOnTouchOutside(false);
                mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mBottomSheetDialog.show();
                ImageView bike_img = (ImageView)mBottomSheetDialog.findViewById(R.id.bike_img);
                bike_img.setImageDrawable(getResources().getDrawable(bikes[i]));
                Button check_out = (Button)mBottomSheetDialog.findViewById(R.id.check_out);
                check_out.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(SelectBike.this,Cart.class);
                        startActivity(i);
                    }
                });
                Button add_more = (Button)mBottomSheetDialog.findViewById(R.id.add_more);
                add_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBottomSheetDialog.cancel();
                    }
                });
            } catch (Exception e){
                System.out.println("EXCEPTION "+e.toString());
            }

        }
    }
}
