package net.ck.expedition.model.test;
import org.apache.log4j.Logger;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;

public class PopUpListener extends MouseAdapter
{
	final static Logger logger = Logger.getRootLogger();
	private JPopupMenu popup;
	
	public void mousePressed(MouseEvent e) {
	    maybeShowPopup(e);
	}

	public void mouseReleased(MouseEvent e) {
	    maybeShowPopup(e);
	}

	private void maybeShowPopup(MouseEvent e) {
	    if (e.isPopupTrigger()) {
	        popup.show(e.getComponent(),
	                   e.getX(), e.getY());
	    }
	}

	public PopUpListener(JPopupMenu popup)
	{
		super();
		this.popup = popup;		
		// TODO Auto-generated constructor stub
	}
	
}

