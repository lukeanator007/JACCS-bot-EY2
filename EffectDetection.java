package jaccsbot.jaccsbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;

public class EffectDetection extends PSCTCommand{


	private boolean conjunctionsCheck=false; //used to check if any conjunctions were found in the effect
	private boolean noConjunctions;
	private HashSet<String> ctsCardTypes = new HashSet<String>();
	private HashSet<String> extraDeckMonsterTypes = new HashSet<String>();
	
	EffectDetection()
	{
		this.replyWrapper="```";
		this.name="effectdetection";
		this.aliases=new String[] {"EffectDetection", "effect detection"};
		this.help="Attempts to detect what types of effects your card has, telling you how your effects work. Use in combination with the fast "
				+ "effect chart to see if something has been made in error: if the effect is being detected as something other than "
				+ "what you want it to be, it probably has incorrect PSCT. EXPERIMENTAL";
		this.arguments="{card type(mon, spell, trap)} <cts> <commands(optional)> {effect text (do not include materials if any)}\nUse cts if the spell or trap you are using"
				+ " remains on the field";
		this.guildOnly=false;
		this.category=new Command.Category("Psct Help");
		
		String[] ctsCardTypesArr= new String[] {"Continuous", "Field","Equip"};
		
		for(int i=0;i<ctsCardTypesArr.length;i++) 
		{
			this.ctsCardTypes.add(ctsCardTypesArr[i]);
		}
		
		
		String[] extraDeckMonsterTypesArr = new String[] {"Fusion", "Synchro", "Xyz", "link"};
		
		for(int i=0;i<extraDeckMonsterTypesArr.length;i++) 
		{
			this.extraDeckMonsterTypes.add(extraDeckMonsterTypesArr[i]);
		}
		
	}
	
	
	
