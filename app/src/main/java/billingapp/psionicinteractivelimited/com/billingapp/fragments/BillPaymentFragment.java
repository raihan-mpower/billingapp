package billingapp.psionicinteractivelimited.com.billingapp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;

import java.util.ArrayList;

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

        return view;
    }
    public void initiateCustomers(Customers customer){
//        TextView UserInformation

        address.setText(customer.getAddress());
        user_name.setText(customer.getName());
        user_id.setText(customer.getCustomer_code());
        amount_due_info.setText(customer.getPrice());

        Tag tag = new Tag(customer.getLast_paid());
        tag.radius = 10f;
        tag.layoutColor = Color.GRAY;
        tag.tagTextColor = Color.BLACK;
        tag.isDeletable = true;
        ArrayList<Tag> tags = new ArrayList<>();

        tags.add(tag);

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
            }
        });

    }



}
