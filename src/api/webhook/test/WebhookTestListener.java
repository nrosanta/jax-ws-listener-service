package api.webhook.test;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class WebhookTestListener
 *
 */
@WebListener
public class WebhookTestListener implements ServletContextListener, ServletRequestListener {

    /**
     * Default constructor. 
     */
    public WebhookTestListener() {
        
    }

	/**
     * @see ServletRequestListener#requestDestroyed(ServletRequestEvent)
     */
    public void requestDestroyed(ServletRequestEvent arg0)  { 
        
    }

	/**
     * @see ServletRequestListener#requestInitialized(ServletRequestEvent)
     */
    public void requestInitialized(ServletRequestEvent arg0)  { 
        System.out.println("WebhookTest Listener Request Initialized");
        ServletContext servletContext = arg0.getServletContext();
    	System.setProperty("log4j.configurationFile",servletContext.getRealPath("/WEB-INF/lib/log4j2.xml"));
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
         System.out.println("WebhookTest Servlet Context Initialized");
    }
	
}