	@Override
	protected String runCard(String str, Card card, boolean isPendulum) {
		
		
		//split the text into words and parse the command
		String text=this.getEffectText(str, card, isPendulum);
		
		boolean mon=false;
		boolean spell=false;
		boolean trap=false;
		boolean cts=false;
		
		
		if(str!=null) 
		{
			try 
			{
				String[] args=text.split(" ");
				//TODO optimise spilt
				if(args[0].equalsIgnoreCase("monster")) 
				{
					mon=true;
					text=text.substring(8);
				}
				else if(args[0].equalsIgnoreCase("mon")) 
				{
					mon=true;
					text=text.substring(4);
				}
				else if (args[0].equalsIgnoreCase("spell")) 
				{
					spell=true;
					text=text.substring(6);
					
				}
				else if (args[0].equalsIgnoreCase("trap")) 
				{
					trap=true;
					text=text.substring(5);
				}
				else 
				{
					
					return "please give a card type, monster, spell or trap";
				}
				if(args[1].equalsIgnoreCase("cts")) 
				{
					cts=true;
					text=text.substring(4);
				}
			}
			catch(Exception e) 
			{
				// called when the substring function errors due to lack of text
				return("You have not provided an effect.");
			}
		}
		else 
		{
			
			if(isPendulum) 
			{
				spell=true;
				cts=true;
			}
			else 
			{
				mon=card.isMonster();
				spell=card.isSpell();
				trap=card.isTrap();
				if(!mon) 
				{
					cts=this.ctsCardTypes.contains(card.getType());
				}
				else 
				{
					if(this.extraDeckMonsterTypes.contains(card.getMonsterType())) 
					{
						text = text.substring(text.indexOf('\n')+1);
					}
					
				}
			}
			
			
		}
		
		
		List<String> list=new ArrayList<String>();
		
		//prepare for sentence split
		text=text.replace("\n", ".");
		text=text.replace(".)", "))."); //lines ending with .) are marked by "))" to prevent ")" spilling over into the next line and to track them 
		text=text.replace("..", ".");
		
		int refferenceIndex=0;
		int quoteIndex=text.indexOf("\"",refferenceIndex);
		int dotIndex=text.indexOf(".",refferenceIndex);
		
		//split(".") while ignoring "."s surrounded by "\"" 
		while (dotIndex>-1) 
		{
			if(dotIndex<quoteIndex||(quoteIndex<0)) 
			{
				list.add(text.substring(0, dotIndex).strip());
				text=text.substring(dotIndex+1);
				refferenceIndex=0;
			}
			else 
			{
				int quoteIndex2=text.indexOf("\"", quoteIndex+1);
				if(quoteIndex2<0) 
				{
					return "It looks like you have used quotation marks incorrectly(\"), please correct this and try again";
				}
				refferenceIndex=quoteIndex2+1;
			}
			dotIndex=text.indexOf(".",refferenceIndex);
			quoteIndex=text.indexOf("\"",refferenceIndex);
			
		}
		
		
		
		
		String[] sentences= list.toArray(new String[list.size()]);
		
		
		List<String> effects=new ArrayList<String>();
		
		//begin core effect check
		String temp=null;
		for(int i=0;i<sentences.length;i++) 
		{
			if(sentences[i].isBlank()) 
			{
				continue;
			}
			//find effect category and call the relevant detection method
			//TODO optimise checks
			temp=this.checkReminderText(sentences[i].toLowerCase());//ignore lines ending in .)
			if(!(temp!=null)) 
			{
				
				temp=bulletPointCheck(sentences[i]);
				if(temp!=null) 
				{
					temp=this.findBulletReason(sentences,i, mon, spell, trap, cts);
				}
				else 
				{
					temp=checkCondition(sentences[i].toLowerCase());
					if(!(temp!=null)) 
					{
						temp=effectCheck(sentences[i].toLowerCase(),mon, spell, trap, cts, i==0);
						if(temp!=null) 
						{
							temp=temp+this.getConjunctions(sentences[i].substring(1+Math.max(sentences[i].indexOf(':'), sentences[i].indexOf(';'))));
						}
						else 
						{
							temp="This effect couldn't be identified, it may be using incorrect PSCT or I might not know this type of effect\f";
						}
					}
				
					temp="("+(i+1)+") "+sentences[i]+".\n"+temp+"\n"+"\f";
					
				
				
				}
			}
			
			effects.add(temp);
		}
		
		if(this.conjunctionsCheck) 
		{
			effects.add("Note conjunctions only apply when the effect is resolving, they do not affect activation legality, in general, if a "
					+ "part of an effect is not optional, it must be able to resolve at time of activation, but there are exceptions to this.\f");
		}
		//reset conjunction check
		this.conjunctionsCheck=false;
		
		
		String msg="";
		
		for(String str1:effects) 
		{
			msg+=str1;
		}
		
		if(msg.length()==0) 
		{
			return ("No effects found, make sure you are using \".\" and new lines in the correct places");
		}	
		else 
		{
			return msg;			
		}
		
		
		
	}

	private String checkReminderText(String line) {
		
		if(line.endsWith("))"))
		{
			return "";	
		}
		
		
		
		return null;
	}



