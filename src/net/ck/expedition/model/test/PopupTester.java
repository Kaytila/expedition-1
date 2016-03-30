package net.ck.expedition.model.test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PopupTester extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static class MessagePopup extends Popup implements WindowFocusListener
	{
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
		public void show()
		{
			dialog.addWindowFocusListener(this);
			dialog.setVisible(true);
		}

		@Override
		public void hide()
		{
			dialog.setVisible(false);
			dialog.removeWindowFocusListener(this);
		}

		public void windowGainedFocus(WindowEvent e)
		{
			// NO-OP
		}

		public void windowLostFocus(WindowEvent e)
		{
			hide();
		}
	}

	public static void main(String[] args)
	{
		final PopupTester popupTester = new PopupTester();
		popupTester.setLayout(new FlowLayout());
		popupTester.setSize(300, 100);
		popupTester.add(new JButton("Click Me")
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void fireActionPerformed(ActionEvent event)
			{
				Point location = getLocationOnScreen();
				int x = (int) location.getX();
				int y = (int) (location.getY() + getHeight());
				JLabel myComponent = new JLabel("Howdy");

				MessagePopup popup = new MessagePopup(popupTester, myComponent, x, y);
				popup.show();
			}
		});
		popupTester.add(new JButton("No Click Me"));
		popupTester.setVisible(true);
		popupTester.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}