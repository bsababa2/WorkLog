package app.entities;

/**
 * Created by Barak on 11/01/14.
 */
public class Inventory implements ColumnToFieldMapper
{
	private int id;
	private Profile profile;
	private int quantity;
	private String remarks;

	public Inventory(int id, Profile profile, int quantity, String remarks)
	{
		this.id = id;
		this.profile = profile;
		this.quantity = quantity;
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

	public Profile getProfile()
	{
		return profile;
	}

	public void setProfile(Profile profile)
	{
		this.profile = profile;
	}

	public int getQuantity()
	{
		return quantity;
	}

	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}

	public String getRemarks()
	{
		return remarks;
	}

	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
	}

	@Override
	public Object getFieldValueByColumnIndex(String columnName)
	{
//		switch (column)
//		{
//			case InventoryTableModel.PROFILE_TYPE_COL: return this.getProfile().getType();
//			case InventoryTableModel.PROFILE_COL: return this.getProfile();
//			case InventoryTableModel.QUANTITY_COL: return this.getQuantity();
//			case InventoryTableModel.REMARKS_COL: return this.getRemarks();
//			default: return null;
//		}

		return null;
	}
}
