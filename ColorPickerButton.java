import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andrewfrolkin on 2016-06-12.
 */
@SuppressWarnings("serial")
public class ColorPickerButton extends JButton implements Observer{

    private Model.ColorPalleteModel colorPalleteModel;

    private Color current;

    public ColorPickerButton(String title, Model _model) {
        super(title);

        colorPalleteModel = _model.colorPalleteModel;

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Color newColor = JColorChooser.showDialog(null, "Choose a color", current);
                //setSelectedColor(newColor);
                current = newColor;
                colorPalleteModel.selectCustomSideBarColor(newColor);
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
