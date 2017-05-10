package billingapp.psionicinteractivelimited.com.billingapp.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.text.ParseException;
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



    //ush.start
    private AutoCompleteTextView customerid;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        ((MainActivity)getActivity()).mViewPager.setCurrentItem(0);
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        final billingdatabaseHelper databasehelper = new billingdatabaseHelper(getActivity(),1);

        MainActivity.territories =territoryRepository.getALLterritory(databasehelper.getReadableDatabase());

        customerid= (AutoCompleteTextView) view.findViewById(R.id.customer_id);


        billpayment = (Button) view.findViewById(R.id.bill_payment);
//
        customerid.setThreshold(-1);



        final ArrayList<String> suggestions = new ArrayList<String>();

        for(int i = 0;i< MainActivity.territories.size();i++){

            Log.v("naam ki",MainActivity.territories.get(i).getName());
            suggestions.add(MainActivity.territories.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, suggestions);
//        atcv.setAdapter(arrayAdapter);
        customerid.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

//        customerid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                customeridOnclick(suggestions,databasehelper);
//            }
//        });


        customerid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s==""){

                }else
                {
                    billingdatabaseHelper dbHelper=new billingdatabaseHelper(getActivity(),1);
                    ArrayList<String> customerSuggestions =  dbHelper.getCustomersByCustomerCodeSubstring(s+"");
                    ArrayAdapter<String> sectorarrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, customerSuggestions);
                    customerid.setAdapter(sectorarrayAdapter);

                }



            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        billpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!checkBlankFields()){


                    //getting the customer_code from the input by split (-)
                String[] str = customerid.getText().toString().split("-");
                String customer_input=str[0];
                    ;
                Log.v("slelected_customer_tv",customer_input);

                billingdatabaseHelper dbHelper=new billingdatabaseHelper(getActivity(),1);
                Customers selectedCustomer =  dbHelper.getCustomerByCustomerCode(customer_input);

                MainActivity.customerForProcessing=selectedCustomer;


                Log.v("selected_customer_test",selectedCustomer.toString());
//                Toast.makeText(getActivity(), ""+selectedCustomer.getName(), Toast.LENGTH_SHORT).show();
                    try {
                        ((MainActivity) getActivity()).billPaymentFragment.initiateCustomers(MainActivity.customerForProcessing);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                ((MainActivity) getActivity()).mViewPager.setPagingEnabled(true);
                ((MainActivity) getActivity()).mViewPager.setCurrentItem(1);

                }
                else
                {
                    Toast.makeText(getContext(), "Select USER", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }





    public void refreshcreateview(){
        final billingdatabaseHelper databasehelper = new billingdatabaseHelper(getActivity(),1);
        MainActivity.territories =territoryRepository.getALLterritory(databasehelper.getReadableDatabase());


        customerid.setThreshold(-1);

        final ArrayList<String> suggestions = new ArrayList<String>();
        for(int i = 0;i< MainActivity.territories.size();i++){
            Log.v("naam ki",MainActivity.territories.get(i).getName());
            suggestions.add(MainActivity.territories.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, suggestions);
        arrayAdapter.notifyDataSetChanged();
    }

//    private boolean checkBlankFields() {
//
//        if (atcv.getEditableText().toString()=="" || sector.getEditableText().toString()=="" || road.getEditableText().toString()=="" || house.getEditableText().toString()=="" ||
//                customerid.getEditableText().toString()=="" || telephonenumber.getEditableText().toString()==""){
//            return false;
//        }
//        return true;
//    }






//    //QR FILL
    public void fillfromQR(String customer_id,String city,String sectorfromqr,String roadfromqr,String housefromqr,String phone){
        customerid.setText(customer_id);
//        telephonenumber.setText(phone);
//        atcv.setText(city);
//        sector.setText(sectorfromqr);
//        house.setText(housefromqr);
//        road.setText(roadfromqr);

    }


    private boolean checkBlankFields() {

        if (customerid.getText().toString().equals("")){
            return true;
        }
        return false;
    }


}
