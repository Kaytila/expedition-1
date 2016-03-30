package net.ck.expedition.utils.swing;
import org.apache.log4j.Logger;
import net.slashie.serf.ui.UserInterface;
public class MessengerService
{
	final static Logger logger = Logger.getRootLogger();

	public MessengerService()
	{
		// TODO Auto-generated constructor stub
	}
	
	
	public static void showImportantMessage(UserInterface userInterface, String string)
	{
		if (userInterface != null)
		{
			userInterface.showImportantMessage(string);
		}
		else
		{
			logger.debug(string);
		}
	}


	public static void showImportantMessage(String message)
	{
		if (UserInterface.getUI() != null)
		{
			UserInterface.getUI().showImportantMessage(message);
		}
		else
		{
			logger.debug(message);
		}
	}
}

