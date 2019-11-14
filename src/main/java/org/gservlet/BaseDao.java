package org.gservlet;

import groovy.sql.Sql;

public abstract class BaseDao {

	protected Sql connection;

	public Sql getConnection() {
		return connection;
	}

	public void setConnection(Sql connection) {
		this.connection = connection;
	}
	
}
