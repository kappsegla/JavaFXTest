package sample.shapes.decorators;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import sample.shapes.Drawable;

public class RotateDecorator extends Decorator{

    private final double angle;

    public RotateDecorator(Drawable drawable, double angle) {
        super(drawable);
        this.angle = angle;
    }

    @Override
    public void draw(GraphicsContext gc, boolean stroke) {
        gc.save();
        gc.transform(new Affine(new Rotate(angle, getXpos(), getYpos())));

        super.draw(gc, stroke);
        gc.restore();
    }

    @Override
    public boolean intersects(double x, double y) {
        //Rotate mousecoords and do an normal intersect
        Rotate rotate = new Rotate(angle, getXpos(), getYpos());
        var p = rotate.transform(x,y);
        return super.intersects(p.getX(), p.getY());
    }
}
