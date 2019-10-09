package sample;

import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import sample.operations.UnDoRedo;
import sample.shapes.*;

import java.util.Optional;

public class Controller {
    @FXML
    public Canvas canvas;

    double x, y;

    Model model;
    UnDoRedo unDoRedo;

    public Controller() {

    }

    public void initialize() {

        model = new Model();
        unDoRedo = new UnDoRedo(model.getShapes());

        //Call draw on canvas when width or height changes
        canvas.widthProperty().addListener(observable -> drawShapes());
        canvas.heightProperty().addListener(observable -> drawShapes());

        //Register for change events on shapelist
        model.getShapes().addListener(this::onListOfDrawablesChanged);
    }

    //Do stuff here that requires that we have a scene.
    //Called after setScene in Main.start
    public void init() {
        //Capture Ctrl-Z for undo
        canvas.getScene().addEventFilter(KeyEvent.KEY_PRESSED,
                new EventHandler<KeyEvent>() {
                    final KeyCombination ctrlZ = new KeyCodeCombination(KeyCode.Z,
                            KeyCombination.CONTROL_DOWN);
                    final KeyCombination ctrlShiftZ = new KeyCodeCombination(KeyCode.Z,
                            KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);

                    public void handle(KeyEvent ke) {
                        if (ctrlZ.match(ke)) {
                            unDoRedo.undo(1);
                            ke.consume(); // <-- stops passing the event to next node
                        } else if (ctrlShiftZ.match(ke)) {
                            unDoRedo.redo(1);
                            ke.consume();
                        }
                    }
                });
    }

    //Changed listener method for shapes list
    public void onListOfDrawablesChanged(ListChangeListener.Change<? extends Drawable> c) {
        while (c.next()) {
            if (c.wasPermutated()) {
                for (int i = c.getFrom(); i < c.getTo(); ++i) {
                    System.out.println("Permuted: " + i + " " + model.getShapes().get(i));
                }
            } else if (c.wasUpdated()) {
                for (int i = c.getFrom(); i < c.getTo(); ++i) {
                    System.out.println("Updated: " + i + " " + model.getShapes().get(i));
                }
            } else {
                for (Drawable removedItem : c.getRemoved()) {
                    System.out.println("Removed: " + removedItem);
                }
                for (Drawable addedItem : c.getAddedSubList()) {
                    System.out.println("Added: " + addedItem);
                }
            }
        }
        System.out.println(model.getShapes().size());
        drawShapes();

    }

    public void canvasOnMouseClicked(MouseEvent event) {
        x = event.getX();
        y = event.getY();

        if (event.getButton() == MouseButton.SECONDARY) {
            Optional<Drawable> shape = model.findIntersection(x, y);
            if (shape.isPresent()) {
                if (event.isControlDown()) {
                    unDoRedo.insertInUnDoRedoForAddDecorator(new StrokeDecorator(shape.get(), Color.BLACK, 5.0));
                } else if (event.isAltDown()) {
                    unDoRedo.insertInUnDoRedoForAddDecorator(new ResizeDecorator(shape.get(),2.0, 2.0));
                } else {
                    if (shape.get().getPaint() != Color.GREEN)
                        unDoRedo.insertInUnDoRedoChangeColor(shape.get(), Color.GREEN);
                }
            }
        } else if (event.getButton() == MouseButton.PRIMARY) {
            if (event.isControlDown())
                unDoRedo.insertInUnDoRedoForInsert(new StrokeDecorator(ShapeFactory.createShape(new ShapeProperties("circle", x, y, Color.RED)), Color.BLACK, 5.0));
            else if (event.isAltDown())
                unDoRedo.insertInUnDoRedoForInsert(new ResizeDecorator(ShapeFactory.createShape(new ShapeProperties("circle", x, y, Color.RED)), 2.0, 2.0));
            else
                unDoRedo.insertInUnDoRedoForInsert(ShapeFactory.createShape(new ShapeProperties("circle", x, y, Color.RED)));
        }
    }


    public void drawShapes() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //Draw all shapes
        for (Drawable shape : model.getShapes()) {
            shape.draw(gc, false);
        }
    }
}
