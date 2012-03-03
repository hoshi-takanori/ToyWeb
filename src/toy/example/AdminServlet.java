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
		response.setStatus(shutdown ? ToyResponse.STATUS_SHUTDOWN : Response.STATUS_OK);
		response.setHeader("Content-Type", "text/html");
		response.println("<html>");
		response.println("<head>");
		response.println("<title>" + getName() + "</title>");
		response.println("</head>");
		response.println("<body>");
		response.println("<h1>" + getName() + "</h1>");
		if (shutdown) {
			response.println("<p>Shutting down...</p>");
		} else {
			response.println("<p>Servlets are:</p>");
			response.println("<ul>");
			for (Servlet servlet : ToyContainer.getServlets()) {
				response.println("<li>" + servlet.getName());
			}
			response.println("</ul>");
			response.println("<form method=\"POST\" action=\"" + request.getPath() + "\">");
			response.println("<input type=\"hidden\" name=\"shutdown\" value=\"yes\">");
			response.println("<input type=\"submit\" value=\"Shutdown\">");
			response.println("</form>");
		}
		response.println("</body>");
		response.println("</html>");
	}
}
