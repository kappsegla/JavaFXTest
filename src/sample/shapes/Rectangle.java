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
        //Todo: Center rectangle around mousepoint
        if (stroke)
            gc.strokeRect(getXpos(), getYpos(), getWidth(), getHeight());
        else {
            gc.setFill(getPaint());
            gc.fillRect(getXpos(), getYpos(), getWidth(), getHeight());
        }
    }

    @Override
    public boolean intersects(double x, double y) {
        //Todo: Center rectangle around mousepoint
        if (x > getXpos() && x < getXpos() + width &&
                y > getYpos() && y < getYpos() + height)
            return true;

        return false;
    }
}
