package sample.shapes;

import javafx.scene.paint.Paint;

public class ShapeProperties {
    public final ShapeType type;
    public final double x;
    public final double y;
    public final Paint paint;

    public ShapeProperties(ShapeType type, double x, double y, Paint paint) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.paint = paint;
    }
}
