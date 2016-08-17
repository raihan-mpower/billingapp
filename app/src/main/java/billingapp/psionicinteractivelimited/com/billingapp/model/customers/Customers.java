package billingapp.psionicinteractivelimited.com.billingapp.model.customers;

/**
 * Created by raihan on 8/17/16.
 */
public class Customers
{
    private String houses_id;

    private String phone;

    private String price;

    private String customer_code;

    private String address;

    private String customers_id;

    private String name;

    private String last_paid;

    public String getHouses_id ()
    {
        return houses_id;
    }

    public void setHouses_id (String houses_id)
    {
        this.houses_id = houses_id;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getPrice ()
    {
        return price;
    }

    public void setPrice (String price)
    {
        this.price = price;
    }

    public String getCustomer_code ()
    {
        return customer_code;
    }

    public void setCustomer_code (String customer_code)
    {
        this.customer_code = customer_code;
    }

    public Customers(String last_paid, String name, String customers_id, String address, String customer_code, String price, String phone, String houses_id) {
        this.last_paid = last_paid;
        this.name = name;
        this.customers_id = customers_id;
        this.address = address;
        this.customer_code = customer_code;
        this.price = price;
        this.phone = phone;
        this.houses_id = houses_id;
    }

    public String getAddress ()

    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getCustomers_id ()
    {
        return customers_id;
    }

    public void setCustomers_id (String customers_id)
    {
        this.customers_id = customers_id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getLast_paid ()
    {
        return last_paid;
    }

    public void setLast_paid (String last_paid)
    {
        this.last_paid = last_paid;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [houses_id = "+houses_id+", phone = "+phone+", price = "+price+", customer_code = "+customer_code+", address = "+address+", customers_id = "+customers_id+", name = "+name+", last_paid = "+last_paid+"]";
    }
}