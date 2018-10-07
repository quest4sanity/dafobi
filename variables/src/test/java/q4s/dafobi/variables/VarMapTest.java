package q4s.dafobi.variables;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import q4s.dafobi.jaxb.common.DataType;
import q4s.dafobi.variables.Var;
import q4s.dafobi.variables.VarMap;

public class VarMapTest {

	VarMap map;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		map = new VarMap();
		map.put("n0", new Var(null));
		map.put("n1", new Var(new BigDecimal("123.3"), DataType.DECIMAL));
		map.put("n2", new Var(new BigDecimal("312.2"), DataType.DECIMAL));
		map.put("n3", new Var(new BigDecimal("321.1"), DataType.DECIMAL));
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
//	public void testVarMapMapOfQextendsStringQextendsAbstractVar() {
//		fail("Not yet implemented");
//	}

	/**
	 * Проверка получения значений с учетом регистронечувствительности ключей.
	 */
	@Test
	public void testGetObject() {
		assertNull(map.get("N0").get());
		assertNull(map.get("n0").get());

		assertEquals(new BigDecimal("123.3"), map.get("N1").get());
		assertEquals(new BigDecimal("123.3"), map.get("n1").get());
	}

	/**
	 * Установка нового значения как и получение существующего должна быть
	 * регистронезависимой.
	 */
	@Test
	public void testPutStringAbstractVar() {
		BigDecimal result = new BigDecimal("100.12");
		map.put("n10", new Var(result, DataType.DECIMAL));

		assertEquals(result, map.get("N10").get());
		assertEquals(result, map.get("n10").get());

		result = new BigDecimal("100.13");
		map.put("N10", new Var(result, DataType.DECIMAL));

		assertEquals(result, map.get("N10").get());
		assertEquals(result, map.get("n10").get());
	}

	@Test
	public void testContainsKeyObject() {
		assertTrue(map.containsKey("n1"));
		assertTrue(map.containsKey("N1"));

		assertFalse(map.containsKey("n10"));
		assertFalse(map.containsKey("N10"));
	}

	@Test
	public void testRemoveObject() {
		assertTrue(map.containsKey("n1"));
		assertTrue(map.containsKey("N1"));
		assertTrue(map.containsKey("n2"));
		assertTrue(map.containsKey("N2"));

		map.remove("n1");
		map.remove("N2");

		assertFalse(map.containsKey("n1"));
		assertFalse(map.containsKey("N1"));
		assertFalse(map.containsKey("n2"));
		assertFalse(map.containsKey("N2"));
	}

//	@Test
//	public void testPutAllMapOfQextendsStringQextendsAbstractVar() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testGetAllValues() {
		Map<String, Object> values = map.getAllValues();

		assertArrayEquals(new String[] { "N0", "N1", "N2", "N3" }, values.keySet().toArray(new String[0]));

		assertEquals(null, values.get("N0"));
		assertEquals(new BigDecimal("123.3"), values.get("N1"));
		assertEquals(new BigDecimal("312.2"), values.get("N2"));
		assertEquals(new BigDecimal("321.1"), values.get("N3"));
	}

//	@Test
//	public void testAllDebugValues() {
//		fail("Not yet implemented");
//	}

}
