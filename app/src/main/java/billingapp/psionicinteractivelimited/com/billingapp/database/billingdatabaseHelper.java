package billingapp.psionicinteractivelimited.com.billingapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import billingapp.psionicinteractivelimited.com.billingapp.MainActivity;
import billingapp.psionicinteractivelimited.com.billingapp.model.customers.Customers;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.House;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Road;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Sector;
import billingapp.psionicinteractivelimited.com.billingapp.model.location.Territory;

/**
 * Created by raihan on 8/17/16.
 */
public class billingdatabaseHelper extends SQLiteOpenHelper {
    public billingdatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    public billingdatabaseHelper(Context context, int i) {
        super(context, "billingDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        customerRepository.createsql(db);
        houseRepository.createsql(db);
        roadsRepository.createsql(db);
        sectorRepository.createsql(db);
        territoryRepository.createsql(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
/////////////////customer methods/////////////////////////////
    public void insert_or_update_Customers(ArrayList<Customers> customerlist){
        SQLiteDatabase database = getWritableDatabase();
        for(int i = 0;i<customerlist.size();i++) {
            try {
                if (getCustomersbyCustomerID(customerlist.get(i).getCustomers_id()).size() > 0) {
                    updateCustomer(customerlist.get(i));
                } else {
                    database.insert(customerRepository.tableName, null, customerRepository.getCustomerValues(customerlist.get(i)));
                }
            }catch (Exception e){

            }
        }
    }
    public ArrayList<Customers> getCustomersbyCustomerID(String customerID){
        return customerRepository.findByCustomerCaseID(customerID,getReadableDatabase());
//        return null;
    }
    public ArrayList<Customers> getCustomersbyHouseID(String houseID){
        return customerRepository.findByCaseID(houseID,getReadableDatabase());
//        return null;
    }
    public int getlastCustomerID(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(CAST("+customerRepository.customers_id+" AS INTEGER))  FROM "+customerRepository.tableName,null);
        cursor.moveToFirst();
        int lastid = 0;
        try{
            lastid = Integer.parseInt(cursor.getString(0));
            cursor.close();
        }catch (Exception e){
            lastid = 0;
        }
        return lastid;
    }
    public void updateCustomer(Customers customer) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues valuesToUpdate = customerRepository.getCustomerValues(customer);
        database.update(customerRepository.tableName, valuesToUpdate, customerRepository.houses_id + " = ?", new String[]{customer.getHouses_id()});
    }
    ///////////////////////////////////////////////////////

    /////////////////house methods/////////////////////////////
    public void insert_or_update_House(ArrayList<House> houselist){
        SQLiteDatabase database = getWritableDatabase();
        for(int i = 0;i<houselist.size();i++) {
            try {
                if (getHousebyID(houselist.get(i).getId()).size() > 0) {
                    updateHouse(houselist.get(i));
                } else {
                    database.insert(houseRepository.tableName, null, houseRepository.getHouseValues(houselist.get(i)));
                }
            }catch (Exception e){

            }
        }
    }
    public ArrayList<House> getHousebyID(String houseID){
        return houseRepository.findByCaseID(houseID,getReadableDatabase());
//        return null;
    }
    public ArrayList<House> getHousesbyRoadID(String RoadID){
        return houseRepository.findByRoadsID(RoadID,getReadableDatabase());
//        return null;
    }
    public void updateHouse(House house) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues valuesToUpdate = houseRepository.getHouseValues(house);
        database.update(houseRepository.tableName, valuesToUpdate, houseRepository.id + " = ?", new String[]{house.getId()});
    }
    ///////////////////////////////////////////////////////


    /////////////////road methods/////////////////////////////
    public void insert_or_update_Road(ArrayList<Road> roadlist){
        SQLiteDatabase database = getWritableDatabase();
        for(int i = 0;i<roadlist.size();i++) {
            try {
                if (getRoadbyID(roadlist.get(i).getId()).size() > 0) {
                    updateRoad(roadlist.get(i));
                } else {
                    database.insert(roadsRepository.tableName, null, roadsRepository.getRoadsValues(roadlist.get(i)));
                }
            }catch (Exception e){}
        }
    }
    public ArrayList<Road> getRoadbyID(String roadID){
        return roadsRepository.findByCaseID(roadID,getReadableDatabase());
//        return null;
    }
    public ArrayList<Road> getRoadbySectorID(String sectorID){
        return roadsRepository.findBySectorsID(sectorID,getReadableDatabase());
//        return null;
    }
    public void updateRoad(Road road) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues valuesToUpdate = roadsRepository.getRoadsValues(road);
        database.update(roadsRepository.tableName, valuesToUpdate, roadsRepository.id + " = ?", new String[]{road.getId()});
    }
    ///////////////////////////////////////////////////////


