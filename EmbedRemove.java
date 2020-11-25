package jaccsbot.jaccsbot;


import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EmbedRemove extends ListenerAdapter{
	
	EmbedRemove()
	{
		
	}
	
	
	
	public void onGuildMessageReceived (GuildMessageReceivedEvent event) 
	{
		//remove embeds from messages containing dueling book links
		if(event.getMessage().getContentRaw().contains("https://www.duelingbook.com")) 
		{
			try 
			{
				event.getMessage().suppressEmbeds(true).queue();
			}
			catch(Exception e) 
			{
				e.printStackTrace();
			}
		}
		

		
	}
	

}
