import java.awt.Color;
import java.util.LinkedList;
import javax.swing.JTextPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ColoringC implements Runnable
{	
	private JTextPane jTextPane;
	private CDoc cdoc;
	private LinkedList<QueueStruct> arr;
	private MutableAttributeSet attr;
	private StyledDocument doc;
	public ColoringC(JTextPane jTextPane, LinkedList<QueueStruct> arr, CDoc cdoc)
	{
		this.jTextPane = jTextPane;
		this.arr = arr;		
		this.attr = new SimpleAttributeSet();
		this.doc =  jTextPane.getStyledDocument();
		this.cdoc = cdoc;
	}
	
	@Override
    public void run() 
	{	
		while(cdoc.complete == false || !arr.isEmpty())
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