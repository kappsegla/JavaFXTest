package sample.shapes.decorators;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Scale;
import sample.shapes.Drawable;

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
        gc.transform(new Affine(new Scale(scaleX, scaleY, getXpos(), getYpos())));
        super.draw(gc, stroke);
        gc.restore();
    }

    @Override
    public boolean intersects(double x, double y) {
        //Change the intersection code to take into account that we are using transformation
        double scaledX = getXpos() + (x - getXpos()) / scaleX;
        double scaledY = getYpos() + (y - getYpos()) / scaleY;

        return super.intersects(scaledX, scaledY);
    }
}
