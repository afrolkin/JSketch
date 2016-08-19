import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andrewfrolkin on 2016-06-06.
 */

@SuppressWarnings("serial")
public class ToolsView extends JPanel implements Observer {
    // the model that this view is showing
    private Model.ToolsModel model;

    private JToggleButton selectionTool;
    private JToggleButton eraserTool;
    private JToggleButton lineTool;
    private JToggleButton circleTool;
    private JToggleButton rectangleTool;
    private JToggleButton fillTool;

    private ButtonGroup toolsGroup;

    private Model.SideBarTool selectedTool;

    ToolsView(Model.ToolsModel model_) {
        // set the model
        model = model_;

        ImageIcon selectIcon = new ImageIcon("select.png");
        ImageIcon eraseIcon = new ImageIcon("eraser.png");
        ImageIcon lineIcon = new ImageIcon("line.png");
        ImageIcon circIcon = new ImageIcon("circ.png");
        ImageIcon rectIcon = new ImageIcon("rect.png");
        ImageIcon fillIcon = new ImageIcon("fill.png");


        selectionTool = new JToggleButton(selectIcon);
        selectionTool.setFocusPainted(false);
        eraserTool = new JToggleButton(eraseIcon);
        eraserTool.setFocusPainted(false);
        lineTool = new JToggleButton(lineIcon);
        lineTool.setFocusPainted(false);
        circleTool = new JToggleButton(circIcon);
        circleTool.setFocusPainted(false);
        rectangleTool = new JToggleButton(rectIcon);
        rectangleTool.setFocusPainted(false);
        fillTool = new JToggleButton(fillIcon);
        fillTool.setFocusPainted(false);
        selectionTool.addActionListener(e -> {
            model.selectTool(Model.SideBarTool.SELECTION);
        });

        eraserTool.addActionListener(e -> {
            model.selectTool(Model.SideBarTool.ERASER);
        });

        circleTool.addActionListener(e -> {
            model.selectTool(Model.SideBarTool.CIRCLE);
        });

        rectangleTool.addActionListener(e -> {
            model.selectTool((Model.SideBarTool.RECT));
        });

        fillTool.addActionListener(e -> {
            model.selectTool(Model.SideBarTool.FILL);
        });

        lineTool.addActionListener(e -> {
            model.selectTool(Model.SideBarTool.LINE);
        });


        selectionTool.setMaximumSize(new Dimension(50, 50));
        selectionTool.setMinimumSize(new Dimension(50, 50));
        selectionTool.setPreferredSize(new Dimension(50, 50));

        toolsGroup = new ButtonGroup();
        toolsGroup.add(selectionTool);
        toolsGroup.add(eraserTool);
        toolsGroup.add(lineTool);
        toolsGroup.add(circleTool);
        toolsGroup.add(rectangleTool);
        toolsGroup.add(fillTool);

        // default selected tool is rectangle
        model.selectTool(Model.SideBarTool.RECT);
        rectangleTool.setSelected(true);

        this.setLayout(new GridLayout(3,2));

        this.add(selectionTool);
        this.add(eraserTool);
        this.add(lineTool);
        this.add(circleTool);
        this.add(rectangleTool);
        this.add(fillTool);
    }

    // Observer interface
    @Override
    public void update(Observable arg0, Object arg1) {
        System.out.println("ToolsView: update");
        selectedTool = model.getSelectedTool();
        if (selectedTool == Model.SideBarTool.SELECTION) {
            selectionTool.setSelected(true);
        } else if (selectedTool == Model.SideBarTool.ERASER) {
            eraserTool.setSelected(true);
        } else if (selectedTool == Model.SideBarTool.RECT) {
            rectangleTool.setSelected(true);
        } else if (selectedTool == Model.SideBarTool.CIRCLE) {
            circleTool.setSelected(true);
        } else if (selectedTool == Model.SideBarTool.LINE) {
            lineTool.setSelected(true);
        } else if (selectedTool == Model.SideBarTool.FILL) {
            fillTool.setSelected(true);
        }
    }

    public Model.SideBarTool getSelectedTool() {
        return selectedTool;
    }
}