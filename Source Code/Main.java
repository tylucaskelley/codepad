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

//This "Main" class creates an instance of the "CodePad" class and displays it to run the program.

//Imports
import javax.swing.JFrame;
import java.awt.Dimension;

//Class Definition
public class Main {
	//Main Method
	public static void main(String[] args) {
		//Make the frame and set its properties
		JFrame frame = new JFrame("CodePad");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800,750);
		frame.setMinimumSize(new Dimension(500,500));

		//Place frame in the middle of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);

		//Create a new "CodePad" and add it to the frame's content pane
		CodePad codepad = new CodePad();
		Container pane = frame.getContentPane();
		pane.add(codepad);

		//Make the frame visible
		frame.setVisible(true);
	}
}