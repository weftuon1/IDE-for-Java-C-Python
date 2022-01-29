import java.awt.Color;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

class MyHighlightPainter extends DefaultHighlightPainter
{
	public MyHighlightPainter(Color color)
	{
		super(color);
	}
}