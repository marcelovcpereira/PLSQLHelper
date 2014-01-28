package basicas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

public class MyCanvas extends JComponent implements Scrollable
{
	public Image _drawSpace;
	public Graphics _drawSpaceG;

	/**
	 * MyCanvas does not support scrolling directly. To create a scrolling
	 * list, you make the MyCanvas the viewport view of a JScrollPane
	 *
	 * &lt;pre&gt;
	 * JScrollPane scrollPane = nw JScrollPane(canvas)
	 * // or in two steps
	 *
	 * JScrollPane scrollPane = new JScrollPane();
	 * scrollPane.getViewport().setView(canvas);
	 * &lt;/pre&gt;
	 *
	 */
	public MyCanvas()
	{
		super();
		this.setSize(1000,1000);
	}
	/**
	 * Overload of getPreferredSize() for access by JScrollPane.... this is
	 * the size of the viewport. It should be calculated as a multiple of
	 * a height of the components inside the components. For example a component
	 * displaying text should have the preferred size as a multiple of the font
	 * size....
	 *
	 * In this case, as a graphics component, it is just the component size.
	 *
	 */
	public Dimension getPreferredSize()
	{
		return new Dimension(this.getSize().width,this.getSize().height);
	}
	/**
	 * Overload of the paint routine.
	 */
	public void paint(Graphics g)
	{
		int iWidth = this.getSize().width;
		int iHeight = this.getSize().height;
		_drawSpace = createImage(this.getSize().width, this.getSize().height);
		_drawSpaceG = _drawSpace.getGraphics();
		_drawSpaceG.setColor(Color.black);
		/*
		_drawSpaceG.fillRect(0, 0, iWidth, iHeight);

		_drawSpaceG.setColor(Color.white);

		_drawSpaceG.drawLine(0,0,1000, 1000);
		_drawSpaceG.drawLine(0,1000, 500, 0);

		g.drawImage(_drawSpace, 0, 0, Color.black, this);*/
	}

	/**
	 * Components that display logical rows or columns shouls compute the scroll increment that
	 * will completely expos one block of rows or columns depending upon the orientation.
	 *
	 * Graphics components should compute how much of the image one wants to scroll each time
	 * the view port is moved by a Unit Increment. In this case, it is a percentage (10%) of
	 * the overall image.
	 *
	 * Scrolling containers like JScrollPane will use this methode each time the user requests
	 * a block scroll.
	 *
	 * This value comes into play when one clicks the scroll bar in the area above or below
	 * the slider. The JScrollPane then moves the view to the next visible area.
	 *
	 * @param &lt;b&gt;visibleRect&lt;/b&gt; - The view area visible within the viewport.
	 * @param &lt;b&gt;orientation&lt;/b&gt; - Either &lt;b&gt;SwingConstants.VERTICAL&lt;/b&gt; or &lt;b&gt;SwingConstants.HORIZONTAL&lt;/b&gt;
	 * @param &lt;b&gt;direction&lt;/b&gt; - If less than zero scroll up/left. If greater than zero scroll down/right.
	 * @return The "Block" increment for scrolling in a specific direction.
	 *
	 */
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
	{
		return (orientation == SwingConstants.VERTICAL) ? visibleRect.height/10 : visibleRect.width/10;
	}
	/**
	 * Components that display logical rows or columns should compute the scroll increment that
	 * will completely expos one block of rows or columns depending upon the orientation.
	 *
	 * Graphics components should compute how much of the image one wants to scroll each time
	 * the view port is moved by a Unit Increment. In this case, it is a percentage (10%) of
	 * the overall image.
	 *
	 * Scrolling containers like JScrollPane will use this method each time the user requests
	 * a unit scroll.
	 *
	 * @param &lt;b&gt;visibleRect&lt;/b&gt; - The view area visible within the viewport.
	 * @param &lt;b&gt;orientation&lt;/b&gt; - Either &lt;b&gt;SwingConstants.VERTICAL&lt;/b&gt; or &lt;b&gt;SwingConstants.HORIZONTAL&lt;/b&gt;
	 * @param &lt;b&gt;direction&lt;/b&gt; - If less than zero scroll up/left. If greater than zero scroll down/right.
	 * @return The "Block" increment for scrolling in a specific direction.
	 *
	 */
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
	{

		return (orientation == SwingConstants.VERTICAL) ? visibleRect.height/10 : visibleRect.width/10;
	}
	/**
	 * Returns the preferred size of the viewport for a view component. This is the size of it requires
	 * to display the entire size of the component. A component without any properties that would
	 * effect the viewport size should return getPreferredSize() here.
	 */
	public Dimension getPreferredScrollableViewportSize()
	{
		return this.getPreferredSize();
	}
	/**
	 * Returns true if a viewport should always force the width of this Scrollable to
	 * match the width of the viewport. For example a normal text view that supported
	 * line wrapping would return a true here, since it would be undesirable for wrapped
	 * lines to dissappear beyond the right edge of the viewport. Not that returning
	 * a true for a Scrollable whose ancestor is a JScrollPane effectively disables
	 * horizontal scrolling!!!!
	 *
	 */
	public boolean getScrollableTracksViewportWidth()
	{
		return false;
	}
	/**
	 * Returns true if a viewport should always force the height of this Scrollable to
	 * match the width of the viewport. For example a normal text view that supported
	 * columnar text that flowed text in left to right columns would return a true here,
	 * to effectively disable vertical scrolling.
	 *
	 * Returing a true for a Scrollable whose ancestor is a JScrollPane effectively disables
	 * vertical scrolling!!!!
	 */
	public boolean getScrollableTracksViewportHeight()
	{
		return false;
	}
}