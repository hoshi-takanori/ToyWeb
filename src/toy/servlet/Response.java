package toy.servlet;

/**
 * Defines an object to assist a servlet in sending a response to the client.
 * The servlet container creates a Response object and passes it as an argument
 * to the servlet's service method.
 */
public interface Response {
	/**
	 * The request has succeeded.
	 */
	public static final String STATUS_OK = "200 OK";

	/**
	 * The response to the request can be found under a different URI and
	 * SHOULD be retrieved using a GET method on that resource.
	 * If the new URI is a location, its URL SHOULD be given by the Location
	 * field in the response.
	 */
	public static final String STATUS_REDIRECT = "303 See Other";

	/**
	 * The request could not be understood by the server due to malformed syntax.
	 */
	public static final String STATUS_BAD_REQUEST = "400 Bad Request";

	/**
	 * The server has not found anything matching the Request-URI.
	 */
	public static final String STATUS_NOT_FOUND = "404 Not Found";

	/**
	 * The server encountered an unexpected condition which prevented it
	 * from fulfilling the request.
	 * The servlet container returns this response if the status is not set
	 * or the servlet throws an exception.
	 */
	public static final String STATUS_ERROR = "500 Internal Server Error";

	/**
	 * Sets the response status, for example, Response.STATUS_OK.
	 * @param status the response status
	 */
	public void setStatus(String status);

	/**
	 * Sets the error status, for example, Response.STATUS_ERROR.
	 * It also sets the error message as the response body.
	 * @param status the response status
	 * @param exception the exception or null
	 */
	public void setError(String status, Exception exception);

	/**
	 * Sets a response header with the given name and value.
	 * If the header had already been set, the new value overwrites the existing one.
	 * @param name the name of the header
	 * @param value the header value
	 */
	public void setHeader(String name, String value);

	/**
	 * Prints a string to the response body.
	 * @param str the string to be printed to the response body
	 */
	public void print(String str);

	/**
	 * Prints a string followed by a newline to the response body.
	 * @param str the string to be printed to the response body
	 */
	public void println(String str);

	/**
	 * Prints a newline to the response body.
	 */
	public void println();

	/**
	 * Sets the response body as an array of bytes.
	 * @param bytes the array of bytes to be used as the response body
	 */
	public void setBytes(byte[] bytes);
}
