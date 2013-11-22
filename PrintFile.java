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
import java.io.*;
import java.awt.print.*;

//class to handle printing current text on-screen
public class PrintFile implements Printable {
    private String printData;

    public PrintFile(String printDataIn) {
		this.printData = printDataIn;
    }

	@Override
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		// Should only have one page
		if (page > 0) {
			return NO_SUCH_PAGE;
		}

		// Adding the "Imageable" to the x and y puts the margins on the page to make it safe for printing.
		Graphics2D g2d = (Graphics2D)g;
		int x = (int) pf.getImageableX();
		int y = (int) pf.getImageableY();        
		g2d.translate(x, y); 

		// Calculate the line height
		Font font = new Font("Serif", Font.PLAIN, 10);
		FontMetrics metrics = g.getFontMetrics(font);
		int lineHeight = metrics.getHeight();

		BufferedReader br = new BufferedReader(new StringReader(printData));

		//Draw the page:
		try {
			String line;
			x += 50;
			y += 50;
			while ((line = br.readLine()) != null) {
				y += lineHeight;
				g2d.drawString(line, x, y);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return PAGE_EXISTS;
	}
}