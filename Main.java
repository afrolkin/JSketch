// HelloMVC: a simple MVC example
// the model is just a counter 
// inspired by code by Joseph Mack, http://www.austintek.com/mvc/

/**
 *  Two views with integrated controllers.  Uses java.util.Observ{er, able} instead
 *  of custom IView.
 */

import javax.swing.*;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

// TODO: have one model for canvas, and one model for sidebar view, maybe one for menu (NO MORE THAN THAT)
public class Main implements Observer{

	public static void main(String[] args) {
		JFrame frame = new JFrame("JSketch");

		Model model = new Model();

		// create CanvasView, tell it about model (and controller)
		CanvasView canvasView = new CanvasView(model);

		JScrollPane jScrollPane = new JScrollPane(canvasView);

		canvasView.setContainer(jScrollPane);

		// tell all relevant models about CanvasView.
		model.canvasModel.addObserver(canvasView);
		model.colorPalleteModel.addObserver(canvasView);
		model.linePalleteModel.addObserver(canvasView);
		model.toolsModel.addObserver(canvasView);
		model.menuBarModel.addObserver(canvasView);

		SideBarContainerView sideBarContainer = new SideBarContainerView(model, frame);

		// let all the views know that they're connected to the canvasModel
		//canvasModel.notifyObservers();

		//create a menubar
		MenuBar menuBar = new MenuBar(model, frame);
		model.menuBarModel.addObserver(menuBar);

		// create the window
		JPanel p = new JPanel(new BorderLayout());
		frame.getContentPane().add(p);

		p.add(sideBarContainer,BorderLayout.WEST);

		p.add(menuBar.menuBar, BorderLayout.NORTH);

		p.add(jScrollPane, BorderLayout.CENTER);

		frame.setPreferredSize(new Dimension(1300,900));
		frame.setMinimumSize(new Dimension(400,300));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		frame.setFocusable(true);
		frame.requestFocus();
	}

	@Override
	public void update(Observable o, Object arg) {

	}
}
