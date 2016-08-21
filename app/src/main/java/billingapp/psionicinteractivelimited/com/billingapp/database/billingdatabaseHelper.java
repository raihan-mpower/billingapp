package billingapp.psionicinteractivelimited.com.billingapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import billingapp.psionicinteractivelimited.com.billingapp.model.customers.Customers;

/**
 * Created by raihan on 8/17/16.
 */
public class billingdatabaseHelper extends SQLiteOpenHelper {
    public billingdatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertCustomers(ArrayList<Customers> customerlist){
        SQLiteDatabase database = getWritableDatabase();
        for(int i = 0;i<customerlist.size();i++) {
            database.insert(customerRepository.tableName, null, customerRepository.getCustomerValues(customerlist.get(i)));
        }
    }
}
