package jaccsbot.jaccsbot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class PsctDocs extends Command{

	PsctDocs()
	{
		this.name="psctdocs";
		this.help="Links the PSCT articles by konami";
		this.aliases=new String[] {"docs"};
		this.category=new Command.Category("Utility");
		this.guildOnly=false;
	}
	
	
	@Override
	protected void execute(CommandEvent event) {
		event.reply("<https://yugiohblog.konami.com/articles/?tag=problem-solving-card-text>");
	}

}
