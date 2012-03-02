package toy.servlet;

/**
 * Defines methods that all servlets must implement.
 */
public interface Servlet {
	/**
	 * Returns the name of the servlet.
	 * @return the name of the servlet
	 */
	public String getName();

	/**
	 * Process a request and set response values.
	 * @param request the request from the client (input parameter)
	 * @param response the response to be returned to the client (output parameter)
	 */
	public void service(Request request, Response response);
}
