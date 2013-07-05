package toy.viewer;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * Layout manager for the toy viewer.
 */
public class ListLayout implements LayoutManager {
	/**
	 * Preferred (minimum) width of the toy viewer.
	 */
	public static final int WIDTH = 200;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeLayoutComponent(Component comp) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return preferredLayoutSize(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Insets insets = parent.getInsets();
		int width = WIDTH + insets.left + insets.right;
		int height = insets.top + insets.bottom;
		for (Component comp : parent.getComponents()) {
			height += comp.getPreferredSize().height;
		}
		return new Dimension(width, height);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();
		int width = parent.getSize().width - insets.left - insets.right;
		int y = insets.top;
		for (Component comp : parent.getComponents()) {
			int height = comp.getPreferredSize().height;
			comp.setBounds(insets.left, y, width, height);
			y += height;
		}
	}
}
