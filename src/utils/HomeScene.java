package utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.util.HashMap;

public class HomeScene extends Scene {

    private HomeScene(Parent parent, double v, double v1) {
        super(parent, v, v1);
    }

    private static Scene getScrollPane(Stage stage, HashMap<String, Object> creds) {
        VBox vBox = new VBox();


        for (int i = 0; i < 30; i++) {
            Label label = new Label("this is home screen");
            label.setStyle("-fx-text-fill: #8B008B;");
            label.setLayoutX(200);

            AnchorPane anchorPane = new AnchorPane();
            anchorPane.prefWidthProperty().bind(vBox.widthProperty());
            anchorPane.setStyle("-fx-background: red");

            anchorPane.getChildren().add(label);
            vBox.getChildren().add(anchorPane);
        }

        vBox.setSpacing(20);

        ScrollPane sp = new ScrollPane();
        sp.setContent(vBox);

        Scene scene = new HomeScene(sp, 700, 500);

        System.out.println("creds.toString() = " + creds.toString());
        
        return scene;
        
    }

    public static Scene getScene(Stage stage, HashMap<String, Object> creds){
        return getScrollPane(stage, creds);
    }


}
