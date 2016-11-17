package billingapp.psionicinteractivelimited.com.billingapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import billingapp.psionicinteractivelimited.com.billingapp.MainActivity;
import billingapp.psionicinteractivelimited.com.billingapp.database.billingdatabaseHelper;
import billingapp.psionicinteractivelimited.com.billingapp.fragments.LocationFragment;
import billingapp.psionicinteractivelimited.com.billingapp.model.customers.Customers;

public class BackgroundTask extends AsyncTask<String,Void,String> {
    ProgressDialog dialog;
    Context ctx;
    String token;
    syncUtils Su;
    LocationFragment locationFragment;
    boolean isFistload;
    String successString = "";
    public BackgroundTask(Context ctx, syncUtils su, LocationFragment locationFragment, boolean isFistLoad)
    {
        this.ctx =ctx;
        this.Su = su;
        this.locationFragment = locationFragment;
        this.isFistload = isFistLoad;
    }
    @Override
    protected void onPreExecute() {
//        alertDialog = new AlertDialog.Builder(ctx).create();
//        alertDialog.setTitle("Login Information....");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        token = preferences.getString("token", "");
        dialog = ProgressDialog.show(ctx,"please wait","synching....g");
    }
    @Override
    protected String doInBackground(String... params) {

//        String sync_url = "http://cable.psionichub.com/sync/billingdata?token="+token;
//        String sync_url = "http://192.168.0.100:8000/sync/billingdata?token="+token;
//        String sync_url = "http://192.168.0.101:8000/sync/billingdata/";

//        String sync_url = "http://cable.psionichub.com/sync/billingdata";

        String sync_url = "http://192.168.0.102:8000/sync/billingdata";

        try {


                URL url = new URL(sync_url);

                billingdatabaseHelper dbHelper=new billingdatabaseHelper(ctx,1);
                ArrayList<Customers> arrayList =  dbHelper.getCustomersWithNoTimestamp();
                String test = arrayList.toString();

                Log.v("Array List length",arrayList.size()+"");

                for (int arrayListCounter=0;arrayListCounter<arrayList.size();arrayListCounter++){
                    Customers customer= arrayList.get(arrayListCounter);
                    //////////////////////////test post/////////////////////////////////////////
                    Map<String, String> parameters = new HashMap<String, String>(2);
                    parameters.put("customers_id",customer.getCustomers_id());
                    parameters.put("total",customer.get_to_sync_total_amount());
                    parameters.put("timestamp", customer.get_to_sync_collection_date()+ " 00:00:00");
                    parameters.put("due", customer.getDue());
                    parameters.put("last_paid_date_num", customer.get_to_sync_paying_for());
                    parameters.put("lat", customer.get_to_sync_lat());
                    parameters.put("lon", customer.get_to_sync_lon());

                    String result = multipartRequest(sync_url, parameters);
                    Log.v("result",result);
                    /////////////////////////test post ////////////////////////////////////////



                    if(result.equalsIgnoreCase("success")){
                        successString = "success";
                    }
                }


            String edit_url = "http://192.168.0.102:8000/sync/customerdata";


                ArrayList<Customers> arrayListforEdit = dbHelper.getCustomersForEdit();
            for (int arrayListCounter=0;arrayListCounter<arrayListforEdit.size();arrayListCounter++){
                Customers customer= arrayListforEdit.get(arrayListCounter);
                //////////////////////////test post/////////////////////////////////////////
                Map<String, String> parameters = new HashMap<String, String>(2);
                parameters.put("customers_id",customer.getCustomers_id());
                parameters.put("name",customer.getName());
                parameters.put("phone", customer.getPhone());

                String result = multipartRequest(edit_url, parameters);
                Log.v("just result",result);
                /////////////////////////test post ////////////////////////////////////////



                if(result.equalsIgnoreCase("success")){
                    dbHelper.deleteCustomerEditEntry(customer);
                }
            }






            } catch (MalformedURLException e) {
                Log.v("exception Malformed",""+e);
                e.printStackTrace();
            } catch (IOException e) {
            Log.v("exception IO",""+e);
                e.printStackTrace();
            }

        return null;
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
    @Override
    protected void onPostExecute(String result) {
        dialog.dismiss();
        if(successString.equalsIgnoreCase("success")) {
            Su.executeAsynctask(locationFragment, isFistload);
        }
    }

    public String multipartRequest(String urlTo, Map<String, String> parmas) {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;

        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;


        try {


            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            String basicAuth="Bearer {"+token+"}";
            connection.setRequestProperty("Authorization",basicAuth);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());

            // Upload POST Data
            Iterator<String> keys = parmas.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = parmas.get(key);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(value);
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


//            if (200 != connection.getResponseCode()) {
//                throw new Exception("Failed to upload code:" + connection.getResponseCode() + " " + connection.getResponseMessage());
//            }

            inputStream = connection.getInputStream();

            result = this.convertStreamToString(inputStream);

            inputStream.close();
            outputStream.flush();
            outputStream.close();

            return result;
        } catch (Exception e) {
            return e.getMessage();
        }

    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
