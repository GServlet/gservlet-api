package org.gservlet;

import static org.junit.Assert.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.gservlet.annotation.Servlet;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import groovy.sql.Sql;
import groovy.xml.MarkupBuilder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;

public class HttpServletTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void testMethods() throws Exception {
		File folder = new File("src/test/resources/" + Constants.SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/" + "HttpServlet.groovy");
		AbstractServlet servlet = (AbstractServlet) scriptManager.loadScript(script);
		assertNotNull(servlet);
		Servlet annotation = servlet.getClass().getAnnotation(Servlet.class);
		assertEquals("HttpServlet", servlet.getClass().getName());
		assertEquals(AbstractServlet.class, servlet.getClass().getSuperclass());
		assertEquals("/servlet", annotation.value()[0]);
		final Map<Object, Object> map = new HashMap<Object, Object>();
		Answer initializeMap = new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				map.put(invocation.getArguments()[0], invocation.getArguments()[1]);
				return null;
			}
		};
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getSession(true)).thenReturn(mock(HttpSession.class));
		when(request.getAttribute(Constants.CONNECTION)).thenReturn(new Sql(mock(DataSource.class)));
		when(request.getServletContext()).thenReturn(mock(ServletContext.class));
		doAnswer(initializeMap).when(request).setAttribute(anyString(), any());
		HttpServletResponse response = mock(HttpServletResponse.class);
		servlet.doGet(request, response);
		assertEquals("get", map.get("state"));
		servlet.doPost(request, response);
		assertEquals("post", map.get("state"));
		servlet.doPut(request, response);
		assertEquals("put", map.get("state"));
		servlet.doDelete(request, response);
		assertEquals("delete", map.get("state"));
		servlet.doOptions(request, response);
		assertEquals("options", map.get("state"));
		servlet.doTrace(request, response);
		assertEquals("trace", map.get("state"));
		servlet.doHead(request, response);
		assertEquals("head", map.get("state"));
		assertEquals(RequestWrapper.class, servlet.getRequest().getClass());
		assertEquals(SessionWrapper.class, servlet.getSession().getClass());
		assertEquals(ContextWrapper.class, servlet.getContext().getClass());
		assertNotNull(servlet.getConnection());
	}

	@Test
	public void testOutput() throws Exception {
		File folder = new File("src/test/resources/" + Constants.SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		File script = new File(folder + "/" + "HttpServlet.groovy");
		AbstractServlet servlet = (AbstractServlet) scriptManager.loadScript(script);
		assertNotNull(servlet);
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		StringWriter out = new StringWriter();
		when(response.getWriter()).thenReturn(new PrintWriter(out));
		servlet.doGet(request, response);
		assertNotNull(servlet.getOut());
		MarkupBuilder builder = servlet.getHtml();
		assertNotNull(builder);
		assertEquals("<!DOCTYPE html>", out.toString().trim());
		out = new StringWriter();
		when(response.getWriter()).thenReturn(new PrintWriter(out));
		Map<String, String> map = new HashMap<>();
		map.put("key", "value");
		servlet.json(map);
		assertEquals("{\"key\":\"value\"}", out.toString());
		assertEquals("{\"key\":\"value\"}", servlet.stringify(map));
	}

}