package toy.container;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import toy.servlet.Request;
import toy.servlet.Servlet;

/**
 * Extremely simple servlet container.
 */
public class ToyContainer {
	/**
	 * The singleton instance of the toy container.
	 */
	private static ToyContainer container;

	/**
	 * The port to listen.
	 */
	private int port;

	/**
	 * The map of the path patterns and servlets.
	 */
	private Map<String, Servlet> servlets;

	/**
	 * Returns the singleton instance.
	 * @param settingName the name of the settings file
	 * @return the singleton instance
	 */
	public static ToyContainer createInstance(String settingName) {
		if (container == null) {
			container = new ToyContainer(settingName);
		}
		return container;
	}

	/**
	 * Returns the singleton instance.
	 * @return the singleton instance
	 */
	public static ToyContainer getInstance() {
		return container;
	}

	/**
	 * Constructs a toy container.
	 * @param settingName the name of the settings file
	 */
	private ToyContainer(String settingName) {
		ResourceBundle settings = ResourceBundle.getBundle(settingName);
		port = Integer.parseInt(settings.getString("CONTAINER_PORT"));
		servlets = new LinkedHashMap<String, Servlet>();
	}

	/**
	 * Adds a servlet to the servlets map.
	 * @param pattern the pattern
	 * @param servlet the servlet
	 */
	public void addServlet(String pattern, Servlet servlet) {
		servlets.put(pattern, servlet);
	}

	/**
	 * Returns a collection of all servlets.
	 * @return a collection of all servlets
	 */
	public Collection<Servlet> getServlets() {
		return servlets.values();
	}

	/**
	 * Finds the servlet which matches the given request.
	 * @param request the request
	 * @return the servlet
	 */
	public Servlet findServlet(Request request) {
		for (String pattern : servlets.keySet()) {
			if (request.getPath().matches(pattern)) {
				return servlets.get(pattern);
			}
		}
		return null;
	}

	/**
	 * Starts the toy container.
	 */
	public void start() {
		try {
			ServerSocket listener = new ServerSocket(port);
			while (true) {
				Socket socket = listener.accept();
				ToyConnection connection = new ToyConnection(socket);
				new Thread(connection).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
