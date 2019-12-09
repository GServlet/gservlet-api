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
import javax.servlet.ServletContext;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ContextWrapperTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void test() throws Exception {
		ServletContext context = mock(ServletContext.class);
		final Map<Object, Object> map = new HashMap<Object, Object>();
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				map.put(invocation.getArguments()[0], invocation.getArguments()[1]);
				return null;
			}
		}).when(context).setAttribute(anyString(), any());
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return map.get(invocation.getArguments()[0]);
			}
		}).when(context).getAttribute(anyString());
		ContextWrapper wrapper = new ContextWrapper(context);
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
	}

}