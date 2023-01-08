import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;

import java.io.IOException;


/** <p>The class for a Bitmap item</p>
 * <p>Bitmap items are responsible for drawing themselves.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
*/

public class BitmapItem extends SlideItem {
  private BufferedImage bufferedImage;
  private String imageName;
  
  protected static final String FILE = "File ";
  protected static final String NOTFOUND = " not found";


  	//level indicates the item-level; name indicates the name of the file with the image
	public BitmapItem(int level, String imageName) {
		super(level);
		setImageName(imageName);
		setBufferedImage();
	}

	public void setImageName(String name) {
		this.imageName = name;
	}

	//Returns the filename of the image
	public String getImageName() {
		return this.imageName;
	}

	public void setBufferedImage() {
		try {
			this.bufferedImage = ImageIO.read(new File(getImageName()));
		} catch (IOException e) {
			System.err.println(FILE + imageName + NOTFOUND);
		}
	}

	public BufferedImage getBufferedImage() {
		return this.bufferedImage;
	}

	//Returns the bounding box of the image
	public Rectangle getBoundingBox(Graphics g, ImageObserver observer, float scale, Style myStyle) {
		return new Rectangle(
							(int) (myStyle.indent * scale),
							0,
							(int) (getBufferedImage().getWidth(observer) * scale),
							((int) (myStyle.leading * scale)) +
							(int) (getBufferedImage().getHeight(observer) * scale));
	}

	//Draws the image
	public void draw(int x, int y, float scale, Graphics g, Style myStyle, ImageObserver observer) {
		g.drawImage(getBufferedImage(),
					calculateImageWidth(x, myStyle, scale),
					calculateImageHeight(y, myStyle, scale),
					(int) (getBufferedImage().getWidth(observer)*scale),
                	(int) (getBufferedImage().getHeight(observer)*scale),
					observer);
	}

	public int calculateImageWidth(int xAs, Style widthStyling, float widthScale) {
		return xAs + (int) (widthStyling.indent * widthScale);
	}

	public int calculateImageHeight(int yAs, Style heightStyling, float heightScale) {
		return yAs + (int) (heightStyling.indent * heightScale);
	}

}
