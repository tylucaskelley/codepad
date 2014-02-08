/*
*    CodePad, Version 1.0
*    By Ty-Lucas Kelley
*	
*	 **LICENSE**
*
*	 This file is a part of CodePad.
*
*	 CodePad is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    CodePad is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with CodePad.  If not, see <http://www.gnu.org/licenses/>.
*/

//This "LineNumbers" class extends JPanel and is used to display the current line number and total number of lines in the document.

//Imports
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;

//Class Definition
public class LineNumbers extends JPanel implements CaretListener, DocumentListener, PropertyChangeListener {
	//Static Variables
	public final static float LEFT = 0.0f;
	public final static float CENTER = 0.5f;
	public final static float RIGHT = 1.0f;
	private final static Border OUTER = new MatteBorder(0, 0, 0, 2, Color.GRAY);
	private final static int HEIGHT = Integer.MAX_VALUE - 1000000;

	//Variables
	private boolean fontState;
	private float alignment;
	private int borderGap;
	private int minDigits;
	private int lastDigits;
	private int lastLine;
	private int lastHeight;
	private Color currentColor;
	private HashMap<String, FontMetrics> fonts;
	private JTextComponent textComp;

	//Constructor
	public LineNumbers(JTextComponent textComp, int minDigits) {
		this.textComp = textComp;
		setFont(textComp.getFont());
		setBorderGap(5);
		setCurrentColor(Color.BLUE);
		setAlignment(RIGHT);
		setMinDigits(minDigits);

		textComp.getDocument().addDocumentListener(this);
		textComp.addCaretListener(this);
		textComp.addPropertyChangeListener("font", this);
	}

	//Paint method
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Determine how much space (width) is available
		FontMetrics fontMetrics = textComp.getFontMetrics(textComp.getFont());
		Insets insets = getInsets();
		int availableWidth = getSize().width - insets.left - insets.right;

		//Determine how much space (height) is available
		Rectangle clip = g.getClipBounds();
		int rowStartOffset = textComp.viewToModel(new Point(0, clip.y));
		int endOffset = textComp.viewToModel(new Point(0, clip.y + clip.height));

