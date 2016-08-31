package billingapp.psionicinteractivelimited.com.billingapp.model.location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raihan on 8/17/16.
 */
public class House
{
    private String id;

    private String roads_id;

    private String house;

    public House(String house, String id, String roads_id) {
        this.house = house;
        this.id = id;
        this.roads_id = roads_id;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getRoads_id ()
    {
        return roads_id;
    }

    public void setRoads_id (String roads_id)
    {
        this.roads_id = roads_id;
    }

    public String getHouse ()
    {
        return house;
    }

    public void setHouse (String house)
    {
        this.house = house;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", roads_id = "+roads_id+", house = "+house+"]";
    }
    public static House jsontoHouse(String json) {
        try {
            JSONObject customerJson = new JSONObject(json);
            House HouseToReturn = new House(customerJson.getString("house"),customerJson.getString("id"),customerJson.getString("roads_id"));
            return HouseToReturn;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<House> returnCustomersFromArray(String JsonArray){
        ArrayList<House> houselist = new ArrayList<House>();
        try {
            JSONArray houseArray = new JSONArray(JsonArray);
            for(int i = 0;i<houseArray.length();i++){
                House house = jsontoHouse(houseArray.get(i).toString());
                houselist.add(house);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return houselist;

    }
}
