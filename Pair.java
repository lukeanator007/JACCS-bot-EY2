/**
 * 
 */
package jaccsbot.jaccsbot;


public class Pair <D,S>{
	
	public D first;
	public S second;
	
	/**
	 * pair of any 2 objects
	 * @param first first object
	 * @param second second object
	 */
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
	
	/**
	 * returns a new typed pair of objects
	 * 
	 * @param <T>
	 * @param <U>
	 * @param first
	 * @param second
	 * @return
	 */
	public static <T,U> Pair<T,U> of(T first, U second) {
		return new Pair<T, U>(first,second);
	}
	
	public String toString() 
	{
		return "["+first.toString()+","+second.toString()+"]";
	}
	
	
	
	
}
