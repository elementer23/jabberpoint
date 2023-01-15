import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JFrame;


/** <p>SlideViewerComponent is a graphical component that ca display Slides.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class SlideViewerComponent extends JComponent {
		
	private Slide slide; //The current slide
	private Font labelFont = null; //The font for labels
	private Presentation presentation = null; //The presentation
	private JFrame frame = null;
	
	private static final long serialVersionUID = 227L;

	public SlideViewerComponent(Presentation presentation, JFrame frame) {
		setBackground(ComponentCode.BGCOLOR);
		setPresentation(presentation);
		labelFont = new Font(ComponentCode.FONTNAME, ComponentCode.FONTSTYLE, ComponentCode.FONTHEIGHT);
		this.frame = frame;
	}

	public void setPresentation(Presentation presentation) {
		this.presentation = presentation;
	}

	public Presentation getPresentation() {
		return this.presentation;
	}

	public Dimension getPreferredSize() {
		return new Dimension(Slide.WIDTH, Slide.HEIGHT);
	}

	public void update(Presentation presentation, Slide data) {
		if (data == null) {
			repaint();
			return;
		}
		setPresentation(presentation);
		this.slide = data;
		repaint();
		frame.setTitle(presentation.getTitle());
	}

//Draw the slide
	public void paintComponent(Graphics g) {
		g.setColor(ComponentCode.BGCOLOR);
		g.fillRect(0, 0, getSize().width, getSize().height);
		if (getPresentation().getSlideNumber() < 0 || slide == null) {
			return;
		}
		g.setFont(labelFont);
		g.setColor(ComponentCode.COLOR);
		g.drawString("Slide " + (1 + getPresentation().getSlideNumber()) + " of " +
                 presentation.getSize(), ComponentCode.XPOS, ComponentCode.YPOS);
		Rectangle area = new Rectangle(0, ComponentCode.YPOS, getWidth(), (getHeight() - ComponentCode.YPOS));
		slide.draw(g, area, this);
	}
}
