package jaccsbot.jaccsbot;

public class Sentence {
	private String[] words;
	private int index=0;
	private int maxIndex=0;
	
	//TODO
	Sentence(String str)
	{
		this.words=str.split(" ");
		for(int i=0;i<this.words.length;i++) 
		{
			String builder ="";
			for(int j=0;j<words[i].length();j++) 
			{
				char tempChar=words[i].charAt(j);
				if(tempChar>='a'&&tempChar<='z') 
				{
					builder=builder+tempChar;
				}
				else if(tempChar>='A'&&tempChar<='Z') 
				{
					builder=builder+tempChar;
				}
			}
			if(!builder.isBlank()) 
			{
				words[this.index]=builder;
				this.index++;
			}
		}
		this.maxIndex=index;
		this.index=0;
	}
	
	public String getNextWord() 
	{
		if(index>=maxIndex) 
		{
			this.resetIndex();
			return null;
		}
		String ans =this.words[this.index];
		this.index++;
		
		
		return ans;
	}
	
	public void resetIndex() 
	{
		this.index=0;
	}
	
	public boolean containsWord(String word) 
	{
		for(int i=0;i<this.maxIndex+1;i++) 
		{
			if(this.words[i].equals(word)) 
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean containsPhrase(String[] phrase) 
	{
		
		for(int i=0;i<this.maxIndex+1-phrase.length;i++) 
		{
			boolean test=true;
			for(int j=0;j<phrase.length;j++) 
			{
				if(!this.words[i+j].equals(phrase[j])) 
				{
					test=false;
					break;
				}
			}
			if(test) return true;
		}
		
		
		return false;
	}
	
	
	
	
	
	
	
	
	
	
}
