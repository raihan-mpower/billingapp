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

import billingapp.psionicinteractivelimited.com.billingapp.MainActivity;
import billingapp.psionicinteractivelimited.com.billingapp.database.billingdatabaseHelper;
import billingapp.psionicinteractivelimited.com.billingapp.model.customers.Customers;

public class BackgroundTask extends AsyncTask<String,Void,String> {
    ProgressDialog dialog;
    Context ctx;
    String token;
    public BackgroundTask(Context ctx)
    {
        this.ctx =ctx;
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

        String sync_url = "http://cable.psionichub.com/sync/billingdata?token="+token;
        Log.v("IN BACKGROUND:TOKEN",sync_url);

            try {
                URL url = new URL(sync_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                //httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();

                /// data from sqlite
                billingdatabaseHelper dbHelper=new billingdatabaseHelper(ctx,1);
                ArrayList<Customers> arrayList =  dbHelper.getCustomersWithNoTimestamp();
                String test = arrayList.toString();
                Log.v("db list",test);



                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode("name", "UTF-8") + "&" +
                        URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode("id", "UTF-8") + "&" +
                        URLEncoder.encode("user_location", "UTF-8") + "=" + URLEncoder.encode("location", "UTF-8");


                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                //httpURLConnection.connect();
                httpURLConnection.disconnect();
                return "Registration Success...";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
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

    }
}
