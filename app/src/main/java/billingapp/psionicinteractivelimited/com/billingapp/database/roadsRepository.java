package billingapp.psionicinteractivelimited.com.billingapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import billingapp.psionicinteractivelimited.com.billingapp.model.location.House;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Road;

/**
 * Created by raihan on 8/19/16.
 */
public class roadsRepository {
    public static String tableName = "roads";
    public static String id = "id";
    public static String sectors_id = "sectors_id";
    public static String road = "road";

    public static String [] columns = {id,sectors_id,road};
    public static String sqlStatement = "CREATE TABLE roads(id VARCHAR PRIMARY KEY, sectors_id VARCHAR, road VARCHAR)";

    public static void createsql(SQLiteDatabase database){
        database.execSQL(sqlStatement);
    }

    public static ContentValues getRoadsValues(Road roads){
        ContentValues values = new ContentValues();
        values.put(id, roads.getId());
        values.put(sectors_id, roads.getSectors_id());
        values.put(road,roads.getRoad());
        return values;
    }
    public static Road getRoad(Cursor cursor){
        Road road = new Road(cursor.getString(0), cursor.getString(1),cursor.getString(2));
        return road;
    }

    public static ArrayList<Road> findByCaseID(String caseId,SQLiteDatabase database) {
        Cursor cursor = database.query(tableName, columns, id + " = ?", new String[]{caseId},
                null, null, null, null);
        return readAllRoad(cursor);
    }
    public static ArrayList<Road> findBySectorsID(String sectorid,SQLiteDatabase database) {
        Cursor cursor = database.query(tableName, columns, sectors_id + " = ?", new String[]{sectorid},
                null, null, null, null);
        return readAllRoad(cursor);
    }
    private static ArrayList<Road> readAllRoad(Cursor cursor) {
        cursor.moveToFirst();
        ArrayList<Road> roads = new ArrayList<Road>();
        while (!cursor.isAfterLast()) {
            Road road = getRoad(cursor);
            roads.add(road);
            cursor.moveToNext();
        }
        cursor.close();
        return roads;
    }

}
