package billingapp.psionicinteractivelimited.com.billingapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
    public static String phone = "phone";
    public static String price = "price";
    public static String customer_code = "customer_code";
    public static String address = "address";
    public static String customers_id = "customers_id";
    public static String name = "name";

    public static String last_paid = "last_paid";
    public static String [] columns = {houses_id,phone,price,customer_code,address,customers_id,name,last_paid};
    public static String sqlStatement = "CREATE TABLE customers(houses_id VARCHAR , phone VARCHAR, price VARCHAR, customer_code VARCHAR, address VARCHAR, customers_id INTEGER PRIMARY KEY, name VARCHAR, last_paid VARCHAR )";

    public static void createsql(SQLiteDatabase database){
        database.execSQL(sqlStatement);
    }

    public static ContentValues getCustomerValues(Customers customer){
        ContentValues values = new ContentValues();
        values.put(houses_id, customer.getHouses_id());
        values.put(phone, customer.getPhone());
        values.put(price, customer.getPrice());
        values.put(customer_code, customer.getCustomer_code());
        values.put(address, customer.getAddress());
        values.put(customers_id, customer.getCustomers_id());
        values.put(name,customer.getName());
        values.put(last_paid,customer.getLast_paid());
        return values;
    }

    public static Customers getCustomer(Cursor cursor){
        Customers customers = new Customers(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6),cursor.getString(7));
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

}
