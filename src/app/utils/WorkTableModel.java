package app.utils;

import app.entities.Job;

import javax.swing.*;
import javax.swing.event.TableModelListener;

/**
 * Created by Barak on 11/01/14.
 */
public class WorkTableModel extends EntityTableModel<Job>
{
	public static final int DATE_COL = 0;
	public static final int CUSTOMER_COL = 1;
	public static final int JOBS_DESCR_COL = 2;
	public static final int PRICE_COL = 3;
	public static final int REMARKS_COL = 4;

	public WorkTableModel()
	{
		super(new String[]{"תאריך", "לקוח", "תיאור עבודה שנעשתה", "מחיר", "הערות"});
	}

	public void setRowHeightWrapper(JTable table)
	{
		TableModelListener[] savedListeners = this.getTableModelListeners();
		for (TableModelListener listener : savedListeners)
		{
			this.removeTableModelListener(listener);
		}

		this.addTableModelListener(new RowWrapTableModelListener(JOBS_DESCR_COL, table));

		for (TableModelListener listener : savedListeners)
		{
			this.addTableModelListener(listener);
		}
	}
}
