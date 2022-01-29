import java.util.StringTokenizer;

class MyStringTokenizer extends StringTokenizer
{
	String oldStr;
	String str;
	String delim;
	String s;
	int currentPosition = 0;
	//int beginPosition = 0;
	
	MyStringTokenizer(String string, String delimiter) 
	{
		super(string, delimiter);
		this.oldStr = string;
		this.str = string;
		this.delim = delimiter;
	}

	public String nextToken() 
	{
		try 
		{		
			s = super.nextToken();
		
			//System.out.println(s+".");
			
			int pos = -1;
			pos = str.indexOf(s);
			if(pos == -1) 
			{
				return null;
			}
			
			int xBegin = pos + s.length(); //next Begin position
			str = str.substring(xBegin);
	
			currentPosition = currentPosition + xBegin;
			
			return s;
		} 
		catch (java.util.NoSuchElementException ex) 
		{
			ex.printStackTrace();
			return null;
		}
	}

	public int getCurrentPosition() 
	{
		return currentPosition;
	}
}