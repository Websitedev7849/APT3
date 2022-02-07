package utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainScene extends Scene {

    private MainScene(Parent parent, double v, double v1) {
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
//    main stage is expected here to change the scene
    private static Group getGroup(Stage stage){
        Group root = new Group();

        Button login_btn = getButton("Login", 140, "-fx-background-color: #1aae9f; -fx-text-fill: #fff;");
        Button signUp_btn = getButton("Sign Up", 200, "-fx-background-color: #c3cfd9;");

        login_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                Scene loginScene = LoginScene.getScene(stage);
                stage.setScene(loginScene);
            }
        });
        signUp_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Scene signupScene = SignupScene.getScene(stage);
                stage.setScene(signupScene);
            }
        });

        root.getChildren().add(login_btn);
        root.getChildren().add(signUp_btn);

        return root;
    }

//    main stage is expected here to change the scene
//    returns instance of this class
    public static Scene getScene(Stage stage){
        return new MainScene(getGroup(stage), 600,400);
    }

}
