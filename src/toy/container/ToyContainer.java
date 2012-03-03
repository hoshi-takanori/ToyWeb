package toy.container;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import toy.example.AdminServlet;
import toy.example.HelloServlet;
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
	 * The map of the path patterns and servlets.
	 */
	private static Map<String, Servlet> servlets;

	/**
	 * Initialize the servlets map.
	 * @param filename
	 * @throws Exception
	 */
	public static void initServlets(String filename) throws Exception {
		servlets = new LinkedHashMap<String, Servlet>();
		if (filename != null) {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.length() > 0 && line.charAt(0) != '#') {
					String[] array = line.split("\t+");
					if (array.length >= 2) {
						servlets.put(array[0], (Servlet) Class.forName(array[1]).newInstance());
					}
				}
			}
		} else {
			servlets.put("^/admin$", new AdminServlet());
			servlets.put("^/hello$", new HelloServlet());
		}
	}

	/**
	 * Returns a collection of all servlets.
	 * @return a collection of all servlets
	 */
	public static Collection<Servlet> getServlets() {
		return servlets.values();
	}

	/**
	 * Finds the servlet which matches the given request.
	 * @param request the request
	 * @return the servlet
	 */
	public static Servlet findServlet(Request request) {
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
	public static void main(String[] args) {
		try {
			initServlets(args.length > 0 ? args[0] : null);

			ServerSocket listener = new ServerSocket(PORT);
			while (true) {
				Socket socket = listener.accept();
				ToyConnection connection = new ToyConnection(socket);
				new Thread(connection).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
