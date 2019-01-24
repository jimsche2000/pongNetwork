package gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class JTextFieldJustNumbersDocument extends PlainDocument {

	private static final long serialVersionUID = 1L;
	private int limit;

	public JTextFieldJustNumbersDocument() {
		limit = -1;
	}

	public JTextFieldJustNumbersDocument(int limit) {
		this.limit = limit;
	}

	public void setCharLimit(int limit) {
		this.limit = limit;
	}

	public void insertString(int offset, String str, AttributeSet set) throws BadLocationException {
		if (str == null) {
			return;
		} else {
			try {
				Integer.parseInt(str);
				if (limit != -1) {
					if ((getLength() + str.length()) <= limit) {
						super.insertString(offset, str, set);
					}
				}
			} catch (Exception e) {
				//Input isn't a Integer-Number
//				e.printStackTrace();
			}
		}
	}
}
