package billingapp.psionicinteractivelimited.com.billingapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import billingapp.psionicinteractivelimited.com.billingapp.LoginActivity;
import billingapp.psionicinteractivelimited.com.billingapp.MainActivity;
import billingapp.psionicinteractivelimited.com.billingapp.R;
import billingapp.psionicinteractivelimited.com.billingapp.model.GPSTracker;
import billingapp.psionicinteractivelimited.com.billingapp.model.customers.Customers;
import billingapp.psionicinteractivelimited.com.billingapp.database.billingdatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PrintReceiptFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrintReceiptFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView address;
    private TextView user_name;
    private TextView user_id;
    private TagView tagGroup;
    private TextView amount_due_info;

    //ush: started
    private TextView mTextView_company;
    private TextView mTextView_user;

    private String print_address="";
    private String print_user_name="";
    private String print_user_id="";
    private String print_due_amount="";
    private String print_due_month="";
    private String print_payment_date="";
    private String print_notice="";
    private String print_powered_by="";
    private int monthsCounter;

    private Button confirmationButton;

    GPSTracker gps;

    Customers customer_global;


    //ush: ends


    public PrintReceiptFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BillPaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrintReceiptFragment newInstance(String param1, String param2) {
        PrintReceiptFragment fragment = new PrintReceiptFragment();
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
        View view = inflater.inflate(R.layout.fragment_print, container, false);

        final billingdatabaseHelper databasehelper = new billingdatabaseHelper(getActivity(),1);

        mTextView_company= (TextView) view.findViewById(R.id.print_texts_company);
        mTextView_user= (TextView) view.findViewById(R.id.print_texts_user);
        //ush: started
        confirmationButton= (Button) view.findViewById(R.id.confirmationButton);


        //ush: ends
//        mTextView_user.setShadowLayer(1, 0, 0, Color.BLACK);
//        mTextView_company.setShadowLayer(1, 0, 0, Color.BLACK);

        confirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gps=new GPSTracker(getContext());
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                    databasehelper.makeTimeStampEmpty(customer_global,""+latitude,""+longitude,""+print_due_amount,monthsCounter,print_payment_date);
//                    Log.v("updddated_attttttttttt",cursor_tostring);




                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

                ((MainActivity)getActivity()).mViewPager.setCurrentItem(0);
                ((MainActivity)getActivity()).locationFragment.getBackFromPrintScreen();

//                Log.v("updddated at",""+databasehelper.makeTimeStampEmpty(customer_global));




            }
        });
//        public void proceedToConfirmation(View v) {
//        Toast.makeText(getContext(), "processing...", Toast.LENGTH_LONG).show();
//        }

        return view;
    }
    public void initiateCustomers(Customers customer,String amount,String months,int monthsCounter){
//        TextView UserInformation
        customer_global=customer;
        String print_address=customer.getAddress();
//        String print_user_name="Psionic Interactive Limited";
        String print_user_name=customer.getName();
        String print_user_id=customer.getCustomer_code();
         print_due_amount=amount;
         print_due_month=months;
        this.monthsCounter=monthsCounter;
        print_payment_date=new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String print_notice="Please submit a signed copy of the bill to the collector.";
        print_powered_by="Psionic Interactive Limited";



        mTextView_company.setText(Html.fromHtml("<b>DIGI 21 Cable Service<b><br>" + "House $3, Road #4, Sector #1<br>" +
                "Uttara Model Town<br>Tel: 8915857<br>*********************************<br><br>"));


        mTextView_user.setText(Html.fromHtml(

                "User Information <br> "+print_address +"<br>"+
                        "User Name: "+print_user_name+"<br>"+
                        "User ID: "+print_user_id+"<br>"+"<br>"+
                        "Amount Due:<br>BDT "+print_due_amount+"<br>"+"<br>"+
                        "Month Due:<br><b>"+print_due_month+"</b><br>"+"<br>"+
                        "Date: "+ print_payment_date+"<br><br>"+
                        print_notice+"<br><br>"+
                        "Powered by: "+print_powered_by
        ));




    }





}
