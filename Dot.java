package jaccsbot.jaccsbot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Dot extends Command{

	@Override
	protected void execute(CommandEvent event) {
		String msg="";
		for (int i=0;i<200;i++) 
		{
			msg=msg+"..........";
		}
		event.reply(msg);
		
	}

	
	public Dot() 
	{
		this.name=".";
		this.hidden=true;
	}
	
}
