import java.awt.Dimension;
import javax.swing.JLabel;

public class Status extends JLabel
{
	private final Dimension statusDim = new Dimension(0, 22);
	private int line = 0;
	private int column = 0;
	private String lang = "java";
	
	@Override
	public Dimension getPreferredSize() 
	{
		return (new Dimension(statusDim));
	}
	
	
	@Override 
	public String toString()
	{
		String tmp = lang + " source file";
		return String.format(" %s", tmp);
	}
	
	public void setLang(String lang)
	{
		this.lang = lang;
		update();
	}
	/*
	public void setColumn(int column)
	{
		this.column = column;
		update();
	}
	
	public void setLine(int line) 
	{
		this.line = line;
		update();
	}
	*/
	public void update()
	{
		this.setText(this.toString());
	}
}