package billingapp.psionicinteractivelimited.com.billingapp.model.customers;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raihan on 8/17/16.
 */
public class Customers
{
    private String houses_id;


    private String flat;

    private String price;

    private String customer_code;

    private String address;

    private String customers_id;

    private String name;

    private String last_paid;

    private String updated_at;

    public String to_sync_lat="default";
    public String to_sync_lon="default";
    public String to_sync_paying_for="default";
    public String to_sync_total_amount="default";
    public String to_sync_collection_date="default";
    public String due = "";

    public String phone = "";



    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getHouses_id ()
    {
        return houses_id;
    }

    public void setHouses_id (String houses_id)
    {
        this.houses_id = houses_id;
    }

    public String getFlat ()
    {
        return flat;
    }

    public void setFlat (String flat)
    {
        this.flat = flat;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }


    public String getPrice () { return price;}

    public void setPrice (String price)
    {
        this.price = price;
    }

    public String getCustomer_code ()
    {
        return customer_code;
    }

    public void setCustomer_code (String customer_code)
    {
        this.customer_code = customer_code;
    }

    public String get_to_sync_lat () { return to_sync_lat;}
    public String get_to_sync_lon () { return to_sync_lon;}
    public String get_to_sync_paying_for () { return to_sync_paying_for;}
    public String get_to_sync_total_amount () { return to_sync_total_amount;}
    public String get_to_sync_collection_date () { return to_sync_collection_date;}

    public void set_to_sync_lat (String a) { this.to_sync_lat=a;}
    public void set_to_sync_lon (String a) {this.to_sync_lon=a;}
    public void set_to_sync_paying_for (String a) {this.to_sync_paying_for=a;}
    public void set_to_sync_total_amount (String a) { this.to_sync_total_amount=a;}
    public void set_to_sync_collection_date (String a) {this.to_sync_collection_date=a;}

    public Customers(  String houses_id,String flat,String price,String customer_code,String address,String customers_id,String name, String last_paid, String updated_at,String phone )
    {
        this.last_paid = last_paid;
        this.name = name;
        this.customers_id = customers_id;
        this.address = address;
        this.customer_code = customer_code;
        this.price = price;
        this.flat = flat;
        this.houses_id = houses_id;
        this.updated_at = updated_at;

        this.phone=phone;
    }

    public String getAddress ()

    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getCustomers_id ()
    {
        return customers_id;
    }

    public void setCustomers_id (String customers_id)
    {
        this.customers_id = customers_id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getLast_paid ()
    {
        return last_paid;
    }

    public void setLast_paid (String last_paid)
    {
        this.last_paid = last_paid;
    }

    //updated_at

    //ius start
    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    //ius end

    @Override
    public String toString()
    {
        return "ClassPojo [houses_id = "+houses_id+", flat = "+flat+", price = "+price+", customer_code = "+customer_code+", address = "+address+", customers_id = "+customers_id+", name = "+name+", last_paid = "+last_paid+", updated_at = "+updated_at+", location: lat = "+to_sync_lat+", location: lon = "+to_sync_lon+", paying for = "+to_sync_paying_for+", bill amount = "+to_sync_total_amount+", collection date = "+to_sync_collection_date+"]";
    }

    public static Customers jsontoCustomers(String json) {
        try {
            JSONObject customerJson = new JSONObject(json);
            Customers customerToReturn = new Customers(customerJson.getString("houses_id"),customerJson.getString("flat"),customerJson.getString("price"),customerJson.getString("customer_code"),customerJson.getString("address"),customerJson.getString("customers_id"),customerJson.getString("name"),customerJson.getString("last_paid"),customerJson.getString("updated_at"),customerJson.getString("phone"));
//            Log.v("imam1","RESULT(servertime) :"+customerJson.getString("server_time"));
            return customerToReturn;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.v("imam1","exception2"+e.getMessage());
            return null;
        }
    }
    public static ArrayList<Customers> returnCustomersFromArray(String JsonResponse){
        Log.v("imam1","JsonResponse :"+ JsonResponse);
        ArrayList<Customers> customerlist = new ArrayList<Customers>();
        try {
                JSONObject jo = new JSONObject(JsonResponse);
            JSONArray jr = jo.getJSONArray("0");
            String serverTime =jo.getString("server_time");
            Log.v("imam1","SERVERTIME : "+serverTime);
            for(int i = 0;i<jr.length();i++){
                Customers customers = jsontoCustomers(jr.get(i).toString());
                customerlist.add(customers);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //zamela
            Log.v("imam1","exception1"+e.getMessage());
        }
        return customerlist;

    }

    public static String return_last_time_from_JsonResponse(String JsonResponse){

        String json_parsed_last_time="1970";
        try {
            JSONObject jo =new JSONObject(JsonResponse);
            json_parsed_last_time=jo.getString("server_time");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("imam1","last_time_change_holo :"+json_parsed_last_time);
        return json_parsed_last_time;
    }
}