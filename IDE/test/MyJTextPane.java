import java.awt.Color;
import java.util.Set;
import java.util.*;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.*;
import java.awt.*;

import java.util.StringTokenizer;


import java.awt.event.*;

import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.text.rtf.RTFEditorKit;
/**
 * 自定义的JTextPane，扩展了JTextPane没有的一些功能（java关键字、单行注释、多行注释加亮）
 * 
 * @author myafluy@gmail.com
 * @since 2013-08-17
 * 
 */

public class MyJTextPane extends JTextPane implements DocumentListener {
    private Set<String> keywords;
    private Style keywordStyle;
    private Style normalStyle;
    private Style classNameStyle;

    public MyJTextPane() {
        super();
        this.getDocument().addDocumentListener(this);
        // 准备着色使用的样式
        keywordStyle = ((StyledDocument) getDocument()).addStyle(
                "Keyword_Style", null);
        normalStyle = ((StyledDocument) getDocument()).addStyle(
                "Keyword_Style", null);

        StyleConstants.setForeground(keywordStyle, Color.RED);

        StyleConstants.setForeground(normalStyle, Color.BLACK);

        //获取关键字
		Set KeyWordSet = new HashSet();  
 
		String s1 = new String("hello");  
		String s2 = new String("world"); 
		String s3 = new String("class"); 
		 
		KeyWordSet.add(s1);  
		KeyWordSet.add(s2);  
		KeyWordSet.add(s3);  

        keywords = KeyWordSet;
        System.out.println(keywords.size());
    }
	
