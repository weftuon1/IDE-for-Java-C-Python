import java.awt.Color;
import java.util.LinkedList;
import javax.swing.JTextPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ColoringP implements Runnable
{	
	private JTextPane jTextPane;
	private PDoc pdoc;
	private LinkedList<QueueStruct> arr;
	private MutableAttributeSet attr;
	private StyledDocument doc;
	public ColoringP(JTextPane jTextPane, LinkedList<QueueStruct> arr, PDoc pdoc)
	{
		this.jTextPane = jTextPane;
		this.arr = arr;		
		this.attr = new SimpleAttributeSet();
		this.doc =  jTextPane.getStyledDocument();
		this.pdoc = pdoc;
	}
	
	@Override
    public void run() 
	{	
		while(pdoc.complete == false || !arr.isEmpty())
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