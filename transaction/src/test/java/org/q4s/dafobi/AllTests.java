package org.q4s.dafobi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.q4s.dafobi.trans.AllTranTests;
import org.q4s.dafobi.trans.jdbc.AllJdbcTests;

@RunWith(Suite.class)
@SuiteClasses({ AllJdbcTests.class,
	AllTranTests.class})
public class AllTests {

}