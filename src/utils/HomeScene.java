package utils;

import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.HashMap;

public class HomeScene extends Scene {

    private static final int SCENE_WIDTH = 500;
    private static final int SCENE_HEIGHT = 700;


    private HomeScene(Parent parent, double v, double v1) {
        super(parent, v, v1);
    }

    private static TextField getInputField(String promptText, int setLayoutY) {
        TextField inputField = null;

        inputField =  new PasswordField();

        inputField.setPromptText(promptText); //c
        inputField.setFont(new Font("Arial", 15));
        inputField.setFocusTraversable(false);
        inputField.setLayoutX(137);
        inputField.setLayoutY(setLayoutY); //c
        inputField.setMinHeight(35);
        inputField.setMinWidth(450);
        inputField.setStyle("-fx-text-box-border: #c3cfd9; -fx-focus-color: #c3cfd9;");

        return inputField;

    }

    private static Button getButton(String btnText,double setLayoutX, double setLayoutY, double width,String styleString) {
        Button button = new Button(btnText);
        button.setLayoutX(setLayoutX);

        button.setLayoutY(setLayoutY); // c

        button.setMinWidth(width);
        button.setMinHeight(35);
        button.setFocusTraversable(false);
        button.setStyle(styleString); //c
        button.setCursor(Cursor.HAND);

        button.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 15));

        return button;
    }

    private static Scene getScrollPane(Stage stage, HashMap<String, Object> creds) {
        VBox vBox = new VBox();

        TextField link_field = getInputField("Paste Link Here", 14);
        Button submit_btn = getButton("Submit", 320,60, 25,"-fx-background-color: #6558f5; -fx-text-fill: #fff;");

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setMinWidth(SCENE_WIDTH);
        anchorPane.getChildren().add(link_field);
        anchorPane.getChildren().add(submit_btn);
//        anchorPane.setStyle("-fx-border-color: green; -fx-border-width: 1px");



        vBox.getChildren().add(anchorPane);
        vBox.setMinWidth(SCENE_WIDTH);
        ScrollPane sp = new ScrollPane();
        sp.setMinWidth(SCENE_WIDTH);
        sp.setContent(vBox);

        Scene scene = new HomeScene(sp, SCENE_HEIGHT, SCENE_WIDTH);


        System.out.println("creds.toString() = " + creds.toString());
        
        return scene;
        
    }

    public static Scene getScene(Stage stage, HashMap<String, Object> creds){
        return getScrollPane(stage, creds);
    }


}
