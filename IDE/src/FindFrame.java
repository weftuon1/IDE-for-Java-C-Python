import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.JTextComponent;

public class FindFrame extends JDialog
{
	private JFrame frame;
	private JPanel upPanel;
	private JPanel downPanel;
	private JTextField findArea;
	private JButton enter;
	private JButton back;
	private JButton next;
	private String findWord;
	private MyHighlightPainter myHighlightPainter;
	private EditText et;
	private JTextComponent lastTextComp;
	private ArrayList<Integer> posList;
	private int posListIndex = -1;
	
	public FindFrame(JFrame f)
	{
		super(f);
		frame = f;	
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setSize(300, 130);
		this.setLocationRelativeTo(this.getParent());
		this.setModal(false);
		this.setTitle("Find");
		this.setResizable(false);
		this.addWindowListener(
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					if(et != null)
						removeHighlightWords(et);
					dispose();
                }
			}
		);
		
		findArea = new JTextField(15);
		myHighlightPainter = new MyHighlightPainter(Color.RED);
		enter = new JButton("Find");
		back = new JButton("Back");
		next = new JButton("Next");
		
		back.setEnabled(false);
		next.setEnabled(false);
		
		enter.setPreferredSize(new Dimension(80, 25));
		back.setPreferredSize(new Dimension(80, 25));
		next.setPreferredSize(new Dimension(80, 25));
		
		findArea.addKeyListener(new findAreaHandler());
		enter.addActionListener(new buttonHandler(0));
		back.addActionListener(new buttonHandler(1));
		next.addActionListener(new buttonHandler(2));
		
		upPanel = new JPanel();
		upPanel.setLayout(new FlowLayout());
		upPanel.add(findArea);
		upPanel.add(enter);
		
		downPanel = new JPanel();
		downPanel.setLayout(new FlowLayout());
		downPanel.add(back);
		downPanel.add(next);
		
		Container c = this.getContentPane();
		c.setLayout(new GridLayout(2, 1, 5, 5));
		c.add(upPanel);
		c.add(downPanel);

		this.setVisible(true);
	}
	
	public void setEditArea(EditText t)
	{
		this.et = t;
	}
	
	public String getFindWord()
	{
		this.findWord = findArea.getText();
		return findWord;
	}
	
	public void removeHighlightWords(JTextComponent textComp)
	{
		Highlighter highlight = textComp.getHighlighter();
		Highlight[] highlights = highlight.getHighlights();
		
		if(highlights.length != 0)
		{
			for(int i=0; i<highlights.length; i++)
			{
				if(highlights[i].getPainter() instanceof MyHighlightPainter)
				{
					highlight.removeHighlight(highlights[i]);
				}
			}
		}
	}
	
	public void highlightWords(JTextComponent textComp, String pattern)
	{
		if(lastTextComp != null)
			this.removeHighlightWords(lastTextComp);
		this.removeHighlightWords(textComp);
		lastTextComp = textComp;
		
		posList = new ArrayList<Integer>();
		posListIndex = -1;
		try
		{
			Highlighter highlight = textComp.getHighlighter();
			Document doc = textComp.getDocument();
			String text = doc.getText(0, doc.getLength());
			/*
			int pos = 0;
			while((pos = text.toUpperCase().indexOf(pattern.toUpperCase(), pos)) >= 0)
			{
				highlight.addHighlight(pos, pos + pattern.length(), myHighlightPainter);
				pos += pattern.length();
			}
			*/
			String str;
			//String delim = " \t\n\r\f\\\"'`~!@#$%^&*_()+-={}[]|:;<>?/.,";
			String delim = " \t\n\r\f\\\"'`~!@#$%^&*()+-={}[]|:;<>?/.,";
			MyStringTokenizer st = new MyStringTokenizer(text, delim);
			while(st.hasMoreTokens()) 
			{
				str = st.nextToken();
				if(str == null)
					return;
				
				if(str.equals(pattern))
				{
					int pos = st.getCurrentPosition();
					posList.add(pos);
					highlight.addHighlight(pos - pattern.length(), pos, myHighlightPainter);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		if(!posList.isEmpty())
		{
			back.setEnabled(true);
			next.setEnabled(true);
		}
		else
		{
			back.setEnabled(false);
			next.setEnabled(false);
		}
	}
	
	public void nextHighlightWords(JTextComponent textComp)
	{
		if(posList.isEmpty() == true)
			return;
		
		posListIndex++;	
		if(posListIndex > posList.size()-1)
			posListIndex = posList.size()-1;
		
		textComp.setCaretPosition(posList.get(posListIndex));
	}
	
	public void backHighlightWords(JTextComponent textComp)
	{
		if(posList.isEmpty() == true)
			return;
		
		posListIndex--;
		if(posListIndex < 0)
			posListIndex = 0;
		
		textComp.setCaretPosition(posList.get(posListIndex));	
	}
	
	private class findAreaHandler extends KeyAdapter
	{
		public findAreaHandler()
		{
			
		}
		
		@Override
		public void keyReleased(KeyEvent ev)
		{
			if(ev.getKeyCode() == KeyEvent.VK_ENTER)
			{
				try
				{
					Method method = Ide.class.getMethod("enter");
					method.invoke(frame);
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
			}
		}
	}
	
	private class buttonHandler implements ActionListener
	{
		private String[] editMethod = new String[]{"enter", "back", "next"};
		private int index;
		
		public buttonHandler(int n)
		{
			index = n;
		}
		
		@Override
		public void actionPerformed(ActionEvent ev)
		{	
			try
			{
				Method method = Ide.class.getMethod(editMethod[index]);
				method.invoke(frame);
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
	}
}