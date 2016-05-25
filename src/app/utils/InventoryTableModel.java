package app.utils;

import app.entities.Inventory;

/**
 * Created by Barak on 22/02/14.
 */
public class InventoryTableModel extends EntityTableModel<Inventory>
{
	public static final String PROFILE_TYPE_COL = "סוג";
	public static final String PROFILE_COL = "יצרן";
	public static final String QUANTITY_COL = "כמות";
	public static final String REMARKS_COL = "הערות";

	public InventoryTableModel()
	{
		super(new String[]{PROFILE_TYPE_COL, PROFILE_COL, QUANTITY_COL, REMARKS_COL});
	}

	@Override
	public EntityTableModel<Inventory> clone() throws CloneNotSupportedException
	{
		return null;
	}
}
