package billingapp.psionicinteractivelimited.com.billingapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import billingapp.psionicinteractivelimited.com.billingapp.model.location.Road;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Sector;

/**
 * Created by raihan on 8/19/16.
 */
public class sectorRepository {
    public static String tableName = "sectors";
    public static String id = "id";
    public static String territory_id = "territory_id";
    public static String sector = "sector";

    public static String [] columns = {id,territory_id,sector};
    public static String sqlStatement = "CREATE TABLE sectors(id VARCHAR PRIMARY KEY, territory_id VARCHAR, sector VARCHAR)";

    public static void createsql(SQLiteDatabase database){
        database.execSQL(sqlStatement);
    }

    public static ContentValues getSectorValues(Sector sectors){
        ContentValues values = new ContentValues();
        values.put(id, sectors.getId());
        values.put(territory_id, sectors.getTerritory_id());
        values.put(sector,sectors.getSector());
        return values;
    }
    public static Sector getSector(Cursor cursor){
        Sector sector = new Sector(cursor.getString(2),cursor.getString(0), cursor.getString(1));
        return sector;
    }

    public static ArrayList<Sector> findByCaseID(String caseId,SQLiteDatabase database) {
        Cursor cursor = database.query(tableName, columns, id + " = ?", new String[]{caseId},
                null, null, null, null);
        return readAllSector(cursor);
    }
    public static ArrayList<Sector> findByterritory_id(String territory_ids,SQLiteDatabase database) {
        Cursor cursor = database.query(tableName, columns, territory_id + " = ?", new String[]{territory_ids},
                null, null, null, null);
        return readAllSector(cursor);
    }
    private static ArrayList<Sector> readAllSector(Cursor cursor) {
        cursor.moveToFirst();
        ArrayList<Sector> sectors = new ArrayList<Sector>();
        while (!cursor.isAfterLast()) {
            Sector sector = getSector(cursor);
            sectors.add(sector);
            cursor.moveToNext();
        }
        cursor.close();
        return sectors;
    }

}
