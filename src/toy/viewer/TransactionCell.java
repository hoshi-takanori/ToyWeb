package toy.viewer;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import toy.servlet.Servlet;
import toy.web.ToyRequest;
import toy.web.ToyResponse;
import toy.web.ToyTransaction;

/**
 * List cell for the transaction.
 */
@SuppressWarnings("serial")
public class TransactionCell extends JComponent {
	public static final int HEIGHT = 80;

	public static final int MARGIN = 10;
	public static final int BAR_WIDTH = 20;
	public static final int ARROW_SIZE = 5;

	private ToyTransaction transaction;

	/**
	 * Constructs a transaction cell.
	 * @param transaction the transaction
	 */
	public TransactionCell(ToyTransaction transaction) {
		this.transaction = transaction;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(ListLayout.WIDTH, HEIGHT);
	}

	public String getRequestString() {
		ToyRequest request = transaction.getRequest();
		String str = request.getMethod() + " " + request.getPath();
		if (request.getRawParameters() != null) {
			str += ", " + request.getRawParameters();
		}
		return str;
	}

	public String getRequestCookie() {
		String str = transaction.getRequest().getHeader("cookie");
		return str == null ? "-" : "Cookie: " + str;
	}

	public String getResponseString() {
		ToyResponse response = transaction.getResponse();
		String str = response.getStatus();
		if (str == null) {
			return "?";
		}
		String location = response.getHeader("Location");
		String type = response.getHeader("Content-Type");
		if (location != null) {
			str += ", " + location;
		} else if (type != null) {
			str += " (" + type + ")";
		}
		return str;
	}

	public String getResponseCookie() {
		String str = null;
		for (String cookie : transaction.getResponse().getCookies()) {
			str = str == null ? cookie : str + ", " + cookie;
		}
		return str == null ? "-" : "Set-Cookie: " + str;
	}

	public String getServletName() {
		Servlet servlet = transaction.getServlet();
		return servlet != null ? servlet.getName() : "?";
	}

	public void drawString(Graphics g, FontMetrics metrics, String str, int center, int width, int y) {
		int fw = metrics.stringWidth(str);
		if (fw > width) {
			width = Math.max(width - metrics.stringWidth("..."), 0);
			do {
				str = str.substring(0, str.length() - 1);
			} while (metrics.stringWidth(str) > width);
			str += "...";
			fw = metrics.stringWidth(str);
		}
		g.drawString(str, center - fw / 2, y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paintComponent(Graphics g) {
		Dimension size = getSize();

		g.drawLine(MARGIN, 0, MARGIN, size.height);
		g.drawLine(MARGIN + BAR_WIDTH, 0, MARGIN + BAR_WIDTH, size.height);

		int x1 = MARGIN + BAR_WIDTH;
		int x2 = size.width - x1;
		int y1 = size.height / 4;
		g.drawLine(x1, y1, x2, y1);
		g.drawLine(x2, y1, x2 - ARROW_SIZE, y1 - ARROW_SIZE);
		g.drawLine(x2, y1, x2 - ARROW_SIZE, y1 + ARROW_SIZE);

		int y2 = size.height - y1;
		g.drawLine(x1, y2, x2, y2);
		g.drawLine(x1, y2, x1 + ARROW_SIZE, y2 - ARROW_SIZE);
		g.drawLine(x1, y2, x1 + ARROW_SIZE, y2 + ARROW_SIZE);

		int x3 = size.width - MARGIN - BAR_WIDTH / 2;
		g.drawLine(x3, 0, x3, MARGIN);
		g.drawRect(x2, MARGIN, BAR_WIDTH, size.height - MARGIN * 2);
		g.drawLine(x3, size.height - MARGIN, x3, size.height);

		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int fh = metrics.getHeight();
		int fd = metrics.getDescent();
		int center = (x1 + x2) / 2;
		int width = x2 - x1 - 12;
		drawString(g, metrics, getRequestString(), center, width, y1 - fd - 1);
		drawString(g, metrics, getRequestCookie(), center, width, y1 + fh);
		drawString(g, metrics, getResponseString(), center, width, y2 - fd - 1);
		drawString(g, metrics, getResponseCookie(), center, width, y2 + fh);

		int y3 = size.height / 2;
		width = size.height - MARGIN * 2 - 4;
		((Graphics2D) g).rotate(Math.PI / 2, x3, y3);
		drawString(g, metrics, getServletName(), x3, width, y3 + (fh - fd) / 2 - 2);
	}
}
