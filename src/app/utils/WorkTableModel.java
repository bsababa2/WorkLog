package app.utils;

import app.entities.Job;
import app.screens.WorkLogScr;

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

	private WorkLogScr workLogScr;

	public WorkTableModel(WorkLogScr workLogScr)
	{
		super(new String[]{"תאריך", "לקוח", "תיאור עבודה שנעשתה", "מחיר", "הערות"});
		this.workLogScr = workLogScr;
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

	@Override
	public void fireTableDataChanged()
	{
		super.fireTableDataChanged();
		workLogScr.initTotalPrice();
	}

	@Override
	public void fireTableRowsInserted(int firstRow, int lastRow)
	{
		super.fireTableRowsInserted(firstRow, lastRow);
		workLogScr.initTotalPrice();
	}

	@Override
	public void fireTableRowsUpdated(int firstRow, int lastRow)
	{
		super.fireTableRowsUpdated(firstRow, lastRow);
		workLogScr.initTotalPrice();
	}

	@Override
	public void fireTableRowsDeleted(int firstRow, int lastRow)
	{
		super.fireTableRowsDeleted(firstRow, lastRow);
		workLogScr.initTotalPrice();
	}
}
