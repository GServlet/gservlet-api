package org.gservlet;

public class BaseListener {

	public void invoke(String method) {
		try {
			getClass().getDeclaredMethod(method).invoke(this);
		} catch (NoSuchMethodException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}