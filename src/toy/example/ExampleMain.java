package toy.example;

import toy.container.ToyContainer;

/**
 * Example main class to run example servlets.
 */
public class ExampleMain {
	/**
	 * The name of the settings file.
	 */
	public static final String SETTING_NAME = "settings";

	/**
	 * Starts the toy container with example servlets.
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		ToyContainer container = ToyContainer.createInstance(SETTING_NAME);
		container.addServlet("/", new AdminServlet());
		container.addServlet(HelloServlet.PATH, new HelloServlet());
		container.addServlet(LoginServlet.PATH, new LoginServlet());
		container.addServlet("/files/.*", new FileServlet());
		container.start();
	}
}
