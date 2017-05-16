package billingapp.psionicinteractivelimited.com.billingapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ContentHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import billingapp.psionicinteractivelimited.com.billingapp.MainActivity;
import billingapp.psionicinteractivelimited.com.billingapp.database.billingdatabaseHelper;
import billingapp.psionicinteractivelimited.com.billingapp.fragments.LocationFragment;
import billingapp.psionicinteractivelimited.com.billingapp.model.customers.Customers;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.House;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Road;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Sector;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Territory;

/**
 * Created by raihan on 9/2/16.
 */
public class syncUtils {
    Context context;
    public syncUtils(Context context) {
        this.context = context;
    }
    public void executeAsynctask(final LocationFragment locationFragment, final boolean isFistLoad){
        (new AsyncTask() {

            ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(context,"please wait","updating...");
            }

            @Override
            protected Object doInBackground(Object[] params) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                String token = preferences.getString("token", "");
                postData(token);
                Log.v("token",token);
                billingdatabaseHelper databasehelper = new billingdatabaseHelper(context,1);
                if(databasehelper.getALLCustomers().size()<1){
                    Log.v("is here","already exists");
                syncCustomers();
                }else{
                    updatedCustomer();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                dialog.dismiss();
                if(isFistLoad){
                    locationFragment.refreshcreateview();
                }
            }
        }).execute();
    }

    public void syncLocalWithServer(String token){
        Log.v("syncLocalWithServer","started");

//        String sync_url = "http://cable.psionichub.com/sync/billingdata?token="+token;
        String sync_url = "http://cable.hmannan.com/sync/billingdata?token="+token;

//        String sync_url = "http://192.168.0.108:8000/sync/billingdata?token="+token;


//        String sync_url = "http://192.168.0.101:8000/sync/billingdata?token="+token;
        Log.v("url",sync_url);

        String data1="";
        String data2="";


        try {
            URL url = new URL(sync_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            String data =
                    URLEncoder.encode("date1","UTF-8")+"="+ URLEncoder.encode(data1,"UTF-8")+"&"+
                    URLEncoder.encode("data2","UTF-8")+"="+ URLEncoder.encode(data2,"UTF-8");

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            httpURLConnection.setDoInput(true);
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String response = "";
            String line = "";
            while ((line = bufferedReader.readLine())!=null)
            {
                response += line;
            }
            Log.v("response",response);

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

//            return response;
        }
//        catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        catch(Exception e){
            Log.v("Exceptiooooooooooooooon",""+e);

        }


    }



    public String postData(String token) {
        String responsestring = "";
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
//        HttpGet httpget = new HttpGet("http://cable.psionichub.com/sync/locations?token="+token);
        HttpGet httpget = new HttpGet("http://cable.hmannan.com/sync/locations?token="+token);

//        HttpGet httpget = new HttpGet("http://192.168.0.108:8000/sync/locations?token="+token);


        try {
            // Add your data
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//            nameValuePairs.add(new BasicNameValuePair("token", token));
//            httpget.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpget);
            String respond = response.getStatusLine().getReasonPhrase();
            int statusCode = response.getStatusLine().getStatusCode();

// Getting the response body.
            String responseBody = EntityUtils.toString(response.getEntity());
            Log.v("testcall",token);
            Log.v("testcall",responseBody);
            ;
            if(statusCode == 200){
                Log.v("testcall",responseBody);
                responsestring = responseBody;
                processResponse(responsestring);

            }else{
                responsestring = "error";
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return responsestring;
    }

    private void processResponse(String responsestring) {
        try {
            JSONArray response = new JSONArray(responsestring);

            Log.v("length",""+response.toString());
            String territoryarray = "";
            for(int i = 0;i<response.length();i++){
//                JSONObject objects = response.getJSONObject(i);
//                Territory territoryToAdd = Territory.jsontoTerritory(objects.getJSONObject("territory").toString());
//                processSectorArray(objects.getJSONObject("territory").toString());
//                MainActivity.territories.add(territoryToAdd);
                JSONObject objects = response.getJSONObject(i);
                Log.v("length",""+objects.toString());
                Territory territoryToAdd = Territory.jsontoTerritory(objects.toString());
                processSectorArray(objects.toString());
                MainActivity.territories.add(territoryToAdd);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        billingdatabaseHelper databasehelper = new billingdatabaseHelper(context,1);
        Log.v("length",""+ MainActivity.territories.size());
        databasehelper.insert_or_update_Territory( MainActivity.territories);

    }

    private void processSectorArray(String territorystring) {
        JSONObject territory = null;

        try {
            territory = new JSONObject(territorystring);
            JSONArray SectorArray = new JSONArray(territory.getString("sector"));
            for(int i = 0;i<SectorArray.length();i++){
                Sector sector = Sector.jsontoSector(SectorArray.getJSONObject(i).toString());
                MainActivity.sectors.add(sector);
                processRoadArray(SectorArray.getJSONObject(i).toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        billingdatabaseHelper databasehelper = new billingdatabaseHelper(context,1);
        Log.v("sector _ length",""+ MainActivity.sectors.size());
        databasehelper.insert_or_update_Sector( MainActivity.sectors);


    }

    private void processRoadArray(String secotrobject) {
        try {
            JSONObject sector = new JSONObject(secotrobject);
            JSONArray RoadArray = new JSONArray(sector.getString("road"));
            for(int i = 0;i<RoadArray.length();i++){
                Road road = Road.jsontoRoad(RoadArray.getJSONObject(i).toString());
                MainActivity.roads.add(road);
                processHouseArray(RoadArray.getJSONObject(i).toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        billingdatabaseHelper databasehelper = new billingdatabaseHelper(context,1);
        Log.v("Road _ length",""+ MainActivity.roads.size());
        databasehelper.insert_or_update_Road( MainActivity.roads);
    }

    private void processHouseArray(String s) {
        try {
            JSONObject road = new JSONObject(s);
            JSONArray HouseArray = new JSONArray(road.getString("house"));
            for(int i = 0;i<HouseArray.length();i++){
                House house = House.jsontoHouse(HouseArray.getJSONObject(i).toString());
                Log.v("house_id",house.getId());
                MainActivity.houses.add(house);
            }
            billingdatabaseHelper databasehelper = new billingdatabaseHelper(context,1);
            Log.v("house _ length",""+ MainActivity.houses.size());
            databasehelper.insert_or_update_House( MainActivity.houses);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void syncCustomers(){

        billingdatabaseHelper databasehelper = new billingdatabaseHelper(context,1);
        int i = databasehelper.getlastCustomerID();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = preferences.getString("token", "");
        String response = getCustomersData(token,i);
        ArrayList<Customers> customerlist = Customers.returnCustomersFromArray(response);
        while(customerlist.size()>0){
            databasehelper.insert_or_update_Customers(customerlist);//shaon
            i = databasehelper.getlastCustomerID();
            Log.v("last customer id",""+i);
             response = getCustomersData(token,i);
            customerlist = Customers.returnCustomersFromArray(response);
        }
    }

    public void updatedCustomer(){
        billingdatabaseHelper databasehelper = new billingdatabaseHelper(context,1);

        int i = 0;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = preferences.getString("token", "");

        String response = getCustomersData(token,i);
        ArrayList<Customers> customerlist = Customers.returnCustomersFromArray(response);

        while(customerlist.size()>0)
        {
            databasehelper.insert_or_update_Customers(customerlist);
            i = Integer.parseInt(customerlist.get(customerlist.size()-1).getCustomers_id());
            Log.v("last customer id",""+i);
            response = getCustomersData(token,i);
            customerlist = Customers.returnCustomersFromArray(response);
        }


    }



    public String getCustomersData(String token,int last_id) {
        String responsestring = "";
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
//        HttpGet httpget = new HttpGet("http://cable.psionichub.com/sync/customers?token="+token+"&last_id="+last_id+"&limit=100");
        HttpGet httpget = new HttpGet("http://cable.hmannan.com/sync/customers?token="+token+"&last_id="+last_id+"&limit=100");
//        HttpGet httpget = new HttpGet("http://192.168.0.108:8000/sync/customers?token="+token+"&last_id="+last_id+"&limit=100");

//        HttpGet httpget = new HttpGet("http://192.168.0.101:8000/sync/customers?token="+token+"&last_id="+last_id+"&limit=100");



        try {
            // Add your data
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//            nameValuePairs.add(new BasicNameValuePair("token", token));
//            httpget.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpget);
            String respond = response.getStatusLine().getReasonPhrase();
            int statusCode = response.getStatusLine().getStatusCode();

// Getting the response body.
            String responseBody = EntityUtils.toString(response.getEntity());
            Log.v("testcall",token);
            Log.v("mytestcall",responseBody);
            ;
            if(statusCode == 200){
                Log.v("testcall",responseBody);
                responsestring = responseBody;
//                processResponse(responsestring);

            }else{
                responsestring = "error";
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return responsestring;
    }

//    class UpdateBlankTimes extends AsyncTask<String, Integer, String> {
//
//        ProgressDialog dialog;
//
//        @Override
//        protected void onPreExecute() {
////            super.onPreExecute();
//            dialog = ProgressDialog.show(context,"please wait","syncing billing info");
//
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            return "";
//        }
//
//        @Override
//        protected void onPostExecute(String content) {
//            dialog.dismiss();
//        }
//    }
}
