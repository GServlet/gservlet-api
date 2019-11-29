package org.gservlet;

import static org.mockito.Mockito.mock;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import javax.servlet.http.HttpSession;
import org.junit.Test;

public class SessionWrapperTest {

	@Test
	public void test() throws Exception {

		HttpSession session = mock(HttpSession.class);
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
	}

}
