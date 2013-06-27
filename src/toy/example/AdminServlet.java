package toy.example;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import toy.servlet.Request;
import toy.servlet.Response;
import toy.servlet.Servlet;
import toy.web.ToyEngine;
import toy.web.ToyResponse;

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
	 * Returns the default path of the servlet.
	 * @param servlet the servlet
	 * @return the default path, or null if not found
	 */
	public String getServletPath(Servlet servlet) {
		Class<? extends Servlet> cls = servlet.getClass();
		try {
			Method method = cls.getMethod("getDefaultPath");
			return (String) method.invoke(servlet);
		} catch (Exception e) {
		}
		try {
			Field field = cls.getField("PATH");
			return (String) field.get(null);
		} catch (Exception e) {
		}
		return null;
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
			for (Servlet servlet : ToyEngine.getInstance().getServlets()) {
				String path = getServletPath(servlet);
				if (path != null) {
					view.printTag("li", view.linkTag(servlet.getName(), path));
				} else {
					view.printTag("li", servlet.getName());
				}
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
