package org.gservlet;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionListener;
import org.gservlet.annotation.ContextAttributeListener;
import org.gservlet.annotation.ContextListener;
import org.gservlet.annotation.Filter;
import org.gservlet.annotation.RequestAttributeListener;
import org.gservlet.annotation.RequestListener;
import org.gservlet.annotation.Servlet;
import org.gservlet.annotation.SessionAttributeListener;
import org.gservlet.annotation.SessionListener;

public class Initializer {

	protected final ServletContext context;
	protected final Map<String, DynamicInvocationHandler> handlers;
	protected final ScriptManager scriptManager;
	protected final Logger logger = Logger.getLogger(Initializer.class.getName());

	public Initializer(ServletContext context) throws Exception {
		this.context = context;
		this.handlers = new HashMap<String, DynamicInvocationHandler>();
		context.setAttribute(Constants.HANDLERS, handlers);
		File folder = new File(context.getRealPath("/") + File.separator + Constants.SCRIPTS_FOLDER);
		this.scriptManager = new ScriptManager(folder);
		init(folder);
	}

	protected void init(File folder) throws Exception {
		loadScripts(folder);
		context.addFilter(Constants.REQUEST_FILTER, new RequestFilter())
				.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");
	}

	public void loadScripts(File folder) throws Exception {
		if (folder.exists()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isFile()) {
						loadScript(file);
					} else {
						loadScripts(file);
					}
				}
			}
			watch(folder);
		}
	}

	public void loadScript(File script) throws Exception {
		Object object = scriptManager.loadScript(script);
		register(object);
	}

	public void register(Object object) throws Exception {
		Annotation[] annotations = object.getClass().getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation instanceof Servlet) {
				addServlet(context, (Servlet) annotation, object);
			}
			if (annotation instanceof Filter) {
				addFilter(context, (Filter) annotation, object);
			}
			if (annotation instanceof ContextListener) {
				addListener(context, annotation, object);
			}
			if (annotation instanceof RequestListener) {
				addListener(context, annotation, object);
			}
			if (annotation instanceof ContextAttributeListener) {
				addListener(context, annotation, object);
			}
			if (annotation instanceof RequestAttributeListener) {
				addListener(context, annotation, object);
			}
			if (annotation instanceof SessionListener) {
				addListener(context, annotation, object);
			}
			if (annotation instanceof SessionAttributeListener) {
				addListener(context, annotation, object);
			}
		}
	}

	protected void addServlet(ServletContext context, Servlet annotation, Object object) {
		String name = annotation.name().trim().equals("") ? object.getClass().getName() : annotation.name();
		ServletRegistration registration = context.getServletRegistration(name);
		if (registration == null) {
			DynamicInvocationHandler handler = new DynamicInvocationHandler(object);
			Object servlet = Proxy.newProxyInstance(this.getClass().getClassLoader(),
					new Class[] { javax.servlet.Servlet.class }, handler);
			handlers.put(name, handler);
			registration = context.addServlet(name, (javax.servlet.Servlet) servlet);
			if (annotation.value().length > 0) {
				registration.addMapping(annotation.value());
			}
			if (annotation.urlPatterns().length > 0) {
				registration.addMapping(annotation.urlPatterns());
			}
		} else {
			String message = "The servlet with the name " + name
					+ " has already been registered. Please use a different name or package";
			throw new RuntimeException(message);
		}
	}

	protected void addFilter(ServletContext context, Filter annotation, Object object) {
		String name = object.getClass().getName();
		FilterRegistration registration = context.getFilterRegistration(name);
		if (registration == null) {
			DynamicInvocationHandler handler = new DynamicInvocationHandler(object);
			Object filter = Proxy.newProxyInstance(this.getClass().getClassLoader(),
					new Class[] { javax.servlet.Filter.class }, handler);
			handlers.put(name, handler);
			registration = context.addFilter(name, (javax.servlet.Filter) filter);
			if (annotation.value().length > 0) {
				registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true,
						annotation.value());
			}
			if (annotation.urlPatterns().length > 0) {
				registration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true,
						annotation.urlPatterns());
			}
		} else {
			String message = "The filter with the name " + name
					+ " has already been registered. Please use a different name or package";
			throw new RuntimeException(message);
		}
	}

	protected void addListener(ServletContext context, Annotation annotation, Object object) {
		DynamicInvocationHandler handler = new DynamicInvocationHandler(object);
		EventListener listener = null;
		if (object instanceof ServletContextAttributeListener) {
			listener = (EventListener) Proxy.newProxyInstance(this.getClass().getClassLoader(),
					new Class[] { ServletContextAttributeListener.class }, handler);
		} else if (object instanceof ServletRequestListener) {
			listener = (EventListener) Proxy.newProxyInstance(this.getClass().getClassLoader(),
					new Class[] { ServletRequestListener.class }, handler);
		} else if (object instanceof ServletRequestAttributeListener) {
			listener = (EventListener) Proxy.newProxyInstance(this.getClass().getClassLoader(),
					new Class[] { ServletRequestAttributeListener.class }, handler);
		} else if (object instanceof HttpSessionListener) {
			listener = (EventListener) Proxy.newProxyInstance(this.getClass().getClassLoader(),
					new Class[] { HttpSessionListener.class }, handler);
		} else if (object instanceof HttpSessionAttributeListener) {
			listener = (EventListener) Proxy.newProxyInstance(this.getClass().getClassLoader(),
					new Class[] { HttpSessionAttributeListener.class }, handler);
		}
		if (listener != null) {
			context.addListener(listener);
		} else if (object instanceof ServletContextListener) {
			ServletContextListener contextListener = (ServletContextListener) object;
			contextListener.contextInitialized(context);
		}
		handlers.put(object.getClass().getName(), handler);
	}

	protected void watch(final File folder) {
		boolean reload = Boolean.parseBoolean(System.getenv(Constants.RELOAD));
		if (reload) {
			new FileWatcher().addListener(new FileAdapter() {
				public void onCreated(String fileName) {
					logger.info("reloading script " + fileName);
					reload(new File(folder + "/" + fileName));
				}

			}).watch(folder);
		}
	}

	protected void reload(File script) {
		try {
			Object object = scriptManager.loadScript(script);
			Annotation[] annotations = object.getClass().getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation instanceof Servlet) {
					Servlet servlet = (Servlet) annotation;
					String name = servlet.name().trim().equals("") ? object.getClass().getName() : servlet.name();
					DynamicInvocationHandler handler = handlers.get(name);
					if (handler != null) {
						handler.setTarget(object);
					} else {
						handler = new DynamicInvocationHandler(object);
						handler.setRegistered(false);
						handlers.put(name, handler);
					}
				} else if (annotation instanceof Filter || annotation instanceof RequestListener
						|| annotation instanceof ContextAttributeListener
						|| annotation instanceof RequestAttributeListener || annotation instanceof SessionListener
						|| annotation instanceof SessionAttributeListener) {
					DynamicInvocationHandler handler = handlers.get(object.getClass().getName());
					if (handler != null) {
						handler.setTarget(object);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void destroy() {
		for (DynamicInvocationHandler handler : handlers.values()) {
			Object target = handler.getTarget();
			if (target instanceof ServletContextListener) {
				ServletContextListener contextListener = (ServletContextListener) target;
				contextListener.contextDestroyed(context);
			}
		}
	}

	public Map<String, DynamicInvocationHandler> getHandlers() {
		return handlers;
	}
	
}