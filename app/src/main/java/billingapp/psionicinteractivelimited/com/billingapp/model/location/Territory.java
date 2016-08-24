package billingapp.psionicinteractivelimited.com.billingapp.model.location;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raihan on 8/17/16.
 */
public class Territory
{
    private String[] sector;

    private String id;

    private String address;

    private String name;

    public Territory(String address, String id, String name) {
        this.address = address;
        this.id = id;
        this.name = name;
    }

    public String[] getSector ()
    {

        return sector;
    }

    public void setSector (String[] sector)
    {
        this.sector = sector;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [sector = "+sector+", id = "+id+", address = "+address+", name = "+name+"]";
    }
    public static Territory jsontoTerritory(String json) {
        try {
            JSONObject territoryJson = new JSONObject(json);
            Territory TerritoryToReturn = new Territory(territoryJson.getString("address"),territoryJson.getString("id"),territoryJson.getString("name"));
            return TerritoryToReturn;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static ArrayList<Territory> returnTerritoryFromArray(String JsonArray){
        ArrayList<Territory> territorylist = new ArrayList<Territory>();
        try {
            JSONArray territoryArray = new JSONArray(JsonArray);
            for(int i = 0;i<territoryArray.length();i++){
                Territory sector = jsontoTerritory(territoryArray.get(i).toString());
                territorylist.add(sector);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return territorylist;
    }
}