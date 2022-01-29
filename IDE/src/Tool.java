import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.lang.reflect.Method;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.border.Border;

public class Tool extends JToolBar
{
	JFrame frame;
	public Tool(JFrame f)
	{
		String[] pngName = {"new", "open", "save", "saveall", "close", "closeall", "undo", "redo", "compile", "run", "cplusr"};
		frame = f;
		JButton b;
		Border emptyBorder = BorderFactory.createEmptyBorder(3, 3, 3, 3);

		for(int i=0;i<11; i++)
		{
			b = new JButton();
			try 
			{
				Image img = ImageIO.read(getClass().getResource("Resources/" + pngName[i] + ".png"));
				b.setIcon(new ImageIcon(img));
			} 
			catch (Exception ex) 
			{
				System.out.println(ex);
			}
			b.addActionListener(new toolHandler(i));
			b.setBorder(emptyBorder);
			b.setOpaque(false);
			this.add(b);
		}
	}

	private class toolHandler implements ActionListener
	{
		private String[] methodStr = new String[]{"New (Ctrl+N)", "Open... (Ctrl+O)", "Save (Ctrl+S)", 
										 "Save All", "Close (Ctrl+W)", "Close All",
										 "Undo (Ctrl+Z)", "Redo (Ctrl+Y)", 
										 "Compile (F9)", "Run (F10)", "Compile & Run (F11)"};
		private String[] methodArr = new String[]{"newFile", "openFile", "saveFile",
									   "saveAll", "closeFile", "closeAll",
									   "undo", "redo",
									   "compile", "execute", "compileRun"};
						
		private int index;		
		
		public toolHandler(int n)
		{
			index = n;			
		}
		
		@Override
		public void actionPerformed(ActionEvent ev)
		{
			try
			{
				Method method = Ide.class.getMethod(methodArr[index]);   		        			
				method.invoke(frame);        
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
	}
}