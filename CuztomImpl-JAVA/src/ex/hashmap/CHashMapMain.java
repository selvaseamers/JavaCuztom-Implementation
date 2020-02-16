package ex.hashmap;

public class CHashMapMain {

	public static void main(String[] args) {

		CMap<String, String> map = new CHashMap<String, String>(20, 20);

		map.put("bala", "spiderman");
		map.put("venkat", "superman");
		map.put("sangeetha", "wonderwomen");
		map.put("pankaj", "hawckeye");
		map.put(null, "ironman");

		System.out.println(map);

		map.put("pankaj", "Flash");

		System.out.println(map);

		map.put(null, "BlackWidow");

		System.out.println(map);

		map.remove("venkat");

		System.out.println(map);

		map.remove(null);

		System.out.println(map);

	}

}
