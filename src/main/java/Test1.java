
public class Test1{
	public static void main(String[] args)
	{
		Object c1 = new C1(), c2 = new C2();
		Bug.bug(c2, c1);
		//Bug.bug(c2, c1);
	}
}