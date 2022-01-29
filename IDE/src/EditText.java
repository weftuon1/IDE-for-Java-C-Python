import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleContext;
import javax.swing.event.DocumentListener;

public class EditText extends JTextPane
{	
	public final static int minFontSize = 12;
	public final static int maxFontSize = 50;
	protected StyleContext myContext;
	protected DefaultStyledDocument myDoc;
	protected DocumentListener jdoc;
	private EditPanel editPane;
	private Status status;
	
	public EditText(EditPanel pane, Status status)
	{
		super();
		editPane = pane;
		this.status = status;
		myContext = new StyleContext();
		myDoc = new DefaultStyledDocument(myContext);
		this.setDocument(myDoc);
		//important
		this.setText("0");
		this.setText("");
		//java 
		System.out.println("Extension:" + editPane.getExt());
		if(editPane.getExt().equals("java"))
			myDoc.addDocumentListener(jdoc = new JDoc(this));
		else if(editPane.getExt().equals("c") || editPane.getExt().equals("cpp"))
			myDoc.addDocumentListener(jdoc = new CDoc(this));
		else if(editPane.getExt().equals("py"))
			myDoc.addDocumentListener(jdoc = new PDoc(this));
		else
			myDoc.addDocumentListener(jdoc = new WDoc(this));

		this.addMouseWheelListener(new MouseWheelListener() 
		{ 
			@Override 
			public void mouseWheelMoved(MouseWheelEvent e) 
			{ 
				if((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) 
				{
					if (e.getWheelRotation() > 0) 
					{ 
						int curSize = getFont().getSize();
						if(curSize > minFontSize)
							setFont(getFont().deriveFont((float)(curSize - 1)));
					} 
					else 
					{ 
						int curSize = getFont().getSize();
						if(curSize < maxFontSize)
							setFont(getFont().deriveFont((float)(curSize + 1)));
					} 
				} 
				else
				{
			       	getParent().dispatchEvent(e);
				}
			}
		}
		);
	}
	
	public void paintText()
	{
		if(jdoc instanceof JDoc)
		{
			JDoc a = (JDoc) jdoc;
			a.paintText();
		}
		else if(jdoc instanceof CDoc)
		{
			CDoc b = (CDoc) jdoc;
			b.paintText();			
		}
		else if(jdoc instanceof PDoc)
		{
			PDoc c = (PDoc) jdoc;
			c.paintText();		
		}		
		else if(jdoc instanceof WDoc)
		{
			WDoc d = (WDoc) jdoc;
			d.paintText();		
		}
	}
	
	public void changeDocumentListener()
	{			 
		myDoc.removeDocumentListener(jdoc);
		
		String ext = editPane.getExt();
		System.out.println(ext);
		if(ext.equals("java"))
			myDoc.addDocumentListener(jdoc = new JDoc(this));
		else if(ext.equals("c") || ext.equals("cpp"))
			myDoc.addDocumentListener(jdoc = new CDoc(this));
		else if(ext.equals("py"))
			myDoc.addDocumentListener(jdoc = new PDoc(this));
		else
			myDoc.addDocumentListener(jdoc = new WDoc(this));
		paintText();
	}
}
