package billingapp.psionicinteractivelimited.com.billingapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import billingapp.psionicinteractivelimited.com.billingapp.MainActivity;
import billingapp.psionicinteractivelimited.com.billingapp.R;
import billingapp.psionicinteractivelimited.com.billingapp.database.billingdatabaseHelper;
import billingapp.psionicinteractivelimited.com.billingapp.database.territoryRepository;
import billingapp.psionicinteractivelimited.com.billingapp.model.customers.Customers;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Road;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Sector;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Territory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AutoCompleteTextView sector;
    private AutoCompleteTextView atcv;
    private AutoCompleteTextView house;
    private AutoCompleteTextView road;

    //ush.start
    private AutoCompleteTextView customerid;
    private AutoCompleteTextView telephonenumber;
    //ush.end

//    private TextView customerid;
//    private TextView telephonnumber;
    private Button billpayment;

    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);



        final billingdatabaseHelper databasehelper = new billingdatabaseHelper(getActivity(),1);
        MainActivity.territories =territoryRepository.getALLterritory(databasehelper.getReadableDatabase());

//        customerid = (TextView)view.findViewById(R.id.customer_id);

        //ush.start
        customerid= (AutoCompleteTextView) view.findViewById(R.id.customer_id);
        telephonenumber = (AutoCompleteTextView) view.findViewById(R.id.telephone_no);
        //ush.end

