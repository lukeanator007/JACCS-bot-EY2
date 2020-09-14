package jaccsbot.jaccsbot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Version extends Command{
	
	private int index1;
	private int index2;
	private String[][] patchNotes;
	private String version;
	
	
	/**
	 * 
	 * @param patchNotes
	 * @param version
	 * @param index1
	 * @param index2
	 */
	Version(String[][] patchNotes, String version, int index1, int index2)
	{
		this.index1=index1;
		this.index2=index2;
		this.version=version;
		this.patchNotes=patchNotes;
		
		this.name="version";
		this.aliases=new String[]{"patchnotes","PatchNotes","patchNotes","Version", "v"};
		this.help="gives the current version and the patch notes for the most recent patch";
		this.guildOnly=false;
		this.category=new Command.Category("Utility");
	}
	
	//TODO read any patch notes
	@Override
	protected void execute(CommandEvent event) {

		event.reply("current version: "+this.version+"\npatch notes:\n"+patchNotes[index1][index2]);
		
	}

}
