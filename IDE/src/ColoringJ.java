import java.awt.Color;
import java.util.LinkedList;
import javax.swing.JTextPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ColoringJ implements Runnable
{	
	private JTextPane jTextPane;
	private JDoc jdoc;
	private LinkedList<QueueStruct> arr;
	private MutableAttributeSet attr;
	private StyledDocument doc;
	public ColoringJ(JTextPane jTextPane, LinkedList<QueueStruct> arr, JDoc jdoc)
	{
		this.jTextPane = jTextPane;
		this.arr = arr;		
		this.attr = new SimpleAttributeSet();
		this.doc =  jTextPane.getStyledDocument();
		this.jdoc = jdoc;
	}
	
	@Override
    public void run() 
	{	
		while(jdoc.complete == false || !arr.isEmpty())
		{
			while(!arr.isEmpty())
			{
				QueueStruct cur = arr.poll();
				StyleConstants.setForeground(attr, cur.getColor());		
				doc.setCharacterAttributes(cur.getPre(), cur.getLen(), attr, false);
			}			
		}					
    }
}