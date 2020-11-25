package jaccsbot.jaccsbot;

import java.util.HashMap;
import java.util.HashSet;

public class JaccsCommandHandler {

	//list of commands: no reasons, counter type, no conjunctions, fast spells 
	//TODO server defaults
	public static String prefix = "$$";
	public static HashMap<Long, JaccsCommandHandler> userCommands = new HashMap<Long, JaccsCommandHandler>();

	private boolean noReasons = false;
	private HashSet<String> counterNames = new HashSet<String>();
	private boolean noConjunctions = false;
	private boolean fastSpells = false;
	private boolean quickSpells = false;
	private HashSet<String> names = new HashSet<String>();
	private HashSet<String> words =new HashSet<String>();
	private String text = "";//the user input

	JaccsCommandHandler(String commands) throws JaccsCommandException {
		parseCommands(commands, this);
	}

	public JaccsCommandHandler() {

	}
	
	/**
	 * deep clone constructor, or default if null is given
	 * @param handler JaccsCommandHandler to deep clone
	 */
	public JaccsCommandHandler(JaccsCommandHandler handler) 
	{
		if((handler!=null)) 
		{
			this.counterNames.addAll(handler.counterNames);
			this.fastSpells=handler.fastSpells;
			this.names.addAll(handler.names);
			this.noConjunctions=handler.noConjunctions;
			this.noReasons=handler.noReasons;
			this.quickSpells=handler.quickSpells;
		}
	}
		
	
	/**
	 * gets a command handler of the default commands from user id or a default one if they do not have default commands set
	 * @param id the id of the user
	 * @return the JaccsCommandHandler associated with this users default commands, or a default JaccsCommandHandler if they have not been set
	 */
	public static JaccsCommandHandler getCommandHandler(Long id) {
		return new JaccsCommandHandler(userCommands.get(id));

	}

	/**
	 * takes a command string and a command handler and parses the commands, adding them to the given command handler
	 * also store the command string with sub-commands removed
	 * 
	 * @param str the command string to parse
	 * @param command 
	 * @return the modified command handler
	 * @throws JaccsCommandException 
	 */
	public static JaccsCommandHandler parseCommands(String str, JaccsCommandHandler command)throws JaccsCommandException 
	{
		if (!(str != null))return null;
			
		
		String[] args = str.split(" ");
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith(prefix)) 
			{
				String tempString = args[i].substring(prefix.length());

				switch (tempString) 
				{
				case "noconjunctions":
					command.setNoConjunctions(true);
					break;
				case "conjunctions":
					command.setNoConjunctions(false);
					break;
				case "noreasons":
					command.setNoReasons(true);
					break;
				case "reasons":
					command.setNoReasons(false);
					break;
				case "counter":
					i++;
					command.addCounterNames(args[i]);
					break;
				case "card":
				case "arch":
				case "name":
					i += command.addName(args, i);
					break;
				case "quickspell":
					command.setQuickSpells(true);
					break;
				case "fastspell":
					command.setFastSpells(true);
					break;
				case "addword":
					command.addWord(tempString.toLowerCase());
				}
			} 
			else 
			{
				String ans = "";
				for (int j = i; j < args.length; j++) 
				{
					ans = ans + args[j] + " ";
				}
				command.text = ans;
				return command;
			}
		}

		return null;

	}

	/**
	 * adds a name to the list of names used in commands
	 * 
	 * @param args the command string split by " " 
	 * @param i the index which the $$name etc is found
	 * @return the index to change i to
	 * @throws JaccsCommandException
	 */
	private int addName(String[] args, int i) throws JaccsCommandException {
		String tempStrCard = "";
		int j;
		for (j = 1; j < args.length; j++) 
		{
			if(i+j>=args.length) throw (new JaccsCommandException("Use a space before \"$$end\""));
			if (args[i + j].equalsIgnoreCase(prefix + "end")) 
			{
				i += j;
				this.names.add(tempStrCard.substring(0, tempStrCard.length() - 1));
				return j;
			} else {
				tempStrCard += args[i + j] + " ";
			}
		}

		throw (new JaccsCommandException("No end command was given"));
	}

	
	@Override
	public String toString() {
		String builder = "";
		builder += "no reasons =" + this.noReasons;
		builder += "\nfast spells =" + this.fastSpells;
		builder += "\nnames =" + this.names;
		builder += "\ncounters =" + this.counterNames;
		builder += "\nno conjunctions =" + this.noConjunctions;

		return builder;
	}
	
	
	public HashSet<String> getWords()
	{
		return this.words;
	}
	
	public void addWord(String str) 
	{
		this.words.add(str);
	}
	
	public boolean isNoReasons() {
		return noReasons;
	}

	public void setNoReasons(boolean noReasons) {
		this.noReasons = noReasons;
	}

	public HashSet<String> getCounterNames() {
		return counterNames;
	}

	public void addCounterNames(String counterName) {
		this.counterNames.add(counterName);
	}

	public boolean isNoConjunctions() {
		return noConjunctions;
	}

	public void setNoConjunctions(boolean noConjunctions) {
		this.noConjunctions = noConjunctions;
	}

	public boolean isQuickSpells() {
		return quickSpells;
	}

	public void setQuickSpells(boolean quickSpells) {
		this.quickSpells = quickSpells;
	}

	public HashSet<String> getNames() {
		return names;
	}

	public boolean isFastSpells() {
		return fastSpells;
	}

	public void setFastSpells(boolean fastSpells) {
		this.fastSpells = fastSpells;
	}

	public String getText() {
		return text;
	}

}
