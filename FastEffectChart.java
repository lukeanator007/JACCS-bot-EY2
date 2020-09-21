package jaccsbot.jaccsbot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class FastEffectChart extends Command{

	//links the fast effect chart
	FastEffectChart()
	{
		this.name="fasteffectchart";
		this.aliases=new String[] {"fast", "fasteffect"};
		this.help="Links the fast effect chart";
		this.category=new Command.Category("Utility");
	}
	
	@Override
	protected void execute(CommandEvent event) {
		event.reply("https://www.yugioh-card.com/en/gameplay/images/T-Flowchart_EN-US.jpg");
		
	}

}
