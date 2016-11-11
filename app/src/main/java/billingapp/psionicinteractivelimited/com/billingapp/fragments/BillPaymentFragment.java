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
import android.widget.Toast;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    private Button print;
    private Button add_month;

    ArrayList<String> months=new ArrayList<>();
    int monthsCounter=0;
    //u.start
    int customer_price;

//    final ArrayList<Tag> tags= new ArrayList<>();
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

        add_month= (Button) view.findViewById(R.id.button_add_month);



        return view;
    }
    public void initiateCustomers(final Customers customer) throws ParseException {
//        TextView UserInformation\

        count = 0;
        address.setText(customer.getAddress());
        user_name.setText(customer.getName());
        user_id.setText(customer.getCustomer_code());

        //u.start
        customer_price=Integer.parseInt(customer.getPrice());
        //u.end

//        Toast.makeText(getContext(), customer.getLast_paid(), Toast.LENGTH_SHORT).show();
        String s=customer.getLast_paid();
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
        Date date=simpleDateFormat.parse(s);
        int m=date.getMonth();
        Toast.makeText(getContext(), "Last paid "+getMonthFromInt(m), Toast.LENGTH_SHORT).show();


        ArrayList<String> monthsdue = getmonthsDue(getMonthFromInt(m));


       final ArrayList<Tag> tags = new ArrayList<>();


        for(int i = 0;i < monthsdue.size();i++){
            Tag tag = new Tag(monthsdue.get(i));
            tag.radius = 10f;
            tag.layoutColor = Color.GRAY;
            tag.tagTextColor = Color.BLACK;
            tag.isDeletable = true;
            tags.add(tag);

            //ush.start
//            months= months + " " + tag.text;
            months.add(tag.text+"");
            monthsCounter++;
            //
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

                Log.v("tag position",""+position);
                Log.v("months counter",""+monthsCounter);
                Log.v("count",""+count);
//                && position==monthsCounter+1
                if(!months.isEmpty() && position==(count-1)){
                    //removing deleted month from "months" string
                    tagGroup.remove(position);
                    Toast.makeText(getActivity(), "Month "+tag.text+" Deleted successfully", Toast.LENGTH_LONG).show();
                    tags.remove(position);
                    months.remove(position);
                    monthsCounter--;
                    //ush.end
                    changePriceandTag(customer,-1);
                }
                else
                {
                    Toast.makeText(getContext(), "Can't delete this tag", Toast.LENGTH_SHORT).show();
                }

                //u.start
//                changePriceandTag(customer,-1);
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
//                    months=monthTextChecker(months);
                    String monthsString=months.toString();
//                    ((MainActivity)getActivity()).printReceipttFragment.initiateCustomers(MainActivity.customerSelected.get(0),amount,months);
                    ((MainActivity)getActivity()).printReceipttFragment.initiateCustomers(MainActivity.customerForProcessing,amount,monthsString,monthsCounter);

                    ((MainActivity)getActivity()).mViewPager.setCurrentItem(2);


                }
            }
        });

        add_month.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {


//                Toast.makeText(getActivity(), ""+months.get(monthsCounter-1), Toast.LENGTH_SHORT).show();
                if(monthsCounter>0) {
                    Tag tag = new Tag(getNextMonth(months.get(monthsCounter - 1)));
                    tag.radius = 10f;
                    tag.layoutColor = Color.GRAY;
                    tag.tagTextColor = Color.BLACK;
                    tag.isDeletable = true;
                    tags.add(tag);

                    months.add(tag.text);
                    monthsCounter++;

                    tagGroup.addTags(tags);
                    changePriceandTag(customer, 1);
                }
                else if(monthsCounter==0){
                    Toast.makeText(getActivity(), ""+customer.getLast_paid(), Toast.LENGTH_SHORT).show();

                    Tag tag = new Tag(getNextMonthServer(customer.getLast_paid().toLowerCase()));

                    tag.radius = 10f;
                    tag.layoutColor = Color.GRAY;
                    tag.tagTextColor = Color.BLACK;
                    tag.isDeletable = true;
                    tags.add(tag);

                    months.add(tag.text);
                    monthsCounter++;

                    tagGroup.addTags(tags);
                    changePriceandTag(customer, 1);


                }else
                {
                    Toast.makeText(getActivity(), "No month added", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    //ush.start

    //comma editor
//    public String monthTextChecker(String monthsForCheck){
//        while(true)
//        if(monthsForCheck.charAt(0)==',' || )
//        {
//            monthsForCheck.replace(",","");
//        }
//
//        return monthsForCheck;
//    }

    public void changePriceandTag(Customers custom,int reduceORadd){

        int newPrice;

        if(reduceORadd<0)
        {
            newPrice = (Integer.parseInt(custom.getPrice()) * count - (Integer.parseInt(custom.getPrice())));
            count--;
        }else{
            newPrice = (Integer.parseInt(custom.getPrice()) * count + (Integer.parseInt(custom.getPrice())));
            count++;
        }

        amount = "" + newPrice;
        amount_due_info.setText(amount);
    }
    //ush.end


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
            for(int i = indexoflastpaid;i<12;i++){
                monthstoreturn.add(months.get(i));
                count++;
            }
            for(int i = 0;i<indexofcurrentmonth;i++){
                monthstoreturn.add(months.get(i));
                count++;
            }
        }
        return monthstoreturn;


    }

    public String getNextMonth(String lastmonth){
        String[] cal={"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"};
        for(int i=0;i<cal.length;i++){
//            Log.v("Last Month", lastmonth);
            if(lastmonth.equals(cal[i])){
//                Log.v("what month", cal[i]);
                return cal[(i+1)%12];
            }
        }
        return "err";

    }

    public String getNextMonthServer(String month){
        String[] cal={"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"};
        for(int i=0;i<12;i++){
//            Log.v("Last Month", lastmonth);
            if(month.contains(cal[i])){
//                Log.v("what month", cal[i]);
                return cal[(i+1)%12];
            }
        }
        return "err";
    }
    public String getMonthFromInt(int m) {
        String[] cal = {"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};
        return cal[m];
    }



}
