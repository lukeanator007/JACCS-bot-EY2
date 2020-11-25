package jaccsbot.jaccsbot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Invite extends Command{

	Invite()
	{
		this.guildOnly=false;
		this.name="invite";
		this.help="gets a link to invite the bot to your own server!";
	}
	
	
	@Override
	protected void execute(CommandEvent event) {
		event.reply("https://discord.com/api/oauth2/authorize?client_id=587601916591210497&permissions=109632&scope=bot");
		
	}

}
