import java.awt.Color;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.util.StringTokenizer;
import javax.swing.text.rtf.RTFEditorKit;

public class Test extends JTextPane implements DocumentListener
{	
	protected StyleContext myContext;
	protected DefaultStyledDocument myDoc;
	private MutableAttributeSet[] keyAttr = new MutableAttributeSet[2];
	private MutableAttributeSet normalAttr;
	private MutableAttributeSet inputAttributes = new RTFEditorKit().getInputAttributes();
	private Color[] keyColor = new Color[2];
	private Color normalColor;
	private String[][] keyWord={{"boolean", "byte", "char", "class", "double", "enum", "float" , "int", "interface", "long", "short", "void",
								 "abstract", "final", "native", "private", "protected", "public", "static", "strictfp", "synchronized", "transient", "volatile",
								 "package"}, 
								{"break", "case", "catch", "continue", "default", "do", "else", "finally", "for", "if", "return", "switch", "throw", "try", "while",
								 "instanceof",
								 "assert", "extends", "implements", "import", "new", "super", "this", "throws"}};
	
	public static void main(String[] args)
	{
		JFrame t = new JFrame();
		t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		t.setSize(500, 500);
		
		Test editArea = new Test();
		t.add(editArea);		
		
		t.setVisible(true);
	}
	
	public Test()
	{
		super();
		myContext = new StyleContext();
		myDoc = new DefaultStyledDocument(myContext);
		this.setDocument(myDoc);
		myDoc.addDocumentListener(this);
		
		keyAttr[0] = new SimpleAttributeSet();
		keyColor[0] = Color.GREEN;
		StyleConstants.setForeground(keyAttr[0], keyColor[0]);
		
		keyAttr[1] = new SimpleAttributeSet();
		keyColor[1] = Color.RED;
		StyleConstants.setForeground(keyAttr[1], keyColor[1]);
				
		normalAttr = new SimpleAttributeSet();
		normalColor = Color.BLUE;
		StyleConstants.setForeground(normalAttr, normalColor);
		
		this.setText("This. is, int automated acknowledgement");
		//this.setLineWrap(false);
		//myDoc.setCharacterAttributes(5, 10, keyAttr, false);
			
		keywordSolve();
	}
	
	public void keywordSolve() 
	{
		try
		{	
			String currentLine = null;
			Element root = myDoc.getDefaultRootElement();
			
			int cursorPos = this.getCaretPosition(); 
			int line = root.getElementIndex(cursorPos); 
			
			Element lineElement = root.getElement(line);	
			int start = lineElement.getStartOffset();
			int end = lineElement.getEndOffset() - 1;
			
			currentLine = myDoc.getText(start, end-start);
			System.out.println(currentLine);
			//myDoc.setCharacterAttributes(start, currentLine.length(), normalAttr, false);
			SwingUtilities.invokeLater(new Coloring(this, start, currentLine.length(), normalColor)); //normalAttr
			
			int i = 0;
			int j = 0;
			int linePos = 0;
			String str;
			String delim = " \t\n\r\f\\\"'`~!@#$%^&*()_+-={}[]|:;<>?/.,";
			//String delim = "\\W";
			MyStringTokenizer st = new MyStringTokenizer(currentLine, delim);
			while(st.hasMoreTokens()) 
			{
				str = st.nextToken();
				if(str == null)
					return;
				
				boolean found = false;
				for(j = 0; j < 2 ; j++)
				{
					
					for(i = 0; i < keyWord[j].length; i++) 
					{			
						if(str.equals(keyWord[j][i]))
						{
							found = true;
							break;
						}				
					}
					if(found == true) 
						break;
				}		
				if(found == false)
					continue;

				linePos = st.getCurrentPosition();
				//System.out.println(linePos);
				//myDoc.setCharacterAttributes(start + linePos - str.length(), str.length(), keyAttr, false);
				SwingUtilities.invokeLater(new Coloring(this, start + linePos - str.length(), str.length(), keyColor[j])); //keyAttr
			}
			inputAttributes.addAttributes(normalAttr);
		} 
		catch (Exception ex) 
		{
			System.out.println("Exception Out");
			ex.printStackTrace();
		}
	}
	
	@Override    
    public void changedUpdate(DocumentEvent e)
	{
		//System.out.println("changedUpdate");
    }

    @Override    
    public void insertUpdate(DocumentEvent e)
	{
		System.out.println("insertUpdate");
        keywordSolve();
    }

    @Override
    public void removeUpdate(DocumentEvent e)
	{
        System.out.println("removeUpdate");
		keywordSolve();
    }
}

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
		
			System.out.println(s+".");
			/*
			if (oldStr.equals(s)) 
			{
				return s;
			}
			*/
			int pos = -1;
			pos = str.indexOf(s);
			if(pos == -1) 
			{
				return null;
			}
			
			int xBegin = pos + s.length(); //next Begin position
			str = str.substring(xBegin);
	
			currentPosition = currentPosition + xBegin;
			//beginPosition = beginPosition + xBegin;
			
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


class Coloring implements Runnable 
{
    private int pre;
    private int len;
    private Color color;
    private JTextPane jTextPane;

    public Coloring(JTextPane jTextPane, int pre, int len, Color color)
	{
        this.jTextPane = jTextPane;
        this.pre = pre;
        this.len = len;
        this.color = color;
    }

    @Override
    public void run()
	{
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setForeground(attr, color);
		StyledDocument doc = jTextPane.getStyledDocument();
        doc.setCharacterAttributes(pre, len, attr, false);
    }
}