package jaccsbot.jaccsbot;

import java.util.HashSet;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;

import net.dv8tion.jda.api.*;


/**
 * Hello world!
 *
 */
public class App 
{
	static String version="stable 0";
	static int verInt1=0;
	static int verInt2=8;
	
	
    public static void main( String[] args ) throws LoginException
    {
    	version=version+"."+verInt1+"."+verInt2;
    	
    	String token = "main Token";//JACCS
    	//String token="testing token";//testing 
    	//tokens omitted for public viewing
        JDA jda = new JDABuilder(token).build();

        jda.addEventListener(new Meme());
        jda.addEventListener(new EmbedRemove());
        
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setPrefix("j!");
        builder.setHelpWord("help");
        builder.setOwnerId("104284192401022976");
        
        
        //psct
        builder.addCommand(new PsctCaps());
        builder.addCommand(new EffectDetection());
        builder.addCommand(new SpellCheck());
        
        
        builder.addCommand(new Testing());
        
        //commands
        builder.addCommand(new SetCommands());
        builder.addCommand(new GetCommands());
        builder.addCommand(new CommandHelp());
        
        
        //utility
        builder.addCommand(new FastEffectChart());
        builder.addCommand(new PsctDocs());
        builder.addCommand(new Version(new VersionChanges().patchNotes, version, verInt1, verInt2));
        builder.addCommand(new Feedback());
        
        //meme
        builder.addCommand(new Dot());
        builder.addCommand(new Delete());
        
        CommandClient client = builder.build();
        
        
        jda.addEventListener(client);
        
    }
    
    
    
    
    
    
    
}




