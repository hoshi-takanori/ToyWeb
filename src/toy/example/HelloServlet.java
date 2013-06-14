package toy.example;

import toy.servlet.Request;
import toy.servlet.Response;
import toy.servlet.Servlet;

/**
 * Example servlet to print "Hello, World."
 */
public class HelloServlet implements Servlet {
	/**
	 * The servlet path.
	 */
	public static final String PATH = "/hello";

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
		BasicView view = new BasicView(response);
		view.printHead("Hello, World!");
		view.printOpenTag("p");
		response.println("method = " + request.getMethod() + view.openTag("br"));
		response.println("path = " + request.getPath());
		view.printCloseTag("p");
		if (! request.getParameterNames().isEmpty()) {
			view.printTag("p", "Parameters are:");
			view.printOpenTag("ul");
			for (String name : request.getParameterNames()) {
				view.printTag("li", name + " = " + request.getParameter(name));
			}
			view.printCloseTag("ul");
		}
		if (! request.getHeaderNames().isEmpty()) {
			view.printTag("p", "Headers are:");
			view.printOpenTag("ul");
			for (String name : request.getHeaderNames()) {
				view.printTag("li", name + " = " + request.getHeader(name));
			}
			view.printCloseTag("ul");
		}
		if (! request.getCookieNames().isEmpty()) {
			view.printTag("p", "Cookies are:");
			view.printOpenTag("ul");
			for (String name : request.getCookieNames()) {
				view.printTag("li", name + " = " + request.getCookie(name));
			}
			view.printCloseTag("ul");
		}
		view.printTail();
	}
}
