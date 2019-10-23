package sample.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Rectangle extends Shape {

    double width;
    double height;

    public Rectangle(double xpos, double ypos, double width, double height, Paint paint) {
        super(xpos, ypos, paint);
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public void draw(GraphicsContext gc, boolean stroke) {
        double hWidth = getWidth() / 2.0;
        double hHeight = getHeight() / 2.0;

        if (stroke)
            gc.strokeRect(getXpos() - hWidth, getYpos() - hHeight, getWidth(), getHeight());
        else {
            gc.setFill(getPaint());
            gc.fillRect(getXpos() - hWidth, getYpos() - hHeight, getWidth(), getHeight());
        }
    }

    @Override
    public boolean intersects(double x, double y) {
        double hWidth = getWidth() / 2.0;
        double hHeight = getHeight() / 2.0;
        if (x > getXpos() - hWidth && x < getXpos() - hWidth + width &&
                y > getYpos() - hHeight && y < getYpos() - hHeight + height)
            return true;
        return false;
    }

    @Override
    public String toSvg() {
        double hWidth = getWidth() / 2.0;
        double hHeight = getHeight() / 2.0;
        return "<rect x=\"" + (getXpos() - hWidth) + "\" y=\"" + (getYpos() - hHeight) + "\" width=\"" +
                getWidth() + "\" height=\"" + getHeight() + "\"/>";
    }
}
