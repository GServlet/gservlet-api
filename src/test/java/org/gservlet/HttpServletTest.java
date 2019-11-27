package org.gservlet;

import static org.junit.Assert.*;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.gservlet.annotation.Servlet;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;

public class HttpServletTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void test() throws Exception {
		File folder = new File("src/test/resources/"+Constants.SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		AbstractServlet servlet = (AbstractServlet) scriptManager.loadScript("HttpServlet.groovy");
		assertNotNull(servlet);
		Annotation[] annotations = servlet.getClass().getAnnotations();
		for(Annotation current : annotations) {
			   if(current instanceof Servlet) {
				   assertEquals("HttpServlet",servlet.getClass().getName());
				   assertEquals(AbstractServlet.class, servlet.getClass().getSuperclass());
				   Servlet annotation = (Servlet) current;
				   assertEquals("/servlet", annotation.value()[0]);
			   }
			}
		final Map<Object,Object> map = new HashMap<Object,Object>();
		Answer initializeMap = new Answer() {
		    public Object answer(InvocationOnMock invocation) throws Throwable {
		      map.put(invocation.getArguments()[0],invocation.getArguments()[1]);
		      return null;
		    }
		};
		HttpServletRequest request = mock(HttpServletRequest.class);
		doAnswer(initializeMap).when(request).setAttribute(anyString(),any());
		servlet.doGet(request, mock(HttpServletResponse.class));
		assertEquals("get", map.get("state"));
		servlet.doPost(request, mock(HttpServletResponse.class));
		assertEquals("post", map.get("state"));
		servlet.doPut(request, mock(HttpServletResponse.class));
		assertEquals("put", map.get("state"));
		servlet.doDelete(request, mock(HttpServletResponse.class));
		assertEquals("delete", map.get("state"));
		servlet.doOptions(request, mock(HttpServletResponse.class));
		assertEquals("options", map.get("state"));
		servlet.doTrace(request, mock(HttpServletResponse.class));
		assertEquals("trace", map.get("state"));
		servlet.doHead(request, mock(HttpServletResponse.class));
		assertEquals("head", map.get("state"));
	}

}