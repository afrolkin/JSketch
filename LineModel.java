import java.awt.*;
import java.io.Serializable;

/**
 * Created by andrewfrolkin on 2016-06-07.
 */
public class LineModel extends ShapeModel implements Serializable {
    private static final long serialVersionUID = -7189536537972565094L;
    private int length;
    private int endX;
    private int endY;

    public LineModel(int originX, int originY, int endX, int endY, LineType lineType, Color lineColor) {
        this.originX = originX;
        this.originY = originY;
        this.length = length;
        this.lineType = lineType;
        this.lineColor = lineColor;

        this.startX = originX;
        this.startY = originY;
        this.endX = endX;
        this.endY = endY;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public void setEndX(int x) {
        endX = x;
    }

    public void setEndY(int y) {
        endY = y;
    }

}