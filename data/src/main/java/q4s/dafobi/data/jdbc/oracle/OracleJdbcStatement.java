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
package q4s.dafobi.data.jdbc.oracle;

import q4s.dafobi.data.jdbc.JdbcConnection;
import q4s.dafobi.data.jdbc.JdbcStatement;

/**
 * Oracle в отличии от стандартного JDBC умеет работать с DECLARE-BEGIN-END
 * блоком и имеет нормальную поддержку CLOB.
 * 
 * @author Q4S
 * 
 */
public class OracleJdbcStatement extends JdbcStatement {

	/**
	 * @see OracleJdbcStatement
	 * 
	 * @param transaction
	 * @param query
	 */
	public OracleJdbcStatement(JdbcConnection transaction, String query) {
		super(transaction, query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * q4s.dafobi.data.jdbc.JdbcStatement#isOperatorCallable(java.lang.
	 * String)
	 */
	@Override
	protected boolean isOperatorCallable(String operator) {
		// Запрос автоматически определяется как Callable, если он имеет
		// формат "{call ...}" или "[declare ... ]begin ... end".
		// TODO Конструкция BEGIN-END существует только в Oracle
		return "{call".equalsIgnoreCase(operator.substring(0, 5))
				|| "begin".equalsIgnoreCase(operator.substring(0, 5))
				|| "declare".equalsIgnoreCase(operator.substring(0, 7));
	}

}
