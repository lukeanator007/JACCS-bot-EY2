package jaccsbot.jaccsbot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;


import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Testing extends Command {


	
	HashSet<String> words=new HashSet<String>();
	
	Testing()
	{
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
		
		event.reply("1234\f1234\f2");

	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