		while (rowStartOffset <= endOffset) {
			try {
				if (isCurrentLine(rowStartOffset)) {
					g.setColor(getCurrentColor());
				} else {	
					g.setColor(getForeground());
				}

				//Get the line number as a string to determine offsets
				String lineNumber = getLineNumbers(rowStartOffset);
				int sWidth = fontMetrics.stringWidth(lineNumber);
				int x = getOffsetX(availableWidth, sWidth) + insets.left;
				int y = getOffsetY(rowStartOffset, fontMetrics);
				g.drawString(lineNumber, x, y);
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}

	private boolean isCurrentLine(int rowStartOffset) {
		int caretPos = textComp.getCaretPosition();
		Element root = textComp.getDocument().getDefaultRootElement();

		if (root.getElementIndex(rowStartOffset) == root.getElementIndex(caretPos)) {
			return true;
		} else {	
			return false;
		}
	}

	protected String getLineNumbers(int rowStartOffset) {
		Element root = textComp.getDocument().getDefaultRootElement();
		int index = root.getElementIndex(rowStartOffset);
		Element line = root.getElement(index);

		if (line.getStartOffset() == rowStartOffset) {
			return String.valueOf(index + 1);
		}
		else {
			return "";
		}
	}

	private int getOffsetX(int aWidth, int sWidth) {
		return (int) ((aWidth - sWidth) * alignment);
	}

	private int getOffsetY(int rowStartOffset, FontMetrics fontMetrics) throws BadLocationException {
		//Get the row-bounding rectangle
		Rectangle r = textComp.modelToView(rowStartOffset);
		int lineHeight = fontMetrics.getHeight();
		int y = r.y + r.height;
		int descent = 0;

		//Check if default font is being used
		if (r.height == lineHeight) {
			descent = fontMetrics.getDescent();
		}

		//Check all attributes for changes
		else {
			if (fonts == null) {
				fonts = new HashMap<String, FontMetrics>();
			}

			Element root = textComp.getDocument().getDefaultRootElement();
			int index = root.getElementIndex(rowStartOffset);
			Element line = root.getElement(index);

			for (int i = 0; i < line.getElementCount(); i++) {
				Element child = line.getElement(i);
				AttributeSet as = child.getAttributes();
				String fontFamily = (String)as.getAttribute(StyleConstants.FontFamily);
				Integer fontSize = (Integer)as.getAttribute(StyleConstants.FontSize);
				String key = fontFamily + fontSize;
				FontMetrics fm = fonts.get(key);

				if (fm == null) {
					Font font = new Font(fontFamily, Font.PLAIN, fontSize);
					fm = textComp.getFontMetrics(font);
					fonts.put(key, fm);
				}
				descent = Math.max(descent, fm.getDescent());
			}
		}
		return y - descent;
	}

	//Set the preferred width of the panel
	private void setPreferredWidth() {
		Element root = textComp.getDocument().getDefaultRootElement();
		int lines = root.getElementCount();
		int digits = Math.max(String.valueOf(lines).length(), minDigits);

		//Update when number of digits in the line number changes
		if (lastDigits != digits) {
			lastDigits = digits;
			FontMetrics fm = getFontMetrics(getFont());
			int width = fm.charWidth('0') * digits;
			Insets i = getInsets();
			int preferredWidth = i.left + i.right + width;
			Dimension d = getPreferredSize();
			d.setSize(preferredWidth, HEIGHT);
			setPreferredSize(d);
			setSize(d);
		}
	}

	/*
	* Accessor Methods
	*/

	//Indicates if font has been changed
	public boolean getFontState() {
		return fontState;
	}

	//Returns the alignment of the digits
	public float getAlignment() {
		return alignment;
	}

	//Returns the border gap
	public int getBorderGap() {
		return borderGap;
	}

	//Returns the minimum display digits
	public int getMinDigits() {
		return minDigits;
	}

	//Returns the number of the last line
	public int getLastDigits() {
		return lastDigits;
	}

	//Returns the last line
	public int getLastLine() {
		return lastLine;
	}

	//Returns the last height
	public int getLastHeight() {
		return lastHeight;
	}

	//Returns the color used to indicate the current line number
	public Color getCurrentColor() {
		return currentColor == null ? getForeground() : currentColor;
	}

	//Returns current font information
	public HashMap<String, FontMetrics> getFonts() {
		return fonts;
	}

	//Returns the JTextComponent
	public JTextComponent getTextComp() {
		return textComp;
	}

	/*
	* Mutator Methods
	*/

	//Sets the font state
	public void setFontState(boolean fontState) {
		this.fontState = fontState;
	}

	//Sets the alignment of the digits
	public void setAlignment(float alignment) {
		this.alignment = alignment > 1.0f ? 1.0f : alignment < 0.0f ? -1.0f : alignment;
	}

	//Sets the border gap
	public void setBorderGap(int borderGap) {
		this.borderGap = borderGap;
		Border inner = new EmptyBorder(0, borderGap, 0, borderGap);
		setBorder(new CompoundBorder(OUTER, inner));
		lastDigits = 0;
		setPreferredWidth();
	}

	//Sets the minimum display digits
	public void setMinDigits(int minDigits) {
		this.minDigits = minDigits;
		setPreferredWidth();
	}

	//Sets the number of the last line
	public void setLastDigits(int lastDigits) {
		this.lastDigits = lastDigits;
	}

	//Sets the last line
	public void setLastLine(int lastLine) {
		this.lastLine = lastLine;
	}

	//Sets the last height
	public void setLastHeight(int lastHeight) {
		this.lastHeight = lastHeight;
	}

	//Sets the color used to indicate the current line number
	public void setCurrentColor(Color currentColor) {
		this.currentColor = currentColor;
	}

	//Sets current font information
	public void setFonts(HashMap<String, FontMetrics> fonts) {
		this.fonts = fonts;
	}

	//Sets the JTextComponent
	public void setTextComp(JTextComponent textComp) {
		this.textComp = textComp;
	}

	/*
	* Implement the Required Interfaces
	*/

	//Implement CaretListener
	@Override
	public void caretUpdate(CaretEvent e) {
		//Get the line the caret is on
		int cPos = textComp.getCaretPosition();
		Element root = textComp.getDocument().getDefaultRootElement();
		int line = root.getElementIndex(cPos);

		//Need to repaint so the correct line number is lit up
		if (lastLine != line) {
			repaint();
			lastLine = line;
		}
	}

	//Implement DocumentListener
	@Override
	public void changedUpdate(DocumentEvent e) {
		documentChanged();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		documentChanged();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		documentChanged();
	}

	//Updates number of lines
	private void documentChanged() {
		SwingUtilities.invokeLater(new Runnable () {
			@Override
			public void run() {
				try {
					int endPos = textComp.getDocument().getLength();
					Rectangle rect = textComp.modelToView(endPos);

					if (rect != null && rect.y != lastHeight) {
						setPreferredWidth();
						repaint();
						lastHeight = rect.y;
					}
				} catch (BadLocationException ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	//Implement PropertyChangeListener
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getNewValue() instanceof Font) {
			if (fontState == true) {
				Font newFont = (Font) evt.getNewValue();
				setFont(newFont);
				lastDigits = 0;
				setPreferredWidth();
			}
			else {
				repaint();
			}
		}
	}
}