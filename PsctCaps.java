package jaccsbot.jaccsbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

/**
 * corrects the capitalisation for yugioh psct
 * Throughout "capsed" is used as an abbreviation for capitalised
 * 
 * @author luke
 *
 */
public class PsctCaps extends Command{
	
	private String[] words=new String[] {"Deck", "Tribute", "Tributed", "Tuner", "Gemini", "Toon", "Spirit", "Union",
			"Xyz","Fusion","Link","Set","Type","Summon","Summoned","Attribute","Level","Spell","Trap","Duel", "Summoning", "Chain",
			"Rank", "tributing", "Token"};
	private String[] types=new String[] {"Aqua", "Beast", "Cyberse", "Dinosaur", "Dragon"
			, "Fairy", "Fiend", "Fish", "Insect", "Machine", "Plant", "Psychic", "Pyro", "Reptile", "Rock", "Sea", "Serpent"
			, "Spellcaster", "Thunder", "Warrior", "Wyrm", "Zombie"};
	private String[] attributes = new String[] {"FIRE", "WIND", "WATER", "EARTH", "LIGHT", "DARK", "DIVINE"};
	private String[] allCaps = new String[] {"ATK","DEF","GY","LP"}; 
	private String[][] pairs = new String[][] {{"Quick", "Effect"},{"Link","Rating"}
	,{"Attack","Position"},{"Defence","Position"}
	,{"Damage","Step"},{"Damage","Calculation"}	,{"Start","Step"},{"Battle","Step"},{"Spell","Card"},{"Trap","Card"}
	,{"Monster","Card"},{"Quick","Effect)"},{"Link","Arrow"},{"Monster","Zone"},{"Main","Monster"},{"Extra","Monster"}
	,{"Spell","&","Trap","Zone"},{"Extra","Deck"},{"Continuous","Spell"},{"Equip","Spell"},{"Field","Spell"}
	,{"Normal","Spell"},{"Ritual","Spell"},{"Pendulum","Zone"},{"Counter","Trap"},{"Normal","Trap"},{"Continious","Trap"}
	,{"Pendulum","Spell"},{"Spell","Counter"},{"Defense","Position"},{"Attack","Position"},{"Field","Zone"},{"Spell","/","Trap","Zone"}
	,{"Quick","-","Play"}
	}; 
	private String[] cardTypes=new String[] {"Fusion", "Synchro", "Xyz", "Link", "Pendulum", "Ritual", "Normal", "Special", 
			 "Flip", "Effect"
			 };
	private String[] cardTypeEndings=new String[] {"Summon", "Summoned","Material", "Monster"};
	private String[] phases = new String[] {"Draw", "Standby", "Main", "Battle", "End"};
	
	private HashSet<String[]> counters=new HashSet<String[]>();
	
	
	private boolean cardTypeDetected=false;
	private String[] cleaned=null;
	private String[] raw=null;
	
	private boolean noReasons=false;
	
	
	
	
	
	public PsctCaps() 
	{
		this.name="psctcaps";
		this.aliases= new String[] {"PsctCaps","PSCTcaps","psctCaps"};
		this.help="checks cardtext for correct capital letter useage, replys with the corrected text or \"No errors found\" if no errors where found";
		this.guildOnly=false;
		this.arguments="<commands (optional)> {text}";
		this.category=new Command.Category("Psct Help");
		
	}
	
	private void runCommands(JaccsCommandHandler commands) 
	{
		//counters
		HashSet<String> counterNames=commands.getCounterNames();
		for(String name:counterNames) 
		{
			//counter names have $$ instead of spaces as specified by the command help command
			String tempStr=name+"$$Counter";
			String[] tempStrArr=tempStr.split("\\$\\$");
			for(int i=0;i<tempStrArr.length;i++) 
			{
				tempStrArr[i]=F.firstLetterToUpperCase(tempStrArr[i].toLowerCase());
			}
			this.counters.add(tempStrArr);
		}
		
		
		this.noReasons=commands.isNoReasons();
		
		
	}

	
	
	
	
