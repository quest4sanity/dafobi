package org.q4s.dafobi.jaxb.dataform;

import static org.junit.Assert.*;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

public class DataformTest {

	@Test
	public void test() throws JAXBException {
		JAXBContext context;
		InputStream is = DataformTest.class.getResourceAsStream("Dw1.xml");
		context = JAXBContext.newInstance(Dataform.class);
		Unmarshaller um = context.createUnmarshaller();
		Dataform xmlFileContent = (Dataform) um.unmarshal(is);
	}
}
