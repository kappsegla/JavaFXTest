package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    //--module-path "C:\Program Files\Java\javafx-sdk-13\lib" --add-modules javafx.controls,javafx.fxml
    Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        //We can't use defualt noargs constructor on Controller.
        //Tell loader how to create our controller with the constructor that takes a model
        Model model = new Model();
        loader.setControllerFactory(param -> new Controller(model));
        Parent root = loader.load();

        controller = loader.getController();
        controller.setStage(primaryStage);

        primaryStage.setTitle("MyPaint");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

        primaryStage.setScene(new Scene(root));

        //Now we can call init when scene is set to do stuff that requires the scene
        controller.init();

        primaryStage.sizeToScene();
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        //We have to shutdown our network connections and thread pool before JVM can close down.
        controller.shutDown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
