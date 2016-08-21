package billingapp.psionicinteractivelimited.com.billingapp.model.location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raihan on 8/17/16.
 */
public class Sector
{
    private String sector;

    private String id;

    private String[] road;

    public Sector(String sector, String id, String territory_id) {
        this.sector = sector;
        this.id = id;
        this.territory_id = territory_id;
    }

    private String territory_id;

    public String getSector ()
    {
        return sector;
    }

    public void setSector (String sector)
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

    public String[] getRoad ()
    {
        return road;
    }

    public void setRoad (String[] road)
    {
        this.road = road;
    }

    public String getTerritory_id ()
    {
        return territory_id;
    }

    public void setTerritory_id (String territory_id)
    {
        this.territory_id = territory_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [sector = "+sector+", id = "+id+", road = "+road+", territory_id = "+territory_id+"]";
    }
    public static Sector jsontoSector(String json) {
        try {
            JSONObject sectorJson = new JSONObject(json);
            Sector SectorToReturn = new Sector(sectorJson.getString("sector"),sectorJson.getString("id"),sectorJson.getString("territory_id"));
            return SectorToReturn;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<Sector> returnSectorFromArray(String JsonArray){
        ArrayList<Sector> sectorlist = new ArrayList<Sector>();
        try {
            JSONArray sectorArray = new JSONArray(JsonArray);
            for(int i = 0;i<sectorArray.length();i++){
                Sector sector = jsontoSector(sectorArray.get(i).toString());
                sectorlist.add(sector);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sectorlist;

    }
}
