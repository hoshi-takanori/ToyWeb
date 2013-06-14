package toy.container;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import toy.servlet.Request;

/**
 * Subclass of Request to be used with ToyContainer.
 */
public class ToyRequest implements Request {
	/**
	 * The method name.
	 */
	private String method;

	/**
	 * The path.
	 */
	private String path;

	/**
	 * The raw parameters.
	 */
	private String rawParams;

	/**
	 * The query parameters.
	 */
	private Map<String, String> params;

	/**
	 * The request headers.
	 */
	private Map<String, String> headers;

	/**
	 * Constructs a ToyRequest object with the given method and path.
	 * @param method the method name
	 * @param path the path
	 */
	public ToyRequest(String method, String path) {
		this.method = method;
		this.path = path;
		params = new HashMap<String, String>();
		headers = new HashMap<String, String>();

		int index = path.indexOf('?');
		if (index >= 0) {
			this.path = path.substring(0, index);
			parseParameters(path.substring(index + 1));
		}
	}

	/**
	 * Parses a query string and set parameters.
	 * @param query the query string
	 */
	public void parseParameters(String query) {
		rawParams = query;
		for (String param : query.split("&")) {
			String[] array = param.split("=", 2);
			if (array.length > 0 && array[0].length() > 0) {
				params.put(array[0], (array.length == 1) ? array[0] : array[1]);
			}
		}
	}

	/**
	 * Sets a request header with the given name and value.
	 * @param name the name of the header
	 * @param value the header value
	 */
	public void setHeader(String name, String value) {
		headers.put(name.toLowerCase(), value.trim());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMethod() {
		return method;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return path;
	}

	/**
	 * Returns the raw parameters.
	 */
	public String getRawParameters() {
		return rawParams;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getParameterNames() {
		return params.keySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getParameter(String name) {
		return params.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDecodedParameter(String name) {
		try {
			String value = getParameter(name);
			return value != null ? URLDecoder.decode(value, "UTF-8") : null;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getIntParameter(String name) {
		String value = getParameter(name);
		return Integer.parseInt(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getHeaderNames() {
		return headers.keySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHeader(String name) {
		return headers.get(name.toLowerCase());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getIntHeader(String name) {
		String value = getHeader(name);
		return Integer.parseInt(value);
	}
}
