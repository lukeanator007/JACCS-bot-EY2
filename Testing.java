package jaccsbot.jaccsbot;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashSet;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import net.dv8tion.jda.api.entities.Guild;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.*;

public class Testing extends Command {

	JDA jda;
	
	Testing(JDA jda)
	{
		this.jda=jda;
		this.name="testing";
		this.hidden=true;
		
	}
	
	
	@Override
	protected void execute(CommandEvent event) {
		if(!(event.getAuthor().getIdLong()==104284192401022976L||event.getAuthor().getIdLong()==159346872216059905L)) 
		{
			event.reply("This command is used by bot devs only.");
			return;
		}
		
		event.reply("testing");
		event.reply(jda.getGuilds().toString());
		
		
		
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
}
