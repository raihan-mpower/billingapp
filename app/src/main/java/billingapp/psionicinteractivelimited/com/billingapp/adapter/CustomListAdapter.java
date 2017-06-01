package billingapp.psionicinteractivelimited.com.billingapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import billingapp.psionicinteractivelimited.com.billingapp.R;
import billingapp.psionicinteractivelimited.com.billingapp.model.customers.Customers;


public class CustomListAdapter extends ArrayAdapter<Customers> {


    static ArrayList<Customers> customers;
    Context context;
    int resource;
//    final boolean[] checkArray;






    public CustomListAdapter(Context context, int resource, ArrayList<Customers> customers) {
        super(context, resource, customers);
        this.customers = customers;
        this.context = context;
        this.resource = resource;


//        checkArray=new boolean[products.size()];
    }

    public void addListItemToAdapter(List<Customers> list){

        customers.addAll(list);
        this.notifyDataSetChanged();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null, true);

        }
        final Customers product = getItem(position);
        Log.v("imam1","listCustomerPrint "+ product.toString());
//        product.toString();

//        Typeface lato_font = Typeface.createFromAsset(getContext().getAssets(), "fonts/lato.ttf");
//
//        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
//        Picasso.with(context).load(product.getImage()).into(imageView);
//
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(product.getName());
//        txtName.setText(product.getName());
//        txtName.setTypeface(lato_font);
//
        TextView id = (TextView) convertView.findViewById(R.id.id);
        id.setText(product.getCustomer_code());
//        txtEmail.setText(product.getEmail());
//        txtEmail.setTypeface(lato_font);
//
        TextView collectiondate = (TextView) convertView.findViewById(R.id.collection_date);
        collectiondate.setText(product.get_to_sync_collection_date());
//        txtMobile.setTypeface(lato_font);

        TextView amount = (TextView) convertView.findViewById(R.id.bill);
        amount.setText(product.get_to_sync_total_amount()+"tk");


//

        return convertView;
    }

}
