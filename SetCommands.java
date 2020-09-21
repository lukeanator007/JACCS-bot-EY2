package jaccsbot.jaccsbot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class SetCommands extends Command{

	SetCommands()
	{
		this.name="setcommands";
		this.help="Sets the given command(s) to be your default commands, as if you typed the given command at the start"
				+ " of each message. Use j!commandhelp for more information on commands. These commands will be reset when "
				+ "the bot resets, so you may have to re-input them. To remove your default commands use j!delcommands or "
				+ "give no arguments when using this command.";
		this.arguments="<commands:[$$command]>";
		this.aliases= new String[] {"defaultcommands", "removecommands", "delcommands"};
		this.category=new Command.Category("Commands");
	}
	
	
	@Override
	protected void execute(CommandEvent event) {
		
		if(event.getArgs().isBlank()) 
		{
			JaccsCommandHandler.userCommands.remove(event.getAuthor().getIdLong());
			event.reply("Your default commands have been removed");
		}
		else 
		{
			try {
				JaccsCommandHandler.userCommands.put(event.getAuthor().getIdLong(), new JaccsCommandHandler(event.getArgs()));
			} catch (JaccsCommandException e) {
				event.reply(e.getMessage());
				return;
			}
			event.reply("Your default commands have been set to "+event.getArgs());
		}
		
		
	}

}
