package jaccsbot.jaccsbot;

import org.apache.commons.text.StringEscapeUtils;

public class Card {

	private int id;
	private String name;
	private String effect;
	private String pendulumEffect;
	private String monsterType= null;
	private boolean monster = false;
	private boolean spell = false;
	private boolean trap = false;
	
	/**
	 * false if the card is a monster and has no effect
	 */
	private boolean hasEffect=true;
	private String type;

	
	
	private boolean hasPicture;
	private boolean isPrivate;
	private boolean isCustom;
	private String creator = null;
	
	
	
	
	public Card() 
	{
		
	}
	
	public Card(String[] arr) 
	{
		//this.arr=arr;
		this.id=Integer.parseInt(arr[0]);
		this.name = stringHandle(arr[1]);
		//arr[2]=treated as
		this.effect=stringHandle(arr[3]);
		pendulumEffect = stringHandle(arr[4]);
		switch(arr[5]) 
		{
		case "\"Monster\"":
			this.monster=true;
			break;
		case "\"Spell\"":
			this.spell=true;
			break;
		case "\"Trap\"":
			this.trap=true;
			break;
		}
		
		
		
		if(monster) 
		{
			this.monsterType= stringHandle(arr[6]);
			this.hasEffect = arr[7].equals("1");
		}
		this.type = stringHandle(arr[8]);
		//arr[9]
		
		
		this.hasPicture = !arr[24].equals("0");
		this.isPrivate = arr[25].equals("1");
		this.isCustom = arr[26]!= null;
		if(this.isCustom) this.creator  = arr[27];
		
	}
	
	private String stringHandle(String str) 
	{
		String ans=StringEscapeUtils.unescapeJava(str);
		ans=ans.replace('\r', '\n');
		ans=ans.substring(1, ans.length()-1);
		return ans;
	}
	
	
	
	public Card(String name, String effect) 
	{
		this.name=name;
		this.effect=effect;
	}

	public boolean equals(Object o) 
	{
		if(this.getClass().equals(o.getClass())) 
		{
			Card card = (Card) o;
			return this.id==card.id;
		}
		return false;
	}

	public int hashCode() 
	{
		return id;
	}
	
	


	public String getName() {
		return name;
	}
	
	public String getEffect() {
		return effect;
	}

	public String getPendulumEffect() {
		return this.pendulumEffect;
	}

	public boolean isSpell() {
		return spell;
	}

	/**
	 * false if the card is a monster and has no effect
	 */
	public boolean hasEffect() {
		return hasEffect;
	}

	public boolean isTrap() {
		return trap;
	}

	public String getType() {
		return type;
	}
	
	public boolean isMonster() {
		return monster;
	}

	public Object getMonsterType() {
		return monsterType;
	}
	
	
}















































