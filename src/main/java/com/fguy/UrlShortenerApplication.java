package com.fguy;

import java.util.EnumSet;
import java.util.logging.Logger;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.TracingConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Jersey Application to run JAX-RS service.
 * 
 * @author fguy
 *
 */
public class UrlShortenerApplication extends ResourceConfig {
	/**
	 * Config Jersey application.
	 */
	public UrlShortenerApplication() {
		packages(this.getClass().getPackage().getName());

		// Logging.
		register(new LoggingFilter(Logger.getLogger(LoggingFilter.class
				.getName()), true));

		// Tracing support.
		property(ServerProperties.TRACING, TracingConfig.ON_DEMAND.name());
	}

	/**
	 * Runs with embedded jetty server.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		int port = System.getProperty("port") != null ? Integer.parseInt(System
				.getProperty("port")) : 8080;
		Server server = new Server(port);

		// set up context
		ServletContextHandler context = new ServletContextHandler(server, "/");

		// register application as a servlet filter
		String className = UrlShortenerApplication.class.getName();
		FilterHolder filter = new FilterHolder();
		filter.setName(className);
		filter.setClassName(ServletContainer.class.getName());
		filter.setInitParameter("javax.ws.rs.Application", className);
		context.addFilter(filter, "/*", EnumSet.of(DispatcherType.REQUEST));
		context.setSessionHandler(new SessionHandler());

		// start server
		server.start();
		System.out.println(String.format("* URL Shortener service port: %d",
				port));
		server.join();
	}
}
