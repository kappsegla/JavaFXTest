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
import sample.shapes.decorators.ResizeDecorator;
import sample.shapes.decorators.RotateDecorator;
import sample.shapes.decorators.StrokeDecorator;

import java.util.Optional;

import static sample.shapes.ShapeType.*;

public class Controller {
    @FXML
    public Canvas canvas;

    double x, y;

    Model model;
    UnDoRedo unDoRedo;

    public Controller(Model model) {
        this.model = model;
    }

    public void initialize() {

       // model = new Model();
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
                         else if (ke.getCode().getCode() == '1') {
                            model.setMode(CIRCLE);
                            drawShapes();
                        } else if (ke.getCode().getCode() == '2') {
                            model.setMode(RECT);
                            drawShapes();
                        } else if (ke.getCode().getCode() == '3') {
                            model.setMode(TRIANGLE);
                            drawShapes();
                        }
//                        //Switch expressions
//                        switch (ke.getCode().getCode()) {
//                            case '1' -> model.setMode(CIRCLE);
//                            case '2' -> model.setMode(RECT);
//                            case '3' -> model.setMode(TRIANGLE);
//                        }
                        drawShapes();
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
            //Selection and change mode
            Optional<Drawable> shape = model.findIntersection(x, y);
            if (shape.isPresent()) {
                if (event.isControlDown()) {
                    //unDoRedo.insertInUnDoRedoForAddDecorator(new StrokeDecorator(shape.get(), Color.BLACK, 5.0));
                    unDoRedo.insertInUnDoRedoForAddDecorator(new RotateDecorator(shape.get(), 10.0));
                } else if (event.isAltDown()) {
                    unDoRedo.insertInUnDoRedoForAddDecorator(new ResizeDecorator(shape.get(), 2.0, 2.0));
                } else if (event.isShiftDown()) {
                    unDoRedo.insertInUnDoRedoChangeColor(shape.get(), getRandomColor());
                }

            }
        } else if (event.getButton() == MouseButton.PRIMARY) {
            //Create new Drawables
            if (event.isControlDown())
                unDoRedo.insertInUnDoRedoForInsert(new StrokeDecorator(ShapeFactory.createShape(new ShapeProperties(model.getShapeType(), x, y, Color.RED)), Color.BLACK, 5.0));
            else if (event.isAltDown())
                unDoRedo.insertInUnDoRedoForInsert(new ResizeDecorator(ShapeFactory.createShape(new ShapeProperties(model.getShapeType(), x, y, Color.RED)), 2.0, 2.0));
            else {
                unDoRedo.insertInUnDoRedoForInsert(ShapeFactory.createShape(new ShapeProperties(model.getShapeType(), x, y, Color.RED)));
            }
        }
    }

    private Color getRandomColor() {
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        return Color.rgb(r, g, b);
    }


    public void drawShapes() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //Draw all shapes
        for (Drawable shape : model.getShapes()) {
            shape.draw(gc, false);
        }
        //Draw help text on top of everything
        gc.setFill(Color.DARKBLUE);
        gc.fillText("LEFT Mouse - New Shape RIGHT Mouse - Select\n" +
                "1 - Circle, 2 - Rectangle, 3 - Triangle\n" +
                "Ctrl - Border, Alt - Increase size, Shift - Change color", 10, 10);
        gc.fillText("Type: " + model.getShapeType(), 10, canvas.getHeight() - 10);

    }
}
