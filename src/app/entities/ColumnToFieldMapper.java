package app.entities;

/**
 * Created by Barak on 11/01/14.
 */
public interface ColumnToFieldMapper
{
	int getId();

	Object getFieldValueByColumnIndex(String columnName);
}
