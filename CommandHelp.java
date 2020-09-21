package jaccsbot.jaccsbot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class CommandHelp extends Command {

	private String temp="All commands are prefixed with \"$$\".\n\n"
			+ "counter {counter name}: Tells psctcaps and spellcheck what types of counter(s) you are using. Replace any spaces with \"$$\". If you use \"-\" or \"/\", "
			+ "the bot treats these characters as having spaces around, so use \"$$\" around them as well. E.G.: $$counter a$$-\n\n"
			+ "noreasons: Tells psctcaps to not give reasons on corrections, use this if you are experianced and want to reduce space useage\n\n"
			+ "noconjunctions: Tells effectdetection to not check for conjunctions\n\n"
			+ "addword {word}: tells spellcheck that the added word should be included in its list (only affects your useage)"
			+ "(name/card/arch) {card/archetype name} $$end: Tells spellcheck what card/archetype name(s) your cards have, and checks them for spelling and correct case useage. "
			;
	
	//command name args (used in?) does
	
	CommandHelp()
	{
		this.name="commandhelp";
		this.help="Lists all avaliable commands and explains their use";
		this.guildOnly=false;

		this.category=new Command.Category("Commands");
		
	}
	
	@Override
	protected void execute(CommandEvent event) {
		
		event.reply(this.temp);
	}

}






























































