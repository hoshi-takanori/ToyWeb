package toy.example;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import toy.viewer.ToyViewer;
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
		ResourceBundle settings = ResourceBundle.getBundle(SETTING_NAME);
		ToyEngine engine = ToyEngine.createInstance(settings);

		engine.addServlet("/", new AdminServlet());
		engine.addServlet(HelloServlet.PATH, new HelloServlet());
		engine.addServlet(LoginServlet.PATH, new LoginServlet());
		engine.addServlet("/files/.*", new FileServlet());

		try {
			if (Boolean.parseBoolean(settings.getString("VIEWER"))) {
				ToyViewer viewer = new ToyViewer();
				engine.addObserver(viewer);
				viewer.open();
			}
		} catch (MissingResourceException e) {
		}

		engine.start();
	}
}
