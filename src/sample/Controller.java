package sample;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.client.SocketClient;
import sample.dialogs.Dialogs;
import sample.operations.UnDoRedoShapes;
import sample.shapes.*;
import sample.shapes.decorators.ResizeDecorator;
import sample.shapes.decorators.RotateDecorator;
import sample.shapes.decorators.StrokeDecorator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import static sample.shapes.ShapeType.*;

public class Controller {
    @FXML
    public Canvas canvas;

    double x, y;

    Model model;
    UnDoRedoShapes unDoRedo;
    SocketClient socketClient = new SocketClient();

    Stage stage;

    public Controller(Model model) {
        this.model = model;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {

        // model = new Model();
        unDoRedo = new UnDoRedoShapes(model.getShapes());

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
                    final KeyCombination ctrlS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
                    final KeyCombination keyC = new KeyCodeCombination(KeyCode.C);

                    public void handle(KeyEvent ke) {
                        if (ctrlZ.match(ke)) {
                            unDoRedo.undo(1);
                            ke.consume(); // <-- stops passing the event to next node
                        } else if (ctrlShiftZ.match(ke)) {
                            unDoRedo.redo(1);
                            ke.consume();
                        } else if (ctrlS.match(ke)) {
                            saveToFile();
                        } else if (ke.getCode().getCode() == '1') {
                            model.setMode(CIRCLE);
                            drawShapes();
                        } else if (ke.getCode().getCode() == '2') {
                            model.setMode(RECT);
                            drawShapes();
                        } else if (ke.getCode().getCode() == '3') {
                            model.setMode(TRIANGLE);
                            drawShapes();
                        } else if (keyC.match(ke)) {
                            enableNetwork();
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

    private void enableNetwork() {
        var host = Dialogs.showHostNamePortDialog(stage);
        host.ifPresent(hostPort -> {
            socketClient.connect(hostPort.getKey(), hostPort.getValue());
            socketClient.setReceiveListener(System.out::println);
        });
    }

    public void saveToFile() {
        Dialogs.showSaveAsFileDialog(stage).ifPresent(path -> {
            try (FileWriter fileWriter = new FileWriter(path)) {
                fileWriter.write("<?xml version=\"1.0\" standalone=\"no\"?>\n" +
                        "<svg width=\""+canvas.getWidth()+"\" height=\""+canvas.getHeight()+"\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">\n");
                for (Drawable shape : model.getShapes()) {
                    fileWriter.write(shape.toSvg());
                }
                fileWriter.write("</svg>");
            } catch (IOException e) {
                e.printStackTrace();
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
                    unDoRedo.insertInUnDoRedoForAddDecorator(new ResizeDecorator(shape.get(), 0.5, 0.5));
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
                Shape shape = ShapeFactory.createShape(new ShapeProperties(model.getShapeType(), x, y, Color.RED));
                unDoRedo.insertInUnDoRedoForInsert(shape);
                sendToServer(shape);
            }
        }
    }

    private void sendToServer(Shape shape) {
        if (socketClient.isConnected())
            socketClient.sendMessage(shape.toSvg());
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
