package billingapp.psionicinteractivelimited.com.billingapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.PublicKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import billingapp.psionicinteractivelimited.com.billingapp.adapter.CustomListAdapter;
import billingapp.psionicinteractivelimited.com.billingapp.database.billingdatabaseHelper;
import billingapp.psionicinteractivelimited.com.billingapp.database.customerRepository;
import billingapp.psionicinteractivelimited.com.billingapp.fragments.BillPaymentFragment;
import billingapp.psionicinteractivelimited.com.billingapp.fragments.LocationFragment;
import billingapp.psionicinteractivelimited.com.billingapp.fragments.PrintReceiptFragment;
import billingapp.psionicinteractivelimited.com.billingapp.model.GPSTracker;
import billingapp.psionicinteractivelimited.com.billingapp.model.customers.Customers;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.House;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Road;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Sector;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Territory;
import billingapp.psionicinteractivelimited.com.billingapp.utils.CustomViewPager;
import billingapp.psionicinteractivelimited.com.billingapp.utils.syncUtils;
import billingapp.psionicinteractivelimited.com.billingapp.utils.BackgroundTask;

import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final Object PERMISSION_REQUEST_CODE_LOCATION =1 ;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ZXingScannerView mScannerView;
    Dialog qrdialog;
    public boolean getcustomerByQR = false;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public CustomViewPager mViewPager;
    public static ArrayList<Territory> territories = new ArrayList<Territory>();
    public static ArrayList<Sector> sectors = new ArrayList<Sector>();
    public static ArrayList<Road> roads = new ArrayList<Road>();
    public static ArrayList<House> houses = new ArrayList<House>();

//    boolean testtest=false;
//    public static Customers customerSelected ;
    //ush.start
    public static ArrayList<Customers> customerSelected =new ArrayList<Customers>() ;
    public static Customers customerForProcessing ;
    syncUtils Su;


    //ush.end
    public BillPaymentFragment billPaymentFragment;

    //ush: started
    public PrintReceiptFragment printReceipttFragment;
    public LocationFragment locationFragment;
    private SharedPreferences preferences;
    //ush: ends


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GPSTracker gps=new GPSTracker(this);

//        if (gps.getLatitude()==0.0 || gps.getLongitude()==0.0) {
//            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,PERMISSION_REQUEST_CODE_LOCATION,getApplicationContext(),CopyOfMap.this);

//            if (
//                    ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION);
//        )
//            {

//            } else {
//
//                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, (Integer) PERMISSION_REQUEST_CODE_LOCATION);
//            }
//        }

        if (!checkPerm(Manifest.permission.ACCESS_FINE_LOCATION,getApplicationContext())) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, (Integer) PERMISSION_REQUEST_CODE_LOCATION);
        }


        if (!gps.canGetLocation()){

            gps.showSettingsAlert();
            Toast.makeText(this, "Please Turn on GPS", Toast.LENGTH_LONG).show();
//            ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION);
//            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, (Integer) PERMISSION_REQUEST_CODE_LOCATION);
        }



        Su = new syncUtils(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        locationFragment = LocationFragment.newInstance("","");

        boolean isFistLoad=preferences.getBoolean("firstLoad",true);
        if(isFistLoad){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstLoad",false);

            editor.apply();

//            Toast.makeText(MainActivity.this, "testest"+isFistLoad, Toast.LENGTH_SHORT).show();
            Su.executeAsynctask(locationFragment,true);
        }
//        else{
////            Toast.makeText(MainActivity.this, "notest"+isFistLoad, Toast.LENGTH_SHORT).show();
//        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
        LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
        mViewPager.setPagingEnabled(false);

    }

    private boolean checkPerm(String accessFineLocation, Context applicationContext) {
        int result = ContextCompat.checkSelfPermission(applicationContext, accessFineLocation);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }

    }


    public void QrScanner(View view){

        qrdialog = new Dialog(this);



        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
//        setContentView(mScannerView);
        qrdialog.setContentView(mScannerView);
        qrdialog.show();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
//            update_the_data_from_server();

            if(isOnline()){
                boolean isFistLoad=preferences.getBoolean("firstLoad",true);
            Su.executeAsynctask(locationFragment, isFistLoad);
            return true;
            }else{
                Toast.makeText(this, "INTERNET PROBLEM", Toast.LENGTH_SHORT).show();
            }

//            ////////////////////
//            boolean isFistLoad=preferences.getBoolean("firstLoad",true);
//            Su.executeAsynctask(locationFragment, isFistLoad);
//            return true;
//            ////////////////


        }
        else if(id == R.id.action_sync){

            boolean isFistLoad = preferences.getBoolean("firstLoad", true);
//            if(!isFistLoad)
//            {
//                int i = 5;
//                intent = new Intent(this, MyBroadcastReceiver.class);
//                pendingIntent = PendingIntent.getBroadcast(
//                        this.getApplicationContext(), 280192, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + (i * 1000), 10000
//                        , pendingIntent);
//
//                Toast.makeText(this, "Alarm will set in " + i + " seconds",
//                        Toast.LENGTH_LONG).show();
//            }

            if(isOnline()) {

                BackgroundTask bt = new BackgroundTask(MainActivity.this, Su, locationFragment, isFistLoad);
                bt.execute();
            }else {
                Toast.makeText(this, "INTERNET PROBLEM", Toast.LENGTH_SHORT).show();
            }
        }
        else if(id == R.id.action_logout){

//            ListView lv= (ListView) findViewById(R.id.log_listView);

            billingdatabaseHelper bh=new billingdatabaseHelper(this,1);
            ArrayList<Customers> arrayList = bh.get_bill_paid_customers_before_update();
            Log.v("imam1","this_is_paid_list "+arrayList.get(0).get_to_sync_total_amount());



//            String houses_id,String flat,String price,String customer_code,String address,String customers_id,String name, String last_paid, String updated_at,String phone



            final Dialog showlog = new Dialog(this);
            showlog.setTitle("BILL PAYMENT DETAILS");
            showlog.setContentView(R.layout.show_log);
            ListView lv= (ListView) showlog.findViewById(R.id.log_listView);
            showlog.show();
            showlog.setCanceledOnTouchOutside(true);
            showlog.setCancelable(true);

            CustomListAdapter adapter = new CustomListAdapter(getApplicationContext(), R.layout.list_item, arrayList);
            lv.setAdapter(adapter);


            Button cancelDialog = (Button)showlog.findViewById(R.id.close_button);
            cancelDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showlog.dismiss();
                }
            });

            //logout disabled start