    /////////////////Sector methods/////////////////////////////
    public void insert_or_update_Sector(ArrayList<Sector> sectorlist){
        SQLiteDatabase database = getWritableDatabase();
        for(int i = 0;i<sectorlist.size();i++) {
            try {
                if (getSectorbyID(sectorlist.get(i).getId()).size() > 0) {
                    updateSector(sectorlist.get(i));
                } else {
                    database.insert(sectorRepository.tableName, null, sectorRepository.getSectorValues(sectorlist.get(i)));
                }
            }catch (Exception e){}
        }
    }
    public ArrayList<Sector> getSectorbyID(String sectorID){
        return sectorRepository.findByCaseID(sectorID,getReadableDatabase());
//        return null;
    }
    public ArrayList<Sector> getSectorbyTerritoryID(String territoryID){
        return sectorRepository.findByterritory_id(territoryID,getReadableDatabase());
//        return null;
    }
    public void updateSector(Sector sector) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues valuesToUpdate = sectorRepository.getSectorValues(sector);
        database.update(sectorRepository.tableName, valuesToUpdate, sectorRepository.id + " = ?", new String[]{sector.getId()});
    }
    ///////////////////////////////////////////////////////


    /////////////////Territory methods/////////////////////////////
    public void insert_or_update_Territory(ArrayList<Territory> territorylist){
        SQLiteDatabase database = getWritableDatabase();
        Log.v("list size",""+territorylist.size());
        for(int i = 0;i<territorylist.size();i++) {
            try {
                if (getTerritorybyID(territorylist.get(i).getId()).size() > 0) {
                    updateTerritory(territorylist.get(i));
                } else {
                    database.insert(territoryRepository.tableName, null, territoryRepository.getTerritoryValues(territorylist.get(i)));
                }
            }catch (Exception e){

            }
        }
    }
    public ArrayList<Territory> getTerritorybyID(String territoryID){
        return territoryRepository.findByCaseID(territoryID,getReadableDatabase());
//        return null;
    }
    public ArrayList<Territory> getTerritories(String territoryID){
        return territoryRepository.getALLterritory(getReadableDatabase());
//        return null;
    }
    public void updateTerritory(Territory territory) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues valuesToUpdate = territoryRepository.getTerritoryValues(territory);
        database.update(territoryRepository.tableName, valuesToUpdate, territoryRepository.id + " = ?", new String[]{territory.getId()});
    }
    ///////////////////////////////////////////////////////

    public String makeTimeStampEmpty(Customers customer){
        SQLiteDatabase database_to_null_timestamp = getWritableDatabase();
        ContentValues valuesToUpdate = new ContentValues();
        valuesToUpdate.put("updated_at", "");
        database_to_null_timestamp.update(customerRepository.tableName, valuesToUpdate, customerRepository.customers_id + " = ?", new String[]{customer.getCustomers_id()});

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+customerRepository.updated_at+" FROM "+customerRepository.tableName,null);
        return cursor.toString();

    }



}
