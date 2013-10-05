import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Barak
 * Date: 28/09/13
 * Time: 23:50
 * To change this template use File | Settings | File Templates.
 */
public class DBManager
{
    private static final String CONNECTION_URL = "jdbc:h2:~/h2/test";
    private static final String CONNECTION_USER = "barak";
    private static final String CONNECTION_PASS = "123";
    private static DBManager dbManager = null;

    private DBManager()
    {
        try
        {
            Class.forName("org.h2.Driver");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static DBManager getSingleton()
    {
        if (dbManager == null)
        {
            dbManager = new DBManager();
        }

        return dbManager;
    }

    private Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(CONNECTION_URL, CONNECTION_USER, CONNECTION_PASS);
    }

    private void disconnect(PreparedStatement ps, ResultSet rs) throws SQLException
    {
        if (ps != null) ps.close();
        if (rs != null) rs.close();
    }

    public List<Customer> getCustomers() throws Exception
    {
        List<Customer> customers = new ArrayList<Customer>();
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            conn = getConnection();
            ps = conn.prepareStatement("SELECT ID, NAME FROM CUSTOMERS ORDER BY NAME");
            rs = ps.executeQuery();

            while (rs.next())
            {
                 customers.add(new Customer(rs.getInt("ID"), rs.getString("NAME")));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            disconnect(ps, rs);
        }

        return customers;
    }

    public List<Job> getJobs() throws Exception
    {
        List<Job> jobs = new ArrayList<Job>();
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            conn = getConnection();
            ps = conn.prepareStatement("SELECT j.ID, j.JOB_DATE, j.CUSTOMER_ID, c.NAME, j.JOB_DESCR, j.PRICE, j.REMARKS" +
                    "  FROM JOBS j, CUSTOMERS c where j.CUSTOMER_ID = c.ID ");
            rs = ps.executeQuery();

            while (rs.next())
            {
                jobs.add(new Job(rs.getInt("ID"), rs.getDate("JOB_DATE"),
                        new Customer(rs.getInt("CUSTOMER_ID"), rs.getString("NAME")),
                        rs.getString("JOB_DESCR"), rs.getDouble("PRICE"), rs.getString("REMARKS")));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            disconnect(ps, rs);
        }

        return jobs;
    }

    public int addJob(Job job) throws Exception
    {
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int id = -1;

        try
        {
            conn = getConnection();
            ps = conn.prepareStatement("SELECT (MAX(ID) + 1) AS NEW_ID FROM JOBS ");
            rs = ps.executeQuery();
            if (rs.next())
            {
                id = rs.getInt("NEW_ID");
                ps = conn.prepareStatement("INSERT INTO JOBS VALUES(?, ?, ?, ?, ?, ?)");
                ps.setInt(1, id);
                ps.setDate(2, new java.sql.Date(job.getJobDate().getTime()));
                ps.setInt(3, job.getCustomer().getId());
                ps.setString(4, job.getJobDescription());
                ps.setDouble(5, job.getPrice());
                ps.setString(6, job.getRemarks());
                ps.executeUpdate();
            }
            else
            {
                throw new SQLException("בעיה נוצרה בעת יצירת ID עבור העבודה החדשה!");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            disconnect(ps, rs);
        }

        return id;
    }

    public void removeJob(int jobId) throws Exception
    {
        Connection conn;
        PreparedStatement ps = null;

        try
        {
            conn = getConnection();
            ps = conn.prepareStatement("DELETE FROM JOBS WHERE ID = ? ");
            ps.setInt(1, jobId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            disconnect(ps, null);
        }
    }

    public void addCustomer(Customer customer) throws Exception
    {
        Connection conn;
        PreparedStatement ps = null;

        try
        {
            conn = getConnection();
            ps = conn.prepareStatement("INSERT INTO CUSTOMERS VALUES((SELECT MAX(ID) + 1 FROM CUSTOMERS), ?)");
            ps.setString(1, customer.getName());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            disconnect(ps, null);
        }
    }

    public void updateCustomer(Customer customer) throws Exception
    {
        Connection conn;
        PreparedStatement ps = null;

        try
        {
            conn = getConnection();
            ps = conn.prepareStatement("UPDATE CUSTOMERS SET NAME = ? WHERE ID = ?");
            ps.setString(1, customer.getName());
            ps.setInt(2, customer.getId());
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            disconnect(ps, null);
        }
    }

    public List<Job> getJobsByDates(Date fromDate, Date toDate) throws Exception
    {
        List<Job> jobs = new ArrayList<Job>();
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            conn = getConnection();
            ps = conn.prepareStatement("SELECT j.ID, j.JOB_DATE, j.CUSTOMER_ID, c.NAME, j.JOB_DESCR, j.PRICE, j.REMARKS" +
                    "  FROM JOBS j, CUSTOMERS c where j.CUSTOMER_ID = c.ID AND j.JOB_DATE >= ? AND j.JOB_DATE <= ? ORDER BY JOB_DATE");
            ps.setDate(1, new java.sql.Date(fromDate.getTime()));
            ps.setDate(2, new java.sql.Date(toDate.getTime()));
            rs = ps.executeQuery();

            while (rs.next())
            {
                jobs.add(new Job(rs.getInt("ID"), rs.getDate("JOB_DATE"),
                        new Customer(rs.getInt("CUSTOMER_ID"), rs.getString("NAME")),
                        rs.getString("JOB_DESCR"), rs.getDouble("PRICE"), rs.getString("REMARKS")));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            disconnect(ps, rs);
        }

        return jobs;
    }
}