	protected void execute(CommandEvent event) 
	{
		JaccsCommandHandler commands =JaccsCommandHandler.getCommandHandler(event.getAuthor().getIdLong());
		String text = null;
		try {
			commands = JaccsCommandHandler.parseCommands(event.getArgs(), commands);
		} catch (JaccsCommandException e1) {
			event.reply(e1.getMessage());
			return;
		}
		runCommands(commands);
		text=commands.getText();
		
		text=text.replaceAll("\n", " \n");
		text=text.replaceAll("-", " - ");
		text=text.replaceAll("/", " / ");
		raw = text.split(" ");
		String[] ans= raw.clone();
		
		String textCheck=text.toLowerCase();
		

		HashSet<encode> toCapsIndex=new HashSet<encode>();
		
		int checkNotTrap=textCheck.indexOf("this card is not treated as a trap card");
		while(checkNotTrap>-1) 
		{
			int spaceCount=3;//start at the amount added due to only "not" being capsed
			for(int i=0;i<checkNotTrap;i++) 
			{
				if(text.charAt(i)==' ') spaceCount++;
			}
			encode temp=new encode("NOT", spaceCount, 13);
			temp.toAllCaps();
			toCapsIndex.add(temp);
			
			checkNotTrap=textCheck.indexOf("this card is not treated as a trap card",checkNotTrap+2);
		}
		
		
		
		text=text.replaceAll("-", "::-::");
		text=text.replaceAll("/", "::/::");
		cleaned = text.split(" ");
		//cleaned removes most punctuation, leaving just the words and important punctuation, here "-" and "/"
		
		//remove start and end punctuation
		for( int i= 0 ; i<cleaned.length;i++) 
		{
			cleaned[i]=F.clean(cleaned[i]);
			
			if(cleaned[i].endsWith("s")) 
			{
				cleaned[i]=cleaned[i].substring(0, cleaned[i].length()-1);
			}
			if(cleaned[i].endsWith("(s")) 
			{
				cleaned[i]=cleaned[i].substring(0, cleaned[i].length()-2);
			}
			if(cleaned[i].endsWith("(s)")) 
			{
				cleaned[i]=cleaned[i].substring(0, cleaned[i].length()-3);
			}
			if(cleaned[i].endsWith(")")) 
			{
				cleaned[i]=cleaned[i].substring(0, cleaned[i].length()-1);
			}
			cleaned[i]=F.clean(cleaned[i]);
		}
		
		

		
		
		Queue<String[]> cardTypeReplace=new LinkedList<String[]>();//instances of card type are added here and handled later
		//entries are in the format {cleaned string,raw string}
		
		
		for(int i=0;i<raw.length;i++) 
		{
			boolean cardTest=false;
			try
			{
				boolean cardTypeTest=cleaned[i].equalsIgnoreCase("card")&&cleaned[i+1].equalsIgnoreCase("type");
				if(cardTypeTest) 
				{
					this.cardTypeDetected=true;
					cardTypeReplace.add(new String[] {cleaned[i],raw[i]});
					cardTypeReplace.add(new String[] {cleaned[i+1],raw[i+1]});
					cleaned[i]=null;
					cleaned[i+1]=null;
					raw[i]=null;
					raw[i+1]=null;
					cardTest=true;
					//setting to null prevents interference from other parts in the method
				}
				
			}
			catch(Exception e) 
			{
				
			}
			
			if(cardTest) 
			{
				try 
				{
					if(raw[i+2].startsWith("(")) //if a bracketed list follows card type, caps them all, except "or"
					{
						int whileLoop=2;
						toCapsIndex.add(new encode(F.firstLetterToUpperCase(cleaned[i+whileLoop].toLowerCase()), i+whileLoop, 6));
						while(true) 
						{
							whileLoop++;
							if(!cleaned[i+whileLoop].equalsIgnoreCase("or")) 
							{
								toCapsIndex.add(new encode(F.firstLetterToUpperCase(cleaned[i+whileLoop].toLowerCase()), i+whileLoop, 6));
							}
							if(raw[i+whileLoop].contains(")")) break;
							
						}
						
						
					}
				}
				catch(Exception e) 
				{
					
				}
			}
		}
		
		
		
		//main loop to detect things that should be capsed
		for (int i =0; i<raw.length;i++) 
		{
			
			if(!(cleaned[i]!=null)) 
			{
				continue;
			}
			
			boolean AllCapsTest=false;//if the i'th word should be in all caps 
			for (int j=0;j<this.attributes.length;j++) 
			{
				if(cleaned[i].equalsIgnoreCase(this.attributes[j])) 
				{
					encode tempEncode=new encode(this.attributes[j], i, 11);
					tempEncode.toAllCaps();
					toCapsIndex.add(tempEncode);
					AllCapsTest=true;
				}
			}
			
			for (int j=0;j<this.allCaps.length;j++) 
			{
				if(cleaned[i].equalsIgnoreCase(allCaps[j] )) 
				{
					encode tempEncode=new encode(allCaps[j], i, 12);
					tempEncode.toAllCaps();
					toCapsIndex.add(tempEncode);
					AllCapsTest=true;
				}
			}
			//TODO reduce usage of try catch blocks
			//prevent all capsed words getting first letter only capsed
			if(AllCapsTest) continue;
			
			//caps words following . : or ●
			if(i>0) 
			{
				if(raw[i-1]!=null) 
				{
					if(raw[i-1].endsWith(".")||raw[i-1].endsWith(".)")) 
					{
						try 
						{
							toCapsIndex.add(new encode(F.firstLetterToUpperCase(raw[i].toLowerCase()), i, 1));
						}
						catch (Exception e) {}
						
					}
					else if(raw[i-1].endsWith(":")) 
					{
						try 
						{
							toCapsIndex.add(new encode(F.firstLetterToUpperCase(raw[i].toLowerCase()), i, 2));
						}
						catch(Exception e) {}
					}
					else if(raw[i-1].endsWith("●")) 
					{
						toCapsIndex.add(new encode(F.firstLetterToUpperCase(raw[i].toLowerCase()), i, 0));
					}
					
				}
			}
			
			
			// \n is at the start of words due to how the split was set up
			if(raw[i].indexOf("\n")>-1) 
			{
				toCapsIndex.add(new encode(F.firstLetterToUpperCase(raw[i].toLowerCase()), i, -1));
			}
			
			//check basic words and types
			for (int j=0; j<words.length;j++) 
			{
				if(cleaned[i].equalsIgnoreCase(words[j]))
				{
					toCapsIndex.add(new encode(words[j], i, 3));
				}
			}	
			for (int j=0; j<types.length;j++) 
			{
				if(cleaned[i].equalsIgnoreCase(types[j]))
				{
					toCapsIndex.add(new encode(types[j], i, 4));
				}
			}	
			
			//check for pairs of words
			for (int j =0;j<this.pairs.length;j++) 
			{
				boolean test =true;
				for(int k=0;k<this.pairs[j].length;k++) 
				{
					try {
						if(!this.pairs[j][k].equalsIgnoreCase(cleaned[i+k]))
						{
							test=false;
							break;
						}
						
					} catch (Exception e) 
					{
						test=false;
						break;
					}
				}
				
				if(test) 
				{
					String[] temp=pairs[j].clone();
					for(int k=0;k<pairs[j].length;k++) 
					{
						temp[k]=F.firstLetterToUpperCase(cleaned[i+k].toLowerCase());
					}
					for(int k=0;k<pairs[j].length;k++) 
					{
							toCapsIndex.add(new encode(temp,temp[k],i+k,5));	
					}
					
				}
				
				
			}
			
			//check for user given counters
			for(String[] counter:this.counters) 
			{
				boolean test =true;
				for(int k=0;k<counter.length;k++) 
				{
					try {
						if(!counter[k].equalsIgnoreCase(cleaned[i+k]))
						{
							test=false;
							break;
						}
						
					} catch (Exception e) 
					{
						test=false;
						break;
					}
				}
				
				if(test) 
				{
					String[] temp=counter.clone();
					for(int k=0;k<counter.length;k++) 
					{
						temp[k]=F.firstLetterToUpperCase(cleaned[i+k].toLowerCase());
					}
					for(int k=0;k<counter.length;k++) 
					{
							toCapsIndex.add(new encode(temp,temp[k],i+k,5));
					}
					
				}
			}
			
			
			
			
			
			
			//card type handling (the words that are card types, not "card type")
			for(int j=0;j<this.cardTypeEndings.length;j++) 
			{
				boolean test1=false;
				if(cleaned[i].equalsIgnoreCase(this.cardTypeEndings[j])) 
				{
					for(int k=0;k<this.cardTypes.length;k++) 
					{
						if(i<1) continue;
						if(!(cleaned[i]!=null)) break;
						if(!(cleaned[i-1]!=null)) break;
						
						if(cleaned[i-1].equalsIgnoreCase(this.cardTypes[k])) 
						{
							String[] temp=new String[] 
									{F.firstLetterToUpperCase(F.firstLetterToUpperCase(cleaned[i-1].toLowerCase())),F.firstLetterToUpperCase(cleaned[i].toLowerCase())};
							
							
							toCapsIndex.add(new encode(temp, F.firstLetterToUpperCase(cleaned[i].toLowerCase()), i, 5) );
							toCapsIndex.add(new encode(temp, F.firstLetterToUpperCase(cleaned[i-1].toLowerCase()), i-1, 5) );
							test1=true;
							break;
						}
					}
					
					if(test1)//go backwards and check for a comma separated list 
					{
						try {
							if(cleaned[i-2].equalsIgnoreCase("or")) 
							{
								toCapsIndex.add(new encode(F.firstLetterToUpperCase(cleaned[i-3].toLowerCase()),i-3,6));
								boolean commaTest=raw[i-3].endsWith(",");
								int k=3;
								while(commaTest) 
								{
									toCapsIndex.add(new encode(F.firstLetterToUpperCase(cleaned[i-k].toLowerCase()),i-k,6));
									k++;
									commaTest=raw[i-k].endsWith(",");
								}
							}
						} catch (Exception e) 
						{
							
						}
						
						
					}
					
				}
			}
			
			//TODO make better
			boolean test2=false;
			if(cleaned[i].equalsIgnoreCase("phase")) 
			{
				
				for(int j=0;j<this.phases.length;j++) 
				{
					if(i<1) continue;
					if(!(cleaned[i]!=null)) continue;
					if(cleaned[i-1].equalsIgnoreCase(this.phases[j])) 
					{
						String[] temp=new String[] 
								{F.firstLetterToUpperCase(F.firstLetterToUpperCase(cleaned[i-1].toLowerCase())),F.firstLetterToUpperCase(cleaned[i].toLowerCase())};
						
						
						toCapsIndex.add(new encode(temp, F.firstLetterToUpperCase(cleaned[i].toLowerCase()), i, 5) );
						toCapsIndex.add(new encode(temp, F.firstLetterToUpperCase(cleaned[i-1].toLowerCase()), i-1, 5) );
						test2=true;
						break;
					}
				}
			}
			if(test2) 
			{
				try {
					if(cleaned[i-2].equalsIgnoreCase("or")) 
					{
						toCapsIndex.add(new encode(F.firstLetterToUpperCase(cleaned[i-3].toLowerCase()),i-3,6));
						boolean commaTest=raw[i-3].endsWith(",");
						int k=3;
						while(commaTest) 
						{
							toCapsIndex.add(new encode(F.firstLetterToUpperCase(cleaned[i-k].toLowerCase()),i-k,6));
							k++;
							commaTest=raw[i-k].endsWith(",");
						}
					}
				} catch (Exception e) 
				{
					
				}
			}
			
			
			
			
		}
		
		//add words wrapped in quotes to an ignore list
		HashSet<Integer> ignoreIndex=new HashSet<Integer>();
		
		boolean quoteOn=false;
		for(int i=0;i<raw.length;i++) 
		{
			int temp = 0;
			if(!(raw[i]!=null)) continue;
			for(int j=0;j<raw[i].length();j++) //words may have 2 or more \" in them
			{
				if(raw[i].substring(j).startsWith("\"")) 
				{
					temp++;
				}
			}
			if(temp>0) 
			{
				ignoreIndex.add(Integer.valueOf(i));
				quoteOn=!quoteOn;
			}
			if(temp>1) 
			{
				quoteOn=!quoteOn;
				
			}
			if(temp>2) 
			{
				event.reply("looks like you have used quotation marks(\") incorrectly, please fix this and try again");
				return;
			}
			
			
			if(quoteOn) 
			{
				ignoreIndex.add(Integer.valueOf(i));
			}
			
		}
		
		if(quoteOn) 
		{
			event.reply("looks like you have used quotation marks(\") incorrectly, please fix this and try again");
			return;
		}
		
		
		
		boolean[] used=new boolean[cleaned.length];//if true that index will be ignored
		for (int i=0;i<used.length;i++) 
		{
			used[i]=false;
		}
		List<Pair<String, Integer>> reasons=new ArrayList<Pair<String, Integer>>();//string of the word, Integer of the reason code
		HashMap<Integer, String> emphIndex=new HashMap<Integer, String>();//Integer of the index, string to wrap word in ("*" for italics, "**" for bold)
		
		
		
		for(Integer integer:ignoreIndex) 
		{
			used[integer.intValue()]=true;
		}
		ignoreIndex.clear();
		
		toCapsIndex.add(new encode(F.firstLetterToUpperCase(raw[0].toLowerCase()), 0, -2));
		
		for(int i=0;i<cleaned.length;i++) 
		{
			if(!(cleaned[i]!=null)) 
			{
				String[] tempReplace=cardTypeReplace.poll();
				cleaned[i]=tempReplace[0];
				raw[i]=tempReplace[1];
			}
		}
		
		
		
		
		//adjust the ans array with the corrected text and add reasons to the reasons list
		for(encode data:toCapsIndex) 
		{
			
			if(!used[data.index]) 
			{
				if(this.cardTypeDetected) 
				{
					if(cleaned[data.index].equalsIgnoreCase("type")) 
					{
						data.toItalics();
						data.reasonChangeType();
						ignoreIndex.add(Integer.valueOf(data.index));
						emphIndex.put(data.addToMapInt(), data.addToMapString());
						reasons.add(data.reasonCodeToString());
						continue;
					}					
				}
				
				
				if(data.needsChanging()) 
				{
					ans[data.index]=data.correctedWord();
					emphIndex.put(data.addToMapInt(), data.addToMapString());
					reasons.add(data.reasonCodeToString());
				}
				
			}
			ignoreIndex.add(Integer.valueOf(data.index));
		}
		
		
		
		for(Integer integer:ignoreIndex) 
		{
			used[integer.intValue()]=true;
		}
		
		
		
		for(Integer integer :emphIndex.keySet()) 
		{
			
			used[integer.intValue()]=true;
			ans[integer.intValue()]=emphIndex.get(integer)+ans[integer.intValue()]+emphIndex.get(integer);
		}
		
		//lowercase all incorrectly capsed words
		for(int i=0;i<used.length;i++) 
		{
			if(!used[i]) 
			{
				String temp4=ans[i].toLowerCase();
				if(temp4!=ans[i]) 
				{
					ans[i]="**"+temp4+"**";
					
					reasons.add(new Pair<String, Integer>(F.clean(temp4)+ " is lower case unless part of a special pair (it may be part of none) "
							+ "or is after a new line, \".\" or a \":\"",i));
				}
			}
		}
		
		
		String txt="";
		String reasonsString="";
		for(int i =0; i<raw.length;i++) 
		{
			if(ans[i].equals("/")||ans[i].equals("-")) //remove spacing added earlier
			{
				txt=txt.substring(0, txt.length()-1);
				txt=txt+ans[i];
			}else 
			{
				txt=txt+ans[i]+" ";
			}
				
			
		}
		
		
		if(reasons.isEmpty()) 
		{
			event.reply("No errors found");
		}
		else 
		{
			while(true) //bubble sort reasons by index
			{
				boolean test=false;
				for(int i=0;i<reasons.size()-1;i++) 
				{
					Pair<String, Integer> tempPair1 =reasons.get(i);
					Pair<String, Integer> tempPair2 =reasons.get(i+1);
					if(tempPair2.getSecond()<tempPair1.getSecond()) 
					{
						test=true;
						reasons.set(i, tempPair2);
						reasons.set(i+1, tempPair1);
						
					}
					
				}
				if(!test) break;
				
				
			}
			
			
			if(!this.noReasons) 
			{
				for(Pair<String, Integer> reason:reasons) 
				{
					reasonsString=reasonsString+reason.first+"\n";
				}
				event.reply(txt);
				event.reply(reasonsString);
			}
			
			
			
		}
		
		resetData();
		
	}


	
	
