package jaccsbot.jaccsbot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class FastEffectChart extends Command{

	//links the fast effect chart
	FastEffectChart()
	{
		this.name="fasteffectchart";
		this.aliases=new String[] {"fast", "fasteffect", "fetc"};
		this.help="Links the fast effect chart. You can also use \"fast\", \"fasteffect\" or \"fetc\"";
		this.category=new Command.Category("Utility");
		this.guildOnly=false;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		event.reply("https://www.yugioh-card.com/en/gameplay/images/T-Flowchart_EN-US.jpg");
		
	}

}
