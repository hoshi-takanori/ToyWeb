package toy.web;

import toy.servlet.Servlet;

/**
 * Request, response, and servlet.
 */
public class ToyTransaction {
	/**
	 * The request.
	 */
	private ToyRequest request;

	/**
	 * The response.
	 */
	private ToyResponse response;

	/**
	 * The servlet.
	 */
	private Servlet servlet;

	/**
	 * Constructor.
	 * @param request the request
	 * @param response the response
	 * @param servlet the servlet
	 */
	public ToyTransaction(ToyRequest request, ToyResponse response, Servlet servlet) {
		this.request = request;
		this.response = response;
		this.servlet = servlet;
	}

	/**
	 * Returns the request.
	 * @return the request
	 */
	public ToyRequest getRequest() {
		return request;
	}

	/**
	 * Returns the response.
	 * @return the response
	 */
	public ToyResponse getResponse() {
		return response;
	}

	/**
	 * Returns the servlet.
	 * @return the servlet
	 */
	public Servlet getServlet() {
		return servlet;
	}
}
