import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by andrewfrolkin on 2016-06-07.
 */
@SuppressWarnings("serial")
public class SideBarContainerView extends JPanel {

    // the model that this view is showing

    SideBarContainerView(Model model, JFrame frame) {
        // create UI
        setLayout(new FlowLayout(FlowLayout.LEFT));

        SideBarView sideBarView = new SideBarView(model, frame);

        this.setMaximumSize(new Dimension(200, 500));
        this.setPreferredSize(new Dimension(200, 500));
        this.setMinimumSize(new Dimension(200, 500));

        this.add(sideBarView);

    }
}
