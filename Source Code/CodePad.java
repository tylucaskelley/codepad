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

//This "CodePad" class puts everything together and creates the panel to be added to the "Main" class's frame.

//Imports
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

//Class Definition
public class CodePad extends JPanel {
	//Components
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private LineNumbers lineNumbers;
	private StatusBar statusBar;

	//Variables
	private boolean lineWrap;

	//Constructor
	public CodePad(int width, int height) {
		//Set the properties
		super.setPreferredSize(width, height);

		//Add the text area and line numbering
		textArea = new JTextArea();


		//Add listeners
	}

	//Accesor Methods

	//Return the textarea
	public void getTextArea() {
		return textArea;
	}


}