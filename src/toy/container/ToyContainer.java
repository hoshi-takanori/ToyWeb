package toy.container;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import toy.servlet.Request;
import toy.servlet.Servlet;

/**
 * Extremely simple servlet container.
 */
public class ToyContainer {
	/**
	 * The port to listen.
	 */
	public static final int PORT = 8080;

	/**
	 * The singleton instance of the toy container.
	 */
	private static ToyContainer container;

	/**
	 * The map of the path patterns and servlets.
	 */
	private Map<String, Servlet> servlets;

	/**
	 * Returns the singleton instance.
	 * @return the singleton instance
	 */
	public static ToyContainer getInstance() {
		if (container == null) {
			container = new ToyContainer();
		}
		return container;
	}

	/**
	 * Constructs a toy container.
	 */
	private ToyContainer() {
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
	 * @param args the command line arguments
	 */
	public void start() {
		try {
			ServerSocket listener = new ServerSocket(PORT);
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
