package data;

import java.util.ArrayList;
import java.util.List;

public class Cli {
	private static List<Person> testData() {
		List<Person> list = new ArrayList<Person>();
		localPerson lp1 = new localPerson("Simon", "Wunderlin", "swunderlin@gmail.com");
		//lp1.id = 1;
		list.add(lp1);
		localPerson lp2 = new localPerson("Samuel", "Pulfer", "samuel.pulfer@gmail.com");
		//lp2.id = 2;
		list.add(lp2);
		
		return list;
	}

	public static void main(String[] args) {
		List<Person> p = testData();
		Item i1 = new todoItem();
		i1.name = "Something";
		//i1.addResponsible(p.get(0).id);
		
		System.out.println(i1);
	}

}
