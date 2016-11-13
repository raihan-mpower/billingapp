package billingapp.psionicinteractivelimited.com.billingapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import billingapp.psionicinteractivelimited.com.billingapp.model.location.House;

/**
 * Created by raihan on 8/19/16.
 */
public class CustomerEditRepository {
    public static String _id = "_id";
    public static String customer_id = "customer_id";
    public static String tablename = "CustomerEdit";
    public static String [] columns = {_id ,customer_id};
    public static String sqlStatement = "CREATE TABLE CustomerEdit(_id INTEGER PRIMARY KEY AUTOINCREMENT,customer_id VARCHAR)";

    public static void createsql(SQLiteDatabase database){
        database.execSQL(sqlStatement);
    }

    public static ContentValues getCustomerEditValues(String customer_ids){
        ContentValues values = new ContentValues();
        values.put(customer_id,customer_ids);
        return values;
    }
    public static String getCustomerIDforEdit(Cursor cursor){
        return cursor.getString(1);
    }


    public static ArrayList<String> readAllCustomerEdit(Cursor cursor) {
        cursor.moveToFirst();
        ArrayList<String> customerdEditIDList = new ArrayList<String>();
        while (!cursor.isAfterLast()) {
            String customer = getCustomerIDforEdit(cursor);
            customerdEditIDList.add(customer);
            cursor.moveToNext();
        }
        cursor.close();
        return customerdEditIDList;
    }

}