	/**
	 * resets all object global data used in execute method
	 */
	private void resetData() 
	{
		this.counters.clear();
		this.cardTypeDetected=false;
		this.raw=null;
		this.cleaned=null;
		
	}







	/**
	 * used to hold the information about words, given that they should be capsed
	 * also holds information about what reason the word should be capsed for and methods to used for checking information
	 * 
	 *
	 */
	private class encode
	{
		private String word;
		private int index;
		private int reasonCode;
		private String[] words=null;
		private boolean italics=false;//if the word should be in italics
		private boolean allCaps=false;//if the word should be in all caps
		/**
		 * stores information about a 1 word to caps, the reason it should be capsed, its index, and the word that should be capsed
		 * 
		 * @param word string from the cleaned array that should be capsed
		 * @param index the index of the word
		 * @param reasonCode the int corresponding to the reason that the word should be capsed. see reasonCodeToString method
		 */
		encode(String word, int index, int reasonCode) 
		{
			this.word=word;
			this.index=index;
			this.reasonCode=reasonCode;
		}
		
		/**
		 * if the word was "type" while cardTypeDetected was true
		 */
		public void reasonChangeType() {
			this.reasonCode=20;
			
		}
		/**
		 * stores information about 1 word in a list or pair to be capsed
		 * 
		 * @param words an array with all the words to be capsed
		 * @param targetWord the specific word to be capsed
		 * @param index the index of the target word
		 * @param reasonCode the int corresponding to the reason that the word should be capsed. see reasonCodeToString method
		 */
		encode(String[] words, String targetWord, int index, int reasonCode) 
		{
			this.words=words.clone();
			this.word=targetWord;
			this.index=index;
			this.reasonCode=reasonCode;
		}
		

