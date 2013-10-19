/*
*    CodePad, Version 1.0
*    By Ty-Lucas Kelley
*	
*	 **LICESNSE**
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

//imports
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;

//class that displays line numbers, highlights current line number.
public class LineNumbering extends JPanel implements CaretListener, DocumentListener, PropertyChangeListener {
	public final static float LEFT = 0.0f;
	public final static float CENTER = 0.5f;
	public final static float RIGHT = 1.0f;
	private final static Border OUTER = new MatteBorder(0, 0, 0, 2, Color.GRAY);
	private final static int HEIGHT = Integer.MAX_VALUE - 1000000;

	//  Text component this LineNumbering component is in sync with
	private JTextComponent component;

	//  Properties that can be changed
	private boolean updateFont;
	private int borderGap;
	private Color currentLineForeground;
	private float digitAlignment;
	private int minimumDisplayDigits;

	//  Keep history information to reduce the number of times the component needs to be repainted
    private int lastDigits;
    private int lastHeight;
    private int lastLine;
	private HashMap<String, FontMetrics> fonts;

	//Create a line number component for a text component. This minimum display width will be based on 3 digits.
	public LineNumbering(JTextComponent component) {
		this(component, 3);
	}
	
	//Create a line number component for a text component. This minimum display width will be based on 3 digits.
	public LineNumbering(JTextComponent component, int minimumDisplayDigits) {
		this.component = component;

		setFont( component.getFont() );

		setBorderGap( 5 );
		setCurrentLineForeground( Color.RED );
		setDigitAlignment( RIGHT );
		setMinimumDisplayDigits( minimumDisplayDigits );

		component.getDocument().addDocumentListener(this);
		component.addCaretListener( this );
		component.addPropertyChangeListener("font", this);
	}
	
	//get the update font property (indicates if a font has been changed)
	public boolean getUpdateFont() {
		return updateFont;
	}

	//when font for related text component has been changed, update the font of the line numbers to match
	public void setUpdateFont(boolean updateFont) {
		this.updateFont = updateFont;
	}

	//get the border gap
	public int getBorderGap() {
		return borderGap;
	}

	//set the border gap
	public void setBorderGap(int borderGap) {
		this.borderGap = borderGap;
		Border inner = new EmptyBorder(0, borderGap, 0, borderGap);
		setBorder( new CompoundBorder(OUTER, inner) );
		lastDigits = 0;
		setPreferredWidth();
	}

	//returns the color used to show the current line number
	public Color getCurrentLineForeground() {
		return currentLineForeground == null ? getForeground() : currentLineForeground;
	}

	//sets the color used to display current line number
	public void setCurrentLineForeground(Color currentLineForeground) {
		this.currentLineForeground = currentLineForeground;
	}

	//returns the alignment of the digits
	public float getDigitAlignment() {
		return digitAlignment;
	}

	//sets the horizontal alignment of the digits in the line number panel
	public void setDigitAlignment(float digitAlignment) {
		this.digitAlignment =
			digitAlignment > 1.0f ? 1.0f : digitAlignment < 0.0f ? -1.0f : digitAlignment;
	}

	//gets the minimum display digits
	public int getMinimumDisplayDigits() {
		return minimumDisplayDigits;
	}

	//specifies the minimum number of digits used to set width of line numbering panel. Default is 3
	public void setMinimumDisplayDigits(int minimumDisplayDigits) {
		this.minimumDisplayDigits = minimumDisplayDigits;
		setPreferredWidth();
	}

	//calculates width needed to display maximum line numbers
	private void setPreferredWidth() {
		Element root = component.getDocument().getDefaultRootElement();
		int lines = root.getElementCount();
		int digits = Math.max(String.valueOf(lines).length(), minimumDisplayDigits);

		//  Update sizes when number of digits in the line number changes
		if (lastDigits != digits) {
			lastDigits = digits;
			FontMetrics fontMetrics = getFontMetrics( getFont() );
			int width = fontMetrics.charWidth( '0' ) * digits;
			Insets insets = getInsets();
			int preferredWidth = insets.left + insets.right + width;

			Dimension d = getPreferredSize();
			d.setSize(preferredWidth, HEIGHT);
			setPreferredSize( d );
			setSize( d );
		}
	}

	//draws the line numbers
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//	Determine the width of the space available to draw the line number
		FontMetrics fontMetrics = component.getFontMetrics( component.getFont() );
		Insets insets = getInsets();
		int availableWidth = getSize().width - insets.left - insets.right;

		//  Determine the rows to draw within the clipped bounds.
		Rectangle clip = g.getClipBounds();
		int rowStartOffset = component.viewToModel( new Point(0, clip.y) );
		int endOffset = component.viewToModel( new Point(0, clip.y + clip.height) );

		while (rowStartOffset <= endOffset) {
			try {
    			if (isCurrentLine(rowStartOffset)) {
    				g.setColor( getCurrentLineForeground() );
				}
    			else {
    				g.setColor( getForeground() );
				}
				
    			//  Get the line number as a string and then determine the
    			//  "X" and "Y" offsets for drawing the string.
    			String lineNumber = getLineNumbering(rowStartOffset);
    			int stringWidth = fontMetrics.stringWidth( lineNumber );
    			int x = getOffsetX(availableWidth, stringWidth) + insets.left;
				int y = getOffsetY(rowStartOffset, fontMetrics);
    			g.drawString(lineNumber, x, y);

    			//  Move to the next row
    			rowStartOffset = Utilities.getRowEnd(component, rowStartOffset) + 1;
			}
			catch(Exception e) {
				break;
			}
		}
	}

	//tells whether or not the mouse is currently positioned on line about to be painted so we can highlight that number
	private boolean isCurrentLine(int rowStartOffset) {
		int caretPosition = component.getCaretPosition();
		Element root = component.getDocument().getDefaultRootElement();

		if (root.getElementIndex( rowStartOffset ) == root.getElementIndex(caretPosition)) {
			return true;
		}
		else {
			return false;
		}
	}

	//gets the line number about to be drawn. For word wrapping, returns empty string
	protected String getLineNumbering(int rowStartOffset)
	{
		Element root = component.getDocument().getDefaultRootElement();
		int index = root.getElementIndex( rowStartOffset );
		Element line = root.getElement( index );

		if (line.getStartOffset() == rowStartOffset) {
			return String.valueOf(index + 1);
		}
		else {
			return "";
		}
	}

	//X offset for proper alignment
	private int getOffsetX(int availableWidth, int stringWidth) {
		return (int)((availableWidth - stringWidth) * digitAlignment);
	}

	//Y offset for current row
	private int getOffsetY(int rowStartOffset, FontMetrics fontMetrics) throws BadLocationException {
		//  Get the bounding rectangle of the row
		Rectangle r = component.modelToView( rowStartOffset );
		int lineHeight = fontMetrics.getHeight();
		int y = r.y + r.height;
		int descent = 0;

		//  The text needs to be positioned above the bottom of the bounding rectangle based on the descent of the font(s) contained on the row.
		
		// default font is being used
		if (r.height == lineHeight) {
			descent = fontMetrics.getDescent();
		}
		//We need to check all the attributes for font changes
		else {
			if (fonts == null) {
				fonts = new HashMap<String, FontMetrics>();
			}
			Element root = component.getDocument().getDefaultRootElement();
			int index = root.getElementIndex( rowStartOffset );
			Element line = root.getElement( index );

			for (int i = 0; i < line.getElementCount(); i++) {
				Element child = line.getElement(i);
				AttributeSet as = child.getAttributes();
				String fontFamily = (String)as.getAttribute(StyleConstants.FontFamily);
				Integer fontSize = (Integer)as.getAttribute(StyleConstants.FontSize);
				String key = fontFamily + fontSize;

				FontMetrics fm = fonts.get( key );

				if (fm == null) {
					Font font = new Font(fontFamily, Font.PLAIN, fontSize);
					fm = component.getFontMetrics( font );
					fonts.put(key, fm);
				}
				descent = Math.max(descent, fm.getDescent());
			}
		}
		return y - descent;
	}

	//Implement CaretListener interface
	@Override
	public void caretUpdate(CaretEvent e) {
		//  Get the line the caret is positioned on
		int caretPosition = component.getCaretPosition();
		Element root = component.getDocument().getDefaultRootElement();
		int currentLine = root.getElementIndex( caretPosition );

		//  Need to repaint so the correct line number can be highlighted
		if (lastLine != currentLine) {
			repaint();
			lastLine = currentLine;
		}
	}

	//Implement DocumentListener interface
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

	//updates number of lines if document is changed (i.e. opened new file)
	private void documentChanged() {
		//  View of the component has not been updated at the time the DocumentEvent is fired
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					int endPos = component.getDocument().getLength();
					Rectangle rect = component.modelToView(endPos);

					if (rect != null && rect.y != lastHeight) {
						setPreferredWidth();
						repaint();
						lastHeight = rect.y;
					}
				}
				catch (BadLocationException ex) { /* nothing to do */ }
			}
		});
	}

	//  Implement PropertyChangeListener interface
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getNewValue() instanceof Font) {
			if (updateFont) {
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