package app.views;

import app.screens.WorkLogScr;
import app.utils.Utils;
import app.utils.WorkTableModel;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.NumberFormat;

/**
 * Created by Barak on 01/06/2016.
 */
public class WorkTableRenderer extends DefaultTableRenderer
{
	private WorkTableModel workTableModel;
	private JTextArea textArea = new JTextArea();

	private int dateCol;
	private int priceCol;
	private int jobsCol;

	public WorkTableRenderer(WorkTableModel workTableModel)
	{
		this.workTableModel = workTableModel;
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		this.dateCol = workTableModel.findColumn(WorkTableModel.DATE_COL);
		this.priceCol = workTableModel.findColumn(WorkTableModel.PRICE_COL);
		this.jobsCol = workTableModel.findColumn(WorkTableModel.JOBS_DESCR_COL);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		int realRow = Utils.getRealRow(row, table);

		if (column == dateCol)
		{
			value = DateFormat.getDateInstance().format(value);
		} else if (column == priceCol)
		{
			value = NumberFormat.getCurrencyInstance().format(value);
		}

		Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		component.setFont(WorkLogScr.DEFAULT_TEXT_FONT);

		if (column == jobsCol)
		{
			textArea.setText(value.toString());
			textArea.setBackground(component.getBackground());
			textArea.setForeground(component.getForeground());

			component = textArea;
		}

		if (workTableModel.getCurrentEntityList().get(realRow).isNewRecord())
		{
			component.setBackground(isSelected ? WorkLogScr.NEW_RECORD_SELECTED_COLOR : WorkLogScr.NEW_RECORD_COLOR);
		} else if (workTableModel.getCurrentEntityList().get(realRow).isUpdated())
		{
			component.setBackground(isSelected ? WorkLogScr.UPDATED_RECORD_SELECTED_COLOR : WorkLogScr.UPDATED_RECORD_COLOR);
		}

		return component;
	}
}
