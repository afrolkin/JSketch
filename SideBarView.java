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
public class SideBarView extends JPanel implements Observer {

    private JButton colorPicker;

    private ToolsView toolsView;
    private Model.ToolsModel toolsModel;

    private ColorPaletteView colorPaletteView;
    private Model.ColorPalleteModel colorPalleteModel;

    private LinePalleteView linePalleteView;
    private Model.LinePalleteModel linePalleteModel;

    private Model model;

    SideBarView (Model model_, JFrame frame) {
        model = model_;

        // create the color picker button
        colorPicker = new ColorPickerButton("Color Picker", model);
        colorPicker.setMaximumSize(new Dimension(193, 50));
        colorPicker.setPreferredSize(new Dimension(193, 50));

        // a GridBagLayout with default constraints centres
        // the widget in the window
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        toolsModel = model.toolsModel;
        toolsView = new ToolsView(toolsModel);
        toolsModel.addObserver(toolsView);

        colorPalleteModel = model.colorPalleteModel;
        colorPaletteView = new ColorPaletteView(colorPalleteModel, frame);
        colorPalleteModel.addObserver(colorPaletteView);

        linePalleteModel = model.linePalleteModel;
        linePalleteView = new LinePalleteView(linePalleteModel, frame);
        linePalleteModel.addObserver(linePalleteView);

        toolsView.setAlignmentX(Component.LEFT_ALIGNMENT);
        colorPaletteView.setAlignmentX(Component.LEFT_ALIGNMENT);
        colorPicker.setAlignmentX(Component.LEFT_ALIGNMENT);
        linePalleteView.setAlignmentX(Component.LEFT_ALIGNMENT);

        // create the window
        this.add(toolsView);
        this.add(colorPaletteView);
        this.add(colorPicker);
        this.add(linePalleteView);
    }

    public Model.ColorPalleteModel.SideBarSelectedColor getSelectedColor() {
        return colorPaletteView.getSelectedSideBarColor();
    }

    public Model.SideBarLineWidth getSelectedLineWidth() {
        return linePalleteView.getSelectedLineWidth();
    }

    public Model.SideBarTool getSelectedTool() {
        return toolsView.getSelectedTool();
    }

    // Observer interface
    // dont think i need this
    @Override
    public void update(Observable arg0, Object arg1) {
        System.out.println("SideBarView: update");
    }
}

