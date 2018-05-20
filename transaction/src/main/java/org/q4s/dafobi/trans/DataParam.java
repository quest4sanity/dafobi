package org.q4s.dafobi.trans;

public class DataParam {
	private final DataType type;
	private final Object value;
	
	public DataParam(DataType type, Object value) {
		this.type = type;
		this.value = value;
	}

	public DataType getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}
}
