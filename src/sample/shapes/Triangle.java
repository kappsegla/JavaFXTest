package sample.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Triangle extends Shape {

    double[] xPoints;
    double[] yPoints;

    public Triangle(double xpos, double ypos, Paint paint) {
        super(xpos, ypos, paint);
        xPoints = new double[]{xpos, xpos - 5, xpos + 5};
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
        //https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle
        var p0x = xPoints[0];
        var p0y = yPoints[0];
        var p1x = xPoints[2];
        var p1y = yPoints[2];
        var p2x = xPoints[1];
        var p2y = yPoints[1];

        var Area = 0.5 *(-p1y*p2x + p0y*(-p1x + p2x) + p0x*(p1y - p2y) + p1x*p2y);
        var s = 1/(2*Area)*(p0y*p2x - p0x*p2y + (p2y - p0y)*x + (p0x - p2x)*y);
        var t = 1/(2*Area)*(p0x*p1y - p0y*p1x + (p0y - p1y)*x + (p1x - p0x)*y);
        if( s > 0 && t > 0 && 1-s-t > 0)
            return true;
        return false;
    }

    private void triangleIntersects(){

    }
}
