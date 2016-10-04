package app.utils;

import app.entities.Job;
import app.screens.WorkLogScr;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import java.util.ArrayList;

/**
 * Created by Barak on 11/01/14.
 */
public class WorkTableModel extends EntityTableModel<Job>
{
	public static final String DATE_COL = "תאריך";
	public static final String CUSTOMER_COL = "לקוח";
	public static final String JOBS_DESCR_COL = "תיאור עבודה שנעשתה";
	public static final String PRICE_COL = "מחיר";
	public static final String REMARKS_COL = "הערות";

	private WorkLogScr workLogScr;

	public WorkTableModel(WorkLogScr workLogScr)
	{
		super(new String[]{DATE_COL, CUSTOMER_COL, JOBS_DESCR_COL, PRICE_COL, REMARKS_COL});
		this.workLogScr = workLogScr;
	}

	public void setRowHeightWrapper(JTable table)
	{
		TableModelListener[] savedListeners = this.getTableModelListeners();
		for (TableModelListener listener : savedListeners)
		{
			this.removeTableModelListener(listener);
		}

		this.addTableModelListener(new RowWrapTableModelListener(findColumn(JOBS_DESCR_COL), table));

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

	@Override
	public EntityTableModel<Job> clone() throws CloneNotSupportedException
	{
		WorkTableModel workTableModel = new WorkTableModel(workLogScr);
		workTableModel.setInitialEntityList(new ArrayList<>(getInitialEntityList()));
		workTableModel.setCurrentEntityList(new ArrayList<>(getCurrentEntityList()));
		return workTableModel;
	}
}
