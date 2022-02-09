package utils;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginScene extends Scene {

    final static String _URL = "http://127.0.0.1:8000/users";

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
        inputField.setFont(new Font("Arial", 15));
        inputField.setFocusTraversable(false);
        inputField.setLayoutX(170);
        inputField.setLayoutY(setLayoutY); //c
        inputField.setMinHeight(35);
        inputField.setMinWidth(250);
        inputField.setStyle("-fx-text-box-border: #c3cfd9; -fx-focus-color: #c3cfd9;");

        return inputField;

    }

    private static boolean isUserValid(String username, String password) throws Exception {

        URL url = new URL(_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("pwd", password);

        String jsonString = jsonObject.toJSONString();


        OutputStream os = connection.getOutputStream();
        os.write(jsonString.getBytes(StandardCharsets.UTF_8), 0, jsonString.length());

        StringBuilder response = new StringBuilder();
        try  {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String responseLine = null;
            while((responseLine = br.readLine()) != null){
                response.append(responseLine.trim());
            }
            System.out.println("response = " + response);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return false;
    }

    private static Group getGroup(Stage stage) {
        Group root = new Group();

        TextField username_field = getInputField("Username", 120, false);
        TextField password_field = getInputField("Password", 170, true);

        Button login_btn = getButton("Login", 220, "-fx-background-color: #1aae9f; -fx-text-fill: #fff;");

        login_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                final String USERNAME = username_field.getText();
                final String PASSWORD = password_field.getText();

                System.out.println("USERNAME = " + USERNAME);
                System.out.println("PASSWORD = " + PASSWORD);

                try {
                    System.out.println("isUserValid(USERNAME, PASSWORD) = " + isUserValid(USERNAME, PASSWORD));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        root.getChildren().add(username_field);
        root.getChildren().add(password_field);
        root.getChildren().add(login_btn);

        return root;
    }

    public static Scene getScene(Stage stage) {
        return new LoginScene(getGroup(stage), 600, 400);
    }
}
