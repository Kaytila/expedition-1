package net.ck.expedition.model.test;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;
import javax.swing.Popup;
import org.apache.log4j.Logger;

public class MessagePopup extends Popup implements WindowFocusListener
{
	final static Logger logger = Logger.getRootLogger();

	private final JWindow dialog;

	public MessagePopup(Frame base, JLabel component, int x, int y)
	{
		super();
		dialog = new JWindow(base);
		dialog.setFocusable(true);
		dialog.setLocation(x, y);
		dialog.setContentPane(component);
		component.setBorder(new JPopupMenu().getBorder());
		dialog.setSize(component.getPreferredSize());
		dialog.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					dialog.setVisible(false);
				}
			}
		});
	}

	@Override
	public void windowGainedFocus(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowLostFocus(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}
}