	/**
	 * 
	 * 
	 * 
	 * @param sentences all the sentences in the effect
	 * @param index the index of the effect to detect
	 * @param monster if card is a monster
	 * @param spell if card is a spell
	 * @param trap if card is a trap
	 * @param cts if card is a continuous card
	 * @return the effect detection analysis of this effect
	 */
	private String findBulletReason(String[] sentences, int index, boolean monster, boolean spell, boolean trap, boolean cts) 
	{
		String ans=null;
		String line=sentences[index].substring(2).toLowerCase(); //remove bullet point from line
		//TODO check for the thing after the bullet point and error if it is incorrect
		boolean isFirstLine=false;
		
		int i=index-1;
		boolean test=false;
		//check the first line before all bullet points to check if it is a condition or contains the word following
		for(;i>-1;i--) 
		{
			if(!sentences[i].startsWith("●")&&!sentences[i].isEmpty()) 
			{
				if(!(this.checkCondition(sentences[i])!=null)||sentences[i].contains(" following ")) 
				{
					test=true;
					break;
				}
			}
			
		}
		if(test) 
		{
			if(i==0) 
			{
				isFirstLine=true;
			}
			
			if(sentences[i].contains("based on")||sentences[i].contains("depending on")) 
			{
				line=line.substring(line.indexOf(':')+2);
			}
			
			
			if(sentences[i].contains("activate 1 of these effects")) 
			{
				ans=this.effectCheck(sentences[i]+line,monster, spell, trap, cts, isFirstLine);
				ans="("+(index+1)+") "+sentences[i]+line+".\n"+ans+"\n";
			}
			else if(sentences[i].indexOf("apply")<sentences[i].indexOf("effect")&&sentences[i].contains("apply")) //check for "apply" some amount of these "effect(s)"
			{
				ans=this.getConjunctions(line);
				ans="("+(index+1)+") "+sentences[i]+line+".\n"+ans+"\n";
			} 
			else if(sentences[i].indexOf("gains")<sentences[i].indexOf("effect")&&sentences[i].contains("gains"))  //check for "gains" the following "effect(s)"
			{
				ans=this.effectCheck(line,monster, spell, trap, cts, isFirstLine);
				ans="("+(index+1)+") "+line+".\n"+ans+"\n";
			} 
			else if(sentences[i].contains("includes")) 
			{
				ans="";
			} 
			else 
			{
				ans=this.effectCheck(line,monster, spell, trap, cts, isFirstLine);
				ans="("+(index+1)+") "+line+".\n"+ans+"\n";
			}
		}
		else 
		{
			ans=this.effectCheck(sentences[index],monster, spell, trap, cts, isFirstLine);	
			ans="    "+sentences[index]+".\n"+ans+"\n";		
		}
		
		
		
		
		
		return ans+"\f";
	}




	
	
	
	private String bulletPointCheck(String str) 
	{
		if(str.indexOf('●')>-1) 
		{
			return "";
		}
		
		
		
		return null;
	}
	

	
	/**
	 * takes a given quick effect and returns the information about it
	 * 
	 * @param line the quick effect effect
	 * @return the information about the quick effect 
	 */
	private String effectCheckQuickEffect(String line)
	{
		String ans="This effect appears to be a quick effect, it is spell speed 2, and can be activated at nearly any time. ";
		if(line.startsWith("when")) 
		{
			if(line.indexOf("activate")<line.indexOf(':'))
			{
				if(line.indexOf("you can: ")>-1) 
				{
					ans=ans+"This effect is optional, and must be directly chained to an activated effect. ";
				}
				else 
				{
					ans=ans+"This effect is mandatory, and must be directly chained to an activated effect. ";
				}
			}
			
		}
		
		return ans;
	}
	
	/**
	 * takes a given trigger effect and returns information about it
	 * 
	 * @param line the trigger effect
	 * @param phaseCheck the phase the effect activates, or "" otherwise
	 * @return
	 */
	private String triggerEffectCheck(String line, String phaseCheck) 
	{
		String ans="";
		if(line.startsWith("when"))
		{
			if(line.contains(": you can")) 
			{
				ans="This effect appears to be an optional trigger effect, it can be activated in the next chain available after "
						+ "meeting its trigger condition, it is spell speed 1 and can be activated during either players turn"
						+ " unless stated otherwise.\nThis effect can \"miss the timing\". This means that if the trigger to "
						+ "activate was not the last thing to happen, this effect cannot activate. Common examples of this "
						+ "include being used as synchro material etc or meeting the trigger at chain link 2 or higher. ";
			}
			
			
			
		} 
		else if (line.startsWith("if")) 
		{
			if(line.contains(": you can")) 
			{
				ans="This effect appears to be an optional trigger effect, it can be activated in the next chain available after "
						+ "meeting its trigger condition, it is spell speed 1 and can be activated during either players turn"
						+ " unless stated otherwise. ";
			}
			else 
			{
				ans="This effect appears to be a mandatory trigger effect, it must be activated in the next chain available after "
						+ "meeting its trigger condition, it is spell speed 1 and can be activated during either players turn"
						+ " unless stated otherwise. ";
			}
		}
		else if(line.startsWith("durring ")&&line.indexOf(" phase")<line.indexOf(':')&&line.contains(" phase")) 
		{	
			if(line.contains(": you can")) 
			{
				ans="This effect appears to be an optional trigger effect";
			}
			else 
			{
				ans="This effect appears to be a mandatory trigger effect";
			}
			ans=ans+", it is activated in the given phase durring an open gamestate on your turn, or when your opponent passes "
					+ "prioity from an open gamestate in your opponents turn";
			
			
			
			
		}
		
		
		if(line.substring(line.indexOf(' ')+1).startsWith("this card is activated")) 
		{
			ans=ans+"This effect is/can be activated in the same chain link as the card is activated in";
		}
		
		
		
		return ans;
	}
	
