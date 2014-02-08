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
import javax.swing.*;
import java.awt.Dimension;

//Class Definition
public class Main {
	//Main Method
	public static void main(String[] args) {
		//Components
		private JFrame frame;
		private Container pane;
		private CodePad codePad;
		private MenuBar menuBar;
		private StatusBar statusBar;

		//Create Swing Thread
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Set look and feel to that of OS
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//Make the frame and set its properties
				frame = new JFrame("CodePad 1.0");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(800,750);
				frame.setMinimumSize(new Dimension(400,350));
				frame.setLocationRelativeTo(null);
				frame.setLayout(new BorderLayout());
				pane = frame.getContentPane();

				//Create a new "CodePad" and add it to the frame's content pane
				codePad = new CodePad(800,750);
				pane.add(codePad, BorderLayout.CENTER);

				//Create a new "MenuBar" and add it to the frame's content pane
				menuBar = new MenuBar();
				frame.setJMenuBar(menuBar);

				//Create a "StatusBar" and add it to the frame's content pane
				statusBar = new StatusBar();
				statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
				frame.add(statusBar, BorderLayout.SOUTH);


				//Make the frame visible
				frame.setVisible(true);
			}
		});
	}
}