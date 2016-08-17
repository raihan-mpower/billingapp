package billingapp.psionicinteractivelimited.com.billingapp.model.location;

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
}