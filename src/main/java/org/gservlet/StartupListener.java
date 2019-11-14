package org.gservlet;

import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class StartupListener implements ServletContextListener {

	protected final Logger logger = Logger.getLogger(getClass().getName());
	protected Initializer initializer;
	protected DatabaseManager databaseManager;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		try {
			initializer = new Initializer(context);
			databaseManager = new DatabaseManager(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("application started on context " + context.getContextPath());
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		initializer.destroy();
		databaseManager.destroy();
	}

}