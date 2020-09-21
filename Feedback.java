package jaccsbot.jaccsbot;

import java.util.HashSet;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Feedback extends Command{

	private HashSet<String> reports=new HashSet<String>();
	
	Feedback()
	{
		this.category=new Command.Category("Utility");
		this.name="feedback";
		this.aliases=new String[] {"bug", "reportbug", "bugreport"};
		this.help="Gives feedback to the bot creator, can be used to ask for features, report bugs or comment on anything.";
		this.guildOnly=false;
		
	}
	
	@Override
	protected void execute(CommandEvent event) {
		//stores reports by other users, if the creator calls this command it lists all reports so far. list is reset on bot updating or restarting
		if(event.getAuthor().getIdLong()==104284192401022976L&&event.getArgs().isBlank()) event.reply(reports.toString());
		else 
		{
			reports.add(event.getArgs()+" -from "+event.getAuthor().getName()+" ID= " +event.getAuthor().getId()+" \n\n");
			event.reply("Your report has been recorded.");
		}
		
	}

}
