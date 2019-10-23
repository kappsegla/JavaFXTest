package sample.operations;

import javafx.scene.paint.Color;
import sample.shapes.*;
import sample.shapes.decorators.Decorator;

import java.util.List;

public class UnDoRedoShapes extends UnDoRedo {

    List<Drawable> shapes;

    public UnDoRedoShapes(List<Drawable> shapeList) {
        shapes = shapeList;
    }

    public void insertInUnDoRedoForInsert(final Drawable shape) {
        Tuple tuple = new Tuple(() -> shapes.add(shape), () -> shapes.remove(shape));
        tuple.doIt.execute();
        undoCommands.push(tuple);
        redoCommands.clear();
    }

    public void insertInUnDoRedoChangeColor(final Drawable shape, final Color newColor) {
        var oldColor = shape.getPaint();
        Tuple tuple = new Tuple(() -> shape.setPaint(newColor), () -> shape.setPaint(oldColor));
        tuple.doIt.execute();
        undoCommands.push(tuple);
        redoCommands.clear();
    }

    public void insertInUnDoRedoForAddDecorator(final Decorator decorator) {
        var shape = decorator.getDrawable();
        Tuple tuple = new Tuple(
                () -> replaceInList(shapes, shape, decorator),
                () -> replaceInList(shapes, decorator, shape));
        tuple.doIt.execute();
        undoCommands.push(tuple);
        redoCommands.clear();
    }

    private <T> void replaceInList(List<T> list, T replace, T with) {
        int index = list.indexOf(replace);
        list.set(index, with);
    }
}
