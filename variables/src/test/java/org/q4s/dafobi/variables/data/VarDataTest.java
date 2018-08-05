/*
 * (C) Copyright 2018 - Vladimir Bogdanov | Data Form Builder
 *
 * https://github.com/quest4sanity/dafobi
 *
 * Licensed under the LGPL, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License in LGPL.txt file in 
 * the root directory or at https://www.gnu.org/licenses/lgpl.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Vladimir Bogdanov - quest4sanity@gmail.com
 */
package org.q4s.dafobi.variables.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.q4s.dafobi.jaxb.common.DataType;
import org.q4s.dafobi.variables.IVar;

public class VarDataTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * @see VarData#VarData()
	 */
	@Test
	public void testVarData() {
		IVar var = new VarData();
		assertNull(var.get());
		assertNull(var.getType());
	}

	/**
	 * @see VarData#VarData(Object)
	 */
	@Test
	public void testVarDataObject() {
		IVar var = new VarData("Test");
		assertEquals("Test", var.get());
		assertNull(var.getType());
	}

	/**
	 * @see VarData#VarData(Object, DataType)
	 */
	@Test
	public void testVarDataObjectDataType() {
		IVar var = new VarData("123.3", DataType.NUMBER);
		assertEquals(new BigDecimal("123.3"), var.get());
		assertEquals(DataType.NUMBER, var.getType());
	}

	/**
	 * Проверка установки нового значения.
	 * 
	 * @see VarData#get()
	 * @see VarData#set(Object)
	 * @see VarData#setType(DataType)
	 */
	@Test
	public void testGetSet() {
		IVar var = new VarData();

		var.set("123.3");
		assertEquals("123.3", var.get());
		assertNull(var.getType());

		var.setType(DataType.NUMBER);
		var.set("123.3");
		assertEquals(new BigDecimal("123.3"), var.get());
		assertEquals(DataType.NUMBER, var.getType());
	}

	/**
	 * Проверка изменения типа данных и автоматического приведения текущего
	 * значения к новому типу.
	 * 
	 * @see VarData#getType()
	 * @see VarData#setType(DataType)
	 */
	@Test
	public void testGetSetType() {
		IVar var = new VarData("123.3");
		assertEquals("123.3", var.get());
		assertNull(var.getType());

		var.setType(DataType.NUMBER);
		assertEquals(new BigDecimal("123.3"), var.get());
		assertEquals(DataType.NUMBER, var.getType());
	}

	/**
	 * Проверка защиты значения от изменения.
	 * 
	 * @see VarData#get()
	 * @see VarData#set(Object)
	 * @see VarData#setReadOnly(boolean)
	 */
	@Test
	public void testGetSetReadonly() {
		VarData var = new VarData("123.3");
		assertEquals("123.3", var.get());
		assertFalse(var.isReadOnly());

		var.set("321.1");
		assertEquals("321.1", var.get());

		var.setReadOnly(true);
		var.set("312.2");
		assertEquals("321.1", var.get());
	}

}
