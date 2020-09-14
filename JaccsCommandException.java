package jaccsbot.jaccsbot;

@SuppressWarnings("serial")
public class JaccsCommandException extends Exception{

	JaccsCommandException()
	{
		super("The JACCS command was inputted incorrectly");
	}
	
	JaccsCommandException(String msg)
	{
		super(msg);
	}
}
