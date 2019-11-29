package org.gservlet;

import static org.mockito.Mockito.mock;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;

public class RequestWrapperTest {

	@Test
	public void test() throws Exception {

		HttpServletRequest request = mock(HttpServletRequest.class);
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
	}

}
