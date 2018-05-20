package org.q4s.dafobi.trans;

import java.util.Map;

/**
 * Класс, реализующий базовый скелет для запросов.
 * 
 * @author Q4S
 * 
 */
public abstract class AbstractStatement implements IStatement {

	@Override
	public void close() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#query(java.util.Map)
	 */
	@Override
	public IResultTable query(Map<String, DataParam> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#execute(java.util.Map)
	 */
	@Override
	public boolean execute(Map<String, DataParam> parameters) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#executeUpdate(java.util.Map)
	 */
	@Override
	public int executeUpdate(Map<String, DataParam> parameters) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#addBatch(java.util.Map)
	 */
	@Override
	public void addBatch(Map<String, DataParam> parameters) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.q4s.dafobi.trans.IStatement#setParam(java.lang.String,
	 * org.q4s.dafobi.trans.DataParam)
	 */
	@Override
	public void setParam(String name, DataParam value) {
		// TODO Auto-generated method stub
	}

}
