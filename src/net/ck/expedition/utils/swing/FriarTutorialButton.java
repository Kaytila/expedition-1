package net.ck.expedition.utils.swing;

import java.awt.Cursor;
import java.awt.Image;
import javax.swing.JButton;
import org.apache.log4j.Logger;

/**
 * 
 * si.drawImage(getMapLayer(), layout.POS_FRIAR_TUTORIAL.x,
 * layout.POS_FRIAR_TUTORIAL.y,
 * ((GFXAppearance)AppearanceFactory.getAppearanceFactory().getAppearance(
 * "DOMINIK")).getImage());
 *
 * @author Claus
 *
 */
public class FriarTutorialButton extends JButton
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getRootLogger();

	public FriarTutorialButton(Image background, Image hover, Cursor c, String text)
	{
		setBackground(background);
		if (background != null)
			setSize(background.getWidth(null), background.getHeight(null));

		setPreferredSize(getSize());
		setHover(hover);

		setCursor(c);
		setText(text);
		clean();
	}

	private void clean()
	{
		// TODO Auto-generated method stub

	}

	private void setHover(Image hover)
	{
		// TODO Auto-generated method stub

	}

	private void setBackground(Image background)
	{
		// TODO Auto-generated method stub

	}
}
