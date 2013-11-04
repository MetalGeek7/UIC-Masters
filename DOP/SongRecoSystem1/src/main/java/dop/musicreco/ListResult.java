package dop.musicreco;

import java.util.List;
import java.util.ArrayList;

public class ListResult {
	private static List list;

	public static void ListResult() {
		list = new ArrayList();
	}

	public static List getList() {
		return list;
	}

	public static void clear() {
		list.clear();
	}

	public static void addToList(Object o) {
		list.add(o);

	}

	public static void setList(List<Object> l) {
		list = l;
	}

	public static Object get(int index) {
		return list.get(index);

	}
}
