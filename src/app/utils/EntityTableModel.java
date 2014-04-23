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
		return entity.getFieldValueByColumnIndex(getColumnName(columnIndex));
	}

	public void updateRow(int row, T updatedEntity)
	{
		this.getCurrentEntityList().set(row, updatedEntity);

		for (int i = 0; i < this.getInitialEntityList().size(); i++)
		{
			if (this.getInitialEntityList().get(i).getId() == updatedEntity.getId())
			{
				this.getInitialEntityList().set(i, updatedEntity);
				this.fireTableRowsUpdated(row, row);

				return;
			}
		}
	}

	public void addRow(T entity)
	{
		this.getCurrentEntityList().add(0, entity);
		this.getInitialEntityList().add(0, entity);
		this.fireTableRowsInserted(0, 0);
	}

	public void removeRows(List<T> entities)
	{
		this.getInitialEntityList().removeAll(entities);
		this.getCurrentEntityList().removeAll(entities);

		this.fireTableDataChanged();
	}

	public void reverseColumns()
	{
		String[] reversedColumnNames = new String[columnNames.length];
		for (int i = 0; i < columnNames.length; i++)
		{
			reversedColumnNames[i] = columnNames[columnNames.length - 1 - i];
		}

		columnNames = reversedColumnNames;
	}

	public abstract EntityTableModel<T> clone() throws CloneNotSupportedException;
}