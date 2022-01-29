import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.border.EmptyBorder;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.AbstractDocument;
import javax.swing.event.DocumentEvent.EventType;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

public class EditPanel extends JPanel
{
	private EditText editArea;
	private JScrollPane scroller;
	private Path path;
	private JTabbedPane tabPane;
	private UndoManager undoManager;
	private Status status;	
	/*private JTable table;
	private DefaultTableModel tableModel;
	private String[] tabletitle = {"File", "Line", "Error", "Message"}; 
	private Object[][] compileData ={{null, null, null, null}};
	private int compileDataRow = 1;
	*/
	public EditPanel(JTabbedPane tp, Status status)
	{
		this.setLayout(new GridLayout());
			
		this.editArea = new EditText(this, status);
		this.editArea.setBackground(Color.BLACK);
		this.editArea.setCaretColor(Color.WHITE);
		this.editArea.setFont(new Font("Consolas", Font.PLAIN, 16));
		this.editArea.setBorder(new EmptyBorder(5, 7, 5, 5));
		
		this.undoManager = new UndoManager();
		this.editArea.getDocument().addUndoableEditListener(new UndoableEditListener()
		{
			public void undoableEditHappened(UndoableEditEvent e)
			{
				UndoableEdit edit = e.getEdit();
				if(edit instanceof AbstractDocument.DefaultDocumentEvent)
				{
					AbstractDocument.DefaultDocumentEvent event = (AbstractDocument.DefaultDocumentEvent)edit;
					if(event.getType() != EventType.CHANGE)
					{
						undoManager.addEdit(e.getEdit());
					}
				}
			}
		}); 
		
		this.scroller = new JScrollPane(editArea);		
		this.add(scroller);		
		TextLineNumber tln = new TextLineNumber(editArea, Color.YELLOW, Color.BLACK, Color.GREEN);
		scroller.setRowHeaderView( tln );		
				
		this.tabPane = tp;
		this.status = status;
		
		/*
		this.table = new JTable();
		this.tableModel = new DefaultTableModel(compileData, tabletitle);
		this.table.setModel(tableModel);
		this.table.setPreferredScrollableViewportSize(new Dimension(400, 100));
		this.table.setCellSelectionEnabled(true);
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		*/
		this.editArea.paintText();
	}	
	
	public String getFilePath()
	{
		if(path==null)
			return "";
		
		return String.format("%s",path.toAbsolutePath());
	}
	
	public String getDir()
	{
		if(path==null)
			return "";
		return String.format("%s", path.getParent().toAbsolutePath()) + '\\';
	}
	
	public String getFileName()
	{
		if(path==null)
			return "";
		String tmp = String.format("%s", path.getFileName());
		return tmp.substring(0, tmp.lastIndexOf('.'));
	}
	
	public String getExt()
	{
		if(path==null)
			return "";
		String tmp = String.format("%s", path.getFileName());
		return tmp.substring(tmp.lastIndexOf('.')+1);
	}
	
	public void setFilePath(String filePosition)
	{
		this.path = Paths.get(filePosition);			
		int tabIndex = tabPane.indexOfComponent(this);
		tabPane.setTitleAt(tabIndex, String.format("%s", this.path.getFileName()));
		setLang();
		editArea.changeDocumentListener();
	}
	
	public void setLang()
	{
		if(getExt().equals("java"))
			status.setLang("Java");
		else if(getExt().equals("c"))
			status.setLang("C");
		else if(getExt().equals("cpp"))
			status.setLang("C++");
		else if(getExt().equals("py"))
			status.setLang("Python");
		else
			status.setLang("Normal");		
	}
	
	public String getText()
	{
		return editArea.getText();
	}
	
	public void setText(String fileString)
	{		
		this.editArea.setText(fileString);
	}
	
	public UndoManager getUndoManager()
	{
		return this.undoManager;
	}
	
	public EditText getEditArea()
	{
		return this.editArea;
	}	
}
