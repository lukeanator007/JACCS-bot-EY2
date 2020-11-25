package jaccsbot.jaccsbot;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.text.StringEscapeUtils;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class SpellCheck extends PSCTCommand{

	HashSet<String> wordsSet =new HashSet<String>();
	
	SpellCheck()
	{
		String[] wordsArr=new String[] {
				"been", "half", "hands", "def", "synchro", "owners", "your", "ignoring", "conducting", "without", "these", "properly", "would", "depending", "rank", "because", "you", "fairy", "addition", "wins", "counters", 
				"optional", "plany", "an", "whose", "as", "at", "left", "plant", "returns", "unused", "wind", "appropriate", "be", "standby", "roll", "least", "turn", "immediately", "choosing", "sea", "result", "rolling", 
				"same", "by", "banishing", "zombie", "targeted", "after", "flip", "hand", "linked", "a", "set", "column", "union", "possible", "procedure", "right", "co", "excavate", "the", "battle", "traps", "destroyed", 
				"x", "junk", "combined", "yours", "did", "die", "battling", "negated", "controls", "resolve", "added", "deck", "moved", "do", "thunder", "down", "attacks", "leave", "exceed", "serpent", "skipping", "add", 
				"its", "pay", "wyrm", "zones", "tuners", "fusion", "take", "materials", "activation", "some", "applied", "for", "back", "choose", "remove", "random", "previously", "wasnt", "ratings", "duel", "end", "owner", 
				"winged", "six", "counter", "special", "with", "gy", "material", "fiend", "atks", "there", "phantasm", "attacked", "divine", "entire", "gains", "number", "gained", "revealed", "text", "per", "if", "order", 
				"reduce", "in", "lower", "is", "it", "detached", "field", "even", "bonze", "become", "other", "tributed", "targets", "top", "have", "sending", "activating", "detaching", "halve", "max", "change", "spirit", 
				"discarding", "draw", "tributes", "names", "machine", "equals", "able", "preceding", "attributes", "shuffle", "cyberse", "return", "lp", "use", "rating", "main", "reptile", "while", "second", "points", 
				"that", "declares", "opponent", "than", "continuous", "xyz", "insect", "different", "directly", "regardless", "summoning", "all", "always", "including", "level", "ritual", "already", "shuffled", "includes", 
				"less", "toon", "becomes", "chosen", "attaches", "mdl", "nd", "were", "lists", "scales", "activate", "attached", "activations", "arms", "position", "gys", "no", "equipped", "piercing", "activates", "total", 
				"times", "and", "of", "extra", "loses", "tokens", "row", "make", "on", "declared", "tied", "or", "tuner", "predator", "control", "exactly", "any", "chooses", "drew", "equip", "earth", "until", "inflict", 
				"activated", "summon", "drawing", "above", "destroys", "draws", "fire", "they", "excavated", "based", "using", "opponents", "them", "then", "toss", "each", "remaining", "pyro", "highest", "monsterss", 
				"difference", "shuffling", "except", "must", "conditions", "inflicted", "inflicts", "discarded", "warrior", "another", "sum", "fist", "long", "into", "current", "min", "defense", "are", "unless", "attack", 
				"does", "attribute", "attach", "takes", "so", "controller", "one", "makes", "many", "declaration", "monster", "controlled", "call", "face", "negate", "fish", "to", "but", "calculation", "treat", "declare", 
				"converter", "had", "aqua", "either", "leaves", "atk", "up", "has", "pendulum", "those", "treated", "this", "thrice", "look", "destroying", "longer", "tributing", "once", "name", "fields", "higher", "involving", 
				"coin", "changed", "next", "cards", "banish", "beast", "non", "nor", "not", "summoned", "now", "spells", "unaffected", "normal", "types", "apply", "again", "center", "was", "start", "battled", "way", "target", 
				"equal", "summons", "ones", "step", "battles", "time", "tribute", "play", "discard", "during", "dragon", "type", "when", "lost", "specifically", "trap", "dinosaur", "between", "phase", "still", "halved", 
				"players", "double", "lose", "destroy", "monsters", "lowest", "spellcaster", "light", "following", "card", "damage", "doubled", "columns", "direct", "gain", "rock", "couldnt", "ways", "dark", "place", 
				"chain", "more", "sided", "lps", "sends", "wrong", "token", "cannot", "heads", "choice", "randomly", "levels", "first", "flipped", "quick", "reveal", "unequip", "before", "possession", "own", "dice", "used", 
				"zone", "only", "from", "pointing", "combination", "otherwise", "resolves", "spell", "bottom", "sent", "water", "both", "gemini", "sequence", "effects", "twice", "transfer", "ranks", "effect", "keep", 
				"send", "who", "link", "reveals", "their", "banished", "can", "targeting", "discards", "psychic", "value", "player", "adds", "rest", "move", "original", "also", "instead", "increase", "attacking", "listed", 
				"currently", "third", "response", "neither", "detach,", "amount", "took", "places", "detatch", "side", "detach", "decrease", "which", "case", "returned", "last", "" 
		};
		this.name="spellcheck";
		this.help="Detects erorrs in spelling, as well as words that are not used in modern psct. If you use a "
				+ "decklink, it automatically adds all card names to the set of card names, as if you used $$name. "
				+ "Note this does correct spelling mistakes";
		this.category=new Command.Category("Psct Help");
		this.guildOnly=false;
		this.arguments = "<$$commands (optional)> {text OR duelingbook deckLink}";
		
		for(int i=0;i<wordsArr.length;i++) 
		{
			this.wordsSet.add(wordsArr[i]);
		}
		
	}
	
	
	
	
	
	@Override
	protected String runCard(String str, Card card, boolean isPendulum) {
		
		String text = this.getEffectText(str, card, isPendulum);
		
		HashSet<String> counterList = new HashSet<String>();
		for(String[] arr : this.counters) 
		{
			for(int i=0;i<arr.length;i++) 
			{
				counterList.add(arr[i]);
			}
		}

		boolean correctionsMade=false;
		
		
		
		
		String[] tempSplit=text.split("\"");//ignore text wrapped in \"
		for(int i=0;i<tempSplit.length;i+=2) 
		{
			//pre split handling
			tempSplit[i]=tempSplit[i].replace('\n', ' ');
			tempSplit[i]=tempSplit[i].replace('.', ' ');
			tempSplit[i]=tempSplit[i].replace('-', ' ');
			tempSplit[i]=tempSplit[i].replace('/', ' ');
		}
		if(!this.names.isEmpty()) 
		{
			int index=1;
			while(index<tempSplit.length) 
			{
				if(!names.contains(tempSplit[index].strip())) 
				{
					tempSplit[index]="**"+tempSplit[index]+"**";
					correctionsMade=true;
				} 
				index+=2;
			}
		}
		
		
		text="";
		for(int i=0;i<tempSplit.length;i++) 
		{
			text+=tempSplit[i]+"\"";
		}
		text=text.substring(0, text.length()-1);
		
		
		
		String[] textArr=text.split(" ");
		boolean quoteOn=false;
		
		for(int i=0;i<textArr.length;i++) 
		{
			
			int temp = 0;
			if(!(textArr[i]!=null)) continue;
			for(int j=0;j<textArr[i].length();j++) 
			{
				if(textArr[i].substring(j).startsWith("\"")) 
				{
					temp++;
				}
			}
			if(temp==1) 
			{
				quoteOn=!quoteOn;
				if(!quoteOn) 
				{
					continue;
				}
			}
			if(temp==2) 
			{
				if(quoteOn) 
				{
					return "looks like you have used quotation marks(\") incorrectly, please fix this and try again";
				}
				else 
				{
					continue;
				}
				
			}
			if(temp>2) 
			{
				return "looks like you have used quotation marks(\") incorrectly, please fix this and try again";
			}
			if(!quoteOn) 
			{
				String comparison = F.letterClean(textArr[i].toLowerCase());
				if(!this.wordsSet.contains(comparison)&&!counterList.contains(F.firstLetterToUpperCase(comparison)))
				{
					textArr[i]="**"+textArr[i]+"**";
					correctionsMade=true;
				}
			}
		}
		
		String msg="";
		for (int i=0;i<textArr.length;i++) 
		{
			msg=msg+textArr[i]+" ";
		}
		
		
		
		if(correctionsMade) 
		{
			msg +="\n```Bold words are misspelt or not used in modern PSCT.";
		}
		else 
		{
			msg = "```No errors found";
		}
		if(text.toLowerCase().contains("counter")&&this.counters.isEmpty()) 
		{
			msg+="\nCounter detected, if you are using a named counter you need to use the $$counter command. e.g. \"$$counter Nitro\"";
		}
		
		return msg + "```";
		
		
	}


	





}




























