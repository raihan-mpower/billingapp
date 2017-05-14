package billingapp.psionicinteractivelimited.com.billingapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import billingapp.psionicinteractivelimited.com.billingapp.model.customers.Customers;

/**
 * Created by raihan on 8/19/16.
 */
public class customerRepository {
    public static String tableName = "customers";
    public static String houses_id = "houses_id";
    public static String flat = "flat";
    public static String price = "price";
    public static String customer_code = "customer_code";
    public static String address = "address";
    public static String customers_id = "customers_id";
    public static String name = "name";
    public static String last_paid = "last_paid";

    //ius. start
    public static String updated_at="updated_at";

    //variable after bill payment (to sync after payment)
    public static String to_sync_lat="to_sync_lat";
    public static String to_sync_lon="to_sync_lon";
    public static String to_sync_paying_for="to_sync_paying_for";
    public static String to_sync_total_amount="to_sync_total_amount";
    public static String to_sync_collection_date="to_sync_collection_date";
    public static String customer_due="due";

    public static String phone="phone";


    //ius. end



    public static String [] columns = {houses_id,flat,price,customer_code,address,customers_id,name,last_paid,updated_at,to_sync_lat,to_sync_lon,to_sync_paying_for,to_sync_total_amount,to_sync_collection_date,customer_due,phone};
    public static String sqlStatement = "CREATE TABLE customers(houses_id VARCHAR , flat VARCHAR, price VARCHAR, customer_code VARCHAR, address VARCHAR, customers_id INTEGER PRIMARY KEY, name VARCHAR, last_paid VARCHAR, updated_at VARCHAR, to_sync_lat VARCHAR, to_sync_lon VARCHAR, to_sync_paying_for VARCHAR, to_sync_total_amount VARCHAR, to_sync_collection_date VARCHAR, due VARCHAR,phone VARCHAR )";

    public static void createsql(SQLiteDatabase database){
        database.execSQL(sqlStatement);
    }

    public static ContentValues getCustomerValues(Customers customer){
        ContentValues values = new ContentValues();
        values.put(houses_id, customer.getHouses_id());
        values.put(flat, customer.getFlat());
        values.put(price, customer.getPrice());
        values.put(customer_code, customer.getCustomer_code());
        values.put(address, customer.getAddress());
        values.put(customers_id, customer.getCustomers_id());
        values.put(name,customer.getName());
        values.put(last_paid,customer.getLast_paid());
        values.put(updated_at,customer.getUpdated_at());
        values.put(to_sync_lat,customer.get_to_sync_lat());
        values.put(to_sync_lon,customer.get_to_sync_lon());
        values.put(to_sync_paying_for,customer.get_to_sync_paying_for());
        values.put(to_sync_total_amount,customer.get_to_sync_total_amount());
        values.put(to_sync_collection_date,customer.get_to_sync_collection_date());
        values.put(customer_due,customer.getDue());
        values.put(phone,customer.getPhone());
        return values;
    }

    public static Customers getCustomer(Cursor cursor){
        Customers customers = new Customers(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(15));
        customers.setDue(cursor.getString(14));
        return customers;
    }

    public static ArrayList<Customers> findByCaseID(String caseId,SQLiteDatabase database) {
        Cursor cursor = database.query(tableName, columns, houses_id + " = ?", new String[]{caseId},
                null, null, null, null);
        return readAllCustomers(cursor);
    }
    public static ArrayList<Customers> findByCustomerCaseID(String caseId,SQLiteDatabase database) {
        Cursor cursor = database.query(tableName, columns, customers_id + " = ?", new String[]{caseId},
                null, null, null, null);
        return readAllCustomers(cursor);
    }
    private static ArrayList<Customers> readAllCustomers(Cursor cursor) {
        cursor.moveToFirst();
        ArrayList<Customers> customers = new ArrayList<Customers>();
        while (!cursor.isAfterLast()) {
            Customers customer = getCustomer(cursor);
            customers.add(customer);
            cursor.moveToNext();
        }
        cursor.close();
        return customers;
    }


    public static ArrayList<Customers> findCustomerByBlankTimestamp(SQLiteDatabase database) {
        Cursor cursor = database.query(tableName, columns, null, null, null, null, null, null);
        cursor.moveToFirst();
        ArrayList<Customers> customers = new ArrayList<Customers>();

        while (!cursor.isAfterLast()) {
            Customers customer = getCustomer(cursor);


            if (cursor.getString(8).equals("")){

                customer.set_to_sync_lat(cursor.getString(9));
                customer.set_to_sync_lon(cursor.getString(10));
                customer.set_to_sync_paying_for(cursor.getString(11));
                customer.set_to_sync_total_amount(cursor.getString(12));
                customer.set_to_sync_collection_date(cursor.getString(13));
                Log.v("temporarycustomer",customer.toString());
                customers.add(customer);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return customers;
    }

    public static Customers findCustomerByCustomerCode(SQLiteDatabase database,String customer_search_code) {

//

        Cursor cursor = database.query(tableName, columns, null, null, null, null, null, null);
        cursor.moveToFirst();

        Customers matchedCustomer=new Customers(null,null,null,null,null,null,null,null,null,null);

        while (!cursor.isAfterLast()) {
            Customers customer = getCustomer(cursor);


            if (cursor.getString(3).equals(customer_search_code)){
                matchedCustomer=customer;
            }
            cursor.moveToNext();
        }
        cursor.close();

//        Log.v("alpha1",customers.get(0).get_to_sync_total_amount());
//        Log.v("alpha1",customers.get(1).get_to_sync_total_amount());


        return matchedCustomer;



    }



    public static ArrayList<Customers> getALLCustomers(SQLiteDatabase readableDatabase) {
        Cursor cursor = readableDatabase.query(tableName, columns,null, null, null, null, null, null);
        return readAllCustomers(cursor);
    }




    public static ArrayList<String> findCustomersByCustomerCodeSubstring(SQLiteDatabase database, String s) {

        Cursor cursor = database.query(tableName, columns, null, null, null, null, null, null);
        cursor.moveToFirst();

        ArrayList<String> customers = new ArrayList<String>();

        while (!cursor.isAfterLast()) {
            Customers customer = getCustomer(cursor);


            if (cursor.getString(3).contains(s)){

                customers.add(customer.getCustomer_code()+"-"+customer.getName());
            }
            cursor.moveToNext();
        }
        cursor.close();
        return customers;


    }
}
