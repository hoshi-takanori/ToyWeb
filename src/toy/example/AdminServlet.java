package toy.example;

import toy.container.ToyContainer;
import toy.container.ToyResponse;
import toy.servlet.Request;
import toy.servlet.Response;
import toy.servlet.Servlet;

/**
 * Example servlet for administration.
 */
public class AdminServlet implements Servlet {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "Admin Servlet";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void service(Request request, Response response) {
		String str = request.getParameter("shutdown");
		boolean shutdown = str != null && str.equals("yes");

		BasicView view = new BasicView(response);
		view.printHead(getName());
		if (shutdown) {
			response.setStatus(ToyResponse.STATUS_SHUTDOWN);
			view.printTag("p", "Shutting down...");
		} else {
			view.printTag("p", "Servlets are:");
			view.printOpenTag("ul");
			for (Servlet servlet : ToyContainer.getInstance().getServlets()) {
				view.printTag("li", servlet.getName());
			}
			view.printCloseTag("ul");
			view.printOpenTag("form", "method", "POST");
			view.printInputTag("hidden", "shutdown", "yes");
			view.printInputTag("submit", null, "Shutdown");
			view.printCloseTag("form");
		}
		view.printTail();
	}
}
