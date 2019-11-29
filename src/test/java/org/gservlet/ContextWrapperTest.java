package org.gservlet;

import static org.mockito.Mockito.mock;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import javax.servlet.ServletContext;
import org.junit.Test;

public class ContextWrapperTest {

	@Test
	public void test() throws Exception {
		ServletContext context = mock(ServletContext.class);
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

	}

}
