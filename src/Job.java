import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Barak
 * Date: 02/10/13
 * Time: 20:29
 * To change this template use File | Settings | File Templates.
 */
public class Job
{
    private int id;
    private Date jobDate;
    private Customer customer;
    private String jobDescription;
    private double price;
    private String remarks;

    private boolean isNewRecord = false;

    public Job(Date jobDate, Customer customer, String jobDescription, double price, String remarks)
    {
        this.jobDate = jobDate;
        this.customer = customer;
        this.jobDescription = jobDescription;
        this.price = price;
        this.remarks = remarks;
        isNewRecord = true;
    }

    public Job(int id, Date jobDate, Customer customer, String jobDescription, double price, String remarks)
    {
        this.id = id;
        this.jobDate = jobDate;
        this.customer = customer;
        this.jobDescription = jobDescription;
        this.price = price;
        this.remarks = remarks;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Date getJobDate()
    {
        return jobDate;
    }

    public void setJobDate(Date jobDate)
    {
        this.jobDate = jobDate;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public String getJobDescription()
    {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription)
    {
        this.jobDescription = jobDescription;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public boolean isNewRecord()
    {
        return isNewRecord;
    }

    public void setNewRecord(boolean newRecord)
    {
        isNewRecord = newRecord;
    }
}
