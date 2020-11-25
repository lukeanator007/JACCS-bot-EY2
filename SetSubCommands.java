package jaccsbot.jaccsbot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class SetSubCommands extends Command{

	SetSubCommands()
	{
		this.name="setsubcommands";
		this.help="Sets the given subcommand(s) to be your default subcommands, as if you typed the given subcommand at the start"
				+ " of each message. Use j!subcommandhelp for more information on subcommands. These commands will be reset when "
				+ "the bot resets, so you may have to re-input them. To remove your default commands use j!delsubcommands or "
				+ "give no arguments when using this command.";
		this.arguments="<commands:[$$command]>";
		this.aliases= new String[] {"defaultcommands", "removesubcommands", "delsubcommands"};
		this.category=new Command.Category("Commands");
	}
	
	
	@Override
	protected void execute(CommandEvent event) {
		
		if(event.getArgs().isBlank()) 
		{
			JaccsCommandHandler.userCommands.remove(event.getAuthor().getIdLong());
			event.reply("Your default subcommands have been removed");
		}
		else 
		{
			try {
				JaccsCommandHandler.userCommands.put(event.getAuthor().getIdLong(), new JaccsCommandHandler(event.getArgs()));
			} catch (JaccsCommandException e) {
				event.reply(e.getMessage());
				return;
			}
			event.reply("Your default subcommands have been set to "+event.getArgs());
		}
		
		
	}

}
