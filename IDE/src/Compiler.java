import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class Compiler extends JTable implements ComponentListener
{
	private final DefaultTableModel DTM = new DefaultTableModel()
		{
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex)
			{
				return false;		
			}
		};
	private final String[] colStr = {"Line", "File", "Message"};
	private int rowData = 0;
	
	public Compiler()
	{
		this.setModel(DTM);
		DTM.setColumnIdentifiers(colStr);		
		DTM.addRow(new Object[]{"", "", ""});
		// layout
		this.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		this.getColumnModel().getColumn(0).setMaxWidth(60);					
	}			

	public int parseMsg(String string, String ext)
	{		
		try
		{		
			int errNumber = 0;
			if(ext.equals("java"))
			{
				int index = string.lastIndexOf('\n');			
				index = string.lastIndexOf('\n', index - 1) + 1;
				errNumber = Integer.parseInt(string.substring(index).split(" ")[0]);
				rowData = errNumber;			
				if(DTM.getRowCount() < rowData)
					DTM.setRowCount(rowData);
				clean();
				String arr[] = string.split("\n");					
				int j=0;
				for(int i=0;i<arr.length;i++)
				{
					int pre = 0;
					int pos = arr[i].indexOf(".java:");				
					if(pos == -1)
						continue;			
					pos += 5;								
					DTM.setValueAt(arr[i].substring(pre, pos), j, 1);
					pre = pos + 1;
					pos = arr[i].indexOf(":", pre);
					DTM.setValueAt(arr[i].substring(pre, pos), j, 0);
					pre = pos + 1;				
					DTM.setValueAt(arr[i].substring(pre), j, 2);			
					j++;
				}
				JOptionPane.showMessageDialog(null, errNumber + " errors");
			}
			else if(ext.equals("c") || ext.equals("cpp"))
			{					
				String arr[] = string.split("\n");		
				for(int i=0;i<arr.length;i++)
					if(arr[i].indexOf(": error:") != -1)
						errNumber++;
				rowData = errNumber;			
				if(DTM.getRowCount() < rowData)
					DTM.setRowCount(rowData);
				clean();
									
				int j=0;
				
				for(int i=0;i<arr.length;i++)
				{
					int pre = 0;
					int pos = arr[i].indexOf(": error:");				
					if(pos == -1)
						continue;
					pos = arr[i].lastIndexOf(":", pos - 1);
					pre = arr[i].lastIndexOf(":", pos - 1);
					DTM.setValueAt(arr[i].substring(pre + 1, pos), j, 0);
					DTM.setValueAt(arr[i].substring(0, pre), j, 1);					
					
					pre = arr[i].lastIndexOf(": error:") + 2;
					DTM.setValueAt(arr[i].substring(pre), j, 2);			
					j++;
				}
				JOptionPane.showMessageDialog(null, errNumber + " errors");
			}
			return errNumber;
		}
		catch(Exception e)
		{
			System.out.println(e);
			rowData = 0;
			clean();
			JOptionPane.showMessageDialog(null, "No error");
			return 0;
		}
	}
	
	public void clean()
	{
		for(int i=0;i<DTM.getRowCount();i++)
			for(int j=0;j<DTM.getColumnCount();j++)
				DTM.setValueAt("", i, j);	
	}	

	@Override
	public void componentHidden(ComponentEvent e) 
	{
    
    }

	@Override
    public void componentMoved(ComponentEvent e) 
	{
    
    }

	@Override
    public void componentResized(ComponentEvent e) 
	{
		int totalh = e.getComponent().getHeight();						
		int headerh = this.getTableHeader().getHeight();
		int rowh = this.getRowHeight();
		DTM.setRowCount(Math.max((totalh-headerh-(int)(rowh*0.25))/rowh, rowData));
    }

	@Override
    public void componentShown(ComponentEvent e) 
	{
    
    }	
}