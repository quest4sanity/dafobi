/**
 * 
 */
package org.q4s.dafobi.tools;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author root
 * 
 */
public class ResourceToolsTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * 
	 * {@link org.q4s.dafobi.tools.ResourceTools#getResource(java.lang.Class, java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetResource() {
		String content = ResourceTools.getResource(ResourceToolsTest.class,
				"ResourceToolsTest_prop.properties", "utf-8");

		assertEquals("name=value\n", content);
	}

	/**
	 * Test method for
	 * 
	 * {@link org.q4s.dafobi.tools.ResourceTools#loadProperties(java.lang.Class, java.lang.String)}
	 * .
	 */
	@Test
	public void testLoadProperties() {
		Properties props = ResourceTools.loadProperties(
				ResourceToolsTest.class, "ResourceToolsTest_prop.properties");

		assertEquals("value", props.get("name"));
	}

}
