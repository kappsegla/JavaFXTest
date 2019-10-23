package sample.dialogs;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.io.File;
import java.util.Optional;

public class Dialogs {
    public static Optional<Pair<String, Integer>> showHostNamePortDialog(Stage stage) {
        // Create the custom dialog.
        Dialog<Pair<String, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Connect to");
        dialog.setHeaderText("Please enter server ip and port");
        dialog.initStyle(StageStyle.UTILITY);

        // Set the button types.
        ButtonType connectButtonType = new ButtonType("Connect", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(connectButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField hostName = new TextField();
        hostName.setPromptText("hostname");
        hostName.setText("82.196.127.10");
        TextField port = new TextField();
        port.setPromptText("port number");
        port.setText("8000");

        grid.add(new Label("Host:"), 0, 0);
        grid.add(hostName, 1, 0);
        grid.add(new Label("Port:"), 0, 1);
        grid.add(port, 1, 1);

        Node connectButton = dialog.getDialogPane().lookupButton(connectButtonType);
        connectButton.setDisable(false);

        // Do some validation (using the Java 8 lambda syntax).
//        hostName.textProperty().addListener((observable, oldValue, newValue) -> {
//            connectButton.setDisable(newValue.trim().isEmpty());
//        });

        dialog.getDialogPane().setContent(grid);

// Request focus on the username field by default.
        Platform.runLater(hostName::requestFocus);

// Convert the result to a hostname-port-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == connectButtonType) {
                return new Pair<>(hostName.getText(), Integer.parseInt(port.getText()));
            }
            return null;
        });

        return dialog.showAndWait();
    }
    public static Optional<File> showSaveAsFileDialog(Stage stage) {
        //Show a file dialog that returns a selected file for opening or null if no file was selected.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("SVG (*.svg)", "*.svg"));

        File path = fileChooser.showSaveDialog(stage);

        //Path can be null if abort was selected
        if (path != null) {
            //We have a valid File object. Use with FileReader or FileWriter
            return Optional.of(path);
        }
        //No file selected
        return Optional.empty();
    }
}