	/**
	 * takes a given trap card effect and returns information about it
	 * 
	 * @param line the trap effect
	 * @param phase the phase of the effect, if any, or "" otherwise
	 * @param firstLine if this effect is on the first line
	 * @return
	 */
	private String effectCheckTrapCheck(String line, String phase, boolean firstLine) 
	{
		String ans="";
		
		int temp=1+Math.max(line.indexOf(':'), line.indexOf(';'));
		
		
		if(line.startsWith("when")||(line.startsWith("if")&&!line.startsWith("if you control"))) 
		{
			if(!(line.indexOf("activate")<line.indexOf(':')&&line.contains("activate")))
			{
				ans=this.triggerEffectCheck(line, phase);
			}
			else 
			{
				ans=this.effectCheckQuickEffect(line);
			}
		}
		else if(line.startsWith("durring ")&&line.indexOf(" phase")<line.indexOf(':')&&line.contains(" phase")&&
				!(phase=="main"||phase=="battle")) 
		{
			ans=this.triggerEffectCheck(line, phase);
		}
		else if(firstLine&&line.substring(temp).startsWith("special summon this card")) 
		{
			ans="This is the effect activated when the card is activated";
		}
		else 
		{
			ans=this.effectCheckQuickEffect(line);
		}
		
		
		
		return ans;
	}
	
	
	
	
	
	/**
	 * checks a given effect and returns information about it by detecting the type of 
	 * effect and running it through the appropriate method 
	 * 
	 * @param line the effect
	 * @param mon if the card is a monster
	 * @param spell if the  card is a spell
	 * @param trap if the card is a trap
	 * @param cts if the card is a continuous card
	 * @param isFirstLine if the effect is on the first line
	 * @return information about the effect
	 */
	private String effectCheck(String line, boolean mon, boolean spell, boolean trap, boolean cts, boolean isFirstLine) 
	{
		if(line.indexOf(" per ")<line.indexOf(':')&&line.contains(" per ")) 
		{
			line=line.substring(line.indexOf(',')+2);
		}
		
		String phaseCheck="";
		try {
			phaseCheck= line.substring(line.lastIndexOf(' ', line.indexOf(" phase") - 1), line.indexOf(" phase"));
		} catch (Exception e) {
		}
		//get the phase if any
		
		String ans="\nEffect type:\n";
		if((spell||trap)&&isFirstLine&&!cts) 
		{
			if(trap) 
			{
				ans=ans+this.effectCheckQuickEffect(line);
			}
			ans="This is the effect that is activated when the card is activated. "+ans+this.getCostsAndConditions(line);
			
		}
		else 
		{
			if(line.indexOf(':')>-1||line.indexOf(';')>-1) 
			{
				
				if(line.indexOf("(quick effect):")>-1&&mon) 
				{
					ans=ans+this.effectCheckQuickEffect(line);
				}
				else if(!trap) 
				{
					if(line.startsWith("when"))
					{
						ans=ans+this.triggerEffectCheck(line,phaseCheck);
					} 
					else if (line.startsWith("if")&&!(line.startsWith("if you control")||line.startsWith("if this card is in your "))) 
					{
						ans=ans+this.triggerEffectCheck(line,phaseCheck);
					}
					else if(line.startsWith("durring ")&&line.indexOf(" phase")<line.indexOf(':')&&line.contains(" phase")&&
							phaseCheck!="main") 
					{
						ans=ans+this.triggerEffectCheck(line,phaseCheck);
					}
					else if(line.startsWith("you can")||line.contains(": you can "))
					{
						ans=ans+"This appears to be an ignition effect, it is an optional effect that can be activated while the card is face up "
								+ "on the field unless stated otherwise. It is spell speed 1 and can only be activated on your turn in an open "
								+ "gamestate. ";
					}
					else 
					{
						return ans+"This appears to be an ignition effect, but it is mandatory, which is incorrect PSCT, make the activation of the "
								+ "effect optional to fix this";
					}
					
				} 
				else 
				{
					ans=ans+this.effectCheckTrapCheck(line,phaseCheck, isFirstLine);
					
				}
				if(ans!="") 
				{
					ans=ans+this.getCostsAndConditions(line);					
				}
			}
			else 
			{
				if((line.contains("(from your ")&&mon)||line.contains("you can also xyz summon ")&&mon) 
				{
					ans=ans+"This effect appears to be a Special Summon effect. This method of summoning does not start a chain, and can be "
							+ "performed during your Main Phase in an open gamestate";
				}
				else 
				{
					ans=ans+"This effect appears to be a continuous effect, it will apply while the card is face-up on the field, unless "
							+ "otherwise stated. The effect can apply while a chain is resolving. ";
				}
				
				
			}
		}
		
		return ans+"\f";
		
		
	}
	
	
	
