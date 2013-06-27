package toy.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import toy.servlet.Request;
import toy.servlet.Servlet;

/**
 * Extremely simple servlet engine.
 */
public class ToyEngine {
	/**
	 * The singleton instance of the toy engine.
	 */
	private static ToyEngine engine;

	/**
	 * The port to listen.
	 */
	private int port;

	/**
	 * The debug mode.
	 */
	private boolean debugMode;

	/**
	 * The map of the path patterns and servlets.
	 */
	private Map<String, Servlet> servlets;

	/**
	 * Returns the singleton instance.
	 * @param settingName the name of the settings file
	 * @return the singleton instance
	 */
	public static ToyEngine createInstance(String settingName) {
		if (engine == null) {
			engine = new ToyEngine(settingName);
		}
		return engine;
	}

	/**
	 * Returns the singleton instance.
	 * @return the singleton instance
	 */
	public static ToyEngine getInstance() {
		return engine;
	}

	/**
	 * Constructs a toy engine.
	 * @param settingName the name of the settings file
	 */
	private ToyEngine(String settingName) {
		ResourceBundle settings = ResourceBundle.getBundle(settingName);
		port = Integer.parseInt(settings.getString("PORT"));
		try {
			debugMode = Boolean.parseBoolean(settings.getString("DEBUG"));
		} catch (MissingResourceException e) {
			debugMode = false;
		}
		servlets = new LinkedHashMap<String, Servlet>();
	}

	/**
	 * Print the log message if the debug mode is true.
	 * @param message the log message
	 */
	public void debugLog(String message) {
		if (debugMode) {
			System.out.println(message);
		}
	}

	/**
	 * Adds a servlet to the servlets map.
	 * @param pattern the pattern
	 * @param servlet the servlet
	 */
	public void addServlet(String pattern, Servlet servlet) {
		debugLog("adding " + servlet.getName() + " (" + pattern + ")");
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
	 * Starts the toy engine.
	 */
	public void start() {
		try {
			debugLog("opening port " + port);
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
