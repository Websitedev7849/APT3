package utils;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class SignupScene extends Scene {

    private static final String _URL = "https://amazonpricetracker3.herokuapp.com/users";

    public SignupScene(Parent parent, double v, double v1) {
        super(parent, v, v1);
    }

    private static Button getButton(String btnText, double setLayoutY, String styleString) {
        Button button = new Button(btnText);
        button.setLayoutX(180);

        button.setLayoutY(setLayoutY); // c

        button.setMinWidth(230);
        button.setMinHeight(35);
        button.setStyle(styleString); //c
        button.setCursor(Cursor.HAND);


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
        inputField.setFont(new Font("Arial", 15));
        inputField.setLayoutX(170);
        inputField.setLayoutY(setLayoutY); //c
        inputField.setMinHeight(35);
        inputField.setMinWidth(250);
        inputField.setStyle("-fx-text-box-border: #c3cfd9; -fx-focus-color: #c3cfd9;");

        return inputField;

    }

    private static HttpResponse<String> isUserExists(String username, String password) throws IOException, InterruptedException {
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("pwd", password);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body.toJSONString()))
                .uri(URI.create(_URL))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }


    private static Group getGroup(Stage stage) {
        Group root = new Group();

        TextField username_field = getInputField("Username", 100, false);
        TextField password_field = getInputField("Password", 150, true);
        TextField confirmPassword_field = getInputField("Confirm Password", 200, true);

        Label label = new Label("");
        label.setLayoutX(235);
        label.setLayoutY(340);
        label.setStyle("-fx-text-fill : red;");
        label.setFont(new Font("Arial", 15));

        Button signUp_btn = getButton("Submit", 250, "-fx-background-color: #1aae9f; -fx-text-fill: #fff;");

        signUp_btn.setOnAction((event)->{
            if (username_field.getText().isEmpty() || password_field.getText().isEmpty() || confirmPassword_field.getText().isEmpty()) {
                System.out.println("fields empty");
                label.setText("Password Field are empty");
            }
            else if( !( password_field.getText().equals(confirmPassword_field.getText()) ) ){
                System.out.println("Password Mismatch");
                label.setText("Password Mismatch");
            }
            else {
                try {
                    HttpResponse<String> response = isUserExists(username_field.getText(), password_field.getText());

                    HashMap<String, Object> creds = new HashMap<>();
                    creds.put("username", username_field.getText());
                    creds.put("password", password_field.getText());

                    switch (response.statusCode()){
                        case 200: {
                            System.out.println("response = " + response.body());
                            stage.setScene(HomeScene.getScene(stage, creds));
                            break;
                        }
                        case 201: {
                            System.out.println("response = " + response.body());
                            label.setText("User Already Exists");
                            break;
                        }
                        default:{
                            System.out.println("response = " + response.body());
                            label.setText("Something Went Wrong");
                        }

                    }

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });



        root.getChildren().add(username_field);
        root.getChildren().add(password_field);
        root.getChildren().add(confirmPassword_field);
        root.getChildren().add(signUp_btn);
        root.getChildren().add(label);

        return root;
    }

    public static Scene getScene(Stage stage) {
        return new SignupScene(getGroup(stage), 600, 400);
    }
}
