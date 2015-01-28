package app.db;

import app.entities.Customer;
import app.entities.Job;
import org.h2.jdbc.JdbcSQLException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Barak
 * Date: 28/09/13
 * Time: 23:50
 * To change this template use File | Settings | File Templates.
 */
public class DBManager
{
	private static final String CONNECTION_USER = "barak";
	private static final String CONNECTION_PASS = "123";
	private static final String DB_LOC_FILE = "db.txt";
	private String connectionUrl = "jdbc:h2:";
	private static DBManager dbManager = null;

	private DBManager() throws Exception
	{
		Class.forName("org.h2.Driver");
		BufferedReader reader = new BufferedReader(new FileReader(DB_LOC_FILE));
		connectionUrl = connectionUrl + reader.readLine();
		reader.close();
	}

	public static DBManager getSingleton() throws Exception
	{
		if (dbManager == null)
		{
			dbManager = new DBManager();
		}

		return dbManager;
	}

	private Connection getConnection() throws SQLException
	{
		try
		{
			return DriverManager.getConnection(connectionUrl, CONNECTION_USER, CONNECTION_PASS);
		}
		catch (JdbcSQLException e)
		{
			e.printStackTrace();
			if (e.getMessage().contains("Database may be already in use"))
			{
				e.setSQL(e.getSQL() + "\nייתכן ואתה נועל את מסד נתונים כיוון שהאפליקציה כבר רצה!");
			}
			throw e;
		}
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
			ps = conn.prepareStatement("SELECT ID, NAME FROM CUSTOMERS WHERE IS_VALID = 'Y' ORDER BY NAME");
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

	public Customer getCustomerByName(String customerName) throws SQLException
	{
		Connection conn;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try
		{
			conn = getConnection();
			ps = conn.prepareStatement("SELECT ID, NAME FROM CUSTOMERS WHERE NAME = TRIM(?) ");
			ps.setString(1, customerName.trim());
			rs = ps.executeQuery();
			if (rs.next())
			{
				return new Customer(rs.getInt("ID"), rs.getString("NAME"));
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

		return null;
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

	public void updateJob(Job job) throws Exception
	{
		Connection conn;
		PreparedStatement ps = null;

		try
		{
			conn = getConnection();
			ps = conn.prepareStatement("UPDATE JOBS SET JOB_DATE = ?, CUSTOMER_ID = ?, JOB_DESCR = ?, PRICE = ?, REMARKS = ? WHERE ID = ?");
			ps.setDate(1, new java.sql.Date(job.getJobDate().getTime()));
			ps.setInt(2, job.getCustomer().getId());
			ps.setString(3, job.getJobDescription());
			ps.setDouble(4, job.getPrice());
			ps.setString(5, job.getRemarks());
			ps.setInt(6, job.getId());
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

	public int addCustomer(Customer customer) throws Exception
	{
		Connection conn;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int id = -1;

		try
		{
			conn = getConnection();
			ps = conn.prepareStatement("SELECT (MAX(ID) + 1) as NEW_ID FROM CUSTOMERS");
			rs = ps.executeQuery();
			if (rs.next())
			{
				id = rs.getInt("NEW_ID");
				ps = conn.prepareStatement("INSERT INTO CUSTOMERS VALUES(?, ?, 'Y')");
				ps.setInt(1, id);
				ps.setString(2, customer.getName());
				ps.executeUpdate();
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
				" FROM JOBS j, CUSTOMERS c where j.CUSTOMER_ID = c.ID AND c.IS_VALID = 'Y' AND j.JOB_DATE >= ?" +
				" AND j.JOB_DATE <= ? ORDER BY JOB_DATE DESC");
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

	public void invalidateCustomer(Customer customer) throws Exception
	{
		Connection conn;
		PreparedStatement ps = null;

		try
		{
			conn = getConnection();
			ps = conn.prepareStatement("UPDATE CUSTOMERS SET IS_VALID = 'N' WHERE ID = ?");
			ps.setInt(1, customer.getId());
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
}
