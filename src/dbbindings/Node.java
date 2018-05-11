package dbbindings;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DBTable
public class Node {
	
	@DBField(PrimaryKey=true, Name="pid")
	Integer id = -1;
	
	@DBField
	String name = null;
	
	@DBField(ForeignKey="Node", One2Many=true)
	List<Node> children = new ArrayList<>();
	
	
	public Node() {
		
	}
	
	public static void main(String[] args) {
		
		String tableName = "";
		Map<String, tField> fields = new HashMap<>();
		Class<Node> obj = Node.class;
		
		if (obj.isAnnotationPresent(DBTable.class)) {

			Annotation annotation = obj.getAnnotation(DBTable.class);
			DBTable table = (DBTable) annotation;
			
			Method[] cl = obj.getDeclaredMethods();
			System.out.println(cl.length);
			
			if (!table.name().isEmpty())
				tableName = table.name();
			else
				tableName = obj.getConstructors().toString();
			
			System.out.printf("Name: %s%n", tableName);
		}
		
		for(Field field : obj.getDeclaredFields()){
			Class<?> ctype = field.getType();
			String type = ctype.getSimpleName();
			Annotation[] annotations = field.getDeclaredAnnotations();
			String name = field.getName();
			String dbname = field.getName();
			//name = name.substring(name.lastIndexOf('.') + 1);
			Annotation a = annotations[0];
			
			//System.out.printf("%n%s: %s", name, annotations);
			//for (Annotation a : annotations) {
				//if(a instanceof DBField){
					DBField mya = (DBField) a;
					if (!mya.Name().isEmpty()) {
						dbname = new String(mya.Name());
					}
				    //System.out.printf(" %s -> %s [%s], ", name, mya.toString(), type);
				    tField f = new tField();
				    f.name = name;
				    f.dbname = dbname;
				    f.type = type;
				    f.isKey = ((DBField) a).PrimaryKey();
				    f.one2many = ((DBField) a).One2Many();
				    f.foreignKey = ((DBField) a).ForeignKey();
				    System.out.printf("   -> %s%n", f);
				    fields.put(name, f);
				//}
			//}
			//System.out.printf("%n");
		}
		
		System.out.println();
		System.out.print(fields);
		System.out.println();
		
		generateFieldList(fields);
	}
	
	static void generateFieldList(Map<String, tField> fields) {
		String buffer = "";
		int i = 0;
		
		for (Map.Entry<String, tField> entry : fields.entrySet()) {
			i++;
		    String key   = entry.getKey();
		    tField value = entry.getValue();
		    
		    String type = "";
		    switch (value.type) {
		    	case("Integer"):
		    		type = "INT";
		    		break;
		    
		    	case("Double"):
		    		type = "INT";
		    		break;
				    
		    	case("String"):
		    		type = "VARCHAR";
		    		break;
		    		
		    	case("List"):
		    		continue;
		    }
		    
		    buffer += value.dbname + " ";
		    buffer += type + " ";
		    if (value.isKey)
		    	buffer += "PRIMARY KEY ";
		    if (i != fields.size())
		    	buffer += ",\n";
		}
		
		System.out.println(buffer);
	}

	@Override
	public String toString() {
		return String.format("Node [id=%s, name=%s, children=%s]", id, name, children);
	}
}
