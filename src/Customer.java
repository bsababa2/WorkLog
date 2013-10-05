/**
 * Created with IntelliJ IDEA.
 * User: Barak
 * Date: 28/09/13
 * Time: 23:53
 * To change this template use File | Settings | File Templates.
 */
public class Customer
{
    public static Customer ALL_VALUES = new Customer("-הכל-");
    private int id;
    private String name;

    public Customer(String name)
    {
        this.name = name;
    }

    public Customer(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (id != customer.id) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return id;
    }
}
