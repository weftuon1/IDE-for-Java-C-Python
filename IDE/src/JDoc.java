import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class JDoc implements DocumentListener
{
	private JTextPane pane;
	private Color[] keyColor = new Color[2];
	private Color normalColor;
	private String docText;
	private int docTextLength = 0;
	private String[][] keyWordStr = {{"boolean", "byte", "char", "class", "double", "enum", "float" , "int", "interface", "long", "short", "void",
								      "abstract", "final", "native", "private", "protected", "public", "static", "strictfp", "synchronized", "transient", "volatile",
								      "package"}, 
								     {"break", "case", "catch", "continue", "default", "do", "else", "finally", "for", "if", "return", "switch", "throw", "try", "while",
								      "instanceof",
								      "assert", "extends", "implements", "import", "new", "super", "this", "throws"}};
	
	private HashSet<String> keyWords1 = new HashSet<String>();
	private HashSet<String> keyWords2 = new HashSet<String>();
	private LinkedList<QueueStruct> arr;
	public boolean complete = true;
	
	public JDoc(JTextPane pane)
	{
		this.pane = pane;
		getDocText();
		
		for(int i=0; i<keyWordStr.length; i++)
		{
			for(int j=0; j<keyWordStr[i].length; j++)
			{
				if(i==0)
					keyWords1.add(keyWordStr[i][j]);
				else if(i==1)
					keyWords2.add(keyWordStr[i][j]);
			}
		}	
			
		keyColor[0] = Color.ORANGE;	
		keyColor[1] = Color.YELLOW;	
		normalColor = Color.WHITE;
	
		//pane.setText("This. is, int automated acknowledgement");
		arr = new LinkedList<QueueStruct>();				
	}
	
	public void colorNormal()
	{
		arr.add(new QueueStruct(0, docTextLength, Color.WHITE));		
	}	

	private int findMinIndex(int pre[])
	{
		int cmp[] = {-1, -1, -1, -1};
		if(Arrays.equals(pre, cmp))
			return -1;
		int index = 0;
		for(int i=0;i<pre.length;i++)
			if(pre[i]!=-1)
			{
				index = i;
				break;
			}
		for(int i=index+1;i<pre.length;i++)
			if(pre[i] < pre[index] && pre[i] != -1)
				index = i;
		return index;
	}
	
	private int colorCh(int pre)
	{		
		int tmp = 0;
		int pos = docTextLength;
		if((tmp = docText.indexOf('\n', pre +1)) != -1)
			pos = tmp;
		String lineStr = docText.substring(pre+1, pos);
		char[] charList = lineStr.toCharArray();
		
		for(int i=0;i<charList.length;i++)
		{
			if(charList[i]=='\'')
			{
				pos = pre + 2 + i;
				break;
			}
			if(charList[i]=='\\' && (i+1)<charList.length)
			{
				charList[i+1] = ' ';
				i += 1;				
			}						
		}
		
		arr.add(new QueueStruct(pre, pos - pre, Color.RED));		
		return pos;
	}
	
	private int colorStr(int pre)
	{		
		int tmp = 0;
		int pos = docTextLength;
		if((tmp = docText.indexOf('\n', pre +1)) != -1)
			pos = tmp;
		String lineStr = docText.substring(pre+1, pos);
		char[] charList = lineStr.toCharArray();
		
		for(int i=0;i<charList.length;i++)
		{
			if(charList[i]=='\"')
			{
				pos = pre + 2 + i;
				break;
			}
			if(charList[i]=='\\' && (i+1)<charList.length)
			{
				charList[i+1] = ' ';
				i += 1;				
			}						
		}
		
		arr.add(new QueueStruct(pre, pos - pre, Color.RED));		
		return pos;
	}
	
	private int colorSingle(int pre)
	{				
		int pos = docText.indexOf('\n', pre+2);
		if(pos == -1)
			pos = docTextLength;		
		arr.add(new QueueStruct(pre, pos - pre, Color.GREEN));
		return pos;
	}
	
	private int colorMulti(int pre)
	{				
		int pos = docText.indexOf("*/", pre+2);
		if(pos == -1)
			pos = docTextLength;
		else
			pos += 2;
		arr.add(new QueueStruct(pre, pos - pre, Color.GREEN));		
		
		return pos;
	}
	
	public void colorStrComment()
	{				
		String[] begin = {"'", "\"", "//", "/*"};
		String[] end = {"'", "\"", "", "*/"};
		int pre[] = {0, 0, 0, 0}, pos = 0;	
		while(true)
		{
			for(int i=0; i<pre.length; i++)
				pre[i] = docText.indexOf(begin[i], pos);
			int index = findMinIndex(pre);					
			
			switch(index)
			{
				case 0:
					pos = colorCh(pre[0]);
					break;
				case 1:
					pos = colorStr(pre[1]);
					break;
				case 2:
					pos = colorSingle(pre[2]);
					break;
				case 3:
					pos = colorMulti(pre[3]);
					break;
				default:
					return;
			}					
		}
	}	
	
	public void keywordSolve() 
	{
		try
		{							
			int start = 0;
			
			int i = 0;
			int j = 0;
			int docPos = 0;
			String str;
			//String delim = " \t\n\r\f\\\"'`~!@#$%^&*_()+-={}[]|:;<>?/.,";
			String delim = " \t\n\r\f\\\"'`~!@#$%^&*()+-={}[]|:;<>?/.,";
			//String delim = "\\W";
			MyStringTokenizer st = new MyStringTokenizer(docText, delim);
			while(st.hasMoreTokens()) 
			{
				str = st.nextToken();
				if(str == null)
					return;				
				
				if(keyWords1.contains(str))
				{
					docPos = st.getCurrentPosition();
					arr.add(new QueueStruct(start + docPos - str.length(), str.length(), keyColor[0]));					
				}
				else if(keyWords2.contains(str))
				{
					docPos = st.getCurrentPosition();
					arr.add(new QueueStruct(start + docPos - str.length(), str.length(), keyColor[1]));					
				}
			}
		} 
		catch (Exception ex) 
		{			
			ex.printStackTrace();
		}
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
		SwingUtilities.invokeLater(new ColoringJ(this.pane, arr, this));
				
		getDocText();
		colorNormal();
		keywordSolve();
		colorStrComment();
		
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