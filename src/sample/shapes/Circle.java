package sample.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Circle extends Shape {
    public Circle(double xpos, double ypos, double radius, Paint paint) {
        super(xpos, ypos, paint);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    private double radius;

    @Override
    public void draw(GraphicsContext gc, boolean stroke) {
        if (stroke)
            gc.strokeOval(getXpos() - getRadius(), getYpos() - getRadius(), getRadius() * 2.0, getRadius() * 2.0);
        else {
            gc.setFill(getPaint());
            gc.fillOval(getXpos() - getRadius(), getYpos() - getRadius(), getRadius() * 2.0, getRadius() * 2.0);
        }
    }

    @Override
    public boolean intersects(double x, double y) {
        //double
        double r = getRadius();
        double x2 = (x - getXpos());
        double y2 = (y - getYpos());
        if ((x2 * x2 + y2 * y2) < r * r)
            return true;
        return false;
    }
}
