package sample.operations;

import javafx.scene.paint.Color;
import sample.shapes.Drawable;
import sample.shapes.ResizeDecorator;
import sample.shapes.Shape;

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

//    public void insertInUnDoRedoForInsert(Drawable shape) {
//        Command cmd = new InsertCommand(shape, shapes);
//        cmd.execute();
//        undoCommands.push(cmd);
//        redoCommands.clear();
//    }
    public void insertInUnDoRedoForInsert(Drawable shape) {
        Tuple tuple = new Tuple(()-> shapes.add(shape), ()-> shapes.remove(shape));
        tuple.doIt.execute();
        undoCommands.push(tuple);
        redoCommands.clear();
    }

    public void changeColorCommand(Drawable shape, Color color) {
//        Command cmd = new ChangeColorCommand(shape, color);
//        cmd.execute();
//        undoCommands.push(cmd);
//        redoCommands.clear();
    }
}
