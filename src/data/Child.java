package data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "itemChild")
public class Child {
	
	@DatabaseField(generatedId = true)
	Integer id;

	@DatabaseField
	todoItem parent;

	@DatabaseField
	todoItem child;

	public Child() {}

}
