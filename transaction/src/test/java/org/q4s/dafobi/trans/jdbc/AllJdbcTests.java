package org.q4s.dafobi.trans.jdbc;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ HsqldbTest.class, //
		JdbcStatementInternalTest.class, //
		JdbcStatementTest.class })
public class AllJdbcTests {

}
