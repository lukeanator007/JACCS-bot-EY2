package jaccsbot.jaccsbot;

public class VersionChanges {

	public String[][] patchNotes=new String[1][];
	
	VersionChanges() 
	{
		initalisePatch0_0();
	}

	
	private void initalisePatch0_0() {
		this.patchNotes[0]=new String[10];
		this.patchNotes[0][0]="ver 0.0.0\nnew commands: psctCaps, version\nbug fixes: none";
		this.patchNotes[0][1]="ver 0.0.1\nnew commands: none\nbug fixes: pairs of words in psctcaps can no longer be ignored if the 1st word was capitalised for another reason";
		this.patchNotes[0][2]="ver 0.0.2\ncommands: psctcaps should be finished, aside from checking named types of counters";
		this.patchNotes[0][3]="ver 0.0.3\ncommands: psctcaps now works with non-<ATTRIBUTE> and Spell/Trap Card. All caps words can no longer be incorrectly capsed as "
				+ "first letter capsed only";
		this.patchNotes[0][4]="ver 0.0.4\nnew commands: effectdetection, bug fixes none";
		this.patchNotes[0][5]="ver 0.0.5\ncommands new feature: psctcaps now can take counters into account, use $$counter <counter_name>"
				+ " before the text";
		this.patchNotes[0][6]="ver 0.0.6\nnew commands:\n"
				+ "j!fasteffectchart: gets the fast effect chart, can be shorted to j!fast\n"
				+ "j!psctdocs: links the psctdocs by konami, can be shorted to j!docs \n"
				+ "j!setcommands: sets default commands\n"
				+ "j!getscommands: gets what your current default commands are\n"
				+ "j!commandhelp: gives information about commands\n"
				+ "bug fixes: psctcaps no longer incorrectly identifes lowercase word pairs to be correct\n"
				+ "psctcaps reasons message no longer puts spaces between other words and \"-\" or \"/\" ";
		this.patchNotes[0][7]="ver 0.0.7\n"
				+ "new commands:\n"
				+ "j!feedback/bugreport/reportbug: logs any feedback/reports a bug.\n"
				+ "j!spellcheck: spellchecks your text. Has $$command support for counter and card names. "
				+ "The database may be incomplete at this time, use j!feedback if there is a word that should be added\n"
				+ "general changes:\n"
				+ "psctcaps now reminds you the $$counter command exists"
				+ "effectdetection now ignores case when declaring card type";
		this.patchNotes[0][8]="ver 0.0.8\n"
				+ "commands:\n"
				+ "j!effectdetection improved\n"
				+ "$$addword adds a word to j!spellcheck for your use only\n"
				+ "bug fixes:\n"
				+ "j!psctcaps now correctly capitalises lists of phases\n"
				+ "j!psctcaps now corectly capitalises cases such as\"word. ATK\"\n"
				+ "j!spellcheck now correctly checks words split by \"-\" or \"/\"\n"
				+ "j!effectdetection no longer messes up ``` useage if a single effect generated a message over 2000 characters in length\n"
				+ "added words to the spellcheck database.\n"
				+ "spellchecking happened on system messages\n"
				+ "known bugs: \n"
				+ "j!spellcheck removes full stops \".\" from text\n"
				+ "j!psctcaps sometimes sends lots of messages for the same error\n";
				
	}
	
	
	
}
