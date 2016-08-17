package billingapp.psionicinteractivelimited.com.billingapp.model.location;

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
}
