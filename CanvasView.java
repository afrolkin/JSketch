// HelloMVC: a simple MVC example
// the canvasModel is just a counter
// inspired by code by Joseph Mack, http://www.austintek.com/mvc/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("serial")
class CanvasView extends JPanel implements Observer, MouseListener, MouseMotionListener {

	// the view's main user interface
	private boolean drawingShape = false;

	private int mouseX;
	private int mouseY;
	private int initialX;
	private int initialY;

	private int dragX;
	private int dragY;
	private boolean draggingShape = false;
	
	// the canvasModel that this view is showing
	private Model model;

	private Model.SideBarTool selectedTool;
	private Model.ColorPalleteModel.SideBarSelectedColor selectedColor;
	private Model.SideBarLineWidth selectedLineWidth;

	private ArrayList<ShapeModel> shapes;
	private int selectedShapeIndex;
	private int currentShapeIndex;

	private boolean fullSize;
	private boolean fitToWindow;
	private JScrollPane container;

	CanvasView(Model model_) {
		// create the view UI
		this.setSize(500,500);
		this.setPreferredSize(new Dimension(1000,800));
		this.setMaximumSize(new Dimension(1000,800));
		// the widget in the window
		this.setLayout(new BorderLayout());
		//this.add(button, new GridBagConstraints());

		this.setBackground(Color.LIGHT_GRAY);

		// set the canvasModel
		model = model_;

		selectedTool = model.toolsModel.getSelectedTool();
		selectedColor = model.colorPalleteModel.getSideBarSelectedColor();
		selectedLineWidth = model.linePalleteModel.getSelectedWidth();

		shapes = new ArrayList<>();
		selectedShapeIndex = -1;

		fullSize = model.menuBarModel.getFullSize();
		fitToWindow = model.menuBarModel.getFitToWindow();

		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), ESCAPE);
		this.getActionMap().put(
				ESCAPE, escape);

		this.setFocusable(true);
		this.requestFocus();
		addMouseListener(this);
		addMouseMotionListener(this);

		repaint();
	}

	private static final String ESCAPE = "escape";
	private Action escape = new AbstractAction(ESCAPE) {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("pressed esc");
			selectedShapeIndex = -1;
			repaint();
		}
	};

	// Observer interface
	// this gets called from the canvasmodel when the model is updated
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof Model.CanvasModel) {
			System.out.println("CanvasView: update Canvas Model");
			ShapeModel shapeToDraw = model.canvasModel.getMostRecentShape();
			drawShape(shapeToDraw);
		} else if (arg0 instanceof Model.ToolsModel) {
			System.out.println("CanvasView: update Tools Model");
			selectedTool = model.toolsModel.getSelectedTool();
			if (selectedShapeIndex != -1) {
				selectedShapeIndex = -1;
				repaint();
			}
		} else if (arg0 instanceof Model.ColorPalleteModel) {
			System.out.println("CanvasView: update Color Pallete Model");
			selectedColor = model.colorPalleteModel.getSideBarSelectedColor();

			ShapeModel selectedShape = null;
			if (selectedShapeIndex != -1) {
				selectedShape = shapes.get(selectedShapeIndex);
			}

			if (selectedShape != null) {
				// change line color of shape
				selectedShape.setLineColor(selectedColor.jColor);
				repaint();
			}
		} else if (arg0 instanceof Model.LinePalleteModel) {
			System.out.println("CanvasView: update Line Pallete Model");

			ShapeModel selectedShape = null;
			if (selectedShapeIndex != -1) {
				selectedShape = shapes.get(selectedShapeIndex);
			}

			selectedLineWidth = model.linePalleteModel.getSelectedWidth();
			if (selectedShape != null) {
				// change line thickness of shape
				ShapeModel.LineType lineType = selectedLineWidth == Model.SideBarLineWidth.MEDIUM ? ShapeModel.LineType.MEDIUM : selectedLineWidth == Model.SideBarLineWidth.THIN ? ShapeModel.LineType.THIN : ShapeModel.LineType.THICK;
				selectedShape.setLineType(lineType);
				repaint();
			}
		} else if (arg0 instanceof Model.MenuBarModel) {
			fitToWindow = model.menuBarModel.getFitToWindow();
			fullSize = model.menuBarModel.getFullSize();

			if (fullSize) {
				container.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
				container.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				container.setEnabled(true);
			} else {
				container.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				container.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
				container.setEnabled(false);
			}

			if (model.menuBarModel.isLoaded()) {
				shapes = model.menuBarModel.getLoadedShapes();
			}
			repaint();
		} else {
			System.out.println("CanvasView: update some other model");
		}
	}

	public void drawShape(ShapeModel shape) {
		System.out.println("Drew some shape");
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if (fitToWindow) {
			x = (int) getScaledMouseCoordinate(x);
			y = (int) getScaledMouseCoordinate(y);
		}

		if (selectedTool == Model.SideBarTool.FILL || selectedTool == Model.SideBarTool.ERASER || selectedTool == Model.SideBarTool.SELECTION) {
			// iterate over the list of shapes (from top shapes to bottom + fill the first encountered shape)
			for (int i = shapes.size() - 1 ; i >= 0 ; i--) {
				if (shapes.get(i) instanceof CircleModel) {
					CircleModel c = (CircleModel) shapes.get(i);
					int radius = c.getHeight()/2;
					int centreX = c.getOriginX() + radius;
					int centreY = c.getOriginY() + radius;
					if ((Math.sqrt((x - centreX)*(x - centreX) + (y-centreY) * (y - centreY)) < radius)) {
						if (selectedTool == Model.SideBarTool.FILL) {
							c.setFillColor(selectedColor.jColor);
						} else if (selectedTool == Model.SideBarTool.ERASER) {
							// remove
							System.out.println("CanvasView: remove circ");
							shapes.remove(i);
						}
						repaint();
						break;
					}
				} else if (shapes.get(i) instanceof RectModel) {
					RectModel r = (RectModel) shapes.get(i);
					if (r.getOriginX() <= x && x <= r.getOriginX() + r.getWidth() && r.getOriginY() <= y && y <= r.getOriginY() + r.getHeight()) {
						if (selectedTool == Model.SideBarTool.FILL) {
							r.setFillColor(selectedColor.jColor);
						} else if (selectedTool == Model.SideBarTool.ERASER) {
							// remove
							System.out.println("CanvasView: remove rect");
							shapes.remove(i);
						}
						repaint();
						break;
					}
				} else if (shapes.get(i) instanceof LineModel) {
					LineModel l = (LineModel) shapes.get(i);
					int shortestDistance = distanceFromPointToLine(x, y, l.getOriginX(), l.getOriginY(), l.getEndX(), l.getEndY());
					if (shortestDistance <= 3) {
						if (selectedTool == Model.SideBarTool.ERASER) {
							// remove
							System.out.println("CanvasView: remove line");
							shapes.remove(i);
						}
						repaint();
						break;
					}
				}
			}
		}

		drawingShape = false;
		draggingShape = false;
	}

	private int distanceFromPointToLine(int x, int y, int originX, int originY, int endX, int endY) {
		double num = (Math.abs(((endY - originY) * x) - ((endX - originX) * y) + endX*originY - endY*originX));
		double demom = (Math.sqrt(Math.pow(endY - originY, 2) + Math.pow(endX - originX, 2)));
		return ((int) (num / demom));
	}

	//todo: change mouse pointer if shape is selectable

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("CanvasView: mouse pressed");
		int x = e.getX();
		int y = e.getY();

		if (fitToWindow) {
			x = (int) getScaledMouseCoordinate(x);
			y = (int) getScaledMouseCoordinate(y);
		}

		if (selectedTool == Model.SideBarTool.RECT || selectedTool == Model.SideBarTool.CIRCLE || selectedTool == Model.SideBarTool.LINE) {
			drawingShape = true;
			System.out.println("CanvasView: mouse pressed here");
			initialX = x;
			initialY = y;
			mouseX = initialX;
			mouseY = initialY;

			ShapeModel currentShape;
			ShapeModel.LineType lineType = selectedLineWidth == Model.SideBarLineWidth.MEDIUM ? ShapeModel.LineType.MEDIUM : selectedLineWidth == Model.SideBarLineWidth.THIN ? ShapeModel.LineType.THIN : ShapeModel.LineType.THICK;

			if (selectedTool == Model.SideBarTool.RECT) {
				currentShape = new RectModel(initialX, initialY, 1, 1, lineType, selectedColor.jColor, new Color(255, 255, 255, 0));
				shapes.add(currentShape);
				currentShapeIndex = shapes.indexOf(currentShape);
			} else if (selectedTool == Model.SideBarTool.CIRCLE) {
				currentShape = new CircleModel(initialX, initialY, 1, 1, lineType, selectedColor.jColor, new Color(255, 255, 255, 0));
				shapes.add(currentShape);
				currentShapeIndex = shapes.indexOf(currentShape);
			} else if (selectedTool == Model.SideBarTool.LINE) {
				currentShape = new LineModel(initialX, initialY, initialX + 1, initialY + 1, lineType, selectedColor.jColor);
				shapes.add(currentShape);
				currentShapeIndex = shapes.indexOf(currentShape);
			}
			repaint();
		} else if (selectedTool == Model.SideBarTool.SELECTION) {
			selectedShapeIndex = -1;
			for (int i = shapes.size() - 1 ; i >= 0 ; i--) {
				if (shapes.get(i) instanceof CircleModel) {
					CircleModel c = (CircleModel) shapes.get(i);
					int radius = c.getHeight()/2;
					int centreX = c.getOriginX() + radius;
					int centreY = c.getOriginY() + radius;
					if ((Math.sqrt((x - centreX)*(x - centreX) + (y-centreY) * (y - centreY)) < radius)) {
						if (selectedTool == Model.SideBarTool.SELECTION) {
							selectedShapeIndex = i;
							dragX = x;
							dragY = y;
							draggingShape = true;
							Model.SideBarLineWidth lineType = c.getLineType() == ShapeModel.LineType.MEDIUM ? Model.SideBarLineWidth.MEDIUM : c.getLineType() == ShapeModel.LineType.THIN ? Model.SideBarLineWidth.THIN : Model.SideBarLineWidth.THICK;

							Model.SideBarColor color;
							Color cColor = c.getLineColor();
							if (cColor == Color.black) {
								color = Model.SideBarColor.BLACK;
							} else if (cColor == Color.RED) {
								color = Model.SideBarColor.RED;
							} else if (cColor == Color.GREEN) {
								color = Model.SideBarColor.GREEN;
							} else if (cColor == Color.BLUE) {
								color = Model.SideBarColor.BLUE;
							} else if (cColor == Color.ORANGE) {
								color = Model.SideBarColor.ORANGE;
							} else if (cColor == Color.PINK) {
								color = Model.SideBarColor.PINK;
							} else {
								color = Model.SideBarColor.OTHER;
								model.colorPalleteModel.selectCustomSideBarColor(cColor);
							}
							model.colorPalleteModel.selectSideBarColor(color);
							model.linePalleteModel.selectWidth(lineType);
						}
						repaint();
						break;
					}
				} else if (shapes.get(i) instanceof RectModel) {
					RectModel r = (RectModel) shapes.get(i);
					if (r.getOriginX() <= x && x <= r.getOriginX() + r.getWidth() && r.getOriginY() <= y && y <= r.getOriginY() + r.getHeight()) {
						if (selectedTool == Model.SideBarTool.SELECTION) {
							selectedShapeIndex = i;
							dragX = x;
							dragY = y;
							draggingShape = true;
							Model.SideBarLineWidth lineType = r.getLineType() == ShapeModel.LineType.MEDIUM ? Model.SideBarLineWidth.MEDIUM : r.getLineType() == ShapeModel.LineType.THIN ? Model.SideBarLineWidth.THIN : Model.SideBarLineWidth.THICK;

							Model.SideBarColor color;
							Color cColor = r.getLineColor();
							if (cColor == Color.black) {
								color = Model.SideBarColor.BLACK;
							} else if (cColor == Color.RED) {
								color = Model.SideBarColor.RED;
							} else if (cColor == Color.GREEN) {
								color = Model.SideBarColor.GREEN;
							} else if (cColor == Color.BLUE) {
								color = Model.SideBarColor.BLUE;
							} else if (cColor == Color.ORANGE) {
								color = Model.SideBarColor.ORANGE;
							} else if (cColor == Color.PINK) {
								color = Model.SideBarColor.PINK;
							} else {
								color = Model.SideBarColor.OTHER;
								model.colorPalleteModel.selectCustomSideBarColor(cColor);
							}
							model.colorPalleteModel.selectSideBarColor(color);
							model.linePalleteModel.selectWidth(lineType);
						}
						repaint();
						break;
					}
				} else if (shapes.get(i) instanceof LineModel) {
					LineModel l = (LineModel) shapes.get(i);
					int shortestDistance = distanceFromPointToLine(x, y, l.getOriginX(), l.getOriginY(), l.getEndX(), l.getEndY());
					if (shortestDistance <= 3) {
						if (selectedTool == Model.SideBarTool.SELECTION) {
							selectedShapeIndex = i;
							dragX = x;
							dragY = y;
							draggingShape = true;
							Model.SideBarLineWidth lineType = l.getLineType() == ShapeModel.LineType.MEDIUM ? Model.SideBarLineWidth.MEDIUM : l.getLineType() == ShapeModel.LineType.THIN ? Model.SideBarLineWidth.THIN : Model.SideBarLineWidth.THICK;

							Model.SideBarColor color;
							Color cColor = l.getLineColor();
							if (cColor == Color.black) {
								color = Model.SideBarColor.BLACK;
							} else if (cColor == Color.RED) {
								color = Model.SideBarColor.RED;
							} else if (cColor == Color.GREEN) {
								color = Model.SideBarColor.GREEN;
							} else if (cColor == Color.BLUE) {
								color = Model.SideBarColor.BLUE;
							} else if (cColor == Color.ORANGE) {
								color = Model.SideBarColor.ORANGE;
							} else if (cColor == Color.PINK) {
								color = Model.SideBarColor.PINK;
							} else {
								color = Model.SideBarColor.OTHER;
								model.colorPalleteModel.selectCustomSideBarColor(cColor);
							}
							model.colorPalleteModel.selectSideBarColor(color);
							model.linePalleteModel.selectWidth(lineType);
						}
						repaint();
						break;
					}
				} else {
					selectedShapeIndex = -1;
				}
			}
		}
	}

	private int distanceBetween(int x1, int y1, int x2, int y2) {
		int dx = x1 - x2;
		int dy = y1 - y2;
		int distance = (int) Math.sqrt(dx * dx + dy * dy);
		return distance;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (drawingShape) {
			int x  = e.getX();
			int y = e.getY();

			if (fitToWindow) {
				x = (int) getScaledMouseCoordinate(x);
				y = (int) getScaledMouseCoordinate(y);
			}

			if (shapes.get(currentShapeIndex) instanceof CircleModel) {
				int minHeight = Math.min(Math.abs(x - shapes.get(currentShapeIndex).getStartX()), Math.abs(y - shapes.get(currentShapeIndex).getStartY()));
				if (x <= shapes.get(currentShapeIndex).getStartX()) {
					((CircleModel) shapes.get(currentShapeIndex)).setOriginX(shapes.get(currentShapeIndex).getStartX() - minHeight);
				} else {
					((CircleModel) shapes.get(currentShapeIndex)).setOriginX(shapes.get(currentShapeIndex).getStartX());
				}

				if (y <= shapes.get(currentShapeIndex).getStartY()) {
					((CircleModel) shapes.get(currentShapeIndex)).setOriginY(shapes.get(currentShapeIndex).getStartY() - minHeight);
				} else {
					((CircleModel) shapes.get(currentShapeIndex)).setOriginY(shapes.get(currentShapeIndex).getStartY());
				}
				((CircleModel) shapes.get(currentShapeIndex)).setWidth(minHeight);
				((CircleModel) shapes.get(currentShapeIndex)).setHeight(minHeight);
			} else if (shapes.get(currentShapeIndex) instanceof RectModel) {
				((RectModel) shapes.get(currentShapeIndex)).setOriginX(Math.min(x, shapes.get(currentShapeIndex).getStartX()));
				((RectModel) shapes.get(currentShapeIndex)).setOriginY(Math.min(y, shapes.get(currentShapeIndex).getStartY()));
				((RectModel) shapes.get(currentShapeIndex)).setWidth(Math.abs(x - shapes.get(currentShapeIndex).getStartX()));
				((RectModel) shapes.get(currentShapeIndex)).setHeight(Math.abs(y - shapes.get(currentShapeIndex).getStartY()));
			} else if (shapes.get(currentShapeIndex) instanceof LineModel) {
				((LineModel) shapes.get(currentShapeIndex)).setEndX(x);
				((LineModel) shapes.get(currentShapeIndex)).setEndY(y);


			}
			repaint();
		}

		if (draggingShape && selectedShapeIndex != -1) {
			//System.out.println("dragging");
			int x = e.getX();
			int y = e.getY();

			if (fitToWindow) {
				x = (int) getScaledMouseCoordinate(x);
				y = (int) getScaledMouseCoordinate(y);
			}

			int translationX = x - dragX;
			int translationY = y - dragY;

			ShapeModel shapeToMove = shapes.get(selectedShapeIndex);

			shapeToMove.originY = shapeToMove.originY + translationY;
			shapeToMove.originX = shapeToMove.originX + translationX;

			if (shapeToMove instanceof LineModel) {
				((LineModel) shapeToMove).setEndX(((LineModel) shapeToMove).getEndX() + translationX);
				((LineModel) shapeToMove).setEndY(((LineModel) shapeToMove).getEndY() + translationY);
			}

			dragX = x;
			dragY = y;
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		drawingShape = false;
		draggingShape = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	public double getScaledMouseCoordinate(int x) {
		double width =  container.getSize().getWidth();
		double height =  container.getSize().getHeight();
		double scaleWidth = width / this.getPreferredSize().getWidth();
		double scaleHeight =  height / this.getPreferredSize().getHeight();

		if (scaleHeight >= scaleWidth) {
			return  x /  scaleWidth;//(1 - scaleWidth) / scaleWidth;
		} else {
			return x / scaleHeight;//(1 - scaleHeight) / scaleHeight;
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Stroke stroke;

		if (fitToWindow) {
			double width = container.getSize().getWidth();
			double height = container.getSize().getHeight();
			double scaleWidth = width / this.getPreferredSize().getWidth();
			double scaleHeight = height / this.getPreferredSize().getHeight();

			//System.out.println(width + " " + height + " " + scaleHeight + " " + scaleWidth);

			if (scaleHeight >= scaleWidth) {
				g2.scale(scaleWidth, scaleWidth);
			} else {
				g2.scale(scaleHeight, scaleHeight);
			}

		}

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// backgrounds
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(0, 0, 5000, 5000);
		g2.setColor(Color.white);
		g2.fillRect(0, 0, 1000, 800);
		g2.setClip(new Rectangle2D.Double(0, 0, 1000, 800));

		for (ShapeModel s: shapes) {
			if (s.lineType == ShapeModel.LineType.THIN) {
				stroke = new BasicStroke(5);
			} else if (s.lineType == ShapeModel.LineType.MEDIUM) {
				stroke = new BasicStroke(10);
			} else { //if (selectedLineWidth == Model.SideBarLineWidth.THICK) {
				stroke = new BasicStroke(20);
			}
			g2.setStroke(stroke);

			g2.setColor(s.getLineColor());

			if (s instanceof CircleModel) {
				g2.drawOval(s.getOriginX(), s.getOriginY(), ((CircleModel) s).getWidth(), ((CircleModel) s).getHeight());
				g2.setColor(((CircleModel) s).getFillColor());
				g2.fillOval(s.getOriginX(), s.getOriginY(), ((CircleModel) s).getWidth(), ((CircleModel) s).getHeight());
			} else if (s instanceof RectModel) {
				g2.drawRect(s.getOriginX(), s.getOriginY(), ((RectModel) s).getWidth(), ((RectModel) s).getHeight());
				g2.setColor(((RectModel) s).getFillColor());
				g2.fillRect(s.getOriginX(), s.getOriginY(), ((RectModel) s).getWidth(), ((RectModel) s).getHeight());
			} else { // LineModel
				g2.drawLine(s.getOriginX(), s.getOriginY(), ((LineModel)s).getEndX(), ((LineModel)s).getEndY());
			}


			ShapeModel selectedShape = null;
			if (selectedShapeIndex != -1 && selectedShapeIndex == shapes.indexOf(s)) {
				selectedShape = shapes.get(selectedShapeIndex);
			}
			if (selectedShape != null) {
				Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
				g2.setStroke(dashed);
				g2.setColor(Color.LIGHT_GRAY);
				if (selectedShape instanceof LineModel) {
					g2.drawLine(selectedShape.getOriginX(), selectedShape.getOriginY(), ((LineModel) selectedShape).getEndX(), ((LineModel) selectedShape).getEndY());
				} else if (selectedShape instanceof CircleModel) {
					g2.drawOval(selectedShape.getOriginX(), selectedShape.getOriginY(), ((CircleModel) selectedShape).getWidth(), ((CircleModel) selectedShape).getHeight());
				} else if (selectedShape instanceof RectModel) {
					g2.drawRect(selectedShape.getOriginX(), selectedShape.getOriginY(), ((RectModel) selectedShape).getWidth(), ((RectModel) selectedShape).getHeight());
				}
			}


		}

		model.menuBarModel.setShapesToSave(shapes);
	}

	public void setContainer(JScrollPane container) {
		this.container = container;
	}
}
