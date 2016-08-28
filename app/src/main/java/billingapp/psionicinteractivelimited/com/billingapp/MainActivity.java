package billingapp.psionicinteractivelimited.com.billingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import billingapp.psionicinteractivelimited.com.billingapp.database.billingdatabaseHelper;
import billingapp.psionicinteractivelimited.com.billingapp.database.sectorRepository;
import billingapp.psionicinteractivelimited.com.billingapp.fragments.BillPaymentFragment;
import billingapp.psionicinteractivelimited.com.billingapp.fragments.LocationFragment;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.House;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Road;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Sector;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Territory;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public static ArrayList<Territory> territories = new ArrayList<Territory>();
    public static ArrayList<Sector> sectors = new ArrayList<Sector>();
    public static ArrayList<Road> roads = new ArrayList<Road>();
    public static ArrayList<House> houses = new ArrayList<House>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        executeAsynctask();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);



    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
    public String postData(String token) {
        String responsestring = "";
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://cable.psionichub.com/sync/locations?token="+token);


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
                JSONObject objects = response.getJSONObject(i);
                Territory territoryToAdd = Territory.jsontoTerritory(objects.getJSONObject("territory").toString());
                processSectorArray(objects.getJSONObject("territory").toString());                
                territories.add(territoryToAdd);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        billingdatabaseHelper databasehelper = new billingdatabaseHelper(this,1);
        Log.v("length",""+territories.size());
        databasehelper.insert_or_update_Territory(territories);

    }

    private void processSectorArray(String territorystring) {
        JSONObject territory = null;

        try {
            territory = new JSONObject(territorystring);
            JSONArray SectorArray = new JSONArray(territory.getString("sector"));
            for(int i = 0;i<SectorArray.length();i++){
                Sector sector = Sector.jsontoSector(SectorArray.getJSONObject(i).toString());
                sectors.add(sector);
                processRoadArray(SectorArray.getJSONObject(i).toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        billingdatabaseHelper databasehelper = new billingdatabaseHelper(this,1);
        Log.v("sector _ length",""+sectors.size());
        databasehelper.insert_or_update_Sector(sectors);


    }

    private void processRoadArray(String secotrobject) {
        try {
            JSONObject sector = new JSONObject(secotrobject);
            JSONArray RoadArray = new JSONArray(sector.getString("road"));
            for(int i = 0;i<RoadArray.length();i++){
                Road road = Road.jsontoRoad(RoadArray.getJSONObject(i).toString());
                roads.add(road);
                processHouseArray(RoadArray.getJSONObject(i).toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        billingdatabaseHelper databasehelper = new billingdatabaseHelper(this,1);
        Log.v("Road _ length",""+roads.size());
        databasehelper.insert_or_update_Road(roads);
    }

    private void processHouseArray(String s) {
        try {
            JSONObject road = new JSONObject(s);
            JSONArray HouseArray = new JSONArray(road.getString("house"));
            for(int i = 0;i<HouseArray.length();i++){
                House house = House.jsontoHouse(HouseArray.getJSONObject(i).toString());
                houses.add(house);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        billingdatabaseHelper databasehelper = new billingdatabaseHelper(this,1);
        Log.v("Road _ length",""+houses.size());
        databasehelper.insert_or_update_House(houses);
    }

    public void executeAsynctask(){
        (new AsyncTask() {

            ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(MainActivity.this,"please wait","processing locations");
            }

            @Override
            protected Object doInBackground(Object[] params) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String token = preferences.getString("token", "");
                postData(token);
                Log.v("token",token);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);


                dialog.dismiss();
            }
        }).execute();
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == 0){
                return LocationFragment.newInstance("","");
            } if(position == 0){
                return BillPaymentFragment.newInstance("","");

            }else {
                return BillPaymentFragment.newInstance("","");

            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Location";
                case 1:
                    return "Bill Payment";
                case 2:
                    return "Print";
            }
            return null;
        }
    }
}
