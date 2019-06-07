package eazi.com.eazirentals.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by tejaswini on 22/03/2019.
 */

public class APICalls {



    public static final String SIGNUP = "index.php/signup";
    public static final String STATE = "index.php/state";
    public static final String OTP = "index.php/otp";
    public static final String OTPVERIFY = "index.php/otp-verify";
    public static final String USERPROFILE = "user.php/user-profile";
    public static final String USERPROFILEUPDATE = "user.php/user-profile-update";
    public static final String REQUEST = "booking.php/request";
    public static final String BIKELIST = "booking.php/bike-list";
    public static final String SERVICE_URL = "http://app.eezeerentals.com/api/src/public/";
    private static InputStream inputstream;
    private static String json;

    public static boolean isNetworkAvailable(Context context) {
        boolean available = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        catch(Exception ex) {
        }
        return available;
    }

    public static void invokeAPIEx(final Context context, final String method, final String url, final List<NameValuePair> params,
                                   final Handler callback) {
        System.out.println("SYSTEMPRINT "+method+" url "+url+" params "+params);
        if(APICalls.isNetworkAvailable(context)) {

                Thread t =new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (method.equals("Post")) {
                                // request method is POST
                                // defaultHttpClient
                                DefaultHttpClient httpClient = new DefaultHttpClient();
                                HttpPost httpPost = new HttpPost(url);
                                httpPost.setEntity(new UrlEncodedFormEntity(params));
                                HttpResponse httpResponse = httpClient.execute(httpPost);
                                HttpEntity httpEntity = httpResponse.getEntity();
                                inputstream = httpEntity.getContent();

                            } else if (method.equals("Get")) {
                                DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                                HttpGet httpGet = new HttpGet(url);
                                HttpResponse response = defaultHttpClient.execute(httpGet);
                                HttpEntity httpEntity = response.getEntity();
                                inputstream = httpEntity.getContent();
                            }

                            BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
                            String line = null;
                            StringBuilder sb = new StringBuilder();

                            while ((line = br.readLine()) != null) {
                                System.out.println("SYSTEMPRINT line " + line);
                                sb.append(line + "\n");
                            }

                            inputstream.close();
                            json = sb.toString();
                            callback.sendMessage(callback.obtainMessage(0, json));

                            System.out.println("SYSTEMPRINT RESPONSE " + json);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            System.out.println("SYSTEMPRINT " + "UnsupportedEncodingException");
                            callback.sendMessage(callback.obtainMessage(0, null));
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                            System.out.println("SYSTEMPRINT " + "ClientProtocolException");
                            callback.sendMessage(callback.obtainMessage(0, null));
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("SYSTEMPRINT " + "IOException");
                            callback.sendMessage(callback.obtainMessage(0, null));
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("SYSTEMPRINT " + "Exception " + e.toString());
                            callback.sendMessage(callback.obtainMessage(0, null));
                        }
                    }
                });
                t.start();

        }



    }
/*
    public static void invokeAPIEx(final Context context, String url, final List<NameValuePair> params,
                                   final Handler callback) {

        RequestQueue queue = Volley.newRequestQueue(context);
        int request = Request.Method.POST;
        request = Request.Method.POST;
        System.out.println("SYSTEMPRINT URL "+SERVICE_URL+url );
        Map<String, String> nParams = new HashMap<String, String>();
        for( int i = 0; i < params.size(); i++ )
        {
            nParams.put( params.get( i ).getName(),
                    params.get( i ).getValue() );
        }
        System.out.println("SYSTEMPRINT PARAMS "+nParams.toString() );


        if(APICalls.isNetworkAvailable(context)) {

            StringRequest strReq = new StringRequest(request, SERVICE_URL+url,
                    new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onResponse(final String response) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("SYSTEMPRINT onResponse "+response);

                                        if (callback != null) {
                                            callback.sendMessage(callback.obtainMessage(0, response));
                                        }
                                    }
                                }).start();


                            try {
                                System.gc();
                                Runtime.getRuntime().gc();
                            } catch (Exception e) {

                                e.printStackTrace();
                                System.out.println("SYSTEMPRINT postsync " + "  Exception  "+ e.toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("SYSTEMPRINT onErrorResponse "+error);

                                    if (callback != null) {
                                        callback.sendMessage(callback.obtainMessage(0, error.toString()));
                                    }
                                }
                            }).start();
                            new CustomToast().Show_Toast(context, ConstantStrings.common_msg, R.color.green);

                        }
                    }) {
                @Override
                public String getBodyContentType() {
                    System.out.println("SYSTEMPRINT getBodyContentType "+ params.toString());

                    return "application/x-www-form-urlencoded";
                }

                @Override
                protected String getParamsEncoding() {
                    return "utf-8";
                }


                @Override
                protected Map<String, String> getPostParams() throws AuthFailureError {

                    Map<String, String> nParams = new HashMap<String, String>();
                    for( int i = 0; i < params.size(); i++ )
                    {
                        nParams.put( params.get( i ).getName(),
                                params.get( i ).getValue() );
                    }
                    return nParams;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    // TODO Auto-generated method stub
                    return params.toString().getBytes();
                }
            };

            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(strReq);
        } else {
            callback.sendMessage(callback.obtainMessage(0, null));

        }
    }
*/

    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append(':');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append(',');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }
}
