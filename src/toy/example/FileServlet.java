package toy.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import toy.servlet.Request;
import toy.servlet.Response;
import toy.servlet.Servlet;

/**
 * Example servlet to deliver existing files.
 */
public class FileServlet implements Servlet {
	/**
	 * Map between file extensions and file types.
	 */
	private HashMap<String, String> fileTypes;

	/**
	 * Constructs a file servlet.
	 */
	public FileServlet() {
		fileTypes = new HashMap<String, String>();
		fileTypes.put("txt", "text/plain");
		fileTypes.put("htm", "text/html");
		fileTypes.put("html", "text/html");
		fileTypes.put("css", "text/css");
		fileTypes.put("js", "text/javascript");
		fileTypes.put("xml", "text/xml");
		fileTypes.put("java", "text/plain");
		fileTypes.put("bmp", "image/bmp");
		fileTypes.put("gif", "image/gif");
		fileTypes.put("jpg", "image/jpeg");
		fileTypes.put("jpeg", "image/jpeg");
		fileTypes.put("png", "image/png");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "File Servlet";
	}

	/**
	 * Returns the file type based on the extension of the given file.
	 * @param file a file to be determined
	 * @return the file type
	 */
	public String getFileType(File file) {
		String name = file.getName();
		int index = name.lastIndexOf('.');
		if (index >= 0) {
			String ext = name.substring(index + 1);
			String type = fileTypes.get(ext);
			if (type != null) {
				return type;
			}
		}
		return "application/octet-stream";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void service(Request request, Response response) {
		File file = new File(new File("."), request.getPath());
		if (file.isDirectory()) {
			String[] list = file.list();
			response.setStatus(Response.STATUS_OK);
			response.setHeader("Content-Type", "text/plain");
			response.println(list.length + " items:");
			for (String item : list) {
				response.println(item);
			}
		} else if (file.isFile()) {
			FileInputStream stream = null;
			try {
				int size = (int) file.length();
				byte[] buffer = new byte[size];
				stream = new FileInputStream(file);
				int read = stream.read(buffer);
				if (read == size) {
					response.setStatus(Response.STATUS_OK);
					response.setHeader("Content-Type", getFileType(file));
					response.setBytes(buffer);
				} else {
					response.setError(Response.STATUS_ERROR, "read < size");
				}
			} catch (IOException exception) {
				response.setError(Response.STATUS_ERROR, exception);
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
					}
				}
			}
		} else {
			response.setError(Response.STATUS_ERROR, "no such file or directory");
		}
	}
}
