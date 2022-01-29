import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class WDoc implements DocumentListener
{
	private JTextPane pane;
	
	private LinkedList<QueueStruct> arr;
	public boolean complete = true;
	private String docText;
	private int docTextLength = 0;
	
	public WDoc(JTextPane pane)
	{
		this.pane = pane;
		getDocText();		
	
		//pane.setText("This. is, int automated acknowledgement");
		arr = new LinkedList<QueueStruct>();				
	}
	
	public void colorNormal()
	{
		arr.add(new QueueStruct(0, docTextLength, Color.WHITE));		
	}		
		
	
	public void getDocText()
	{
		try
		{
			docText = pane.getDocument().getText(0, pane.getDocument().getLength());
			docTextLength = docText.length();
		}
		catch(Exception ex)
		{			
			ex.printStackTrace();
		}
	}
	
	public void paintText()
	{	
		complete = false;
		arr.clear();
		SwingUtilities.invokeLater(new ColoringW(this.pane, arr, this));
				
		getDocText();
		colorNormal();		
		
		this.complete = true;
	}
	
	@Override    
    public void changedUpdate(DocumentEvent e) 
	{
		
    }

    @Override    
    public void insertUpdate(DocumentEvent e) 
	{		
		this.paintText();
    }

    @Override
    public void removeUpdate(DocumentEvent e) 
	{ 
		this.paintText();
    }
}