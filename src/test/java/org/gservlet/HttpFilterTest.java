package org.gservlet;

import static org.junit.Assert.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.gservlet.annotation.Filter;
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

public class HttpFilterTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void testFilter() throws Exception {
		File folder = new File("src/test/resources/"+Constants.SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		AbstractFilter filter = (AbstractFilter) scriptManager.loadScript("HttpFilter.groovy");
		assertNotNull(filter);
		Filter annotation = filter.getClass().getAnnotation(Filter.class);
		assertEquals("HttpFilter",filter.getClass().getName());
		assertEquals("/*", annotation.value()[0]);
		final Map<Object,Object> map = new HashMap<Object,Object>();
		Answer initializeMap = new Answer() {
		    public Object answer(InvocationOnMock invocation) throws Throwable {
		      map.put(invocation.getArguments()[0],invocation.getArguments()[1]);
		      return null;
		    }
		};
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getAttribute(Constants.CONNECTION)).thenReturn(new Sql(mock(DataSource.class)));
		ServletContext context = mock(ServletContext.class);
		when(request.getServletContext()).thenReturn(context);
		when(request.getSession(true)).thenReturn(mock(HttpSession.class));
		final Map<String, DynamicInvocationHandler> handlers = new HashMap<>();
		when(context.getAttribute(Constants.HANDLERS)).thenReturn(handlers);
		doAnswer(initializeMap).when(request).setAttribute(anyString(),any());
		filter.doFilter(request, mock(HttpServletResponse.class), mock(FilterChain.class));
		assertEquals("filtering", map.get("state"));
		filter.init(mock(FilterConfig.class));
		assertEquals("init", map.get("state"));
		assertNotNull(filter.getConfig());
		filter.destroy();
		assertEquals("destroy", map.get("state"));
		assertEquals(RequestWrapper.class, filter.getRequest().getClass());
		assertEquals(SessionWrapper.class, filter.getSession().getClass());
		assertEquals(ContextWrapper.class, filter.getContext().getClass());
		assertNotNull(filter.getFilterChain());
		assertNotNull(filter.getConnection());
		filter.next();
		DefaultRequestFilter defaultRequestFilter = new DefaultRequestFilter();
		assertFalse(defaultRequestFilter.getClass().isAnnotationPresent(WebListener.class));
		defaultRequestFilter.init(null);
		AbstractServlet servlet = (AbstractServlet) scriptManager.loadScript("HttpServlet.groovy");
		assertNotNull(servlet);
		DynamicInvocationHandler handler = new DynamicInvocationHandler(servlet);
		handler.setTarget(servlet);
		handler.setRegistered(false);
		handlers.put("servlet", handler);
		when(request.getRequestURI()).thenReturn("/servlet");
		String[] methods = {"get","post","put","delete","options","head","trace"};
		for(String method : methods) {
			when(request.getMethod()).thenReturn(method);
			defaultRequestFilter.doFilter(request, mock(HttpServletResponse.class), mock(FilterChain.class));
			assertEquals(method, map.get("state"));
		}
		defaultRequestFilter.destroy();
	}
	
	@Test
	public void testOutput() throws Exception {
		File folder = new File("src/test/resources/"+Constants.SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		AbstractFilter filter = (AbstractFilter) scriptManager.loadScript("HttpFilter.groovy");
		assertNotNull(filter);
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		StringWriter out = new StringWriter();
		when(response.getWriter()).thenReturn(new PrintWriter(out));
		filter.doFilter(request, response, mock(FilterChain.class));
		assertNotNull(filter.getOut());
		MarkupBuilder builder = filter.getHtml();
		assertNotNull(builder);
		assertEquals("<!DOCTYPE html>", out.toString().trim());
		out = new StringWriter();
		when(response.getWriter()).thenReturn(new PrintWriter(out));
		Map<String,String> map = new HashMap<>();
		map.put("key", "value");
		filter.json(map);
		assertEquals("{\"key\":\"value\"}", out.toString());
		assertEquals("{\"key\":\"value\"}", filter.stringify(map));
	}

}
