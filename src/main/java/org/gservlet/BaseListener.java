package org.gservlet;

public class BaseListener {

	protected Object event;
	
	protected void route(Object event, String method) {
		this.event = event;
		invoke(method);
	}
	
	protected void invoke(String method) {
		try {
			getClass().getDeclaredMethod(method).invoke(this);
		} catch (NoSuchMethodException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}