package toy.container;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import toy.servlet.Request;
import toy.servlet.Response;
import toy.servlet.Servlet;

/**
 * Processes the connection for ToyContainer.
 */
public class ToyConnection implements Runnable {
	/**
	 * The connection socket.
	 */
	private Socket socket;

	/**
	 * Constructs a ToyConnection object with the given socket.
	 * @param socket the connection socket
	 */
	public ToyConnection(Socket socket) {
		this.socket = socket;
	}

	/**
	 * Reads the request information and returns the request object.
	 * @return the request object
	 * @throws IOException if an I/O error occurs
	 */
	public Request getRequest() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		String line = reader.readLine();
		if (line == null) {
			return null;
		}
		String[] array = line.split(" ");
		if (array.length < 2 || array.length > 3) {
			return null;
		}

		ToyRequest request = new ToyRequest(array[0], array[1]);
		if (array.length == 3) {
			while (true) {
				line = reader.readLine();
				if (line == null) {
					return null;
				} else if (line.length() == 0) {
					break;
				} else {
					array = line.split(":", 2);
					if (array.length == 2) {
						request.setHeader(array[0], array[1]);
					}
				}
			}

			int length;
			try {
				length = request.getIntHeader("Content-Length");
			} catch (NumberFormatException e) {
				length = 0;
			}
			if (length > 0) {
				char[] buffer = new char[length];
				length = reader.read(buffer);
				if (length > 0 && request.getMethod().equals(Request.METHOD_POST)) {
					request.parseParameters(new String(buffer, 0, length));
				}
			}
		}

		return request;
	}

	/**
	 * Processes the connection.
	 */
	@Override
	public void run() {
		boolean shutdown = false;
		try {
			Request request = getRequest();
			ToyResponse response = new ToyResponse();
			if (request == null) {
				response.setStatus(Response.STATUS_BAD_REQUEST);
			} else {
				Servlet servlet = ToyContainer.findServlet(request);
				if (servlet != null) {
					try {
						servlet.service(request, response);
					} catch (Exception e) {
						response.setStatus(Response.STATUS_ERROR);
					}
				}
			}
			if (response.getStatus().equals(ToyResponse.STATUS_SHUTDOWN)) {
				shutdown = true;
				response.setStatus(Response.STATUS_OK);
			}
			PrintStream stream = new PrintStream(socket.getOutputStream(), false, "UTF-8");
			response.writeTo(stream, request.getMethod().equals(Request.METHOD_HEAD));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (shutdown) {
			System.exit(0);
		}
	}
}
