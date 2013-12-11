import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/* This work is hereby released into the Public Domain.
 * To view a copy of the public domain dedication, visit
 * http://creativecommons.org/licenses/publicdomain/
 *
 * Barak Harizi: Improved so the combo box would search not only by the prefix, but by all the sub strings of the combo
 * items.
 */
public class AutoCompletion extends PlainDocument
{
	JComboBox comboBox;
	ComboBoxModel model;
	JTextComponent editor;
	// flag to indicate if setSelectedItem has been called
	// subsequent calls to remove/insertString should be ignored
	boolean selecting=false;
	boolean hitBackspace=false;
	boolean hitBackspaceOnSelection;

	KeyListener editorKeyListener;
	FocusListener editorFocusListener;

	String pattern = "";

	public AutoCompletion(final JComboBox comboBox)
	{
		this.comboBox = comboBox;
		model = comboBox.getModel();

		comboBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (!selecting) editor.selectAll();
			}
		});

		comboBox.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent e)
			{
				if (e.getPropertyName().equals("editor")) configureEditor((ComboBoxEditor) e.getNewValue());
				if (e.getPropertyName().equals("model")) model = (ComboBoxModel) e.getNewValue();
			}
		});

		editorKeyListener = new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if (comboBox.isDisplayable()) comboBox.setPopupVisible(true);
				hitBackspace=false;
				switch (e.getKeyCode())
				{
					// determine if the pressed key is backspace (needed by the remove method)
					case KeyEvent.VK_BACK_SPACE :
						hitBackspace=true;
						hitBackspaceOnSelection=editor.getSelectionStart()!=editor.getSelectionEnd();
						break;
					// ignore delete key
					case KeyEvent.VK_DELETE :
						e.consume();
						comboBox.getToolkit().beep();
						break;
					default:
						if (isValidChar(e.getKeyChar()))
						{
							pattern += e.getKeyChar();
						}
				}
			}
		};

		// Highlight whole text when gaining focus
		editorFocusListener = new FocusAdapter()
		{
			public void focusGained(FocusEvent e)
			{
				editor.selectAll();
			}
			public void focusLost(FocusEvent e)
			{
				pattern = "";
			}
		};
		configureEditor(comboBox.getEditor());
		// Handle initially selected object
		Object selected = comboBox.getSelectedItem();
		if (selected!=null) setText(selected.toString());
		editor.selectAll();
	}

	public static void enable(JComboBox comboBox)
	{
		// has to be editable
		comboBox.setEditable(true);
		// change the editor's document
		new AutoCompletion(comboBox);
	}

	void configureEditor(ComboBoxEditor newEditor)
	{
		if (editor != null)
		{
			editor.removeKeyListener(editorKeyListener);
			editor.removeFocusListener(editorFocusListener);
		}

		if (newEditor != null)
		{
			editor = (JTextComponent) newEditor.getEditorComponent();
			editor.addKeyListener(editorKeyListener);
			editor.addFocusListener(editorFocusListener);
			editor.setDocument(this);
		}
	}

	public void remove(int offs, int len) throws BadLocationException
	{
		// return immediately when selecting an item
		if (selecting) return;

		if (hitBackspace)
		{
			if (editor.getSelectionStart() == editor.getSelectionEnd()) editor.setCaretPosition(0);
			if (editor.getCaretPosition() > 0)
			{
				pattern = pattern.substring(0, pattern.length() - 1);
				editor.moveCaretPosition(editor.getCaretPosition() - 1);
			}
			else
			{
				comboBox.getToolkit().beep();
			}
		}
	}

	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
	{
		// return immediately when selecting an item
		if (selecting) return;

		// lookup and select a matching item
		Object item = lookupItem(pattern);

		if (item != null)
		{
			// insert the string into the document
			super.insertString(offs, str, a);
			setSelectedItem(item);
			int indexPattern = item.toString().indexOf(pattern);
			setText(item.toString());
			editor.select(indexPattern, indexPattern+pattern.length());
		}
		else
		{
			pattern = pattern.substring(0, pattern.length() - 1);
		}
	}

	private void setText(String text)
	{
		try
		{
			// remove all text and insert the completed string
			super.remove(0, getLength());
			super.insertString(0, text, null);
		}
		catch (BadLocationException e)
		{
			throw new RuntimeException(e.toString());
		}
	}

	private void setSelectedItem(Object item)
	{
		selecting = true;
		model.setSelectedItem(item);
		selecting = false;
	}

	private Object lookupItem(String pattern)
	{
		Object selectedItem = model.getSelectedItem();
		// only search for a different item if the currently selected does not match
		if (selectedItem != null && selectedItem.toString().contains(pattern))
		{
			return selectedItem;
		}
		else
		{
			// iterate over all items
			for (int i=0, n=model.getSize(); i < n; i++)
			{
				Object currentItem = model.getElementAt(i);
				// current item starts with the pattern?
				if (currentItem != null && currentItem.toString().contains(pattern))
				{
					return currentItem;
				}
			}
		}
		// no item starts with the pattern => return null
		return null;
	}

	public boolean isValidChar(char c)
	{
		return (c >='א' && c <= 'ת' ) || (c >= '0' && c <= '9') || c == ' ' || c == '"';
	}
}