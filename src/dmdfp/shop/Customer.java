package dmdfp.shop;

/**
 * Created by khk on 3/7/14.
 */
public class Customer
{
    private int id;
    private String name;

    public Customer(int id, String name)
    {
        setId(id);
        setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
