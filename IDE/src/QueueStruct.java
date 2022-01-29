import java.awt.Color;
import javax.swing.JTextPane;

public class QueueStruct
{	
	private int pre;
	private int len;
	private Color color;
	
	public QueueStruct(int pre, int len, Color color)
	{		
		this.pre = pre;
		this.len = len;
		this.color = color;				
	}	
	
	public int getPre()
	{
		return this.pre;		
	}
	
	public int getLen()
	{
		return this.len;		
	}
	
	public Color getColor()
	{
		return this.color;
	}
}