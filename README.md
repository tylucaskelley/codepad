# Notepad

Notepad is a basic text editor written in Java. It works on multiple platforms
and supports features such as file creation and editing, cut and paste, etc.

---

### Screenshot

![Notepad](img/screenshot.png "Notepad")

### Motivation

I made this as a way to learn how to write Java programs, and also as an
introduction to GUI applications. The text editor itself is not anything
special, and shouldn't replace your current editor.

### Installation

Make sure you have Java 6.x, 7.x, or 8.x installed, and then download the 
[ZIP](https://github.com/tylucaskelley/notepad/archive/master.zip). Once you
uncompress the folder, just run `notepad.jar` to open the editor.

### Features

Here's what Notepad can do:

1. Create and edit text files
2. Show line numbers
3. Cut, copy, and paste
4. Undo and redo
5. Print your files
6. A link to the repository for updates

### Contributing

See [CONTRIBUTING.md](https://github.com/tylucaskelley/notepad/blob/master/.github/CONTRIBUTING.md)
for details.

To build the project (starting from main directory):

```bash
$ javac -d bin src/*.java
$ cd bin
$ jar cvfe Notepad.jar NotepadDriver *.class
$ mv Notepad.jar ..
```

### License

Notepad is licensed by the
[MIT License](https://github.com/tylucaskelley/notepad/blob/master/LICENSE).
