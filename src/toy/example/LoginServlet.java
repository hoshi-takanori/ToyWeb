package toy.example;

import toy.servlet.Request;
import toy.servlet.Response;
import toy.servlet.Servlet;

/**
 * Example servlet to implement login and logout.
 */
public class LoginServlet implements Servlet {
	/**
	 * The servlet path.
	 */
	public static final String PATH = "/login";

	/**
	 * The cookie name for login.
	 */
	public static final String LOGIN_COOKIE = "LOGIN";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "Login Servlet";
	}

	/**
	 * Prints the login screen.
	 * @param request the request (input parameter)
	 * @param response the response (output parameter)
	 */
	public void printLoginForm(Request request, Response response) {
		BasicView view = new BasicView(response);
		view.printHead(getName());
		view.printOpenTag("form", "method", "POST", "action", PATH);
		view.printInputTag("hidden", "mode", "login");
		response.print("Name: ");
		view.printInputTag("text", "login", null);
		view.printInputTag("submit", null, "Login");
		view.printCloseTag("form");
		view.printTail();
	}

	/**
	 * Prints the logout screen.
	 * @param request the request (input parameter)
	 * @param response the response (output parameter)
	 */
	public void printMenuForm(Request request, Response response) {
		String login = request.getCookie(LOGIN_COOKIE);
		BasicView view = new BasicView(response);
		view.printHead(getName());
		view.printTag("p", "Welcome, " + login + "!");
		view.printOpenTag("form", "method", "POST", "action", PATH);
		view.printInputTag("hidden", "mode", "logout");
		view.printInputTag("submit", null, "Logout");
		view.printCloseTag("form");
		view.printTail();
	}

	/**
	 * Handle the login procedure.
	 * @param request the request (input parameter)
	 * @param response the response (output parameter)
	 */
	public void handleLogin(Request request, Response response) {
		String login = request.getDecodedParameter("login");
		if (login != null && login.length() > 0) {
			response.addCookie(LOGIN_COOKIE + "=" + login);
			response.setRedirect(PATH);
		} else {
			response.setError(Response.STATUS_ERROR, "failed to log in");
		}
	}

	/**
	 * Handle the logout procedure.
	 * @param request the request (input parameter)
	 * @param response the response (output parameter)
	 */
	public void handleLogout(Request request, Response response) {
		response.addCookie(LOGIN_COOKIE + "=; expires=Thu, 1-Jan-1970 00:00:00 GMT");
		response.setRedirect(PATH);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void service(Request request, Response response) {
		String mode = request.getParameter("mode");
		if (mode == null) {
			String login = request.getCookie(LOGIN_COOKIE);
			if (login != null && login.length() > 0) {
				printMenuForm(request, response);
			} else {
				printLoginForm(request, response);
			}
		} else if (mode.equals("login")) {
			handleLogin(request, response);
		} else if (mode.equals("logout")) {
			handleLogout(request, response);
		} else {
			response.setError(Response.STATUS_ERROR, "unknown mode");
		}
	}
}
