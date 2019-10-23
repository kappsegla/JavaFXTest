package sample.shapes.decorators;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import sample.shapes.Drawable;

public abstract class Decorator implements Drawable {
    private final Drawable drawable;

    public Decorator(Drawable drawable) {
        this.drawable = drawable;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    @Override
    public void setXpos(double xpos) {
        drawable.setXpos(xpos);
    }

    @Override
    public void setYpos(double ypos) {
        drawable.setYpos(ypos);
    }

    @Override
    public double getXpos() {
        return drawable.getXpos();
    }

    @Override
    public DoubleProperty xposProperty() {
        return drawable.xposProperty();
    }

    @Override
    public double getYpos() {
        return drawable.getYpos();
    }

    @Override
    public DoubleProperty yposProperty() {
        return drawable.yposProperty();
    }

    @Override
    public void draw(GraphicsContext gc, boolean stroke) {
        drawable.draw(gc,stroke);
    }

    @Override
    public Paint getPaint() {
        return drawable.getPaint();
    }

    @Override
    public ObjectProperty<Paint> paintProperty() {
        return drawable.paintProperty();
    }

    @Override
    public void setPaint(Paint paint) {
        drawable.setPaint(paint);
    }

    @Override
    public boolean intersects(double x, double y) {
        return drawable.intersects(x,y);
    }

    @Override
    public String toSvg() {
        return drawable.toSvg();
    }
}
