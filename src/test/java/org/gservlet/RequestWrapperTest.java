package org.gservlet;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class RequestWrapperTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void test() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		final Map<Object, Object> map = new HashMap<Object, Object>();
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				map.put(invocation.getArguments()[0], invocation.getArguments()[1]);
				return null;
			}
		}).when(request).setAttribute(anyString(), any());
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return map.get(invocation.getArguments()[0]);
			}
		}).when(request).getAttribute(anyString());
		RequestWrapper wrapper = new RequestWrapper(request);
		Method[] methods = wrapper.getClass().getDeclaredMethods();
		for (Method method : methods) {
			method.setAccessible(true);
			Type[] types = method.getGenericParameterTypes();
			Object[] args = new Object[types.length];
			for (int i = 0; i < types.length; i++) {
				args[i] = null;
			}
			method.invoke(wrapper, args);
		}
		wrapper.propertyMissing("myAttribute", "myValue");
		assertEquals("myValue", map.get("myAttribute"));
		assertEquals("myValue", wrapper.propertyMissing("myAttribute"));
		String json = "{\"key\":\"value\"}";
		final ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes());
		when(request.getContentType()).thenReturn("application/json");
		when(request.getInputStream()).thenReturn(new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return in.read();
			}
			
			@Override
			public void setReadListener(ReadListener readListener) {
			}
			
			@Override
			public boolean isReady() {
				return true;
			}
			
			@Override
			public boolean isFinished() {
				return false;
			}
		});
		Map result = (Map) wrapper.propertyMissing("body");
		assertEquals("value", result.get("key"));
	}

}