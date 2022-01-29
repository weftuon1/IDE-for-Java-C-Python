import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File; 
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import javax.swing.ButtonGroup;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.undo.UndoManager;

public class Ide extends JFrame
{	
	// Field
	private JTabbedPane tabPane;	// right panel
	private Project structure;		// left panel
	private Message msg;		//bottom panel
	private Status status;
	private JSplitPane sph;		
	private JSplitPane spv;
	private UndoManager undomgr;
	private FindFrame findFrame;
	private EditPanel findPanel;
	private String findWord;
	private ReplaceFrame replaceFrame;
	private String replaceWord;
	public static String compileMessage;
	
	// parameter
	private boolean pause = true;			
	
	public static void main(String[] args)
	{
		Ide a = new Ide();
	}
	
	public Ide()
	{
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setMinimumSize(new Dimension(500, 500));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("IDE");
		this.setResizable(true);
		this.setJMenuBar(new Menu(this));
		
		// initial
		tabPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		tabPane.addChangeListener(new ChangeListener() 
		{
			public void stateChanged(ChangeEvent e) 
			{
				EditPanel selectedPane = (EditPanel)(tabPane.getSelectedComponent());
				if(selectedPane == null)			
					return;			
				else
					selectedPane.setLang();
			}
		}
		);
		
		structure = new Project();
		msg = new Message();
		status = new Status();		
		
		//layout
		sph = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, structure, tabPane);
		sph.setDividerLocation(180);
		spv = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, sph, msg);		
		spv.setDividerLocation(500);
		sph.setDividerSize(4);
		spv.setDividerSize(4);		
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout(1,1));
		c.add(new Tool(this), BorderLayout.NORTH);
		c.add(spv, BorderLayout.CENTER);
		c.add(status, BorderLayout.SOUTH);		
		newFile();
		this.setVisible(true);		
	}
	
	public void newFile()
	{			
		int n = 0;
		for(int i=1; ; i++)
		{
			boolean getIndex = true;
			for(int j=0; j<tabPane.getTabCount(); j++)
			{
				String str = ("new " + i);
				if(str.equals(tabPane.getTitleAt(j)))
				{
					getIndex = false;
					break;
				}
			}
			if(getIndex)
			{
				n = i;
				break;			
			}
		}
		
		EditPanel panel = new EditPanel(tabPane, status);
		tabPane.addTab("new " + n, panel);
		tabPane.setSelectedIndex(tabPane.getTabCount()-1);		
	}
	
	public void openFile()
	{
		System.out.println("Open");
		
		FileDialog fd = new FileDialog(this, "Open", FileDialog.LOAD);   
		fd.setVisible(true);  

		if(fd!=null)
		{   
			String dir = fd.getDirectory();
			String fileName = fd.getFile();  
			if(dir!=null && fileName!=null)			
			{
				System.out.println("dir: " + dir);
				System.out.println("fileName: " + fileName);

				BufferedReader reader;
				try
				{
					reader = new BufferedReader(new FileReader (dir + fileName));
				} 
				catch(Exception e)
				{
					System.out.println(e);
					return;
				}
				String line = null;
    				StringBuilder stringBuilder = new StringBuilder();
				String ls = System.getProperty("line.separator");

				try 
				{
					while((line = reader.readLine()) != null) 
					{
						stringBuilder.append(line);
						stringBuilder.append(ls);
					}
					newFile();
					EditPanel selectedPane = (EditPanel)(tabPane.getSelectedComponent());
					selectedPane.setText(stringBuilder.toString());
					selectedPane.setFilePath(dir + fileName);					
				}
				catch(Exception ex)
				{
					System.out.println(ex.toString());
				}
				finally
				{
			  		try
					{
						reader.close();
					}
					catch(Exception e)
					{
						System.out.println(e.toString());
					}
				}					
			}
		} 
	}
	
	public void saveFile()
	{
		EditPanel selectedPane = (EditPanel)(tabPane.getSelectedComponent());
		if(selectedPane == null)			
			return;			
		
		System.out.println("Save");
		
		String currentPath = selectedPane.getFilePath();
		if(currentPath!="")
		{
			try(FileWriter fw = new FileWriter(currentPath))
			{
				fw.write(selectedPane.getText());
				fw.close();				
			}
			catch(Exception e)
			{
				System.out.println(e);
			}					
		}
		else
		{
			saveAs();
		}
	}
	
	public void saveAs()
	{
		EditPanel selectedPane = (EditPanel)(tabPane.getSelectedComponent());
		if(selectedPane == null)			
			return;			
		
		System.out.println("Save As");
				
		FileDialog fd = new FileDialog(this, "Save As", FileDialog.SAVE);   
		fd.setVisible(true);   
		if(fd!=null)
		{   
			String dir = fd.getDirectory();
			String fileName = fd.getFile();  
			if(dir!=null && fileName!=null)			
			{
				System.out.println("dir: " + dir);
				System.out.println("fileName: " + fileName);
				try(FileWriter fw = new FileWriter(dir + fileName)) 
				{
					fw.write(selectedPane.getText());
					fw.close();
					selectedPane.setFilePath(dir + fileName);
				}
				catch(Exception e)
				{
					System.out.println(e);
				}					
			}
		}  
	}
	
	public void saveAll()
	{
		System.out.println("Save All");
		
		for(int i=0; i<tabPane.getTabCount(); i++)
		{
			tabPane.setSelectedIndex(i);
			saveFile();
		}
	}
	
	public void closeFile()
	{
		System.out.println("Close " + (tabPane.getTitleAt(tabPane.getSelectedIndex())));
		
		tabPane.remove(tabPane.getSelectedIndex());
		if(tabPane.getTabCount()==0)
			newFile();
	}
	
	public void closeAll()
	{
		System.out.println("Close All");
		
		for(int i=tabPane.getTabCount()-1; i>=0; i--)
		{
			tabPane.remove(i);
		}
		if(tabPane.getTabCount()==0)
			newFile();
	}
	
	public void undo()
	{
		EditPanel selectedPane = (EditPanel)(tabPane.getSelectedComponent());
		if(selectedPane == null)			
			return;
		
		undomgr = selectedPane.getUndoManager();
		
		//System.out.println("Undo");
		if(undomgr.canUndo())
		{
			try
			{
				undomgr.undo();
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
	}
	
	public void redo()
	{
		EditPanel selectedPane = (EditPanel)(tabPane.getSelectedComponent());
		if(selectedPane == null)			
			return;
		
		undomgr = selectedPane.getUndoManager();
		
		//System.out.println("Redo");
		if(undomgr.canRedo())
		{
			try
			{
				undomgr.redo();
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
	}
	
	public void cut()
	{
		
	}
	
	public void copy()
	{
		/*
		EditPanel selectedPane = (EditPanel)(tabPane.getSelectedComponent());
		if(selectedPane == null)			
			return;
		
		String cpStr = selectedPane.getEditArea().getSelectedText();
		System.out.println(cpStr);
		StringSelection stringSelection = new StringSelection(cpStr);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
		*/
	}
	
	public void paste()
	{
		
	}
	
	public void selectAll()
	{
		EditPanel selectedPane = (EditPanel)(tabPane.getSelectedComponent());
		if(selectedPane == null)			
			return;
		EditText t = selectedPane.getEditArea();
		t.setSelectionStart(0);
		t.setSelectionEnd(t.getDocument().getLength());
	}
	
	public void find()
	{
		findFrame = new FindFrame(this);
	}
	
	public void replace()
	{
		System.out.println("Ide replace");
		replaceFrame = new ReplaceFrame(this);
	}
	
	public void enter()
	{
		findPanel = (EditPanel)(tabPane.getSelectedComponent());
		if(findPanel == null)			
			return;
		findWord = findFrame.getFindWord();
		if(findWord == null)
			return;
		EditText t = findPanel.getEditArea();
		findFrame.setEditArea(t);
		
		findFrame.highlightWords(t, findWord);
	}
	
	public void next()
	{
		EditPanel selectedPane = (EditPanel)(tabPane.getSelectedComponent());
		if(selectedPane == null || findPanel != selectedPane)			
			return;
		
		/*
		String word = findFrame.getFindWord();
		if(word == null || findWord.equals(word) == false)
			return;
		*/
		
		EditText t = findPanel.getEditArea();
		
		findFrame.nextHighlightWords(t);
	}
	
	public void back()
	{
		EditPanel selectedPane = (EditPanel)(tabPane.getSelectedComponent());
		if(selectedPane == null || findPanel != selectedPane)			
			return;
		
		/*
		String word = findFrame.getFindWord();
		if(word == null || findWord.equals(word) == false)
			return;
		*/
		
		EditText t = findPanel.getEditArea();
		
		findFrame.backHighlightWords(t);
	}
	
	public void enterR()
	{
		findPanel = (EditPanel)(tabPane.getSelectedComponent());
		if(findPanel == null)			
			return;
		findWord = replaceFrame.getFindWord();
		if(findWord == null)
			return;
		EditText t = findPanel.getEditArea();
		replaceFrame.setEditArea(t);
		
		replaceFrame.highlightWords(t, findWord);
	}
	
	
	public void replaceR()
	{
		EditPanel selectedPane = (EditPanel)(tabPane.getSelectedComponent());
		if(selectedPane == null || findPanel != selectedPane)			
			return;
		
		/*
		String word = replaceFrame.getFindWord();
		if(word == null || findWord.equals(word) == false)
			return;
		*/
		
		replaceWord = replaceFrame.getReplaceWord();
		if(replaceWord == null)
			return;
		
		EditText t = findPanel.getEditArea();
		
		replaceFrame.replaceHighlightWords(t);
	}
	
	public void nextR()
	{
		EditPanel selectedPane = (EditPanel)(tabPane.getSelectedComponent());
		if(selectedPane == null || findPanel != selectedPane)			
			return;
		
		String word = replaceFrame.getFindWord();
		if(word == null || findWord.equals(word) == false)
			return;
		
		EditText t = findPanel.getEditArea();
		
		replaceFrame.nextHighlightWords(t);
	}
	
	public void backR()
	{
		EditPanel selectedPane = (EditPanel)(tabPane.getSelectedComponent());
		if(selectedPane == null || findPanel != selectedPane)			
			return;
		
		String word = replaceFrame.getFindWord();
		if(word == null || findWord.equals(word) == false)
			return;
		
		EditText t = findPanel.getEditArea();
		
		replaceFrame.backHighlightWords(t);
	}
	
	public void typeset()
	{
		EditPanel panel = (EditPanel)(tabPane.getSelectedComponent());
		if(panel == null)			
			return;
		
		//EditText textComp = selectedPane.getEditArea();
		String tab = "\t";
		int tabLength = tab.length();
		String nextLine = "\n";
		int nextLineLength = nextLine.length();
		
		//System.out.println("nextLine Length : " + nextLineLength);
		System.out.println("Autoformat");
		
		String text = panel.getText();
		
		text = text.replaceAll("\t", "");
		//System.out.println(text);
		
		//panel.setText(text);
		
		int tabCount = 0;
		int pos = 0;
		boolean add = false;
		boolean sub = false;
		text = addTab(text, pos, tabCount);
		while((pos = text.indexOf(nextLine, pos)) >= 0)
		{
			pos += nextLineLength;
			int a = text.indexOf(nextLine, pos);
			int b = text.indexOf('{', pos);
			int c = text.indexOf('}', pos);
			
			if(b < a && b != -1 && a != -1)
			{
				add = true;
				tabCount++;
			}
			else
				add = false;
			
			if(c < a && c != -1 && a != -1)
			{
				sub = true;
				tabCount--;
				if(tabCount < 0)
					tabCount = 0;
			}
			else if(c != -1 && a == -1)
			{
				sub = true;
				tabCount--;
				if(tabCount < 0)
					tabCount = 0;
			}
			else
				sub = false;
			
			if(add == true && sub == false)
				text = addTab(text, pos, tabCount-1);
			else
				text = addTab(text, pos, tabCount);
			
			//System.out.println(tabCount);
		}
		
		//System.out.println(text);
		panel.setText(text);
	}
	
	public String addTab(String text, int pos, int count)
	{
		for(int i=0; i<count; i++)
		{
			text = text.substring(0, pos+i) + "\t" + text.substring(pos+i, text.length());	
		}
		return text;
	}
	
	public boolean compile() throws Exception
	{
		saveFile();
		compileMessage = "";
		EditPanel selectedPane = (EditPanel)(tabPane.getSelectedComponent());
		String fileName = selectedPane.getFileName();
		String dir = selectedPane.getDir();
		String ext = selectedPane.getExt();
		
		if(fileName.equals("") || dir.equals("") || ext.equals(""))
			return false;
		if(ext.equals("java") || ext.equals("c") || ext.equals("cpp"))
		{
			String cmd = "";			
			if(ext.equals("java"))
				cmd += "javac -cp " + dir + " " + dir + fileName + "." + ext;						
			else 
				cmd += ".\\MinGW64\\bin\\g++.exe " + dir + fileName + "." + ext + " -o " + dir + fileName;
			System.out.println("Command: " + cmd);
			Process process = Runtime.getRuntime().exec(cmd);
			Thread getInput = new Thread(new SubTask(process.getInputStream()));
			Thread getErr = new Thread(new SubTask(process.getErrorStream()));	
			getInput.start();
			getErr.start();
			getInput.join();
			getErr.join();
			process.waitFor();	
			System.out.println("Compile Message");
			System.out.println(compileMessage);
			System.out.println("===============");
			return msg.compilerMsg.parseMsg(compileMessage, ext) == 0;			
		}
		else if(ext.equals("py"))
			return true;
		else
			return false;
	}
	
	public void solveCompileMessage()
	{
		
	}

	public void execute() throws Exception
	{		
		EditPanel selectedPane = (EditPanel)(tabPane.getSelectedComponent());
		String fileName = selectedPane.getFileName();
		String dir = selectedPane.getDir();
		String ext = selectedPane.getExt();
		
		if(fileName.equals("") || dir.equals("") || ext.equals(""))
			return;
		if(ext.equals("java") || ext.equals("py") || ext.equals("c") || ext.equals("cpp"))
		{
			String cmd = "cmd /k start ";
			if(this.pause)
				cmd += "ConsolePauser.exe ";
			if(ext.equals("java"))
				cmd += "java -cp " + dir + " " + fileName;			
			else if(ext.equals("py"))
				cmd += "python " + dir + fileName + "." + ext;				
			else
				cmd += dir + fileName + ".exe";
			System.out.println("Command: " + cmd);
			Process process = Runtime.getRuntime().exec(cmd);		
		}
	}
	
	public void compileRun()
	{
		try
		{
			if(compile() == true)
				execute();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	private class SubTask implements Runnable
	{
		private BufferedReader iReader;
		
		public SubTask(InputStream istream)
		{		
			iReader = new BufferedReader(new InputStreamReader(istream));
		}
	
		public void run()
		{
			try
			{
				String input = iReader.readLine();
				while (input != null)
				{
					Ide.compileMessage += input + "\n";
					input = iReader.readLine();
				}
			} 
			catch (IOException ioe)
			{
			}
		}
	}
}
