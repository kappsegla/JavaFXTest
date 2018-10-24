package sample.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Scale;

public class ResizeDecorator extends Decorator {

    private final double scaleX;
    private final double scaleY;

    public ResizeDecorator(Drawable drawable, double scaleX, double scaleY) {
        super(drawable);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public void draw(GraphicsContext gc, boolean stroke) {
        gc.save();
        //Translate origin to our point
        gc.translate(-getXpos(),-getYpos());
        //Scale around origin
        gc.scale(scaleX, scaleY);
        super.draw(gc, stroke);
        gc.restore();
    }

    @Override
    public boolean intersects(double x, double y) {
        //TODO: must change the intersection code to take into account that we are using transformation'
        return super.intersects(x, y);
    }
}
