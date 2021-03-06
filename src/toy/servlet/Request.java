package toy.servlet;

import java.util.Set;

/**
 * Defines an object to provide client request information to a servlet.
 */
public interface Request {
	/**
	 * HTTP HEAD method.
	 */
	public static final String METHOD_HEAD = "HEAD";

	/**
	 * HTTP GET method.
	 */
	public static final String METHOD_GET = "GET";

	/**
	 * HTTP POST method.
	 */
	public static final String METHOD_POST = "POST";

	/**
	 * Returns the method name, for example, Request.METHOD_GET.
	 * @return the method name
	 */
	public String getMethod();

	/**
	 * Returns the path, for example, "/foo/bar.cgi".
	 * Note that the path doesn't include the query string.
	 * @return the path
	 */
	public String getPath();

	/**
	 * Returns the set of all parameter names.
	 * @return the set of all parameter names
	 */
	public Set<String> getParameterNames();

	/**
	 * Returns the parameter value specified by name.
	 * Note that the parameter value is NOT decoded.
	 * @param name the name to specify the parameter (case sensitive)
	 * @return the parameter value, or null if there's no parameter specified by name
	 */
	public String getParameter(String name);

	/**
	 * Returns the URL-decoded parameter value specified by name.
	 * @param name the name to specify the parameter (case sensitive)
	 * @return the URL-decoded parameter value, or null if there's no parameter specified by name
	 */
	public String getDecodedParameter(String name);

	/**
	 * Returns the parameter value specified by name as an int.
	 * @param name the name to specify the parameter (case sensitive)
	 * @return the parameter value as an int
	 * @throws NullPointerException if there's no parameter specified by name
	 * @throws NumberFormatException if the parameter value can't be converted to an int
	 */
	public int getIntParameter(String name);

	/**
	 * Returns the set of all header names.
	 * Note that the header names are converted to lower case.
	 * @return the set of all header names
	 */
	public Set<String> getHeaderNames();

	/**
	 * Returns the header value specified by name.
	 * @param name the name to specify the header (case insensitive)
	 * @return the header value, or null if there's no header specified by name
	 */
	public String getHeader(String name);

	/**
	 * Returns the header value specified by name as an int.
	 * @param name the name to specify the header (case insensitive)
	 * @return the header value as an int
	 * @throws NullPointerException if there's no header specified by name
	 * @throws NumberFormatException if the header value can't be converted to an int
	 */
	public int getIntHeader(String name);

	/**
	 * Returns the set of all cookie names.
	 * @return the set of all cookie names
	 */
	public Set<String> getCookieNames();

	/**
	 * Returns the cookie value specified by name.
	 * @param name the name to specify the cookie (case sensitive)
	 * @return the cookie value, or null if there's no cookie specified by name
	 */
	public String getCookie(String name);
}
