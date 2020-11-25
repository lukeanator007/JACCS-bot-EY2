package jaccsbot.jaccsbot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;

public class DeckLinkParser {

	
	public static List<Card> parseLink(String link) throws Exception 
	{
		if(link.startsWith("https://www.duelingbook.com/deck?id=")) 
		{
			int ans=-1;
			try 
			{
				//F.log(link.substring(36));
				ans= Integer.parseInt(link.substring(36).strip());
			}
			catch(Exception e) 
			{
				throw new Exception("link is not a valid decklink");
			}
			return parse(ans);
			
		}
		else 
		{
			throw new Exception("link is not a valid decklink");
		}
		
	}
	
	public static List<Card> parse(int id) throws Exception 
	{
		
		WebClient webClient = new WebClient();
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		String content = "";
		try {
		  String searchUrl = "https://www.duelingbook.com/php-scripts/load-deck.php?id="+id;
		  Page page = webClient.getPage(searchUrl);
		  
		  content = page.getWebResponse().getContentAsString();
		  webClient.close();
		  
		}catch(Exception e){
		  e.printStackTrace();
		}
		//id, name, treated_as, effect, pendulum_effect, card_type, monster_color, 
		//is_effect, type, attribute, level, ability, flip, pendulum, scale, arrows, atk, def, tcg_limit, ocg_limiy, serial_number, 
		//tcg,ocg, rush, pic, hidden, custom, username
		
		if(!content.startsWith("{\"action\":\"Success\"")) 
		{
			F.log("not found");
			throw new Exception("error: deck not found");
		}

		int deckIndex = 1;//index 0 is '{'
		
		List<Card> cards = new ArrayList<Card>();
		int cardIndexStart=0;
		
		cardIndexStart=indexOfJS(content, '{', deckIndex)+1;
		deckIndex=cardIndexStart;
		
		for(;cardIndexStart!=0;) 
		{
			int i=0;
			String[] cardData = new String[30];
			while(i<30) 
			{
				int start=indexOfJS(content, ':', deckIndex)+1;
				int end1=Math.max(indexOfJS(content, ',', start),0);
				int end2=Math.max(indexOfJS(content, '}', start),0);
				int end = Math.min(end1, end2);
				
				
				cardData[i] =content.substring(start, end);
				if(end2<end1) break;
				deckIndex=end+1;
				
				
				i++;
			}
			cards.add(new Card(cardData));
			//F.logArray(cardData);
			//F.log(cardIndexStart);
			cardIndexStart=indexOfJS(content, '{', deckIndex)+1;
			deckIndex=cardIndexStart;
			
		}
		
		
		
		return cards;
		
	}
	

	
	
	
	/**
	 * 
	 * @param str
	 * @param ch
	 * @param fromIndex
	 * @return
	 */
	public static int indexOfJS(String str, char ch, int fromIndex) 
	{
		boolean quoteOn=false;
		for(int i=fromIndex;i<str.length();i++) 
		{
			switch(str.charAt(i)) 
			{
			case '\\':
				i++;
				break;
			case '\"':
				quoteOn=!quoteOn;
				break;
			default: 
				if(str.charAt(i)==ch&&!quoteOn) return i;
			}
		}
		
		return -1;
		
	}
	
	
	
	
	
	
	
	
	
	
}



























