package toy.viewer;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import toy.web.ToyTransaction;

/**
 * Visualizes the request/response transaction.
 */
public class ToyViewer implements Observer {
	public static final int WIDTH = 400;
	public static final int HEIGHT = 600;

	public static final int SCROLL_INTERVAL = 20;
	public static final int SCROLL_DELTA = 10;

	public static final int CLOSE_DELAY = 200;

	private JFrame frame;
	private JPanel panel;
	private JScrollPane scrollPane;

	private Timer scrollTimer;
	private int lastY;

	private Timer closeTimer;
	private boolean openCell;

	/**
	 * Constructs a toy viewer.
	 */
	public ToyViewer() {
		frame = new JFrame("ToyViewer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel(new ListLayout());
		scrollPane = new JScrollPane(panel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		frame.getContentPane().add(scrollPane);

		scrollPane.getViewport().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				int y = scrollPane.getViewport().getViewPosition().y;
				if (y < lastY) {
					scrollTimer.stop();
				}
			}
		});

		scrollTimer = new Timer(SCROLL_INTERVAL, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JViewport viewport = scrollPane.getViewport();
				Point pos = viewport.getViewPosition();
				Dimension size = viewport.getExtentSize();
				int y = Math.min(panel.getSize().height - size.height, pos.y + SCROLL_DELTA);
				if (y != pos.y) {
					viewport.setViewPosition(new Point(pos.x, y));
					lastY = y;
				} else {
					scrollTimer.stop();
				}
			}
		});

		closeTimer = new Timer(CLOSE_DELAY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.add(new IdleCell(false));
				panel.revalidate();
				lastY = scrollPane.getViewport().getViewPosition().y;
				scrollTimer.restart();
				openCell = false;
			}
		});
		closeTimer.setRepeats(false);
	}

	/**
	 * Opens the toy viewer.
	 */
	public void open() {
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(Observable observable, Object obj) {
		if (obj instanceof ToyTransaction) {
			final ToyTransaction transaction = (ToyTransaction) obj;
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (! openCell) {
						panel.add(new IdleCell(true));
						openCell = true;
					}
					panel.add(new TransactionCell(transaction));
					panel.revalidate();
					lastY = scrollPane.getViewport().getViewPosition().y;
					scrollTimer.restart();
					closeTimer.restart();
				}
			});
		}
	}
}
