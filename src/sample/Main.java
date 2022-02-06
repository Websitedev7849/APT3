package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.MainScene;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{

//        stage is passed here to change the scene of stage
        Scene scene = MainScene.getScene(stage);
        stage.setScene(scene);
        stage.setTitle("First JavaFX Application");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
