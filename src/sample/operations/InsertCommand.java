//package sample.operations;
//
//import sample.shapes.Drawable;
//import sample.shapes.Shape;
//
//import java.util.List;
//
//public class InsertCommand implements Command {
//
//    private Drawable shape;
//    List<Drawable> shapes;
//
//    public InsertCommand(Drawable shape, List<Drawable> shapes) {
//        this.shape = shape;
//        this.shapes = shapes;
//    }
//
//    @Override
//    public void execute() {
//        shapes.add(shape);
//    }
//
//    @Override
//    public void unExecute() {
//        shapes.remove(shape);
//    }
//}
