package jaccsbot.jaccsbot;

import java.util.HashSet;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Invite.Channel;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;


/**
 * Hello world!
 *
 */
public class App 
{
	static String version="stable 0";
	static int verInt1=0;
	static int verInt2=9;
	
	static long testingGuildID=586975397212717075L;
	static long bugChannelID=780413435656667147L;
	
	
    public static void main( String[] args ) throws LoginException
    {
    	version=version+"."+verInt1+"."+verInt2;
    	
    	String token = "main token";//JACCS
    	//String token="testing token";//testing 
    	//tokens omitted for public viewing
        JDA jda = JDABuilder.createDefault(token).build();
        

        
        jda.addEventListener(new Meme());
        jda.addEventListener(new EmbedRemove());
        
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setPrefix("j!");
        builder.setHelpWord("help");
        builder.setOwnerId("104284192401022976");
        
        
        //psct
        builder.addCommand(new PsctCaps());
        //builder.addCommand(new EffectDetection()); removed from current version due to needed several fixes
        builder.addCommand(new SpellCheck());
        
        
        builder.addCommand(new Testing(jda));
        
        //commands
        builder.addCommand(new SetSubCommands());
        builder.addCommand(new GetSubCommands());
        builder.addCommand(new SubCommandHelp());
        
        
        //utility
        builder.addCommand(new FastEffectChart());
        builder.addCommand(new PsctDocs());
        builder.addCommand(new Version(new VersionChanges().patchNotes, version, verInt1, verInt2));
        builder.addCommand(new Feedback(bugChannelID));
        builder.addCommand(new Invite());
        
        //meme
        builder.addCommand(new Dot());
        builder.addCommand(new Delete());
        
        CommandClient client = builder.build();
        
        
        jda.addEventListener(client);
        
        
        
        
        
        
    }
    
    
    
    
    
    
    
}




