import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JFrame;
import java.awt.*;
import javax.swing.*;

public class Test extends JTextPane
{
	public static void main(String[] args)
	{
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(500, 500);
		Test t = new Test();
		t.setBounds(500, 500, 500, 500);
		JPanel p = new JPanel();
		p.add(t);
				
		JScrollPane s = new JScrollPane(p);
		f.add(s);
		
		f.setVisible(true);
	}
}