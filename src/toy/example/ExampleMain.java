package toy.example;

import toy.web.ToyEngine;

/**
 * Example main class to run example servlets.
 */
public class ExampleMain {
	/**
	 * The name of the settings file.
	 */
	public static final String SETTING_NAME = "settings";

	/**
	 * Starts the toy engine with example servlets.
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		ToyEngine engine = ToyEngine.createInstance(SETTING_NAME);
		engine.addServlet("/", new AdminServlet());
		engine.addServlet(HelloServlet.PATH, new HelloServlet());
		engine.addServlet(LoginServlet.PATH, new LoginServlet());
		engine.addServlet("/files/.*", new FileServlet());
		engine.start();
	}
}
