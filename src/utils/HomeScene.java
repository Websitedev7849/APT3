package utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashMap;

public class HomeScene extends Scene {

    private HomeScene(Parent parent, double v, double v1) {
        super(parent, v, v1);
    }

    private static Scene getScrollPane(Stage stage, HashMap<String, Object> creds) {
        VBox root = new VBox();

        for (int i = 0; i < 30; i++) {
            Button btn = new Button("button");
            btn.setAlignment(Pos.BASELINE_CENTER);
            root.getChildren().add(btn);
        }

        root.minWidth(500);

        root.setPadding(new Insets(10));
        root.setSpacing(10);

        ScrollPane sp = new ScrollPane();
        sp.setContent(root);
        sp.setStyle("-fx-background: red");

        Scene scene = new Scene(sp, 700, 500);

        System.out.println("creds.toString() = " + creds.toString());
        
        return scene;
        
    }

    public static Scene getScene(Stage stage, HashMap<String, Object> creds){
        return getScrollPane(stage, creds);
    }


}
