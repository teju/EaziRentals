package eazi.com.eazirentals.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Patterns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tejaswini on 23/05/2019.
 */

public class Helper {

    public static String convertDate(String date,String fromFormat ,String toFormat){
        SimpleDateFormat dateFormat = new SimpleDateFormat(fromFormat);

        Date myDate = null;
        SimpleDateFormat timeFormat = null;
        try {
            myDate = dateFormat.parse(date);
            timeFormat = new SimpleDateFormat(toFormat);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String finalDate = timeFormat.format(myDate);
        return finalDate;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkRuntimePermission(Activity activity, List<String> permissionNeededList, int requestCode) {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        for (String permission : permissionNeededList) {
            if (!addPermission(activity, permissionsList, permission))
                permissionsNeeded.add(permission);
        }
        if (permissionsList.size() > 0) {
            activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), requestCode);
            return false;
        } else
            return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean addPermission(Activity activity, List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!activity.shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }


    public static boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public static boolean isValidOtp(String phone) {
        if(phone.length() >= 6) {
            return true;
        }
        return false;
    }

    public static boolean isValidString(String str) {

        if(str != null && !str.isEmpty()) {
            return true;
        }
        return false;
    }
    public static double calculateGst(double total){
        double gst = (total / 100.0f) * 15;
        return gst;
    }

}
