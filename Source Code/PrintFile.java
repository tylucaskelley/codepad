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

//This "PrintFile" class handles all printing logic.

//Imports
import java.awt.*;
import java.io.*;
import java.awt.print.*;

//Class Definition
public class PrintFile implements Printable {
	//Variables
	private String printData;

	//Constructor
	public PrintFile(String input) {
		printData = input;
	}

	@Override
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
		//Should only have one page
		if (page > 0) {
			return NO_SUCH_PAGE;
		}

		//Set margins so data is printer-safe
		Graphics2D g2D = (Graphics2D)g;
		int x = (int) pf.getImageableX();
		int y = (int) pf.getImageableY();
		g2D.translate(x,y);

		//Calcualte the line height
		Font font = new Font("Serif", Font.PLAIN, 10);
		FontMetrics fm = g.getFontMetrics(font);
		int lineHeight = fm.getHeight();

		BufferedReader br = new BufferedReader(new StringReader(printData));
		try {
			String line;
			x += 50;
			y += 50;
			while ((line = br.readLine()) != null) {
				y += lineHeight;
				g2D.drawString(line, x, y);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return PAGE_EXISTS;
	}
}