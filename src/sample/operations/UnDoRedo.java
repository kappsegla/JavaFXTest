package sample.operations;

import javafx.scene.paint.Color;
import sample.shapes.*;

import java.util.List;
import java.util.Stack;

public class UnDoRedo {
    Stack<Tuple> undoCommands = new Stack<>();
    Stack<Tuple> redoCommands = new Stack<>();

    List<Drawable> shapes;

    public UnDoRedo(List<Drawable> shapeList) {
        shapes = shapeList;
    }

    public void redo(int levels) {
        for (int i = 1; i <= levels; i++) {
            if (redoCommands.size() != 0) {
                Tuple command = redoCommands.pop();
                command.doIt.execute();
                undoCommands.push(command);
            }
        }
    }

    public void undo(int levels) {
        for (int i = 1; i <= levels; i++) {
            if (undoCommands.size() != 0) {
                Tuple command = undoCommands.pop();
                command.undoIt.execute();
                redoCommands.push(command);
            }
        }
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
        Tuple tuple = new Tuple(() -> shapes.replaceAll(drawable -> drawable.equals(shape) ? decorator : drawable),
                                () -> shapes.replaceAll(drawable -> drawable.equals(decorator) ? shape : drawable));
        tuple.doIt.execute();
        undoCommands.push(tuple);
        redoCommands.clear();
    }
}