		void toItalics() 
		{
			this.italics=true;
		}
		
		void toAllCaps() 
		{
			this.allCaps=true;
		}
		
		/**
		 * returns an Integer, used for adding to the HashMap
		 * @return this.index
		 */
		public Integer addToMapInt() 
		{
			return this.index;
		}
		/**
		 * returns the string to wrap the word in if correction is needed
		 * used to add to the HashMap
		 * @return 
		 */
		public String addToMapString() 
		{
			if(this.italics) 
			{
				return "*";
			}
			else 
			{
				return "**";
			}
		}
		/**
		 * checks if this.word is correctly capsed
		 * @return true if this word needs changing and false otherwise
		 */
		public boolean needsChanging() 
		{
			if(this.reasonCode<3) 
			{
				return !raw[this.index].equals(this.word); 
				
			}
			else 
			{
				return !cleaned[this.index].equals(this.word);
			}
			
		} 
		/**
		 * returns this.word with corrected capitalisation
		 * @return the corrected word
		 */
		public String correctedWord() 
		{
			if(this.allCaps) 
			{
				return raw[this.index].toUpperCase();
			}
			else 
			{
				if(this.reasonCode<3) 
				{
					return this.word;
				}
				else 
				{
					return F.firstLetterToUpperCase(raw[this.index].toLowerCase());
				}
			}
			
		}
		
		
		
		
		/**
		 * takes this.reasoncode and returns the reason as a string. pairs this string with this.index for use in the set
		 * @return pair of reasoncode as string and index
		 */
		public Pair<String, Integer> reasonCodeToString()
		{
			switch(reasonCode) 
			{
			case -2: return new Pair<String, Integer>("always capitalise the first word",this.index);
			case -1: return new Pair<String, Integer>("always capitalise after a new line",this.index);
			case 0: return new Pair<String, Integer>("always capitalise after ●",this.index);
			case 1: return new Pair<String, Integer>("always capitalise after .",this.index);
			case 2: return new Pair<String, Integer>("always capitalise after :",this.index);
			case 3: return new Pair<String, Integer>(word+" is always capitalised",this.index);
			case 4: return new Pair<String, Integer>("types are always capitalised",this.index);
			case 5: return Pair.of("\""+this.wordsToString()+"\" is always capitalised in this pair",this.index);
			case 6: return new Pair<String, Integer>("always capitalise "+word+" in lists",this.index);
			
			
			case 11: return new Pair<String, Integer>("attributes are always in all caps",this.index);
			case 12: return new Pair<String, Integer>(word+" is always in all caps",this.index);
			case 13: return Pair.of("\"NOT\" is always in all caps in this phrase", this.index);
			
			case 20: return Pair.of("\"Type\" could not be capitalised automatically. If \"type\" is referring to a Monster Type"
					+ ", it should be capitalised. If \"type\" is referring to a card type, it should be lowercase", this.index);
			}
			
			
			
			return Pair.of("reason code="+this.reasonCode,this.index);
		}
		
		/**
		 * takes the words array and combines it into one string
		 * @return this.words as one string, with correct spacing
		 */
		private String wordsToString() 
		{
			String ans="";
			for (int i = 0; i < this.words.length; i++) {
				if(this.words[i].equals("/")||this.words[i].equals("-")) 
				{
					ans=ans.substring(0, ans.length()-1);
					ans=ans+this.words[i];
				}else 
				{
					ans=ans+this.words[i]+" ";
				}
				
			}
			if(ans.charAt(ans.length()-1)==' ') 
			{
				ans=ans.substring(0, ans.length()-1);
			}
			return ans;
		}
		
	}
	
	
	
}



























