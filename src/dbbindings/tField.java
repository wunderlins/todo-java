package dbbindings;

public class tField {
	public String name = "";
	public String dbname = "";
	public String type = "";
	public boolean isKey = false;
	public boolean one2many = false;
	public String foreignKey = "";
	
	@Override
	public String toString() {
		return String.format("field [name=%s, dbname=%s, type=%s, isKey=%s, one2many=%s, foreignKey=%s]", name,
				dbname, type, isKey, one2many, foreignKey);
	}
}

