import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andrewfrolkin on 2016-06-06.
 */
public class MenuBar implements Observer {

    private final JMenuItem menuItem4;
    private final JMenuItem menuItem5;
    private final JMenuItem menuItem3;
    private final JMenuItem menuItem2;
    private final JMenuItem menuItem1;
    JMenuBar menuBar;
    JMenu menu, submenu;
    JMenuItem menuItem;
    JRadioButtonMenuItem rbMenuItem;
    JCheckBoxMenuItem cbMenuItem;
    Model.MenuBarModel model;
    JFrame frame;

    // the view's main user interface
    private JButton button;

    MenuBar(Model _model, JFrame frame) {
        this.model = _model.menuBarModel;
        //Create the menu bar.
        menuBar = new JMenuBar();
        this.frame = frame;

        //Build the first menu.
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu);

        //a group of JMenuItems
        // new
        menuItem1 = new JMenuItem("New",
                KeyEvent.VK_T);
        menu.add(menuItem1);
        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // set the listtoload in the model to empty
                model.setLoadedShapes(new ArrayList<ShapeModel>());
            }
        });

        // load
        menuItem2 = new JMenuItem("Load",
                KeyEvent.VK_Y);
        menu.add(menuItem2);
        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // open a load file dialog
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
                        "ser files (*.ser)", "ser");
                fileChooser.setFileFilter(xmlfilter);
                if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    // load from file
                    // serialize the file and send the loaded list of shapes to the model

                    try(
                            InputStream file2 = new FileInputStream(file);
                            InputStream buffer = new BufferedInputStream(file2);
                            ObjectInput input = new ObjectInputStream (buffer);
                    ){
                        //deserialize the List
                        @SuppressWarnings("unchecked")
                        ArrayList<ShapeModel> shapes = (ArrayList<ShapeModel>)input.readObject();
                        System.out.println(shapes.size());
                        model.setLoadedShapes(shapes);
                    }
                    catch(ClassNotFoundException ex){
                        ex.printStackTrace();
                    }
                    catch(IOException ex){
                        ex.printStackTrace();
                    }

                }

            }
        });

        // save
        menuItem3 = new JMenuItem("Save",
                KeyEvent.VK_U);
        menu.add(menuItem3);
        menuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get the list ofshapestosave from the model

                // create the list into some file
                try {

                    // create a new file with an ObjectOutputStream
                    // open a save file dialog
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setSelectedFile(new File("project.ser"));
                    FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
                            "ser files (*.ser)", "ser");
                    fileChooser.setFileFilter(xmlfilter);
                    if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        // save to file

                        FileOutputStream out = new FileOutputStream(file);
                        ObjectOutputStream oout = new ObjectOutputStream(out);

                        // write something in the file
                        oout.writeObject(model.getShapesToSave());
                        oout.flush();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });

        //Build second menu in the menu bar.
        menu = new JMenu("View");
        menu.setMnemonic(KeyEvent.VK_I);
        menuBar.add(menu);

        // full size
        menuItem4 = new JMenuItem("Full Size",
                KeyEvent.VK_O);
        menu.add(menuItem4);
        menuItem4.setEnabled(false);
        menuItem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setFullSize(true);
            }
        });

        // fit to window
        menuItem5 = new JMenuItem("Fit to Window",
                KeyEvent.VK_P);
        menu.add(menuItem5);
        menuItem5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setFitToWindow(true);
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        boolean fitToWindow = model.getFitToWindow();

        System.out.println("MenuBar update");
        if (fitToWindow) {
            menuItem4.setEnabled(true);
            menuItem5.setEnabled(false);
        } else {
            menuItem4.setEnabled(false);
            menuItem5.setEnabled(true);
        }
    }
}