	public static void main(String[] args) 
	{
		JFrame frame = new JFrame("test text pane");
		frame.getContentPane().add(new MyJTextPane());
		WindowListener wndCloser = new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e) 
			{
				System.exit(0);
			}
		};
		frame.addWindowListener(wndCloser);
		final int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds ( inset, inset, screenSize.width - inset*2, screenSize.height - inset*2 );
		frame.show();
	}
	
    /**
     * 设置全文本属性
     * 
     * @param attr
     * @param replace
     */
    public void setTextAttributes(AttributeSet attr, boolean replace) {
        int p0 = 0;
        int p1 = this.getText().length();
        if (p0 != p1) {
            StyledDocument doc = getStyledDocument();
            doc.setCharacterAttributes(p0, p1 - p0, attr, replace);
        } else {
            MutableAttributeSet inputAttributes = getInputAttributes();
            if (replace) {
                inputAttributes.removeAttributes(inputAttributes);
            }
            inputAttributes.addAttributes(attr);
        }
    }

    /**
     * 单行注释
     */
    public void setSingleLineNoteCharacterAttributes() {
        String text = this.getText();
        int startPointer = 0;
        int endPointer = 0;
        if ((startPointer = text.indexOf("//")) == -1) {
            return;
        }

        while ((endPointer = text.substring(startPointer).indexOf("\n")) != -1) {
            endPointer = startPointer + endPointer;
            if (startPointer >= endPointer) {
                break;
            }
            int hangshu = text.substring(0, endPointer).split("\\n").length;
            System.out.println("hangshu:" + hangshu);
            SwingUtilities
                    .invokeLater(new ColouringWord(this, startPointer - hangshu
                            + 1, endPointer - hangshu, new Color(63, 217, 95)));
            startPointer = text.substring(endPointer + 1).indexOf("//");
            startPointer = startPointer + endPointer + 1;

        }
    }

    /**
     * 多行注释
     */
    public void setMultiLineNoteCharacterAttributes() {
        String text = this.getText();
        int startPointer = 0;
        int endPointer = 0;
        if ((startPointer = text.indexOf("/*")) == -1) {
            return;
        }

        while ((endPointer = text.substring(startPointer).indexOf("*/")) != -1) {
            endPointer = startPointer + endPointer;
            if (startPointer >= endPointer) {
                break;
            }
            int hangshu = text.substring(0, endPointer).split("\\n").length;
            int kuaju = text.substring(startPointer, endPointer).split("\\n").length;
            SwingUtilities.invokeLater(new ColouringWord(this, startPointer
                    - hangshu + kuaju, endPointer + 3 - hangshu, new Color(63,
                    217, 95)));
            startPointer = text.substring(endPointer + 1).indexOf("/*");
            startPointer = startPointer + endPointer + 1;

        }
    }

    /**
     * 实时加亮关键字
     * @param styledDocument
     * @param pos
     * @param len
     * @throws BadLocationException
     */
    public void myColouring(StyledDocument styledDocument, int pos, int len)
            throws BadLocationException {
        int start = indexOfWordStart(styledDocument, pos);
        int end = indexOfWordEnd(styledDocument, pos + len);

        char ch;
        while (start < end) {
            ch = getCharAt(styledDocument, start);
            if (Character.isLetter(ch) || ch == '_') {//判断是否为字母
                start = myColouringWord(styledDocument, start);
            } else {
                SwingUtilities.invokeLater(new ColouringTask(styledDocument,
                        start, 1, normalStyle));
                ++start;
            }
        }
    }

    /**
     * 实时着色
     * 
     * @param doc
     * @param pos
     * @return
     * @throws BadLocationException
     */
    public int myColouringWord(StyledDocument doc, int pos)
            throws BadLocationException {
        int wordEnd = indexOfWordEnd(doc, pos);
        String word = doc.getText(pos, wordEnd - pos);

        if (keywords.contains(word)) {
            SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd
                    - pos, keywordStyle));
        } else {
            SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd
                    - pos, normalStyle));
        }

        return wordEnd;
    }

    /**
     * 取得在文档中下标在pos处的字符.
     * 
     * @param doc
     * @param pos
     * @return
     * @throws BadLocationException
     */
    public char getCharAt(Document doc, int pos) throws BadLocationException {
        return doc.getText(pos, 1).charAt(0);
    }

    /**
     * 取得下标为pos时, 它所在的单词开始的下标.
     * 
     * @param doc
     * @param pos
     * @return
     * @throws BadLocationException
     */
    public int indexOfWordStart(Document doc, int pos)
            throws BadLocationException {
        // 从pos开始向前找到第一个非单词字符.
        for (; pos > 0 && isWordCharacter(doc, pos - 1); --pos)
            ;

        return pos;
    }

    /**
     * 取得下标为pos时, 它所在的单词结束的下标.
     * 
     * @param doc
     * @param pos
     * @return
     * @throws BadLocationException
     */
    public int indexOfWordEnd(Document doc, int pos)
            throws BadLocationException {
        // 从pos开始向前找到第一个非单词字符.
        for (; isWordCharacter(doc, pos); ++pos)
            ;

        return pos;
    }

    /**
     * 如果一个字符是字母, 数字, 下划线, 则返回true.
     * 
     * @param doc
     * @param pos
     * @return
     * @throws BadLocationException
     */
    public boolean isWordCharacter(Document doc, int pos)
            throws BadLocationException {
        char ch = getCharAt(doc, pos);
        if (Character.isLetter(ch) || Character.isDigit(ch) || ch == '_') {
            return true;
        }
        return false;
    }

    @Override
    // 给出属性或属性集发生了更改的通知
    public void changedUpdate(DocumentEvent e) {

    }

    @Override
    // 给出对文档执行了插入操作的通知
    public void insertUpdate(DocumentEvent e) {
        try {
            myColouring((StyledDocument) e.getDocument(), e.getOffset(),
                    e.getLength());
            // noteFinder.ColorNote(this.getText());// 给注释上色
            setSingleLineNoteCharacterAttributes();
            setMultiLineNoteCharacterAttributes();
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    // 给出移除了一部分文档的通知
    public void removeUpdate(DocumentEvent e) {
        try {
            // 因为删除后光标紧接着影响的单词两边, 所以长度就不需要了
            myColouring((StyledDocument) e.getDocument(), e.getOffset(), 0);
            // noteFinder.ColorNote(this.getText());// 给注释上色
            setSingleLineNoteCharacterAttributes();
            setMultiLineNoteCharacterAttributes();
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 多线程绘制颜色
     * 
     * 
     * 
     */
    private class ColouringTask implements Runnable {
        private StyledDocument doc;
        private Style style;
        private int pos;
        private int len;

        public ColouringTask(StyledDocument doc, int pos, int len, Style style) {
            this.doc = doc;
            this.pos = pos;
            this.len = len;
            this.style = style;
        }

        public void run() {
            try {
                // 这里就是对字符进行着色
                doc.setCharacterAttributes(pos, len, style, false);
            } catch (Exception e) {
            }
        }
    }

}

/**
 * 多线程绘制颜色
 * 
 * 
 * 
 */
class ColouringWord implements Runnable {
    private int startPointer;
    private int endPointer;
    private Color color;
    private JTextPane jTextPane;

    public ColouringWord(JTextPane jTextPane, int pos, int len, Color color) {
        this.jTextPane = jTextPane;
        this.startPointer = pos;
        this.endPointer = len;
        this.color = color;
    }

    @Override
    public void run() {
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(attributeSet, color);
        boolean replace = false;
        int p0 = startPointer;
        int p1 = endPointer;
        if (p0 != p1) {
            StyledDocument doc = jTextPane.getStyledDocument();
            doc.setCharacterAttributes(p0, p1 - p0, attributeSet, replace);
        } else {
            MutableAttributeSet inputAttributes = jTextPane
                    .getInputAttributes();
            if (replace) {
                inputAttributes.removeAttributes(inputAttributes);
            }
            inputAttributes.addAttributes(attributeSet);
        }
    }
}