//        telephonenumber = (TextView)view.findViewById(R.id.telephone_no);
        atcv = (AutoCompleteTextView)view.findViewById(R.id.city);
        sector = (AutoCompleteTextView)view.findViewById(R.id.sector_block);
        road = (AutoCompleteTextView)view.findViewById(R.id.road_no);
        house = (AutoCompleteTextView)view.findViewById(R.id.house_number);

        billpayment = (Button) view.findViewById(R.id.bill_payment);
        atcv.setThreshold(-1);
        sector.setThreshold(-1);
        road.setThreshold(-1);
        house.setThreshold(-1);
        customerid.setThreshold(-1);
        telephonenumber.setThreshold(-1);

        final ArrayList<String> suggestions = new ArrayList<String>();
        for(int i = 0;i< MainActivity.territories.size();i++){
            Log.v("naam ki",MainActivity.territories.get(i).getName());
            suggestions.add(MainActivity.territories.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, suggestions);
        atcv.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        atcv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            actvOnclick(suggestions,databasehelper);


            }
        });
        billpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).billPaymentFragment.initiateCustomers(MainActivity.customerForProcessing);

                ((MainActivity)getActivity()).mViewPager.setCurrentItem(1);
            }
        });

        return view;
    }

    private void actvOnclick(ArrayList<String> suggestions, final billingdatabaseHelper databasehelper) {
        int index = suggestions.indexOf(atcv.getEditableText().toString());
        Territory territory = MainActivity.territories.get(index);
        MainActivity.sectors = databasehelper.getSectorbyTerritoryID(territory.getId());
        final ArrayList<String> sectorsuggestions = new ArrayList<String>();
        for(int i = 0;i< MainActivity.sectors.size();i++){
            Log.v("naam ki",MainActivity.sectors.get(i).getSector());
            sectorsuggestions.add(MainActivity.sectors.get(i).getSector());
        }
        Log.v("sect size ki","__"+sectorsuggestions.size());
        ArrayAdapter<String> sectorarrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, sectorsuggestions);
        sector.setAdapter(sectorarrayAdapter);
        sectorarrayAdapter.notifyDataSetChanged();
        sector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sectorOnClick(sectorsuggestions,databasehelper);
            }
        });
    }

    private void sectorOnClick(ArrayList<String> sectorsuggestions, final billingdatabaseHelper databasehelper) {
        int index = sectorsuggestions.indexOf(sector.getEditableText().toString());
        Sector sectorselect = MainActivity.sectors.get(index);
        MainActivity.roads = databasehelper.getRoadbySectorID(sectorselect.getId());
        final ArrayList<String> roadsuggestions = new ArrayList<String>();
        for(int i = 0;i< MainActivity.roads.size();i++){
            Log.v("road naam ki",MainActivity.roads.get(i).getRoad());
            roadsuggestions.add(MainActivity.roads.get(i).getRoad());
        }
        Log.v("road size ki","__"+roadsuggestions.size());
        ArrayAdapter<String> roadarrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, roadsuggestions);
        road.setAdapter(roadarrayAdapter);
        roadarrayAdapter.notifyDataSetChanged();
        road.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               RoadOnClick(roadsuggestions,databasehelper);
            }
        });
    }

    private void RoadOnClick(ArrayList<String> roadsuggestions, final billingdatabaseHelper databasehelper) {
        int index = roadsuggestions.indexOf(road.getEditableText().toString());
        Road Roadselect = MainActivity.roads.get(index);
        MainActivity.houses = databasehelper.getHousesbyRoadID(Roadselect.getId());
        final ArrayList<String> housesuggestions = new ArrayList<String>();
        for(int i = 0;i< MainActivity.houses.size();i++){
            Log.v("house naam ki",MainActivity.houses.get(i).getHouse());
            housesuggestions.add(MainActivity.houses.get(i).getHouse());
        }
        Log.v("house size ki","__"+housesuggestions.size());
        ArrayAdapter<String> housearrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, housesuggestions);
        house.setAdapter(housearrayAdapter);
        housearrayAdapter.notifyDataSetChanged();
        house.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             houseOnclick(housesuggestions,databasehelper);
            }
        });
    }

    private void houseOnclick(final ArrayList<String> housesuggestions, final billingdatabaseHelper databasehelper) {
        final int index = housesuggestions.indexOf(house.getEditableText().toString());

        final ArrayList<Customers> customers = databasehelper.getCustomersbyHouseID(MainActivity.houses.get(index).getId());
        if(customers.size()>0){
//                    customerid.setText(customers.get(0).getCustomers_id());
//                    telephonenumber.setText(customers.get(0).getPhone());
            MainActivity.customerSelected = customers;
            final ArrayList<String> customerSuggestions = new ArrayList<String>();
            final ArrayList<String> customerphoneSuggestions = new ArrayList<String>();
            for(int i = 0;i< MainActivity.customerSelected.size();i++){
                Log.v("customer",MainActivity.customerSelected.get(i).getCustomers_id());
                customerSuggestions.add(MainActivity.customerSelected.get(i).getCustomers_id());
                customerphoneSuggestions.add(MainActivity.customerSelected.get(i).getPhone());
            }
            ArrayAdapter<String> customerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, customerSuggestions);
            customerid.setAdapter(customerArrayAdapter);
            customerArrayAdapter.notifyDataSetChanged();
            customerid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int customerindex = customerSuggestions.indexOf(customerid.getEditableText().toString());
//                    houseOnclick(customerSuggestions,databasehelper);
                    MainActivity.customerForProcessing = customers.get(customerindex);
                    telephonenumber.setText(MainActivity.customerForProcessing.getPhone());
                }
            });

            ArrayAdapter<String> customerPhoneArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, customerphoneSuggestions);
            telephonenumber.setAdapter(customerPhoneArrayAdapter);
            customerPhoneArrayAdapter.notifyDataSetChanged();
            telephonenumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int customerindex = customerphoneSuggestions.indexOf(telephonenumber.getEditableText().toString());
//                    houseOnclick(customerSuggestions,databasehelper);
                    MainActivity.customerForProcessing = customers.get(customerindex);
                    customerid.setText( MainActivity.customerForProcessing.getCustomers_id());
                }
            });


            billpayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (checkBlankFields()){
                        ((MainActivity) getActivity()).billPaymentFragment.initiateCustomers(MainActivity.customerForProcessing);
                        ((MainActivity) getActivity()).mViewPager.setCurrentItem(1);

                    }
                    else
                    {
                        Toast.makeText(getContext(),"USER INFORMATION MISSING",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private boolean checkBlankFields() {

        if (atcv.getEditableText().toString()=="" || sector.getEditableText().toString()=="" || road.getEditableText().toString()=="" || house.getEditableText().toString()=="" ||
                customerid.getEditableText().toString()=="" || telephonenumber.getEditableText().toString()==""){
            return false;
        }
        return true;
    }


}
