package org.gservlet;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConstantsTest {

	@Test
	public void test() {
		assertEquals("scripts", Constants.SCRIPTS_FOLDER);
		assertEquals("gservlet.dataSource", Constants.DATASOURCE);
		assertEquals("gservlet.connection", Constants.CONNECTION);
		assertEquals("gservlet.reload", Constants.RELOAD);
		assertEquals("conf", Constants.CONFIG_FOLDER);
		assertEquals("db.properties", Constants.DB_CONFIG_FILE);
		assertEquals("gservlet.handlers", Constants.HANDLERS);
		assertEquals("gservlet.requestFilter", Constants.REQUEST_FILTER);
		assertEquals("servlet.response", Constants.SERVLET_RESPONSE);
		assertEquals("servlet.filterChain", Constants.FILTER_CHAIN);
	}

}
