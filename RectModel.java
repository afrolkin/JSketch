import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by andrewfrolkin on 2016-06-07.
 */
public class RectModel extends ShapeModel implements Serializable {
    private static final long serialVersionUID = -1551920972654618575L;
    private int height;
    private int width;
    private Color fillColor;

    public RectModel(int originX, int originY, int width, int height, LineType lineType, Color lineColor, Color fillColor) {
        this.originX = originX;
        this.originY = originY;
        this.width = width;
        this.height = height;
        this.lineType = lineType;
        this.lineColor = lineColor;
        this.fillColor = fillColor;

        this.startX = originX;
        this.startY = originY;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setFillColor(Color c) {
        fillColor = c;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Color getFillColor() {
        return fillColor;
    }
}