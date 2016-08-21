package billingapp.psionicinteractivelimited.com.billingapp.model.location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raihan on 8/17/16.
 */
public class Road
{
    private String id;

    private String sectors_id;

    private House[] house;

    public Road(String id, String sectors_id, String road) {
        this.id = id;
        this.sectors_id = sectors_id;
        this.road = road;
    }

    private String road;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getSectors_id ()
    {
        return sectors_id;
    }

    public void setSectors_id (String sectors_id)
    {
        this.sectors_id = sectors_id;
    }

    public House[] getHouse ()
    {
        return house;
    }

    public void setHouse (House[] house)
    {
        this.house = house;
    }

    public String getRoad ()
    {
        return road;
    }

    public void setRoad (String road)
    {
        this.road = road;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", sectors_id = "+sectors_id+", house = "+house+", road = "+road+"]";
    }
    public static Road jsontoRoad(String json) {
        try {
            JSONObject roadJson = new JSONObject(json);
            Road RoadToReturn = new Road(roadJson.getString("id"),roadJson.getString("sectors_id"),roadJson.getString("road"));
            return RoadToReturn;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<Road> returnCustomersFromArray(String JsonArray){
        ArrayList<Road> roadlist = new ArrayList<Road>();
        try {
            JSONArray roadArray = new JSONArray(JsonArray);
            for(int i = 0;i<roadArray.length();i++){
                Road road = jsontoRoad(roadArray.get(i).toString());
                roadlist.add(road);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return roadlist;

    }
}