	/**
	 * checks the conjunctions of a given clause and returns information about them
	 * clauses are separated by : ; or .
	 * 
	 * @param clause the clause to check
	 * @return information about each conjunction in the clause
	 */
	private String getConjunctions(String clause) 
	{
		int index=0;
		String clone=clause;
		
		String ans="";
		if(this.noConjunctions) 
		{
			return "";
		}
		
		
		int finalIndex=0;
		for(int i=0;i<clone.length();i++) 
		{
			int j=i;
			if(clone.substring(i).startsWith(" and if you do, ")) 
			{
				i+=" and if you do, ".length();
				ans=ans+(clone.substring(index, i)+"\n<Do A, and if you do, do B>\r\n" + 
						"    Timeline: Considered simultaneous. Both happen at the same time.\r\n" + 
						"\r\n" + 
						"    Causation: A is required for B, but NOT vice-versa. If A does not happen, then stop. If B "
						+ "cannot happen, you still do A.\n\n"+"\f");
				
			}
			else if(clone.substring(i).startsWith(", also, after that, ")) 
			{
				i+=" also, after that, ".length();
				ans=ans+(clone.substring(index, i)+"\n<Do A, then also do B>\r\n" + 
						"    Timeline: B happens after A, even though they’re part of one card effect. These things happen in sequence, "
						+ "not simultaneously.\r\n" + 
						"\r\n" + 
						"    Causation: Neither is required for the other. Just do as much as you can!\n\n"+"\f");
			}
			else if(clone.substring(i).startsWith(" and ")) 
			{
				i+=" and ".length();
				ans=ans+(clone.substring(index, i)+"\nAND: <Do A and B>\r\n" + 
						"    Timeline: Considered simultaneous. Both happen at the same time.\r\n" + 
						"\r\n" + 
						"    Causation: BOTH are required. If you cannot do both, then you do nothing.\n\n"+"\f");
			}
			else if(clone.substring(i).startsWith(", also ")) 
			{
				i+=", also ".length();
				ans=ans+(clone.substring(index, i)+"\nALSO: <Do A, also do B>\r\n" + 
						"    Timeline: Considered simultaneous. Both happen at the same time.\r\n" + 
						"\r\n" + 
						"    Causation: Neither is required for the other. Just do as much as you can!\n\n"+"\f");
			}
			else if(clone.substring(i).startsWith(", then")) 
			{
				i+=", then".length();
				ans=ans+clone.substring(index, i)+"\n<Do A, then do B>\r\n" + 
						"    Timeline: B happens after A, even though they’re part of one card effect. These things happen in sequence, "
						+ "not simultaneously.\r\n" + 
						"\r\n" + 
						"    Causation: A is required for B, but NOT vice-versa. If A does not happen, then stop. If B cannot happen, "
						+ "you still do A.\n\n"+"\f";
			}
			if(j!=i) 
			{
				finalIndex=i;
			}
			
		}
		
		
		if(ans.equals("")) 
		{
			ans="\n\nThis effect has no conjunctions, there is only 1 part to this effect\n"+"\f";
		}
		else 
		{
			ans="\n\nConjunctions:\n"+ans+clone.substring(finalIndex);
			this.conjunctionsCheck=true;
		}
		
		
		
		
		
		return ans;
	}
	
	
	
	
	
	
	/**
	 * gets the conditions and costs of an activated effect
	 * 
	 * @param line the effect
	 * @return information about the costs and conditions
	 */
	private String getCostsAndConditions(String line) 
	{
		String ans="\nActivation requirments and text:\n";
		int index1=line.indexOf(":");
		if(index1>-1) 
		{
			ans=ans+ "\""+line.substring(0, index1)+"\" is the activation condition, this must be met before the effect can be activated";
			index1++;
		}
		else 
		{
			ans=ans+"This effect has no activation condition, it can be activated at any time the card/effect could normaly activate";
		}
		int index2=line.indexOf(";");
		if(index2>-1) 
		{
			
			ans=ans+"\n\""+line.substring(index1+1,index2)+"\" is the activation text, and is performed when the effect is activated, before resolving "
					+ "the chain.";
		}
		else 
		{
			ans=ans+"\nThis effect has no activation text, nothing happens when this effect is activated before resolving the chain";
		}
		
		 
		ans="\n"+ans;
		
		
		return ans;
	}
	

