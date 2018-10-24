//package sample.operations;
//
//import javafx.scene.paint.Paint;
//import sample.shapes.Drawable;
//import sample.shapes.Shape;
//
//public class ChangeColorCommand implements Command {
//
//    private Drawable shape;
//    private Paint paint;
//    private Paint oldPaint;
//
//    public ChangeColorCommand(Drawable shape, Paint paint) {
//        this.shape = shape;
//        this.paint = paint;
//    }
//
//    @Override
//    public void execute() {
//        oldPaint = shape.getPaint();
//        shape.setPaint(paint);
//    }
//
//    @Override
//    public void unExecute() {
//        shape.setPaint(oldPaint);
//    }
//}
