package toy.viewer;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * List cell for the idle period.
 */
@SuppressWarnings("serial")
public class IdleCell extends JComponent {
	public static final int HEIGHT = 10;

	public static final int MARGIN = 10;
	public static final int BAR_WIDTH = 20;

	private boolean openFlag;

	/**
	 * Constructs an idle cell.
	 * @param openFlag true if open, false otherwise
	 */
	public IdleCell(boolean openFlag) {
		this.openFlag = openFlag;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(ListLayout.WIDTH, HEIGHT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paintComponent(Graphics g) {
		Dimension size = getSize();
		int x = MARGIN + BAR_WIDTH / 2;
		g.drawLine(x, 0, x, size.height);
		x = size.width - x;
		g.drawLine(x, 0, x, size.height);
		int y = openFlag ? size.height - 1 : 0;
		g.drawLine(MARGIN, y, MARGIN + BAR_WIDTH, y);
	}
}
