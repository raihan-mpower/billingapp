package billingapp.psionicinteractivelimited.com.billingapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import billingapp.psionicinteractivelimited.com.billingapp.fragments.BillPaymentFragment;
import billingapp.psionicinteractivelimited.com.billingapp.fragments.LocationFragment;
import billingapp.psionicinteractivelimited.com.billingapp.fragments.PrintReceiptFragment;
import billingapp.psionicinteractivelimited.com.billingapp.model.customers.Customers;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.House;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Road;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Sector;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Territory;
import billingapp.psionicinteractivelimited.com.billingapp.utils.syncUtils;
import billingapp.psionicinteractivelimited.com.billingapp.utils.BackgroundTask;

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
    public ViewPager mViewPager;
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
    //ush: ends


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Su = new syncUtils(this);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);


        boolean isFistLoad=preferences.getBoolean("firstLoad",true);
        if(isFistLoad){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstLoad",false);
            editor.apply();

            Toast.makeText(MainActivity.this, "testest"+isFistLoad, Toast.LENGTH_SHORT).show();
            Su.executeAsynctask();
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
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);



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


            Su.executeAsynctask();
            Toast.makeText(MainActivity.this, "Updating...", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(id == R.id.action_sync){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            String token = preferences.getString("token", "");

//            Su.syncLocalWithServer(token);
            BackgroundTask bt=new BackgroundTask(MainActivity.this);
            bt.execute();
            Su.executeAsynctask();

//            Toast.makeText(MainActivity.this, "Syncing...", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(id == R.id.action_logout){
            Toast.makeText(MainActivity.this, "Logging out...", Toast.LENGTH_SHORT).show();


        }

        return super.onOptionsItemSelected(item);
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
                return LocationFragment.newInstance("","");
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
