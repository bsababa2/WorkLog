package app.utils;

import app.entities.ColumnToFieldMapper;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Barak on 11/01/14.
 */
public abstract class EntityTableModel<T extends ColumnToFieldMapper> extends AbstractTableModel
{
	private String[] columnNames;

	private List<T> currentEntityList = new ArrayList<T>();
	private List<T> initialEntityList = new ArrayList<T>();

	public EntityTableModel(String[] columnNames)
	{
		super();
		this.columnNames = columnNames;
	}

	public List<T> getInitialEntityList()
	{
		return initialEntityList;
	}

	public void setInitialEntityList(List<T> initialEntityList)
	{
		this.initialEntityList = initialEntityList;
		this.currentEntityList = new ArrayList<T>(initialEntityList);
		this.fireTableDataChanged();
	}

	public List<T> getCurrentEntityList()
	{
		return currentEntityList;
	}

	public void setCurrentEntityList(List<T> currentEntityList)
	{
		this.currentEntityList = currentEntityList;
		this.fireTableDataChanged();
	}

	@Override
	public String getColumnName(int column)
	{
		return columnNames[column];
	}

	@Override
	public int getRowCount()
	{
		return currentEntityList.size();
	}

	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		T entity = currentEntityList.get(rowIndex);
		return entity.getFieldValueByColumnIndex(columnIndex);
	}
}