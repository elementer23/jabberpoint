import java.awt.MenuBar;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.io.IOException;

import javax.swing.JOptionPane;

/** <p>The controller for the menu</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */
public class MenuController extends MenuBar {
	
	private Frame parentFrame; //The frame, only used as parent for the Dialogs
	private Presentation presentation; //Commands are given to the presentation
	
	private static final long serialVersionUID = 227L;

	public MenuController(Frame parentFrame, Presentation presentation) {
		setParentFrame(parentFrame);

		setPresentation(presentation);

		createFileMenu();
		createViewMenu();
		createHelpMenu();

	}

	public void setParentFrame(Frame parentFrame) {
		this.parentFrame = parentFrame;
	}

	public Frame getParentFrame() {
		return this.parentFrame;
	}

	public void setPresentation(Presentation presentation) {
		this.presentation = presentation;
	}

	public Presentation getPresentation() {
		return this.presentation;
	}

	public void createFileMenu() {
		MenuItem menuItem;

		Menu fileMenu = new Menu(MenuCode.FILE);

		fileMenu.add(menuItem = mkMenuItem(MenuCode.OPEN));
		menuItem.addActionListener(actionEvent -> openTestPresentation());

		fileMenu.add(menuItem = mkMenuItem(MenuCode.NEW));
		menuItem.addActionListener(actionEvent -> openNewPresentation());

		fileMenu.add(menuItem = mkMenuItem(MenuCode.SAVE));
		menuItem.addActionListener(actionEvent -> savePresentation());

		fileMenu.addSeparator();

		fileMenu.add(menuItem = mkMenuItem(MenuCode.EXIT));
		menuItem.addActionListener(actionEvent -> getPresentation().exit(0));

		add(fileMenu);
	}

	public void openTestPresentation() {

		getPresentation().clear();

		Accessor xmlAccessor = new XMLAccessor();
		try {
			xmlAccessor.loadFile(getPresentation(), MenuCode.TESTFILE);
			getPresentation().setSlideNumber(0);
		} catch (IOException exc) {
			JOptionPane.showMessageDialog(getParentFrame(), MenuCode.IOEX + exc,
					MenuCode.LOADERR, JOptionPane.ERROR_MESSAGE);
		}

		getParentFrame().repaint();

	}

	public void openNewPresentation() {
		getPresentation().clear();
		getParentFrame().repaint();
	}

	public void savePresentation() {
		Accessor xmlAccessor = new XMLAccessor();

		try {
			xmlAccessor.saveFile(getPresentation(), MenuCode.SAVEFILE);
		} catch (IOException exc) {
			JOptionPane.showMessageDialog(getParentFrame(), MenuCode.IOEX + exc,
					MenuCode.SAVEERR, JOptionPane.ERROR_MESSAGE);
		}
	}

	public void createViewMenu() {
		MenuItem menuItem;

		Menu viewMenu = new Menu(MenuCode.VIEW);

		viewMenu.add(menuItem = mkMenuItem(MenuCode.NEXT));
		menuItem.addActionListener(actionEvent -> getPresentation().nextSlide());

		viewMenu.add(menuItem = mkMenuItem(MenuCode.PREV));
		menuItem.addActionListener(actionEvent -> getPresentation().prevSlide());

		viewMenu.add(menuItem = mkMenuItem(MenuCode.GOTO));
		menuItem.addActionListener(actionEvent -> goToPageNumber());

		add(viewMenu);
	}

	public void goToPageNumber() {

		String pageNumberStr = JOptionPane.showInputDialog(MenuCode.PAGENR);

		if (pageNumberStr == null) {
			return;
		}

		if (!pageNumberStr.isEmpty()) {
			int pageNumber = Integer.parseInt(pageNumberStr);
			getPresentation().setSlideNumber(pageNumber - 1);
		}
	}

	public void createHelpMenu() {
		MenuItem menuItem;

		Menu helpMenu = new Menu(MenuCode.HELP);

		helpMenu.add(menuItem = mkMenuItem(MenuCode.ABOUT));
		menuItem.addActionListener(actionEvent -> AboutBox.show(getParentFrame()));

		setHelpMenu(helpMenu);	//Needed for portability (Motif, etc.).
	}

//Creating a menu-item
	public MenuItem mkMenuItem(String name) {
		return new MenuItem(name, new MenuShortcut(name.charAt(0)));
	}
}