	/**
	 * called by checkCondition
	 * 
	 * @param line the effect
	 * @param check "activate" or "use"
	 * @return information about the condition
	 */
	private String checkConditionCall1(String line,String check) 
	{
		int indexActivate1=line.indexOf("you "+check+" this");
		if(indexActivate1>-1) 
		{
			int indexActivate2=-1;
			
			for(int i=indexActivate1-2;i>-1;i--) 
			{
				if(line.charAt(i)==' ') 
				{
					indexActivate2=i;
					break;
				}
			}
			
			return"\nThe"+line.substring(indexActivate2, indexActivate1-1)+" you "+check+" this effect, the restriction applies";
			
		}
		
		return "";
	}
	
	/**
	 * checks if a given effect is a condition
	 * 
	 * @param line the effect to be checked
	 * @return information about the condition if a condition is given and null otherwise
	 */
	private String checkCondition(String line) 
	{
		String ans=null;
		if(line.startsWith("you can only")||line.startsWith("you cannot")) 
		{
			ans="This effect appears to be a conditional restriction.";
			
			ans+=this.checkConditionCall1(line, "activate");
			ans+=this.checkConditionCall1(line, "use");
			
			//TODO make more smart
			//TODO make everything check for incorrect PSCT
		}
		else if((line.startsWith("must be")||line.startsWith("must first be"))&&!line.contains("(from your"))
		{
			ans="This effect is a summoning restriction";
			
		}
		else if(line.startsWith("cannot be")&&(line.contains("summon")||line.contains("set"))) 
		{
			ans="This effect appears to be a summoning restriction";
		}
		else if(line.startsWith("Activate this card only")) 
		{
			ans="This effect appears to be a restriction on the activation of a Spell/Trap that remains face-up on the field";
		}
		
		if(ans!=null) 
		{
			return ans+"\f";
		}
		else 
		{
			return null;
		}
		
	}
	

	
	
	
	
	
	
	
	
	
}



























































