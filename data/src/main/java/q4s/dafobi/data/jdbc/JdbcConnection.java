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
package q4s.dafobi.data.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import q4s.dafobi.data.AbstractConnection;
import q4s.dafobi.data.IStatement;
import q4s.dafobi.exception.DataException;

public class JdbcConnection extends AbstractConnection {

	private final Connection connection;

	public JdbcConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @return Связь с подключением через JDBC.
	 */
	public final Connection getConnection() {
		return this.connection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see q4s.dafobi.data.AbstractTransaction#prepare(java.lang.String)
	 */
	@Override
	public IStatement prepare(final String statement) {
		// TODO здесь должен быть механизм выбора правильной имплементации
		return new JdbcStatement(this, statement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see q4s.dafobi.data.ITransaction#setAutocommit(boolean)
	 */
	@Override
	public void setAutocommit(boolean autoCommit) {
		try {
			connection.setAutoCommit(autoCommit);

		} catch (SQLException e) {
			throw new DataException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see q4s.dafobi.data.ITransaction#getAutocommit()
	 */
	@Override
	public boolean getAutocommit() {
		try {
			return connection.getAutoCommit();

		} catch (SQLException e) {
			throw new DataException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.q4s.dafobi.trans.ITransactionon#commit()
	 */
	@Override
	public void commit() {
		try {
			connection.commit();

		} catch (SQLException e) {
			throw new DataException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seorg.q4s.dafobi.trans.ITransactionion#rollback()
	 */
	@Override
	public void rollback() {
		try {
			connection.rollback();

		} catch (SQLException e) {
			throw new DataException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see q4s.dafobi.data.ITransactionn#getLastError()
	 */
	@Override
	public void getLastError() {
		// try {
		// connection.getL;
		//
		// } catch (SQLException e) {
		// throw new RuntimeException(e);
		// }
	}

}
