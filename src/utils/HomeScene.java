package utils;

import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    private static HttpResponse<String> getUsersProduct(HashMap<String, Object> creds) throws IOException, InterruptedException {
        final String _URL = "https://amazonpricetracker3.herokuapp.com/usersproduct";
        final String QUERY_STRING = "?username=" + creds.get("username") + "&pwd=" + creds.get("password");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(_URL + QUERY_STRING))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());

    }

    private static Scene getScrollPane(Stage stage, HashMap<String, Object> creds) {
        VBox vBox = new VBox();

        TextField link_field = getInputField("Paste Link Here", 14);
        Button submit_btn = getButton("Submit", 320,60, 25,"-fx-background-color: #6558f5; -fx-text-fill: #fff;");

        Pane searchPane = new Pane();
        searchPane.setMinWidth(SCENE_WIDTH);
        searchPane.getChildren().add(link_field);
        searchPane.getChildren().add(submit_btn);
//        searchPane.setStyle("-fx-border-color: red; -fx-border-width: 1px");

        Pane productsPane = new Pane();
        productsPane.setMinWidth(SCENE_WIDTH);

//        productsPane.getChildren().add(random);
//        productsPane.setStyle("-fx-border-color: green; -fx-border-width: 1px");


        vBox.getChildren().add(searchPane);
        vBox.getChildren().add(productsPane);

        vBox.setMinWidth(SCENE_WIDTH);
        vBox.setSpacing(23);
        ScrollPane sp = new ScrollPane();
        sp.setMinWidth(SCENE_WIDTH);
        sp.setContent(vBox);

        Scene scene = new HomeScene(sp, SCENE_HEIGHT, SCENE_WIDTH);


        System.out.println("creds.toString() = " + creds.toString());

        /**
        try {
            HttpResponse <String> response = null;

            response = getUsersProduct(creds);
            JSONParser parser = new JSONParser();
            JSONObject body = (JSONObject) parser.parse(response.body());

            JSONArray products = (JSONArray) body.get("products");




        }
        catch (IOException | InterruptedException | ParseException e) {
            e.printStackTrace();
        }
        */

        return scene;
        
    }

    public static Scene getScene(Stage stage, HashMap<String, Object> creds){
        return getScrollPane(stage, creds);
    }


}
