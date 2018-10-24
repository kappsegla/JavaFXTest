package sample.operations;

public class Tuple {

    public Command doIt;
    public Command undoIt;

    public Tuple(Command doIt, Command undoIt){
           this.doIt = doIt;
           this.undoIt = undoIt;
    }
}
