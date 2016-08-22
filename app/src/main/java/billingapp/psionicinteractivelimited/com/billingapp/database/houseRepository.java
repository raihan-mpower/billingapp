package billingapp.psionicinteractivelimited.com.billingapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import billingapp.psionicinteractivelimited.com.billingapp.model.customers.Customers;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.House;

/**
 * Created by raihan on 8/19/16.
 */
public class houseRepository {
    public static String tableName = "house";
    public static String id = "id";
    public static String roads_id = "roads_id";
    public static String house = "house";

    public static String [] columns = {id,roads_id,house};
    public static String sqlStatement = "CREATE TABLE house(id VARCHAR PRIMARY KEY, roads_id VARCHAR, house VARCHAR)";

    public static void createsql(SQLiteDatabase database){
        database.execSQL(sqlStatement);
    }

    public static ContentValues getHouseValues(House House){
        ContentValues values = new ContentValues();
        values.put(id, House.getId());
        values.put(roads_id, House.getRoads_id());
        values.put(house,House.getHouse());
        return values;
    }
    public static House getHouse(Cursor cursor){
        House house = new House(cursor.getString(2),cursor.getString(0), cursor.getString(1));
        return house;
    }

    public static ArrayList<House> findByCaseID(String caseId,SQLiteDatabase database) {
        Cursor cursor = database.query(tableName, columns, id + " = ?", new String[]{caseId},
                null, null, null, null);
        return readAllHouse(cursor);
    }
    public static ArrayList<House> findByRoadsID(String roadId,SQLiteDatabase database) {
        Cursor cursor = database.query(tableName, columns, roads_id + " = ?", new String[]{roadId},
                null, null, null, null);
        return readAllHouse(cursor);
    }
    private static ArrayList<House> readAllHouse(Cursor cursor) {
        cursor.moveToFirst();
        ArrayList<House> houses = new ArrayList<House>();
        while (!cursor.isAfterLast()) {
            House house = getHouse(cursor);
            houses.add(house);
            cursor.moveToNext();
        }
        cursor.close();
        return houses;
    }

}
