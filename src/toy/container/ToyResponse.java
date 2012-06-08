package toy.container;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import toy.servlet.Response;

/**
 * Subclass of Response to be used with ToyContainer.
 */
public class ToyResponse implements Response {
	/**
	 * The special status code to shutdown the servlet container.
	 */
	public static final String STATUS_SHUTDOWN = "XXX Shutdown";

	/**
	 * The response status.
	 */
	private String status;

	/**
	 * The response headers.
	 */
	private Map<String, String> headers;

	/**
	 * The response body to be printed.
	 */
	private StringBuilder buffer;

	/**
	 * The response body as an array of bytes.
	 */
	private byte[] bytes;

	/**
	 * Construct a ToyResponse object.
	 */
	public ToyResponse() {
		headers = new HashMap<String, String>();
		buffer = new StringBuilder();
	}

	/**
	 * Returns the response status.
	 * @return the response status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setError(String status, String message) {
		this.status = status;
		headers.clear();
		buffer.delete(0, buffer.length());
		bytes = null;

		setHeader("Content-Type", "text/html");
		println("<html><head>");
		println("<title>" + status + "</title>");
		println("</head><body>");
		println("<h1>" + status + "</h1>");
		println("<pre>" + message + "</pre>");
		println("</body></html>");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setError(String status, Exception exception) {
		if (exception == null) {
			setError(status, "An unknown error has occurred.");
		} else {
			StringWriter writer = new StringWriter();
			exception.printStackTrace(new PrintWriter(writer));
			setError(status, writer.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHeader(String name, String value) {
		headers.put(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void print(String str) {
		buffer.append(str);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void println(String str) {
		buffer.append(str).append('\n');
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void println() {
		buffer.append('\n');
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	/**
	 * Writes the response to the stream.
	 * @param stream the stream to which the response is written
	 * @param headerOnly if true, don't write the response body
	 * @throws IOException if an I/O error occurs
	 */
	public void writeTo(PrintStream stream, boolean headerOnly) throws IOException {
		stream.println("HTTP/1.0 " + status);
		for (String name : headers.keySet()) {
			stream.println(name + ": " + headers.get(name));
		}
		stream.println();

		if (! headerOnly) {
			if (bytes == null) {
				stream.print(buffer.toString());
			} else {
				stream.write(bytes);
			}
		}
	}
}
