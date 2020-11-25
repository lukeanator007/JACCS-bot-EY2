package jaccsbot.jaccsbot;

import java.util.HashSet;
import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public abstract class PSCTCommand extends Command{
	
	protected HashSet<String[]> counters=new HashSet<String[]>();
	protected HashSet<String> addedWords=new HashSet<String>();
	protected HashSet<String> names=new HashSet<String>();
	protected boolean noReasons;
	protected boolean noConjunctions;
	/**
	 * the string wrapped around each reply message. use \"```\" for code boxes
	 */
	protected String replyWrapper="";
	

	protected void execute(CommandEvent event) 
	{
		JaccsCommandHandler commands =JaccsCommandHandler.getCommandHandler(event.getAuthor().getIdLong());
		try {
			commands = JaccsCommandHandler.parseCommands(event.getArgs(), commands);
		} catch (JaccsCommandException e1) {
			event.reply(e1.getMessage());
			return;
		}
		runCommands(commands);
		
		String text=commands.getText();
		
		String msg="";
		if(text.startsWith("https://www.")) 
		{
			event.reply("processing...");
			List<Card> cards = null;
			try {
				cards = DeckLinkParser.parseLink(text);
				
			} catch (Exception e) {
				event.reply(e.getMessage());
			}
			for(Card card : cards) 
			{
				this.names.add(card.getName());
			}
			
			
			for(Card card : cards) 
			{
				if(card.getPendulumEffect().length()>0) msg+=card.getName()+" pendulum effect\n"+runCard(null, card, true)+"\n\n\f";
				if(card.hasEffect()) 
				{
					msg+=card.getName()+"\n"+runCard(null, card, false)+"\n\n\f";
				}
				
			}
			
			
			
		}
		else 
		{
			msg=runCard(text, null, false);
		}
		
		//send reply msg without exceeding discord character limit and without braking up statements 
		// /f is read by discord as blank, not as a space, so it does not affect the user end message
		while (msg.length()>1990) 
		{
			event.reply(replyWrapper+msg.substring(0,msg.lastIndexOf('\f', 1990))+replyWrapper);
			msg=msg.substring(msg.lastIndexOf('\f', 1990));
			
		}
		event.reply(replyWrapper+msg+replyWrapper);	
		
	}
	
	/**
	 * gets the effect text, regardless of which of card/str is null
	 * also handles the pendulum case
	 * 
	 * @param str
	 * @param card
	 * @param isPendulum
	 * @return
	 */
	protected String getEffectText(String str, Card card, boolean isPendulum) 
	{
		String text;
		if(str!= null) text = str;
		else if(isPendulum) text = card.getPendulumEffect();
		else text = card.getEffect();
		return text;
	}
	
	
	/**
	 * take a card OR effect string and return the relevant psct corrections
	 * @param effect
	 * @param card
	 * @param isPendulum if the card is a pendulum, this function will be called twice, once for the monster effect
	 * then once for the pendulum effect, in which case isPendulum is set to true
	 * 
	 * @return
	 */
	protected abstract String runCard(String effect, Card card, boolean isPendulum);

	private void runCommands(JaccsCommandHandler commands) 
	{
		//counters
		HashSet<String> counterNames=commands.getCounterNames();
		this.counters.clear();
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
		
		this.names = commands.getNames();
		this.addedWords.addAll(commands.getWords());
		this.noConjunctions=commands.isNoConjunctions();
		
		
		
		
		
	}
	
	
	
	
	
	
}






































