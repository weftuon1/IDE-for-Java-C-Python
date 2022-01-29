import javax.swing.event.DocumentEvent.EventType;
import javax.swing.text.AbstractDocument;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

class UndoManagerFix extends UndoManager
{
	private EditText editArea;
	
	public UndoManagerFix(EditText editArea)
	{
		super();
		this.editArea = editArea;
	}
	
    @Override
    public synchronized void undo() throws CannotUndoException
	{
        do
		{
            UndoableEdit edit = super.editToBeUndone();
            if(edit instanceof AbstractDocument.DefaultDocumentEvent)
			{
                AbstractDocument.DefaultDocumentEvent event = (AbstractDocument.DefaultDocumentEvent)edit;
                if(event.getType() == EventType.CHANGE)
				{
                    if(super.canUndo())
						super.undo();
                    continue;
                }
            }
            break;
        }
		while(true);

        if(super.canUndo())
			super.undo();
    }

    @Override
    public synchronized void redo() throws CannotRedoException
	{	
        int caretPosition = this.editArea.getCaretPosition();
		
		if(super.canRedo())
		{
			//System.out.println("canRedo");
			super.redo();
		}	
		//else
			//System.out.println("cannotRedo");
		
        do
		{
            UndoableEdit edit = super.editToBeRedone();
            if(edit instanceof AbstractDocument.DefaultDocumentEvent)
			{
                AbstractDocument.DefaultDocumentEvent event = (AbstractDocument.DefaultDocumentEvent)edit;
                if(event.getType() == EventType.CHANGE)
				{
					if(super.canRedo())					
						super.redo();
                    continue;
                }
            }
            break;
        }
		while(true);
			
        this.editArea.setCaretPosition(caretPosition);
    }
}