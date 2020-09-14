package jaccsbot.jaccsbot;

public class F {
	
	static char[] letters =new char[] {'q','w','e','r','t','y','u','i','o','p','a','s','d','f','g','h','j','k','l','z','x','c','v','b','n','m',
			'Q','W','E','R','T','Y','U','I','O','P','A','S','D','F','G','H','J','K','L','Z','X','C','V','B','N','M'};
	
	/**
	 * char and returns true if it a letter
	 * 
	 * @param in
	 * @return
	 */
	public static boolean isLetter(char in) 
	{
		for(int i=0;i<letters.length;i++) 
		{
			if(in==letters[i]) 
			{
				return true;
			}
		}
		
		
		return false;
	}
	
	
	public static boolean isLowerCase(char in) 
	{
		for(int i=0;i<26;i++) 
		{
			if(in==letters[i]) 
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * 
	 * @param 
	 * @return 
	 * @return returns all lowercase letters in the given string in the same order
	 */
	public static String letterClean(String str) 
	{
		String ans="";
		for(int i=0;i<str.length();i++) 
		{
			char tempChar=str.charAt(i);
			if(tempChar<='z'&&tempChar>='a') 
			{
				ans=ans+tempChar;
			}
		}
		
		return ans;
	}
	
	
	
	/**
	 * removes the first and last characters of a string if they are not letters
	 * 
	 * 
	 * @param str
	 * @return
	 */
	public static String clean(String strIn)
	{
		
		
		String str=strIn;
		if(str=="") 
		{
			
		}
		else 
		{
			try {
				if(!F.isLetter(str.charAt(0))) 
				{
					str=str.substring(1);
				}
				try 
				{
					if(!F.isLetter(str.charAt(str.length()-1))) 
					{
						str=str.substring(0,str.length()-1);
					}
				}
				catch(Exception e)
				{
					
				}
			} catch (Exception e) {
				F.log(str);
			}
		}
		
		
		return str;
	}
	
	
	
	
	public static void log(Object obj) 
	{
		if(obj!=null) {
			System.out.println(obj.toString());
		}
		else 
		{
			System.out.println("null");
		}
			
	}
	
	public static void logArray(Object[] array) 
	{
		log("Array Start");
		for(int i=0;i<array.length;i++) 
		{
			log(array[i]);
		}
		log("Array End");
	}
	
	
	
	/**
	 * 
	 * @param str
	 * @return str with the first letter changed to upper case, unless it is already a capital, in which case return str
	 */
	public static String firstLetterToUpperCase(String str) 
	{
		char[] temp=str.toCharArray();
		boolean test=true;
		for(int i=0;i<temp.length;i++) 
		{
			
			if (test) 
			{
				try {
					for(int j=0;j<letters.length;j++) 
					{
						if(temp[i]==letters[j]) 
						{
							try {
								temp[i]=letters[j+26];
							} catch (Exception e) {}
							
							test=false;
							
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					F.log("firstlettertouppercaseerror "+str);
				}
			}
				
			
		}
		
		String ans=new String(temp);
		
		
		return ans;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
