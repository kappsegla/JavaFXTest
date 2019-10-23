package sample.operations;

import java.util.Stack;

public class UnDoRedo {
    Stack<Tuple> undoCommands = new Stack<>();
    Stack<Tuple> redoCommands = new Stack<>();

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
}
