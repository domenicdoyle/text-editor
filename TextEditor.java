import javax.swing.*;
import javax.swing.undo.UndoManager;

/* keep track of the current file and whether
a file has been changed/modified since last save */
private static java.io.File currentFile = null;
private static boolean isModified = false;

void main() {
    SwingUtilities.invokeLater(() -> {
        //make a frame, set it to 800x600, add exit behavior
        JFrame frame = new JFrame("My Text Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        //add text area to type in and scrollbar bounded to frame
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);

        //menu bar at the top
        JMenuBar menuBar = new JMenuBar();

        //FILE MENU --------------------------------------
        JMenu fileMenu = new JMenu("FILE");
        //items to go in FILE tab
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        //add all items into the file menu
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);

        //EDIT MENU --------------------------------------
        JMenu editMenu = new JMenu("EDIT");
        //items to go in EDIT tab
        JMenuItem cutItem = new JMenuItem("Cut");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem pasteItem = new JMenuItem("Paste");
        JMenuItem undoItem = new JMenuItem("Undo");
        JMenuItem redoItem = new JMenuItem("Redo");
        //add all items into edit tab
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.add(undoItem);
        editMenu.add(redoItem);

        //Add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        //Attach menu bar to the frame
        frame.setJMenuBar(menuBar);

        //CLEAR TEXT AREA
        newItem.addActionListener(e -> {
            textArea.setText("");
        });

        //LOAD TXT FILES IN
        openItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(frame);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    java.io.File file = fileChooser.getSelectedFile();
                    java.util.Scanner scanner = new java.util.Scanner(file);
                    StringBuilder sb = new StringBuilder();

                    while (scanner.hasNextLine()){
                        sb.append(scanner.nextLine()).append("\n");
                    }
                    scanner.close();
                    textArea.setText(sb.toString());
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        //SAVE TEXT FILES
        saveItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(frame);

            if(option == JFileChooser.APPROVE_OPTION) {
                try {
                    java.io.File file = fileChooser.getSelectedFile();

                    /* make sure by default we save their files as
                     .txt if they don't specify a type */
                    String path = file.getAbsolutePath();
                    if (!path.toLowerCase().endsWith(".txt")) {
                        file = new java.io.File(path + ".txt");
                    }

                    java.io.FileWriter writer = new java.io.FileWriter(file);
                    writer.write(textArea.getText());
                    writer.close();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        //cut,copy,paste
        cutItem.addActionListener(e -> textArea.cut());
        copyItem.addActionListener(e -> textArea.copy());
        pasteItem.addActionListener(e -> textArea.paste());

        //Undo and Redo
        UndoManager undoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(undoManager);

        undoItem.addActionListener(e -> {
            if(undoManager.canUndo()) {
                undoManager.undo();
            }
        });
        redoItem.addActionListener(e -> {
            if(undoManager.canRedo()) {
                undoManager.redo();
            }
        });











        frame.setVisible(true);
    });
}
