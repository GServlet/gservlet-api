/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

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
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
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
		Part part = mock(Part.class);
		when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"fieldName\"; filename=\"filename.jpg\"");
		assertEquals("filename.jpg", wrapper.getFileName(part));
		
	}

}