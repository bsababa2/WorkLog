package app.utils;

import app.entities.Inventory;

/**
 * Created by Barak on 22/02/14.
 */
public class InventoryTableModel extends EntityTableModel<Inventory>
{
	public static final int PROFILE_TYPE_COL = 0;
	public static final int PROFILE_COL = 1;
	public static final int QUANTITY_COL = 2;
	public static final int REMARKS_COL = 3;

	public InventoryTableModel()
	{
		super(new String[]{"סוג", "יצרן", "כמות", "הערות"});
	}

	@Override
	public EntityTableModel<Inventory> clone() throws CloneNotSupportedException
	{
		return null;
	}
}
