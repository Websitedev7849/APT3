package utils;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;


public class LoginScene extends Scene {

    final static String _URL = "https://amazonpricetracker3.herokuapp.com/users";

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

    private static HttpResponse<String> isUserValid(String username, String password) throws IOException, InterruptedException {
        final String QUERY_STRING = "?username=" + username +"&pwd=" + password;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(_URL + QUERY_STRING))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());

    }

    private static Group getGroup(Stage stage) {
        Group root = new Group();

        TextField username_field = getInputField("Username", 120, false);
        TextField password_field = getInputField("Password", 170, true);
        Button login_btn = getButton("Login", 220, "-fx-background-color: #1aae9f; -fx-text-fill: #fff;");

        username_field.setText("heinzdoof");
        password_field.setText("123456789");

        Label label = new Label("");
        label.setLayoutX(235);
        label.setLayoutY(340);
        label.setStyle("-fx-text-fill : red;");
        label.setFont(new Font("Arial", 15));

        login_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                final String USERNAME = username_field.getText();
                final String PASSWORD = password_field.getText();


                try {
                    HttpResponse <String> response = isUserValid(USERNAME, PASSWORD);
                    JSONParser parser = new JSONParser();

                    JSONObject body = (JSONObject) parser.parse(response.body());

                    if(response.statusCode() == 200) { // USER LOGGED IN
                        label.setText((String) body.get("message"));
                        label.setStyle("-fx-text-fill : green;");

                        HashMap<String, Object> creds = new HashMap<>();
                        creds.put("username", USERNAME);
                        creds.put("password", PASSWORD);

                        stage.setScene(HomeScene.getScene(stage, creds));
                    }
                    else if (response.statusCode() == 403){ // USER LOG IN FAILED
                        label.setText((String) body.get("message"));
                        label.setStyle("-fx-text-fill : red;");
                    }
                } catch (IOException | InterruptedException | ParseException e) {
                    e.printStackTrace();
                    label.setText("Connection Problem");
                    label.setStyle("-fx-text-fill : red;");
                }


            }
        });

        root.getChildren().add(username_field);
        root.getChildren().add(password_field);
        root.getChildren().add(login_btn);
        root.getChildren().add(label);

        return root;
    }

    public static Scene getScene(Stage stage) {
        return new LoginScene(getGroup(stage), 600, 400);
    }
}
