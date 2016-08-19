import java.awt.*;
import java.io.Serializable;

/**
 * Created by andrewfrolkin on 2016-06-07.
 */

public abstract class ShapeModel implements Serializable {
    private static final long serialVersionUID = 5226764371302467534L;
    protected int originX;
    protected int originY;
    protected int startX;
    protected int startY;
    protected int z;
    protected LineType lineType;
    protected Color lineColor;

    public enum LineType {
        MEDIUM, THIN, THICK
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getZ() {
        return z;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setOriginX(int x) {
        originX = x;
    }

    public void setOriginY(int y) {
        originY = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setLineType(LineType l) {
        lineType = l;
    }

    public LineType getLineType() {
        return lineType;
    }

    public void setLineColor(Color c) {
        lineColor = c;
    }

}
