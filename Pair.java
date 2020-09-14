/**
 * 
 */
package jaccsbot.jaccsbot;


public class Pair <D,S>{
	
	public D first;
	public S second;
	
	
	Pair (D first, S second)
	{
		this.first=first;
		this.second=second;
	}
	
	public D getFirst() 
	{
		return first;
	}
	
	public S getSecond() 
	{
		return second;
	}
	
	public static <T,U> Pair<T,U> of(T first, U second) {
		return new Pair<T, U>(first,second);
	}
	
	public String toString() 
	{
		return "["+first.toString()+","+second.toString()+"]";
	}
	
	
	
	
}
