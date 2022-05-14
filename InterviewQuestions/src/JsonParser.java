// https://mkyong.com/java/jackson-how-to-parse-json/

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {

	public static void main(String[] args) {

		ObjectMapper mapper = new ObjectMapper();

		Staff staff = createStaff();

		try {
			// JSON file to Java object
			Staff staff = mapper.readValue(new File("c:\\test\\staff.json"), Staff.class);

			// JSON string to Java object
			String jsonInString = "{\"name\":\"mkyong\",\"age\":37,\"skills\":[\"java\",\"python\"]}";
			Staff staff2 = mapper.readValue(jsonInString, Staff.class);

			// compact print
			System.out.println(staff2);

			// pretty print
			String prettyStaff1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(staff2);

			System.out.println(prettyStaff1);

			// Java objects to JSON file
			mapper.writeValue(new File("c:\\test\\staff.json"), staff);

			// Java objects to JSON string - compact-print
			String jsonString = mapper.writeValueAsString(staff);

			System.out.println(jsonString);

			// Java objects to JSON string - pretty-print
			String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(staff);

			System.out.println(jsonInString2);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static Staff createStaff() {

		Staff staff = new Staff();

		staff.setName("mkyong");
		staff.setAge(38);
		staff.setPosition(new String[] { "Founder", "CTO", "Writer" });
		Map<String, BigDecimal> salary = new HashMap() {
			{
				put("2010", new BigDecimal(10000));
				put("2012", new BigDecimal(12000));
				put("2018", new BigDecimal(14000));
			}
		};
		staff.setSalary(salary);
		staff.setSkills(Arrays.asList("java", "python", "node", "kotlin"));

		return staff;

	}

	public class Staff {

		private String name;
		private int age;
		private String[] position; // Array
		private List<String> skills; // List
		private Map<String, BigDecimal> salary; // Map

		// getters , setters, some boring stuff
	}

}
