import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Observer;
import java.util.Observable;
/**
 * Created by andrewfrolkin on 2016-06-06.
 */
@SuppressWarnings("serial")
public class ColorPaletteView extends JPanel implements Observer {
    // the model that this view is showing
    private Model.ColorPalleteModel model;

    private JFrame frame;

    private Model.ColorPalleteModel.SideBarSelectedColor selectedSideBarColor;

    private JToggleButton col1;
    private JToggleButton col2;
    private JToggleButton col3;
    private JToggleButton col4;
    private JToggleButton col5;
    private JToggleButton col6;

    private JComboBox<String> colList;

    private ButtonGroup colsGroup;

    private boolean small = false;

    ColorPaletteView(Model.ColorPalleteModel model_, JFrame frame) {
        // set the model
        model = model_;
        this.frame = frame;

        col1 = new JToggleButton();
        col2 = new JToggleButton();
        col3 = new JToggleButton();
        col4 = new JToggleButton();
        col5 = new JToggleButton();
        col6 = new JToggleButton();

        col1.setBackground(new Color(46,46,46));
        col1.setOpaque(true);
        col1.setBorderPainted(false);
        col2.setBackground(Color.red);
        col2.setOpaque(true);
        col2.setBorderPainted(false);
        col3.setBackground(Color.green);
        col3.setOpaque(true);
        col3.setBorderPainted(false);
        col4.setBackground(Color.blue);
        col4.setOpaque(true);
        col4.setBorderPainted(false);
        col5.setBackground(Color.pink);
        col5.setOpaque(true);
        col5.setBorderPainted(false);
        col6.setBackground(Color.orange);
        col6.setOpaque(true);
        col6.setBorderPainted(false);

        col1.setMaximumSize(new Dimension(50, 50));
        col1.setMinimumSize(new Dimension(50, 50));
        col1.setPreferredSize(new Dimension(50, 50));

        col1.addActionListener(e -> {
            model.selectSideBarColor(Model.SideBarColor.BLACK);
        });

        col2.addActionListener(e -> {
            model.selectSideBarColor(Model.SideBarColor.RED);
        });

        col3.addActionListener(e -> {
            model.selectSideBarColor(Model.SideBarColor.GREEN);
        });

        col4.addActionListener(e -> {
            model.selectSideBarColor(Model.SideBarColor.BLUE);
        });

        col5.addActionListener(e -> {
            model.selectSideBarColor(Model.SideBarColor.PINK);
        });

        col6.addActionListener(e -> {
            model.selectSideBarColor(Model.SideBarColor.ORANGE);
        });

        colsGroup = new ButtonGroup();
        colsGroup.add(col1);
        colsGroup.add(col2);
        colsGroup.add(col3);
        colsGroup.add(col4);
        colsGroup.add(col5);
        colsGroup.add(col6);

        // default selected tool is red
        model.selectSideBarColor(Model.SideBarColor.RED);
        col2.setSelected(true);

        this.setLayout(new GridLayout(3,2));


        String[] lineTypes = new String[] {"Black", "Red", "Green", "Blue", "Pink", "Orange"};
        colList = new JComboBox<>(lineTypes);
        colList.setMaximumSize(new Dimension(195, 25));
        colList.setMinimumSize(new Dimension(195, 25));
        colList.setPreferredSize(new Dimension(195, 25));
        colList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (colList.getSelectedItem() == "Black") {
                    model.selectSideBarColor(Model.SideBarColor.BLACK);
                } else if (colList.getSelectedItem() == "Red") {
                    model.selectSideBarColor(Model.SideBarColor.RED);
                } else if (colList.getSelectedItem() == "Green") {
                    model.selectSideBarColor(Model.SideBarColor.GREEN);
                } else if (colList.getSelectedItem() == "Blue") {
                    model.selectSideBarColor(Model.SideBarColor.BLUE);
                } else if (colList.getSelectedItem() == "Orange") {
                    model.selectSideBarColor(Model.SideBarColor.ORANGE);
                } else if (colList.getSelectedItem() == "Pink") {
                    model.selectSideBarColor(Model.SideBarColor.PINK);
                }
            }
        });
        colList.setSelectedIndex(1);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if (small == false && frame.getSize().getHeight() < 425) {
                    setLayout(new GridLayout(1,1));
                    small = true;
                    add(colList);
                    remove(col1);
                    remove(col2);
                    remove(col3);
                    remove(col4);
                    remove(col5);
                    remove(col6);
                } else if (small && frame.getSize().getHeight() > 425) {
                    setLayout(new GridLayout(3,2));
                    small = false;
                    remove(colList);
                    add(col1);
                    add(col2);
                    add(col3);
                    add(col4);
                    add(col5);
                    add(col6);
                }
            }
        });

        this.add(col1);
        this.add(col2);
        this.add(col3);
        this.add(col4);
        this.add(col5);
        this.add(col6);
    }

    // Observer interface
    @Override
    public void update(Observable arg0, Object arg1) {
        System.out.println("SideBarColorView: update");
        selectedSideBarColor = model.getSideBarSelectedColor();

        if (selectedSideBarColor.sbColor == Model.SideBarColor.BLACK) {
            col1.setSelected(true);
            colList.setSelectedIndex(0);
        } else if (selectedSideBarColor.sbColor == Model.SideBarColor.RED) {
            col2.setSelected(true);
            colList.setSelectedIndex(1);
        } else if (selectedSideBarColor.sbColor == Model.SideBarColor.GREEN) {
            col3.setSelected(true);
            colList.setSelectedIndex(2);
        } else if (selectedSideBarColor.sbColor == Model.SideBarColor.BLUE) {
            col4.setSelected(true);
            colList.setSelectedIndex(3);
        } else if (selectedSideBarColor.sbColor == Model.SideBarColor.PINK) {
            col5.setSelected(true);
            colList.setSelectedIndex(4);
        } else if (selectedSideBarColor.sbColor == Model.SideBarColor.ORANGE) {
            col6.setSelected(true);
            colList.setSelectedIndex(5);
        } else if (selectedSideBarColor.sbColor == Model.SideBarColor.OTHER){ // none
            col1.setSelected(false);
            col2.setSelected(false);
            col3.setSelected(false);
            col4.setSelected(false);
            col5.setSelected(false);
            col6.setSelected(false);
        }
    }

    public Model.ColorPalleteModel.SideBarSelectedColor getSelectedSideBarColor() {
        return selectedSideBarColor;
    }
}
