package org.gservlet;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class SessionWrapperTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void test() throws Exception {
		HttpSession session = mock(HttpSession.class);
		final Map<Object, Object> map = new HashMap<Object, Object>();
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				map.put(invocation.getArguments()[0], invocation.getArguments()[1]);
				return null;
			}
		}).when(session).setAttribute(anyString(), any());
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return map.get(invocation.getArguments()[0]);
			}
		}).when(session).getAttribute(anyString());
		SessionWrapper wrapper = new SessionWrapper(session);
		Method[] methods = wrapper.getClass().getDeclaredMethods();
		for (Method method : methods) {
			method.setAccessible(true);
			Type[] types = method.getGenericParameterTypes();
			Object[] args = new Object[types.length];
			for (int i = 0; i < types.length; i++) {
				if ("int".equals(types[i].getTypeName())) {
					args[i] = 10;
				} else {
					args[i] = null;
				}
			}
			method.invoke(wrapper, args);
		}
		wrapper.propertyMissing("myAttribute", "myValue");
		assertEquals("myValue", map.get("myAttribute"));
		assertEquals("myValue", wrapper.propertyMissing("myAttribute"));
	}

}