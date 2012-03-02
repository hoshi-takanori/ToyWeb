package toy.container;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import toy.servlet.Response;

/**
 * Subclass of Response to be used with ToyContainer.
 */
public class ToyResponse implements Response {
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
		status = Response.STATUS_ERROR;
		headers = new HashMap<String, String>();
		buffer = new StringBuilder();
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
