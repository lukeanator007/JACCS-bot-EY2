package jaccsbot.jaccsbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;

public class EffectDetection extends Command{


	private boolean conjunctionsCheck=false;
	private boolean noConjunctions;
	
	
	EffectDetection()
	{
		
		this.name="effectdetection";
		this.aliases=new String[] {"EffectDetection", "effect detection"};
		this.help="Attempts to detect what types of effects your card has, telling you how your effects work. Use in combination with the fast "
				+ "effect chart to see if something has been made in error: if the effect is being detected as something other than "
				+ "what you want it to be, it probably has incorrect PSCT. EXPERIMENTAL";
		this.arguments="{card type(mon, spell, trap)} <cts> <commands(optional)> {effect text (do not include materials if any)}\nUse cts if the spell or trap you are using"
				+ " remains on the field";
		this.guildOnly=false;
		this.category=new Command.Category("Psct Help");
	}
	
	private void runCommands(JaccsCommandHandler commands) 
	{
		
		
		//conjunctions
		this.noConjunctions=commands.isNoConjunctions();
		
		
	}
	
	@Override
	protected void execute(CommandEvent event) {
		
		
		
		String text=event.getArgs();
		
		String[] args=text.split(" ");
		boolean mon=false;
		boolean spell=false;
		boolean trap=false;
		boolean cts=false;
		
		
		try 
		{
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
				event.reply("please give a card type, monster, spell or trap");
				return;
			}
			if(args[1].equalsIgnoreCase("cts")) 
			{
				cts=true;
				text=text.substring(4);
			}
		}
		catch(Exception e) 
		{
			event.reply("You have not provided an effect.");
		}
		
		JaccsCommandHandler commands =JaccsCommandHandler.getCommandHandler(event.getAuthor().getIdLong());
		try {
			commands=JaccsCommandHandler.parseCommands(text, commands);
		} catch (JaccsCommandException e) {
			event.reply(e.getMessage());
			return;
		}
		runCommands(commands);
		text=commands.getText();
		
		List<String> list=new ArrayList<String>();
		
		
		

		text=text.replace("\n", ".");
		text=text.replace(".)", ")).");
		text=text.replace("..", ".");
		
		int refferenceIndex=0;
		int quoteIndex=text.indexOf("\"",refferenceIndex);
		int dotIndex=text.indexOf(".",refferenceIndex);
		
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
					event.reply("It looks like you have used quotation marks incorrectly(\"), please correct this and try again");
					return;
				}
				refferenceIndex=quoteIndex2+1;
			}
			dotIndex=text.indexOf(".",refferenceIndex);
			quoteIndex=text.indexOf("\"",refferenceIndex);
			
		}
		
		
		
		
		String[] sentences= list.toArray(new String[list.size()]);
		
		
		List<String> effects=new ArrayList<String>();
		
		
		String temp=null;
		for(int i=0;i<sentences.length;i++) 
		{
			if(sentences[i].isBlank()) 
			{
				continue;
			}
						
			temp=this.checkReminderText(sentences[i].toLowerCase());
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
		
		
		
		
		String msg="```";
		
		for(String str:effects) 
		{
			msg+=str;
		}
		msg+="```";
		
		
		
		while (msg.length()>1990) 
		{
			event.reply(msg.substring(0,msg.lastIndexOf('\f', 1990))+"```");
			msg="```"+msg.substring(msg.lastIndexOf('\f', 1990));
			
		}
		
		
		
		
		if(msg.equals("``````")) 
		{
			event.reply("No effects found, make sure you are using \".\" and new lines in the correct places");
		}	
		else 
		{
		event.reply(msg);			
		}
		this.conjunctionsCheck=false;
		
	}

	private String checkReminderText(String line) {
		
		if(line.endsWith("))"))
		{
			return "";	
		}
		
		
		
		return null;
	}




	private String findBulletReason(String[] sentences, int index, boolean monster, boolean spell, boolean trap, boolean cts) 
	{
		String ans=null;
		boolean test=false;
		String line=sentences[index].substring(2).toLowerCase();
		boolean isFirstLine=false;
		
		int i=index-1;
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
			else if(sentences[i].indexOf("apply")<sentences[i].indexOf("effect")&&sentences[i].contains("apply")) 
			{
				ans=this.getConjunctions(line);
				ans="("+(index+1)+") "+sentences[i]+line+".\n"+ans+"\n";
			} 
			else if(sentences[i].indexOf("gains")<sentences[i].indexOf("effect")&&sentences[i].contains("gains")) 
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
	
	private String triggerEffectCheck(String line, String test) 
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
	
	
	private String effectCheckTrapCheck(String line, String test, boolean firstLine) 
	{
		String ans="";
		
		int temp=1+Math.max(line.indexOf(':'), line.indexOf(';'));
		
		
		if(line.startsWith("when")||(line.startsWith("if")&&!line.startsWith("if you control"))) 
		{
			if(!(line.indexOf("activate")<line.indexOf(':')&&line.contains("activate")))
			{
				ans=this.triggerEffectCheck(line, test);
			}
			else 
			{
				ans=this.effectCheckQuickEffect(line);
			}
		}
		else if(line.startsWith("durring ")&&line.indexOf(" phase")<line.indexOf(':')&&line.contains(" phase")&&
				!(test=="main"||test=="battle")) 
		{
			ans=this.triggerEffectCheck(line, test);
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
	
	
	
	
	
	
	private String effectCheck(String line, boolean mon, boolean spell, boolean trap, boolean cts, boolean isFirstLine) 
	{
		if(line.indexOf(" per ")<line.indexOf(':')&&line.contains(" per ")) 
		{
			line=line.substring(line.indexOf(',')+2);
		}
		
		String test="";
		try {
			test= line.substring(line.lastIndexOf(' ', line.indexOf(" phase") - 1), line.indexOf(" phase"));
		} catch (Exception e) {
		}
		
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
						ans=ans+this.triggerEffectCheck(line,test);
					} 
					else if (line.startsWith("if")&&!(line.startsWith("if you control")||line.startsWith("if this card is in your "))) 
					{
						ans=ans+this.triggerEffectCheck(line,test);
					}
					else if(line.startsWith("durring ")&&line.indexOf(" phase")<line.indexOf(':')&&line.contains(" phase")&&
							test!="main") 
					{
						ans=ans+this.triggerEffectCheck(line,test);
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
					ans=ans+this.effectCheckTrapCheck(line,test, isFirstLine);
					
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
		
		if(ans!=null) 
		{
			return ans+"\f";
		}
		else 
		{
			return null;
		}
		
		
	}
	
	
	
	
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
		
		
		if(ans=="") 
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
	 * gets the conditions and "costs" of an activated effect
	 * 
	 * @param line
	 * @return 
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



























































