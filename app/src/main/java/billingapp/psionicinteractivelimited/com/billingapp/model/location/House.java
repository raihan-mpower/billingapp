package billingapp.psionicinteractivelimited.com.billingapp.model.location;

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
}
