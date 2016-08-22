package billingapp.psionicinteractivelimited.com.billingapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import billingapp.psionicinteractivelimited.com.billingapp.model.location.Sector;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Territory;

/**
 * Created by raihan on 8/19/16.
 */
public class territoryRepository {
    public static String tableName = "territory";
    public static String id = "id";
    public static String address = "address";
    public static String name = "name";

    public static String [] columns = {id,address,name};
    public static String sqlStatement = "CREATE TABLE territory(id VARCHAR PRIMARY KEY, address VARCHAR, name VARCHAR)";

    public static void createsql(SQLiteDatabase database){
        database.execSQL(sqlStatement);
    }

    public static ContentValues getTerritoryValues(Territory territory){
        ContentValues values = new ContentValues();
        values.put(id, territory.getId());
        values.put(address, territory.getAddress());
        values.put(name,territory.getName());
        return values;
    }
    public static Territory getTerritory(Cursor cursor){
        Territory territory = new Territory(cursor.getString(1),cursor.getString(0), cursor.getString(2));
        return territory;
    }

    public static ArrayList<Territory> findByCaseID(String caseId,SQLiteDatabase database) {
        Cursor cursor = database.query(tableName, columns, id + " = ?", new String[]{caseId},
                null, null, null, null);
        return readAllTerritory(cursor);
    }
    public static ArrayList<Territory> getALLterritory(SQLiteDatabase database) {
        Cursor cursor = database.query(tableName, columns, null, null,
                null, null, null, null);
        return readAllTerritory(cursor);
    }
    private static ArrayList<Territory> readAllTerritory(Cursor cursor) {
        cursor.moveToFirst();
        ArrayList<Territory> territories = new ArrayList<Territory>();
        while (!cursor.isAfterLast()) {
            Territory territory = getTerritory(cursor);
            territories.add(territory);
            cursor.moveToNext();
        }
        cursor.close();
        return territories;
    }

}
