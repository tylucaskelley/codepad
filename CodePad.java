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
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.undo.*;

import java.io.*;
import java.net.*;
import java.awt.print.*;
import java.io.BufferedReader; 
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.*;

public class CodePad {
	//instance variables
	private JFrame frame; //the frame itself
	private JTextArea textArea; //the text area containing the document
	private JScrollPane scrollPane; //the scrollable pane to which the text area is added
	private LineNumbering lineNumbering; //the line numbering object
	
	private JMenuBar menubar; //the top menu bar
	
	private JMenu fileMenu; //file menu and its items
	private JMenuItem newItem;
	private JMenuItem openItem;
	private JMenuItem saveItem;
	private JMenuItem saveAsItem;
	private JMenuItem printItem;
	private JMenuItem closeItem;
	
	private JMenu editMenu; //edit menu and its items
	private JMenuItem cutItem;
	private JMenuItem copyItem;
	private JMenuItem pasteItem;
	private JMenuItem undoItem;
	private JMenuItem redoItem;
	private JMenuItem findAndReplaceItem;
	
	private JMenu styleMenu;
	private JMenuItem fontItem;
	private JMenuItem themeItem;
	private JMenuItem lineNumbersItem;
	private JMenuItem configureItem;
	
	private JMenu helpMenu; //help menu and its items
	private JMenuItem aboutItem;
	private JMenuItem helpItem;
	private JMenuItem timeDateItem;
	private JMenuItem updateItem;
	
	final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(); //value for keyboard shortcuts
	private JFileChooser fc = new JFileChooser(); //the GUI file chooser
	private Charset charset = Charset.forName("UTF-8"); //Defines charset
	private UndoManager undoManager = new UndoManager(); //stores undoable events
	
	//constructor
	public CodePad() {
		makeFrame();
	}
	
	//puts everything together and makes it visible
	private void makeFrame() {
		//make frame and add all menus and items
		frame = new JFrame("CodePad");
		frame.setSize(800,750);
		frame.setMinimumSize(new Dimension(500,500));
		makeMenuBar();
		
		//create the default font
		Font consolas;
		consolas = new Font("Consolas", Font.PLAIN, 14);
		
		//create text area and add it to the scrollable pane, then add that pane to the frame
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setFont(consolas);
		scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		//add line numbering
		lineNumbering = new LineNumbering(textArea);
		scrollPane.setRowHeaderView(lineNumbering);		
		frame.add(scrollPane);
		
		//add the UndoableEditListener to textArea
		Document doc = textArea.getDocument();
		doc.addUndoableEditListener(new UndoableEditListener() {
			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				undoManager.addEdit(e.getEdit());
			}
		});
		
