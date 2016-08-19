import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andrewfrolkin on 2016-06-06.
 */
@SuppressWarnings("serial")
public class LinePalleteView extends JPanel implements Observer {
    // the model that this view is showing
    private Model.LinePalleteModel model;

    private JToggleButton line1;
    private JToggleButton line2;
    private JToggleButton line3;

    private JComboBox<String> lineList;

    private ButtonGroup colsGroup;
    private boolean small = false;

    private JFrame frame;

    private Model.SideBarLineWidth selectedLineWidth;

    LinePalleteView(Model.LinePalleteModel model_, JFrame frame) {
        // set the model
        model = model_;
        this.frame = frame;

        ImageIcon width1 = new ImageIcon("width1.png");
        ImageIcon width2 = new ImageIcon("width2.png");
        ImageIcon width3 = new ImageIcon("width3.png");
        line1 = new JToggleButton(width1);
        line2 = new JToggleButton(width2);
        line3 = new JToggleButton(width3);

        line1.setMaximumSize(new Dimension(195, 25));
        line1.setMinimumSize(new Dimension(195, 25));
        line1.setPreferredSize(new Dimension(195, 25));
        line1.setFocusPainted(false);

        line2.setMaximumSize(new Dimension(195, 25));
        line2.setMinimumSize(new Dimension(195, 25));
        line2.setPreferredSize(new Dimension(195, 25));
        line2.setFocusPainted(false);

        line3.setMaximumSize(new Dimension(195, 25));
        line3.setMinimumSize(new Dimension(195, 25));
        line3.setPreferredSize(new Dimension(195, 25));
        line3.setFocusPainted(false);

        line1.setAlignmentX(Component.LEFT_ALIGNMENT);
        line2.setAlignmentX(Component.LEFT_ALIGNMENT);
        line3.setAlignmentX(Component.LEFT_ALIGNMENT);

        line1.addActionListener(e -> {
            model.selectWidth(Model.SideBarLineWidth.THIN);
        });

        line2.addActionListener(e -> {
            model.selectWidth(Model.SideBarLineWidth.MEDIUM);
        });

        line3.addActionListener(e -> {
            model.selectWidth(Model.SideBarLineWidth.THICK);
        });


        colsGroup = new ButtonGroup();
        colsGroup.add(line1);
        colsGroup.add(line2);
        colsGroup.add(line3);

        // default selected tool is thin
        model.selectWidth(Model.SideBarLineWidth.THIN);
        line1.setSelected(true);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        String[] lineTypes = new String[] {"Thin", "Medium", "Thick"};
        lineList = new JComboBox<>(lineTypes);
        lineList.setMaximumSize(new Dimension(195, 25));
        lineList.setMinimumSize(new Dimension(195, 25));
        lineList.setPreferredSize(new Dimension(195, 25));
        lineList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lineList.getSelectedItem() == "Thin") {
                    model.selectWidth(Model.SideBarLineWidth.THIN);
                } else if (lineList.getSelectedItem() == "Medium") {
                    model.selectWidth(Model.SideBarLineWidth.MEDIUM);
                } else if (lineList.getSelectedItem() == "Thick") {
                    model.selectWidth(Model.SideBarLineWidth.THICK);
                }
            }
        });
        lineList.setSelectedIndex(0);

        this.add(line1);
        this.add(line2);
        this.add(line3);
        //this.add(lineList);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if (small == false && frame.getSize().getHeight() < 480) {
                    small = true;
                    add(lineList);
                    remove(line1);
                    remove(line2);
                    remove(line3);
                } else if (small && frame.getSize().getHeight() > 480) {
                    small = false;
                    remove(lineList);
                    add(line1);
                    add(line2);
                    add(line3);
                }
            }
        });

    }

    // Observer interface
    @Override
    public void update(Observable arg0, Object arg1) {

        System.out.println("LinePalletteView: update");
        selectedLineWidth = model.getSelectedWidth();

        if (selectedLineWidth == Model.SideBarLineWidth.THIN) {
            line1.setSelected(true);
            lineList.setSelectedIndex(0);
        } else if (selectedLineWidth == Model.SideBarLineWidth.MEDIUM) {
            line2.setSelected(true);
            lineList.setSelectedIndex(1);
        } else if (selectedLineWidth == Model.SideBarLineWidth.THICK) {
            line3.setSelected(true);
            lineList.setSelectedIndex(2);
        }
    }

    public Model.SideBarLineWidth getSelectedLineWidth() {
        return selectedLineWidth;
    }
}