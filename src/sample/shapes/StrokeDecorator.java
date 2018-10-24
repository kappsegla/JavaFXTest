package sample.shapes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class StrokeDecorator extends Decorator {

    private Paint paint;
    private double width;

    public StrokeDecorator(Drawable drawable, Paint paint, double width) {
        super(drawable);
        this.paint = paint;
        this.width = width;
    }

    @Override
    public void draw(GraphicsContext gc, boolean stroke) {
        gc.save();
        gc.setStroke(paint);
        gc.setLineWidth(width);
        //For this decorator we will draw with stroke enabled
        //Other decorators might not do an extra draw operation
        //and instead change some parameters before calling draw below
        super.draw(gc, true);
        gc.restore();
        //Always paint our inner object with incoming settings
        super.draw(gc, stroke);
    }
}
