package utils;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginScene extends Scene {
    private LoginScene(Parent parent, double v, double v1) {
        super(parent, v, v1);
    }

    private static Button getButton(String btnText, double setLayoutY, String styleString) {
        Button button = new Button(btnText);
        button.setLayoutX(180);

        button.setLayoutY(setLayoutY); // c

        button.setMinWidth(230);
        button.setMinHeight(35);
        button.setFocusTraversable(false);
        button.setStyle(styleString); //c

        button.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 15));

        return button;
    }

    private static TextField getInputField(String promptText, int setLayoutY, boolean isPasswordField) {
        TextField inputField = null;

        if(isPasswordField)
            inputField =  new PasswordField();
        else
            inputField = new TextField();

        inputField.setPromptText(promptText); //c
        inputField.setFocusTraversable(false);
        inputField.setLayoutX(170);
        inputField.setLayoutY(setLayoutY); //c
        inputField.setMinHeight(35);
        inputField.setMinWidth(250);
        inputField.setStyle("-fx-text-box-border: #c3cfd9; -fx-focus-color: #c3cfd9;");

        return inputField;

    }

    private static Group getGroup(Stage stage) {
        Group root = new Group();

        TextField username_field = getInputField("Username", 120, false);
        TextField password_field = getInputField("Password", 170, true);

        Button login_btn = getButton("Login", 220, "-fx-background-color: #1aae9f; -fx-text-fill: #fff;");

        root.getChildren().add(username_field);
        root.getChildren().add(password_field);
        root.getChildren().add(login_btn);

        return root;
    }

    public static Scene getScene(Stage stage) {
        return new LoginScene(getGroup(stage), 600, 400);
    }
}
