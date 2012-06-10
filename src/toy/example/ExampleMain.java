package toy.example;

import toy.container.ToyContainer;

/**
 * Example main class to run example servlets.
 */
public class ExampleMain {
	/**
	 * Starts the toy container with example servlets.
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		ToyContainer container = ToyContainer.getInstance();
		container.addServlet("/", new AdminServlet());
		container.addServlet("/hello", new HelloServlet());
		container.start();
	}
}
