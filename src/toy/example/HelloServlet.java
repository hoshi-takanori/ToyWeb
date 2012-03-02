package toy.example;

import toy.servlet.Request;
import toy.servlet.Response;
import toy.servlet.Servlet;

/**
 * Example servlet to print "Hello, World."
 */
public class HelloServlet implements Servlet {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "Hello Servlet";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void service(Request request, Response response) {
		response.setStatus(Response.STATUS_OK);
		response.setHeader("Content-Type", "text/plain");
		response.println("Hello, World.");
		response.println();
		response.println("method = " + request.getMethod());
		response.println("path = " + request.getPath());
		for (String name : request.getParameterNames()) {
			response.println("param[" + name + "] = " + request.getParameter(name));
		}
		for (String name : request.getHeaderNames()) {
			response.println("header[" + name + "] = " + request.getHeader(name));
		}
	}
}
