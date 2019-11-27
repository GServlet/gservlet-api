package org.gservlet;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConstantsTest {

	@Test
	public void test() {
		assertEquals(Constants.SCRIPTS_FOLDER, "scripts");
		assertEquals(Constants.DATASOURCE, "gservlet.dataSource");
		assertEquals(Constants.CONNECTION, "gservlet.connection");
		assertEquals(Constants.RELOAD, "gservlet.reload");
		assertEquals(Constants.CONFIG_FOLDER, "conf");
		assertEquals(Constants.DB_CONFIG_FILE, "db.properties");
		assertEquals(Constants.HANDLERS, "gservlet.handlers");
		assertEquals(Constants.REQUEST_FILTER, "gservlet.requestFilter");
		assertEquals(Constants.SERVLET_RESPONSE, "servlet.response");
		assertEquals(Constants.FILTER_CHAIN, "servlet.filterChain");
	}

}
