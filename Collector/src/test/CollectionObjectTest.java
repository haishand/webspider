package test;

import static org.junit.Assert.*;
import objs.CollectionObject;

import org.junit.Test;

public class CollectionObjectTest {

	@Test
	public void testCollectionObject() {
		String urls = "http://127.0.0.1/test2.html|http://127.0.0.1/test1.html";
		String filters = "用户名称|类型|测试";
		String tables = "test2|test1";
		
		CollectionObject co = new CollectionObject("type1", urls, filters, tables, 10);
		assertEquals(2, co.getUrls().size());
		assertEquals(3, co.getFilters().size());
		assertEquals(co.getUrls().get(0), "http://127.0.0.1/test2.html");
		assertEquals(co.getUrls().get(1), "http://127.0.0.1/test1.html");
		assertEquals(co.getFilters().get(0), "用户名称");
		assertEquals(co.getFilters().get(1), "类型");
		assertEquals(co.getFilters().get(2), "测试");
		assertEquals(co.getTableNames().get(0), "test2");
		assertEquals(co.getTableNames().get(1), "test1");
	}

}
