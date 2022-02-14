package utils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class FluctuationStage extends Application {
    String asin;
    FluctuationStage(String asin){
        this.asin = asin;
    }

    @Override
    public void start(Stage stage) throws Exception {


        System.out.println("this.asin = " + this.asin);

        Pane pane = new Pane();
        pane.getChildren().add(new Label( this.asin ));

        Scene scene = new Scene(pane);
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
