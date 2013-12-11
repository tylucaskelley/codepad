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

//This "MenuBar" class contains the menu bar and all of its items.

//Imports
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//Class Definition
public class MenuBar extends JMenuBar {
	//Components
	private JMenu fileMenu;
		private JMenuItem newItem;
		private JMenuItem openItem;
		private JMenuItem saveItem;
		private JMenuItem saveAsItem;
		private JMenuItem printItem;
		private JMenuItem closeItem;
	private JMenu editMenu;
		private JMenuItem cutItem;
		private JMenuItem copyItem;
		private JMenuItem pasteItem;
		private JMenuItem undoItem;
		private JMenuItem redoItem;
		private JMenuItem findItem;
	private JMenu styleMenu;
		private JMenuItem fontItem;
		private JMenuItem lineWrapItem;
		private JMenuItem backgroundItem;
	private JMenu helpMenu;
		private JMenuItem aboutItem;
		private JMenuItem helpItem;
		private JMenuItem timeDateItem;
		private JMenuItem updateItem;

	//Constructor
	public MenuBar() {
		makeMenuBar();
	}

	//Puts together the menu bar
	private void makeMenuBar() {
		makeFileMenu();
		makeEditMenu();
		makeStyleMenu();
		makeHelpMenu();
	}

	//Methods related to the file menu

	//Makes the file menu
	public void makeFileMenu() {

	}

	//Creates a new file
	public void newFile() {

	}

	//Opens a file using a JFileChooser
	public void openFile() {

	}

	//Saves current file
	public void saveFile() {

	}

	//Saves the current file as a new file
	public void saveAs() {

	}

	//Sends the current file to be printed
	public void printFile() {

	}

	//Closes the program
	public void closeProgram() {

	}

	//Methods related to the edit menu

	//Makes the edit menu
	public void makeEditMenu() {

	}

	//Undo last action
	private void undoAction() {
		
	}

	//Redo last action
	private void redoAction() {

	}

	//Find and Replace text
	private void findAndReplace() {

	}

	//Methods related to the style menu

	//Make the style menu
	public void makeStyleMenu() {

	}

	//Changes the font, size, color, and styling 
	private void changeFont() {

	}

	//Sets the lineWrap on or off
	private void changeLineWrap() {

	}

	//Reverses the background color between black, grey, and white
	private void changeBackground() {

	}

	//Methods related to the help menu

	//Make the help menu
	public void makeHelpMenu() {

	}

	//Shows an about popup
	public void showAbout() {

	}

	//Opens Help.txt
	public void openHelp() {

	}

	//Shows Time and Date in popup, with option to add timestamp to end of document
	public void showDate() {

	}

	//Sends the user to GitHub for latest version
	public void checkForUpdates() {

	}
}