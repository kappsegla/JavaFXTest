package sample.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Triangle extends Shape {

    double[] xPoints;
    double[] yPoints;

    public Triangle(double xpos, double ypos, Paint paint) {
        super(xpos, ypos, paint);
        xPoints = new double[]{xpos,  xpos - 5,xpos + 5};
        yPoints = new double[]{ypos - 5, ypos + 5, ypos + 5};
    }

    @Override
    public void draw(GraphicsContext gc, boolean stroke) {
        if (stroke)
            gc.strokePolygon(xPoints, yPoints, 3);
        else {
            gc.setFill(getPaint());
            gc.fillPolygon(xPoints, yPoints, 3);
        }
    }

    @Override
    public boolean intersects(double x, double y) {
        return false;
    }
}
