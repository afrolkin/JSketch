import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by andrewfrolkin on 2016-06-12.
 */
public class Model {

    public Model() {
        toolsModel = new ToolsModel();
        canvasModel = new CanvasModel();
        colorPalleteModel = new ColorPalleteModel();
        linePalleteModel = new LinePalleteModel();
        menuBarModel = new MenuBarModel();
    }

    public enum SideBarLineWidth {
        THIN, MEDIUM, THICK, NONE
    }

    public enum SideBarTool {
        SELECTION, ERASER, LINE, CIRCLE, RECT, FILL, NONE
    }


    public enum SideBarColor {
        BLACK, RED, GREEN, BLUE, ORANGE, PINK, NONE, OTHER;
    }

    public ToolsModel toolsModel;
    public CanvasModel canvasModel;
    public ColorPalleteModel colorPalleteModel;
    public LinePalleteModel linePalleteModel;
    public MenuBarModel menuBarModel;

    public class MenuBarModel extends Observable {
        private boolean fitToWindow;
        private boolean fullSize;
        private ArrayList<ShapeModel> loadedShapes;
        private ArrayList<ShapeModel> shapesToSave;
        private boolean loaded;

        MenuBarModel() {
            fitToWindow = false;
            fullSize = true;
            setChanged();
        }

        public void setFitToWindow(boolean flag) {
            fitToWindow = flag;
            fullSize = !flag;
            setChanged();
            notifyObservers();
        }

        public void setFullSize(boolean flag) {
            fitToWindow = !flag;
            fullSize = flag;
            setChanged();
            notifyObservers();
        }

        public boolean getFullSize() {
            return fullSize;
        }

        public boolean getFitToWindow() {
            return fitToWindow;
        }

        public ArrayList<ShapeModel> getLoadedShapes() {
            loaded = false;
            return loadedShapes;
        }


        public void setLoadedShapes(ArrayList<ShapeModel> loadedShapes) {
            this.loadedShapes = loadedShapes;
            this.loaded = true;
            setChanged();
            notifyObservers();
        }

        public ArrayList<ShapeModel> getShapesToSave() {
            return shapesToSave;
        }

        public void setShapesToSave(ArrayList<ShapeModel> shapesToSave) {
            this.shapesToSave = shapesToSave;
        }

        public boolean isLoaded() {
            return loaded;
        }
    }

    public class ToolsModel extends Observable {

        private SideBarTool selectedTool;

        ToolsModel() {
            selectedTool = SideBarTool.NONE;
            setChanged();
        }


        public void selectTool(SideBarTool tool) {
            selectedTool = tool;
            setChanged();
            notifyObservers();
        }

        public SideBarTool getSelectedTool() {
            return selectedTool;
        }

    }


    public class LinePalleteModel extends Observable {

        private SideBarLineWidth selectedWidth;

        LinePalleteModel() {
            selectedWidth = SideBarLineWidth.NONE;
            setChanged();
        }


        public void selectWidth(SideBarLineWidth width) {
            selectedWidth = width;
            setChanged();
            notifyObservers();
        }

        public SideBarLineWidth getSelectedWidth() {
            return selectedWidth;
        }
    }

    public class ColorPalleteModel extends Observable {

        public class SideBarSelectedColor {
            public SideBarColor sbColor;
            public Color jColor;

            public SideBarSelectedColor() {
                sbColor = SideBarColor.NONE;
            }

            public SideBarSelectedColor(SideBarColor sbC) {
                sbColor = sbC;
                setJColor();
            }

            public void setColor(SideBarColor col) {
                sbColor = col;
                setJColor();
            }

            public void setColor(Color col) {
                sbColor = SideBarColor.OTHER;
                jColor = col;
            }

            private void setJColor() {
                switch (sbColor) {
                    case BLACK:
                        jColor = Color.black;
                        break;
                    case RED:
                        jColor = Color.red;
                        break;
                    case GREEN:
                        jColor = Color.green;
                        break;
                    case BLUE:
                        jColor = Color.blue;
                        break;
                    case ORANGE:
                        jColor = Color.orange;
                        break;
                    case PINK:
                        jColor = Color.pink;
                        break;
                    case NONE:
                        break;
                    case OTHER:
                        break;
                }
            }
        }

        private SideBarSelectedColor sideBarSelectedColor;

        public ColorPalleteModel() {
            sideBarSelectedColor = new SideBarSelectedColor();
            setChanged();
        }

        public void selectSideBarColor(SideBarColor sbC) {
            sideBarSelectedColor.setColor(sbC);
            setChanged();
            notifyObservers();
        }

        public void selectCustomSideBarColor(Color sbC) {
            sideBarSelectedColor.setColor(sbC);
            setChanged();
            notifyObservers();
        }

        public SideBarSelectedColor getSideBarSelectedColor() {
            return sideBarSelectedColor;
        }

    }

    public class CanvasModel extends Observable {
        // the data in the model, just a counter
        private java.util.List<ShapeModel> shapes;
        private ShapeModel mostRecentShape;

        CanvasModel() {
            shapes = new ArrayList<ShapeModel>();
            setChanged();
        }

        public void addShape(ShapeModel shape) {
            shapes.add(shape);
            mostRecentShape = shape;
            setChanged();
            notifyObservers();
        }

        public ShapeModel getMostRecentShape() {
            return mostRecentShape;
        }
    }



}
