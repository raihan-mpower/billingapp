package billingapp.psionicinteractivelimited.com.billingapp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import billingapp.psionicinteractivelimited.com.billingapp.MainActivity;
import billingapp.psionicinteractivelimited.com.billingapp.R;
import billingapp.psionicinteractivelimited.com.billingapp.model.customers.Customers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link BillPaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillPaymentFragment extends Fragment {
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
    public int count = 0;
    String amount = "0.00";
    String months = "";
    private Button print;

    //u.start
    int customer_price;
    //u.end


    public BillPaymentFragment() {
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
    public static BillPaymentFragment newInstance(String param1, String param2) {
        BillPaymentFragment fragment = new BillPaymentFragment();
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
        View view = inflater.inflate(R.layout.fragment_bill_payment, container, false);
        address = (TextView) view.findViewById(R.id.address_info);
        user_name = (TextView) view.findViewById(R.id.username_info);
        user_id = (TextView) view.findViewById(R.id.userid_info);
        amount_due_info = (TextView) view.findViewById(R.id.amount_due_info);
        tagGroup = (TagView)view.findViewById(R.id.tag_group);
        print = (Button)view.findViewById(R.id.button_print);

        return view;
    }
    public void initiateCustomers(final Customers customer){
//        TextView UserInformation\

        count = 0;
        address.setText(customer.getAddress());
        user_name.setText(customer.getName());
        user_id.setText(customer.getCustomer_code());

        //u.start
        customer_price=Integer.parseInt(customer.getPrice());
        //u.end

        ArrayList<String> monthsdue = getmonthsDue(customer.getLast_paid());


        final ArrayList<Tag> tags = new ArrayList<>();

        for(int i = 0;i < monthsdue.size();i++){
            Tag tag = new Tag(monthsdue.get(i));
            tag.radius = 10f;
            tag.layoutColor = Color.GRAY;
            tag.tagTextColor = Color.BLACK;
            tag.isDeletable = true;
            tags.add(tag);
        }



        tagGroup.addTags(tags);
        //set click listener
        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
            }
        });

        //set delete listener
        tagGroup.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(final TagView view, final Tag tag, final int position) {
                tagGroup.remove(position);
                if(months.equalsIgnoreCase("")){
                    months = tag.text;
                }else {
                    months = months + "," + tag.text;
                }
//                tag.text;
                //u.start
//                amount_due_info.setText(Integer.parseInt(amount)-customer_price);
//                amount_due_info.setText("100");


                try {
//                Log.v("price now",""+Integer.parseInt(customer.getPrice())*count);
                    int newPrice=(Integer.parseInt(customer.getPrice())*count-(Integer.parseInt(customer.getPrice())));
                    amount_due_info.setText(""+newPrice);
                    amount = ""+newPrice;
                }catch (Exception e){
                    amount_due_info.setText(customer.getPrice());
//
                }
//                tagGroup = (TagView)view.findViewById(R.id.tag_group);
//                tagGroup.addTags(tags);

                //u.end



            }
        });
        if(customer.getPrice()!=null) {
            try {
//                Log.v("price now",""+Integer.parseInt(customer.getPrice())*count);
                amount_due_info.setText(""+(Integer.parseInt(customer.getPrice())*count));
                amount = ""+(Integer.parseInt(customer.getPrice())*count);
            }catch (Exception e){
//                amount_due_info.setText(customer.getPrice());
//
            }
        }


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amountInInt= Integer.parseInt(amount);
                if(amountInInt<=0){
                        //do nothing
                }else{
                    ((MainActivity)getActivity()).printReceipttFragment.initiateCustomers(MainActivity.customerSelected.get(0),amount,months);
                    ((MainActivity)getActivity()).mViewPager.setCurrentItem(2);


                }
            }
        });

    }

    public ArrayList<String> getmonthsDue(String lastpaidmonth){
        ArrayList<String> months = new ArrayList<String>();
        months.add("jan");
        months.add("feb");
        months.add("mar");
        months.add("apr");
        months.add("may");
        months.add("jun");
        months.add("jul");
        months.add("aug");
        months.add("sep");
        months.add("oct");
        months.add("nov");
        months.add("dec");

        Calendar cal= Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(cal.getTime());

        String strippedlastpaidmonth = lastpaidmonth.substring(0,3);
        Log.v("last paid month",""+strippedlastpaidmonth);
        String strippedcurrentmonth = month_name.substring(0,3);

        int indexoflastpaid = months.indexOf(strippedlastpaidmonth.toLowerCase());
        int indexofcurrentmonth = months.indexOf(strippedcurrentmonth.toLowerCase());

        Log.v("last paid month",""+indexoflastpaid);
        Log.v("current month",""+indexofcurrentmonth);

        ArrayList<String> monthstoreturn = new ArrayList<String>();
        if((indexofcurrentmonth-indexoflastpaid)>0){
            for(int i = indexoflastpaid;i<indexofcurrentmonth;i++){
                monthstoreturn.add(months.get(i));
                count ++;
            }
        }else if((indexofcurrentmonth-indexoflastpaid)<0){
            for(int i = indexofcurrentmonth;i<12;i++){
                monthstoreturn.add(months.get(i));
                count++;
            }
            for(int i = 0;i<indexoflastpaid;i++){
                monthstoreturn.add(months.get(i));
                count++;
            }
        }
        return monthstoreturn;


    }



}
