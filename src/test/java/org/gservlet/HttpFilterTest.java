package org.gservlet;

import static org.junit.Assert.*;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;

public class HttpFilterTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void test() throws Exception {
		File folder = new File("src/test/resources/"+Constants.SCRIPTS_FOLDER);
		assertEquals(true, folder.exists());
		ScriptManager scriptManager = new ScriptManager(folder);
		Filter filter = (Filter) scriptManager.loadScript("HttpFilter.groovy");
		assertNotNull(filter);
		Annotation[] annotations = filter.getClass().getAnnotations();
		for(Annotation current : annotations) {
		   if(current instanceof org.gservlet.annotation.Filter) {
			   assertEquals("HttpFilter",filter.getClass().getName());
			   org.gservlet.annotation.Filter annotation = (org.gservlet.annotation.Filter) current;
			   assertEquals("/*", annotation.value()[0]);
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
		filter.doFilter(request, mock(ServletResponse.class), mock(FilterChain.class));
		assertEquals("filtering", map.get("state"));
		assertNotNull(map.get("requestWrapper"));
		
	}

}
