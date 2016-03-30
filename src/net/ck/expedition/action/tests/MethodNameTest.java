package net.ck.expedition.action.tests;
import org.apache.log4j.Logger;

public class MethodNameTest
{
	final static Logger logger = Logger.getRootLogger();

	private static final int CLIENT_CODE_STACK_INDEX;

	static
	{
		// Finds out the index of "this code" in the returned stack trace -
		// funny but it differs in JDK 1.5 and 1.6
		int i = 0;
		for (StackTraceElement ste : Thread.currentThread().getStackTrace())
		{
			i++;
			if (ste.getClassName().equals(MethodNameTest.class.getName()))
			{
				break;
			}
		}
		CLIENT_CODE_STACK_INDEX = i;
	}

	public static void main(String[] args)
	{
		logger.debug("methodName() = " + methodName());
		logger.debug("CLIENT_CODE_STACK_INDEX = " + CLIENT_CODE_STACK_INDEX);
	}

	public static String methodName()
	{
		logger.debug("methodName() = " + methodName());
		logger.debug("CLIENT_CODE_STACK_INDEX = " + CLIENT_CODE_STACK_INDEX);
		return Thread.currentThread().getStackTrace()[CLIENT_CODE_STACK_INDEX].getMethodName();
	}
}
