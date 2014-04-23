package app.entities;

import app.utils.WorkTableModel;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Barak
 * Date: 02/10/13
 * Time: 20:29
 * To change this template use File | Settings | File Templates.
 */
public class Job implements ColumnToFieldMapper
{
	private int id;
	private Date jobDate;
	private Customer customer;
	private String jobDescription;
	private double price;
	private String remarks;

	private boolean isNewRecord = false;
	private boolean isUpdated = false;

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

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Job job = (Job) o;

		if (id != job.id) return false;
		if (Double.compare(job.price, price) != 0) return false;
		if (customer != null ? !customer.equals(job.customer) : job.customer != null) return false;
		if (jobDate != null ? !jobDate.equals(job.jobDate) : job.jobDate != null) return false;
		if (jobDescription != null ? !jobDescription.equals(job.jobDescription) : job.jobDescription != null)
			return false;
		if (remarks != null ? !remarks.equals(job.remarks) : job.remarks != null) return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int result;
		long temp;
		result = id;
		result = 31 * result + (jobDate != null ? jobDate.hashCode() : 0);
		result = 31 * result + (customer != null ? customer.hashCode() : 0);
		result = 31 * result + (jobDescription != null ? jobDescription.hashCode() : 0);
		temp = Double.doubleToLongBits(price);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + (remarks != null ? remarks.hashCode() : 0);
		return result;
	}

	public boolean isUpdated()
	{
		return isUpdated;
	}

	public void setUpdated(boolean updated)
	{
		isUpdated = updated;
	}

	@Override
	public Object getFieldValueByColumnIndex(String columnName)
	{
		if (columnName.equals(WorkTableModel.DATE_COL)) return this.getJobDate();
		if (columnName.equals(WorkTableModel.CUSTOMER_COL)) return this.getCustomer();
		if (columnName.equals(WorkTableModel.JOBS_DESCR_COL)) return this.getJobDescription();
		if (columnName.equals(WorkTableModel.PRICE_COL)) return this.getPrice();
		if (columnName.equals(WorkTableModel.REMARKS_COL)) return this.getRemarks();
		return null;
	}
}
