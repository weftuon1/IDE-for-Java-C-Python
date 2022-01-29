import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class Message extends JTabbedPane
{
	public final Compiler compilerMsg = new Compiler();	
	public final JScrollPane scroller;	
	
	public Message()
	{
		this.setTabPlacement(JTabbedPane.TOP);
		this.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		this.addTab("Compiler", scroller = new JScrollPane(compilerMsg));		
		this.addTab("Close", new JPanel());				
		scroller.addComponentListener(compilerMsg);
	}
}