//            Toast.makeText(MainActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean("isLoggedIn",false);
//            editor.apply();
//            startActivity(new Intent(MainActivity.this,LoginActivity.class));
//            finish();

            //logout disabled end

        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

//    private boolean update_the_data_from_server() {
//
//        boolean isFistLoad=preferences.getBoolean("firstLoad",true);
//
//        Su.executeAsynctask(locationFragment, isFistLoad);
////            Toast.makeText(MainActivity.this, "Updating...", Toast.LENGTH_SHORT).show();
//        return true;
//
//    }


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
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
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
                return locationFragment;
            }else if(position == 1){
                billPaymentFragment = BillPaymentFragment.newInstance("","");

                return billPaymentFragment;

            }else {
//                return BillPaymentFragment.newInstance("","");

                //ush: started
                printReceipttFragment = PrintReceiptFragment.newInstance("","");
                return printReceipttFragment;

                //ush: ends

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
                    return "Customer";
                case 1:
                    return "Bill Payment";
                case 2:
                    return "Receipt";
            }
            return null;
        }
    }
        @Override
        public void handleResult(Result rawResult) {
            // Do something with the result here
            getcustomerByQR = true;
            mScannerView.stopCamera();
            qrdialog.dismiss();

            Log.e("handler", rawResult.getText()); // Prints scan results
            Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
            String qrJson = rawResult.getText();
            JSONObject object = null;
            String customerid = "";
            String city = "";
            String sector = "";
            String road = "";
            String house = "";
            String flat = "";
            try {
                object = new JSONObject(qrJson);
                JSONArray customers = object.getJSONArray("customers");
                for(int i = 0;i<customers.length();i++) {
                    JSONObject currentcustomer = customers.getJSONObject(i);
                    customerid = currentcustomer.getString("id");
                    city = currentcustomer.getString("city");
                    sector = currentcustomer.getString("sector");
                    road = currentcustomer.getString("Road");
                    house =  currentcustomer.getString("house");
                    flat = currentcustomer.getString("flat");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final billingdatabaseHelper databasehelper = new billingdatabaseHelper(this,1);
            locationFragment.fillfromQR(customerid,city,sector,road,house,flat);
            MainActivity.customerForProcessing = customerRepository.findByCustomerCaseID(customerid,databasehelper.getReadableDatabase()).get(0);

            // show the scanner result into dialog box.
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Scan Result");
//            builder.setMessage(rawResult.getText());
//            AlertDialog alert1 = builder.create();
//            alert1.show();

            // If you would like to resume scanning, call this method below:
            // mScannerView.resumeCameraPreview(this);
        }


}
