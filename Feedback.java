package jaccsbot.jaccsbot;

import java.util.HashSet;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

public class Feedback extends Command{

	private long bugChannelID;
	
	Feedback(long bugChannelID)
	{
		this.bugChannelID= bugChannelID;
		this.category=new Command.Category("Utility");
		this.name="feedback";
		this.aliases=new String[] {"bug", "reportbug", "bugreport"};
		this.help="Gives feedback to the bot creator, can be used to ask for features, report bugs or comment on anything.";
		this.guildOnly=false;
		
	}
	
	@Override
	protected void execute(CommandEvent event) {
		//stores reports by other users, if the creator calls this command it lists all reports so far. list is reset on bot updating or restarting
		
		
		event.getJDA().getTextChannelById(this.bugChannelID)
		.sendMessage(event.getArgs()+" -from "+event.getAuthor().getName()+" ID= " +event.getAuthor().getId()+" \n\n").queue();
		event.reply("Your report has been recorded.");
		
	}

	
	
	
}
