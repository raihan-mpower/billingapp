package billingapp.psionicinteractivelimited.com.billingapp.model.location;

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
}