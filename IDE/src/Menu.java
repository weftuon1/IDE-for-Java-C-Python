import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import java.lang.reflect.Method;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

public class Menu extends JMenuBar
{
	JFrame frame;
	public Menu(JFrame f)
	{		
		frame = f;
		this.setBackground(new Color(227, 253, 250));
		
		// cancel the default F10
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "none");
		
		//File menu
		String[] fileItems = new String[]{"New", "Open...", "Save", "Save As...", "Save All", "Close", "Close All"};	
		char[] fileShortcuts = {'N', 'O', 'S', ' ', ' ', 'W', ' '};
		JMenu fileMenu = new JMenu("File");		
		for(int i=0; i<fileItems.length; i++)
		{
			JMenuItem item = new JMenuItem(fileItems[i]);
			if(fileShortcuts[i] != ' ')
			{
				//Ctrl + '...'
				item.setAccelerator(KeyStroke.getKeyStroke(fileShortcuts[i],
				Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
			}	
			item.addActionListener(new fileHandler(i));
			fileMenu.add(item);
		}
		
		//Edit menu
		String[] editItems = new String[]{"Undo", "Redo", "Cut", "Copy", "Paste", "Select All"};
		char[] editShortcuts = {'Z', 'Y', 'X', 'C', 'V', 'A'};
		JMenu editMenu = new JMenu("Edit");		
		for(int i=0; i<editItems.length; i++)
		{		
			JMenuItem item = new JMenuItem(editItems[i]);
			
			switch(i)
			{
				case 2:
					item = editMenu.add(new DefaultEditorKit.CutAction());
					item.setText("Cut");
					break;
				case 3:
					//item.configurePropertiesFromAction(new DefaultEditorKit.CopyAction());
					item = editMenu.add(new DefaultEditorKit.CopyAction());
					item.setText("Copy");
					break;
				case 4:
					//item.configurePropertiesFromAction(new DefaultEditorKit.PasteAction());
					item = editMenu.add(new DefaultEditorKit.PasteAction());
					item.setText("Paste");
					break;
			}
			
			if(editShortcuts[i] != ' ')
			{
				//Ctrl + '...'				
				item.setAccelerator(KeyStroke.getKeyStroke(editShortcuts[i],
				Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
			}
			item.addActionListener(new editHandler(i));
			editMenu.add(item);
		}
		
		//Search menu
		String[] searchItems = new String[]{"Find", "Replace"};
		char[] searchShortcuts = {'F', 'R'};
		JMenu searchMenu = new JMenu("Search");		
		for(int i=0; i<searchItems.length; i++)
		{
			JMenuItem item = new JMenuItem(searchItems[i]);	
			if(searchShortcuts[i] != ' ')
			{
				//Ctrl + '...'				
				item.setAccelerator(KeyStroke.getKeyStroke(searchShortcuts[i],
				Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
			}
			item.addActionListener(new searchHandler(i));
			searchMenu.add(item);
		}
		
		//View menu
		String[] viewItems = new String[]{"Typesetting"};
		char[] viewShortcuts = {'T'};
		JMenu viewMenu = new JMenu("View");		
		for(int i=0; i<viewItems.length; i++)
		{
			JMenuItem item = new JMenuItem(viewItems[i]);
			if(viewShortcuts[i] != ' ')
			{
				//Ctrl + '...'
				item.setAccelerator(KeyStroke.getKeyStroke(viewShortcuts[i],
				Toolkit.getDefaultToolkit( ).getMenuShortcutKeyMask( ), false));
			}
			item.addActionListener(new viewHandler(i));
			viewMenu.add(item);
		}
		
		//Execute menu
		String[] exeItems = new String[]{"Compile", "Run", "Compile & Run"};
		String[] exeShortcuts = {"F9", "F10", "F11"};
		JMenu exeMenu = new JMenu("Execute");		
		for(int i=0; i<exeItems.length; i++)
		{
			JMenuItem item = new JMenuItem(exeItems[i]);
			if(!exeShortcuts[i].equals(" "))
			{
				item.setAccelerator(KeyStroke.getKeyStroke(exeShortcuts[i]) );
			}
			item.addActionListener(new exeHandler(i));
			exeMenu.add(item);
		}		
		
		this.add(fileMenu);
		this.add(editMenu);
		this.add(searchMenu);
		this.add(viewMenu);
		this.add(exeMenu);
	}
	
	private class fileHandler implements ActionListener
	{
		private String[] fileMethod = new String[]{"newFile", "openFile", "saveFile", "saveAs", "saveAll", "closeFile", "closeAll"};
		private int index;		
		
		public fileHandler(int n)
		{
			index = n;			
		}
		
		@Override
		public void actionPerformed(ActionEvent ev)
		{
			try
			{
				Method method = Ide.class.getMethod(fileMethod[index]);   		        			
				method.invoke(frame);        
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}		
	}
	
	private class editHandler implements ActionListener
	{
		private String[] editMethod = new String[]{"undo", "redo", "cut", "copy", "paste", "selectAll"};
		private int index;		
		
		public editHandler(int n)
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
	
	private class searchHandler implements ActionListener
	{
		private String[] editMethod = new String[]{"find", "replace"};
		private int index;		
		
		public searchHandler(int n)
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
	
	private class viewHandler implements ActionListener
	{
		private String[] editMethod = new String[]{"typeset"};
		private int index;		
		
		public viewHandler(int n)
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
	
	private class exeHandler implements ActionListener
	{
		private String[] exeMethod = new String[]{"compile", "execute", "compileRun"};
		private int index;		
		
		public exeHandler(int n)
		{
			index = n;			
		}
		
		@Override
		public void actionPerformed(ActionEvent ev)
		{
			try
			{
				Method method = Ide.class.getMethod(exeMethod[index]);   		        			
				method.invoke(frame);        
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}		
	}
}