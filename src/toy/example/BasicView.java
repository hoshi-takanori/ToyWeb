package toy.example;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import toy.servlet.Response;

/**
 * Basic HTML view.
 */
public class BasicView {
	/**
	 * The response.
	 */
	protected Response response;

	/**
	 * Constructs a basic view.
	 * @param response the response
	 */
	public BasicView(Response response) {
		this.response = response;
	}

	/**
	 * Returns an HTML-escaped string.
	 * @param str the string to be HTML-escaped
	 * @param br if true, convert newline characters to <br>'s
	 * @return an HTML-escaped string
	 */
	public String htmlEscape(String str, boolean br) {
		if (str == null) {
			return null;
		}
		str = str.replace("&", "&amp;");
		str = str.replace("<", "&lt;");
		str = str.replace(">", "&gt;");
		str = str.replace("\"", "&quot;");
		if (br) {
			str = str.replace("\n", "&lt;br&gt;");
		}
		return str;
	}

	/**
	 * Returns an open tag <tag attrs[0]="attrs[1]" attrs[2]="attrs[3]" ...>.
	 * @param tag the tag
	 * @param attrs the keys and values of the tag attributes
	 * @return an open tag string
	 */
	public String openTag(String tag, String... attrs) {
		String str = "<" + tag;
		for (int i = 0; i < attrs.length; i += 2) {
			str += " " + attrs[i];
			if (i + 1 < attrs.length && attrs[i + 1] != null) {
				str += "=\"" + htmlEscape(attrs[i + 1], false) + "\"";
			}
		}
		return str + ">";
	}

	/**
	 * Returns a close tag </tag>.
	 * @param tag the tag
	 * @return a close tag string
	 */
	public String closeTag(String tag) {
		return "</" + tag + ">";
	}

	/**
	 * Returns a tagged string <tag attrs[0]="attrs[1]" attrs[2]="attrs[3]" ...>str</tag>.
	 * @param tag the tag
	 * @param str the string to be tagged
	 * @param attrs the keys and values of the tag attributes
	 * @return a tagged string
	 */
	public String taggedStr(String tag, String str, String... attrs) {
		return openTag(tag, attrs) + str + closeTag(tag);
	}

	/**
	 * Returns a link tag <a href="path?params[0]=params[1]&params[2]=params[3]...">str</a>.
	 * @param str the string to be tagged
	 * @param path the URL path
	 * @param params the keys and value of the URL parameters
	 * @return a link tag
	 */
	public String linkTag(String str, String path, String... params) {
		if (params != null && params.length > 0) {
			String sep = "?";
			for (int i = 0; i < params.length; i += 2) {
				path += sep + params[i];
				if (i + 1 < params.length) {
					try {
						path += "=" + URLEncoder.encode(params[i + 1], "UTF-8");
					} catch (UnsupportedEncodingException e) {
						path += "=" + params[i + 1];
					}
				}
				sep = "&";
			}
		}
		return taggedStr("a", str, "href", path);
	}

	/**
	 * Returns an input tag <input type="type" name="name" value="value" attrs>.
	 * @param type the type of the input tag
	 * @param name the name of the input tag
	 * @param value the value of the input tag
	 * @param attrs the additional attributes, for example, "checked"
	 * @return an input tag
	 */
	public String inputTag(String type, String name, String value, String... attrs) {
		ArrayList<String> list = new ArrayList<String>();
		list.add("type");
		list.add(type);
		if (name != null) {
			list.add("name");
			list.add(name);
		}
		if (value != null) {
			list.add("value");
			list.add(value);
		}
		for (String attr : attrs) {
			list.add(attr);
		}
		String[] array = new String[list.size()];
		list.toArray(array);
		return openTag("input", array);
	}

	/**
	 * Prints an open tag <tag attrs[0]="attrs[1]" attrs[2]="attrs[3]" ...>.
	 * @param tag the tag
	 * @param attrs the keys and values of the tag attributes
	 */
	public void printOpenTag(String tag, String... attrs) {
		response.println(openTag(tag, attrs));
	}

	/**
	 * Prints a close tag </tag>.
	 * @param tag the tag
	 */
	public void printCloseTag(String tag) {
		response.println(closeTag(tag));
	}

	/**
	 * Prints a tagged string <tag attrs[0]="attrs[1]" attrs[2]="attrs[3]" ...>str</tag>.
	 * @param tag the tag
	 * @param str the string to be tagged
	 * @param attrs the keys and values of the tag attributes
	 */
	public void printTag(String tag, String str, String... attrs) {
		response.println(taggedStr(tag, str, attrs));
	}

	/**
	 * Prints an input tag <input type="type" name="name" value="value" attrs>.
	 * @param type the type of the input tag
	 * @param name the name of the input tag
	 * @param value the value of the input tag
	 * @param attrs the additional attributes, for example, "checked"
	 */
	public void printInputTag(String type, String name, String value, String... attrs) {
		response.println(inputTag(type, name, value, attrs));
	}

	/**
	 * Prints an HTML head.
	 * @param title the page title
	 * @param tags the additional tags to be printed in the head tag
	 */
	public void printHead(String title, String... tags) {
		response.setStatus(Response.STATUS_OK);
		response.setHeader("Content-Type", "text/html; charset=UTF-8");

		printOpenTag("html");
		printOpenTag("head");
		printTag("title", title);
		for (String tag : tags) {
			response.println(tag);
		}
		printCloseTag("head");
		printOpenTag("body");
		printTag("h1", title);
	}

	/**
	 * Prints an HTML tail.
	 */
	public void printTail() {
		printCloseTag("body");
		printCloseTag("html");
	}
}
