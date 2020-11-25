package jaccsbot.jaccsbot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class GetSubCommands extends Command{

	GetSubCommands()
	{
		this.name="getcommands";
		this.help="Gets your current default commands";
		this.category=new Command.Category("Commands");
	}
	
	
	@Override
	protected void execute(CommandEvent event) {
		JaccsCommandHandler temp=JaccsCommandHandler.userCommands.get(event.getAuthor().getIdLong());
		if(!(temp!=null)) 
		{
			event.reply("You have no set commands.");
			return;
		}
		event.reply(temp.toString());
	}

}