		//add the KeyListener to textArea
		textArea.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_TAB) {
					e.consume();
					fourSpaces(textArea);
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {
				//do nothing
			}
			@Override
			public void keyReleased(KeyEvent e) {
				//do nothing
			}			
		});

		//make the frame visible and make sure it doesn't run in background
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	//make the menubar and its menus
	private void makeMenuBar() {
		menubar = new JMenuBar();
		frame.setJMenuBar(menubar);
		makeFileMenu();
		makeEditMenu();
		makeHelpMenu();
	}
	//make the file menu
	private void makeFileMenu() {
		fileMenu = new JMenu("File");
		menubar.add(fileMenu);
		
		newItem = new JMenuItem("New");
			newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));
			newItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					newFile();
				}
			});
		fileMenu.add(newItem);
		openItem = new JMenuItem("Open");
			openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, SHORTCUT_MASK));
			openItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					openFile();
				}
			});
		fileMenu.add(openItem);
		saveItem = new JMenuItem("Save");
			saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SHORTCUT_MASK));
			saveItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveFile();
				}
			});
		fileMenu.add(saveItem);
		saveAsItem = new JMenuItem("Save As...");
			saveAsItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveAs();
				}
			});
		fileMenu.add(saveAsItem);
		printItem = new JMenuItem("Print");
			printItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, SHORTCUT_MASK));
			printItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					printFile();
				}
			});	
		fileMenu.add(printItem);
		closeItem = new JMenuItem("Close");
			closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
			closeItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeProgram();
				}
			});
		fileMenu.add(closeItem);
	}

	//make the edit menu
	private void makeEditMenu() {
		editMenu = new JMenu("Edit");
		menubar.add(editMenu);
		
		cutItem = new JMenuItem(new DefaultEditorKit.CutAction());
		cutItem.setText("Cut");
		cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, SHORTCUT_MASK));
		editMenu.add(cutItem);
		
		copyItem = new JMenuItem(new DefaultEditorKit.CopyAction());
		copyItem.setText("Copy");
		copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, SHORTCUT_MASK));
		editMenu.add(copyItem);
		
		pasteItem = new JMenuItem(new DefaultEditorKit.PasteAction());
		pasteItem.setText("Paste");
		pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, SHORTCUT_MASK));
		editMenu.add(pasteItem);
		
		undoItem = new JMenuItem("Undo");
			undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, SHORTCUT_MASK));
			undoItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					undoAction();
				}
			});
		editMenu.add(undoItem);
		
		redoItem = new JMenuItem("Redo");
		redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, SHORTCUT_MASK));
		redoItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				redoAction();
			}
		});
		editMenu.add(redoItem);		
	}
	//make the help menu
	private void makeHelpMenu() {
		helpMenu = new JMenu("Help");
		menubar.add(helpMenu);
		
		aboutItem = new JMenuItem("About");
			aboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, SHORTCUT_MASK));
			aboutItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showAbout();
				}
			});
		helpMenu.add(aboutItem);
		updateItem = new JMenuItem("Check for Updates");
			updateItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateLink();
				}
			});
		helpMenu.add(updateItem);
		timeDateItem = new JMenuItem("Time and Date");
			timeDateItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, SHORTCUT_MASK));
			timeDateItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showDate();
				}
			});
		helpMenu.add(timeDateItem);
	}
	
	//methods for the fileMenu
	private void newFile() {
		textArea.setText("");
		frame.setTitle("CodePad");
		undoManager.discardAllEdits();
	}
	private void openFile() {
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fc.showOpenDialog(frame);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File openedFile = fc.getSelectedFile();
			BufferedReader reader = null;
		
			textArea.setText("");
			frame.setTitle(openedFile.toString());
		
			try {
				String line;
				reader = new BufferedReader(new FileReader(openedFile));
				while ((line = reader.readLine()) != null) {
					textArea.append(line + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			undoManager.discardAllEdits();
		}
		else {
			//do nothing
		}
	}
	private void saveFile() {
		File fileToSave = new File(frame.getTitle());
		BufferedWriter writer = null;
		
		frame.setTitle(fileToSave.toString());
		
		try {
			String doc = textArea.getText();
			if (fileToSave.getName() == "CodePad") {
				saveAs();
			}
			else {
				writer = new BufferedWriter(new FileWriter(fileToSave.getAbsoluteFile()));
				writer.write(doc);
				writer.close();
				undoManager.discardAllEdits();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void saveAs() {
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fc.showSaveDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			undoManager.discardAllEdits();
			File fileToSave = fc.getSelectedFile();	
			BufferedWriter writer = null;
		
			frame.setTitle(fileToSave.toString());
		
			try {
				String doc = textArea.getText();
				if (!fileToSave.exists()) {
					fileToSave.createNewFile();
				}
				writer = new BufferedWriter(new FileWriter(fileToSave.getAbsoluteFile()));
				writer.write(doc);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			//do nothing
		}
	}
	private void printFile() {
        String printData = textArea.getText();
		
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(new PrintFile(printData));
		job.setJobName(frame.getTitle());
		
		boolean doPrint = job.printDialog();
		if (doPrint) { 
			try {
				job.print();
			}
			catch (PrinterException e) {
				e.printStackTrace();
			}
		}  
	}
	private void closeProgram() {
		System.exit(0);
	}
	
	//methods for the editMenu: cut copy and paste use "default editor kit" that comes with the OS so they're not present here
	private void redoAction() {
		try {
			if (undoManager.canRedo()) {
				undoManager.redo();
			}
		} catch (CannotUndoException e) {
			e.printStackTrace();
		}
	}
	private void undoAction() {
		try {
			if (undoManager.canUndo()) {
				undoManager.undo();
			}
		} catch (CannotUndoException e) {
			e.printStackTrace();
		}
	}
	
	//methods for the helpMenu
	private void showAbout() {
		JOptionPane.showMessageDialog(frame, "CodePad, Version 0.9\nBy: Ty-Lucas Kelley\n\nCodePad is a simple text editor with\nthe powerful features that programmers expect.\nFor more open-source projects, visit\nwww.tylucaskelley.com");
	}
	private void showDate() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm MM/dd/yyyy");
		Date date = new Date();
		JOptionPane.showMessageDialog(frame, dateFormat.format(date));
	}
	private void updateLink() {
		if (Desktop.isDesktopSupported()) {
			try {
				URI update = new URI("https://github.com/tylucaskelley/CodePad");
				Desktop.getDesktop().browse(update);
			}
			catch (URISyntaxException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			//do nothing
		}	
	}
	
	//make tab do four spaces instead of eight
	private void fourSpaces(JTextArea ta) {
		ta.append("    ");
	}
}