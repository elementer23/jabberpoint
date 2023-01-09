import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.font.TextAttribute;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.text.AttributedString;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/** <p>A text item.</p>
 * <p>A text item has drawing capabilities.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class TextItem extends SlideItem {
	private String text;

//A textitem of int level with text string
	public TextItem(int level, String text) {
		super(level);
		setText(text);
	}

	public void setText(String text) {
		this.text = text;
	}

//Returns the text
	public String getText() {
		return text == null ? "" : text;
	}

//Returns the AttributedString for the Item
	public AttributedString getAttributedString(Style style, float scale) {
		AttributedString attrStr = new AttributedString(getText());
		attrStr.addAttribute(TextAttribute.FONT, style.getFont(scale), 0, getText().length());

		return attrStr;
	}

	private List<TextLayout> getLayouts(Graphics g, Style s, float scale) {

		List<TextLayout> layouts = new ArrayList<>();

		FontRenderContext frc = ((Graphics2D) g).getFontRenderContext();

		LineBreakMeasurer measurer = new LineBreakMeasurer(getAttributedString(s, scale).getIterator(), frc);

		TextLayout layout = measurer.nextLayout(setWrappingWidth(s, scale));

		layouts.add(layout);

		return layouts;
	}

	private float setWrappingWidth(Style s, float scale) {
		return (Slide.WIDTH - s.indent) * scale;
	}

//Returns the bounding box of an Item
	public Rectangle getBoundingBox(Graphics g, ImageObserver observer, 
			float scale, Style myStyle) {

		List<TextLayout> layouts = getLayouts(g, myStyle, scale);

		int width = 0;
		int height = (int) (myStyle.leading * scale);
		int xCoordinate = (int) (myStyle.leading * scale);

		for (TextLayout layout : layouts) {
			Rectangle2D bounds = layout.getBounds();

			if (bounds.getWidth() > width) {
				width = (int) bounds.getWidth();
			}

			if (bounds.getHeight() > 0) {
				height += bounds.getHeight();
			}

			height += layout.getLeading() + layout.getDescent();
		}
		return new Rectangle(xCoordinate, 0, width, height );
	}

//Draws the item
	public void draw(int x, int y, float scale, Graphics g, 
			Style myStyle, ImageObserver o) {
		if (getText() == null || getText().length() == 0) {
			return;
		}
		List<TextLayout> layouts = getLayouts(g, myStyle, scale);
		Point pen = new Point(x + (int)(myStyle.indent * scale), 
				y + (int) (myStyle.leading * scale));
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(myStyle.color);
		Iterator<TextLayout> it = layouts.iterator();
		while (it.hasNext()) {
			TextLayout layout = it.next();
			pen.y += layout.getAscent();
			layout.draw(g2d, pen.x, pen.y);
			pen.y += layout.getDescent();
		}
	  }

	public String toString() {
		return "TextItem[" + getLevel()+","+getText()+"]";
	}
}
