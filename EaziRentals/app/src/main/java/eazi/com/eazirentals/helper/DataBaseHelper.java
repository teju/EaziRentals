package eazi.com.eazirentals.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import eazi.com.eazirentals.models.BikeList;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static  int database_version  = 1;
    private final Context context;
    public String Cart =" CREATE TABLE `Cart` (Id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,"+
            "image TEXT, bike_id TEXT, price TEXT,branch TEXT,from_time TEXT,to_time TEXT,user_id TEXT," +
            " CONSTRAINT bike_id UNIQUE (bike_id)); ";

    private static final String DELETE_Cart = "DROP TABLE IF EXISTS Cart" ;


    public DataBaseHelper(Context context) {
        super(context, "rentals_db",null, database_version);
        this.context=context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Cart);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_Cart);
        onCreate(db);
    }


    public boolean addToCart(DataBaseHelper dbh, String name,
                               String image, String bike_id, String price,String branch,String from_time,
                             String to_time,String user_id){
        SQLiteDatabase sq=dbh.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("image", image);
        cv.put("bike_id", bike_id);
        cv.put("price", price);
        cv.put("branch", branch);
        cv.put("from_time", from_time);
        cv.put("to_time", to_time);
        cv.put("user_id", user_id);
        sq.insert("Cart", null, cv);
        return true;

    }






    public List<BikeList> getAllContacts(String where) {
        List<BikeList> dataListList = new ArrayList<BikeList>();

        String selectQuery = "SELECT * FROM Cart "+where ;

        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("DataBaseHelper123 getAllContacts " + selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BikeList contactsDeo = new BikeList();
                contactsDeo.setUniqueId(cursor.getString(0));
                contactsDeo.setName(cursor.getString(1));
                contactsDeo.setImage(cursor.getString(2));
                contactsDeo.setId(cursor.getString(3));
                contactsDeo.setPrice(cursor.getString(4));
                contactsDeo.setBranch(cursor.getString(5));
                contactsDeo.setFrom(cursor.getString(6));
                contactsDeo.setTo(cursor.getString(7));
                contactsDeo.setUser_id(cursor.getString(8));
                dataListList.add(contactsDeo);
            } while (cursor.moveToNext());
        }
        return dataListList;
    }



    public void deleteFromCart (String bike_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        String where = "";
        if(bike_id.length() != 0) {
            where = " where bike_id = '"+bike_id+"'";
        }
        String deleteQuery = "Delete from Cart"+where;
        Log.d("settingdeletequery", deleteQuery);
        database.execSQL(deleteQuery);
    }

}
