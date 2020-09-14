package jaccsbot.jaccsbot;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Meme extends ListenerAdapter{

	public Meme() 
	{
		
	}
	
	
	
	public void onGuildMessageReceived (GuildMessageReceivedEvent event) 
	{	
		String txt=event.getMessage().getContentRaw();
		
		if(event.getGuild().getIdLong()!= 170669983079071745L) 
		{
			if(txt.indexOf("Splain")>-1) 
			{
				event.getChannel().sendMessage("Dear sir/madam,\r\n" + 
						"It has come to my attention that your grammatical skills need improvement. "
						+ "You seem to have made the error of capitalizing the suffix \"splain\", "
						+ "an error one would expect of only baboons. "
						+ "Clearly, a grammatical lesson concerning the suffix is required. "
						+ "To define a Suffix, it is an affix occurring at the end of a word, base, or phrase. "
						+ "Examples being \"itis\", \"ing\", \"splain\", \"able\". "
						+ "For clarification, an affix is one or more sounds or letters occurring as a bound form attached "
						+ "to the beginning or end of a word, base, or phrase or inserted within a word or base and serving "
						+ "to produce a derivative word or an inflectional form (Source - Merriam-Webster). In Layman's terms, "
						+ "these are syllables that are added to the end of an existing word to add to or change the meaning, "
						+ "and become part of the word. These are not capitalized, as they are a continuation of the word. "
						+ "I thank you for taking the time to take this lesson, and so as to not use any more of your time, "
						+ "i ask that you please refrain from making this grammatical error in the future, else we will need "
						+ "to teach this lesson once more.").queue();
			}
		}
		
			
		if(event.getGuild().getId().equals("623213807023554560")) 
		{
			//event.getMessage().delete().queue();
			//event.getChannel().sendMessage("no").queue();
			
			
		}
		
		
		
	}
	
	
}
