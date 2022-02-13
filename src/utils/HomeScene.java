package utils;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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
import java.util.ArrayList;
import java.util.HashMap;

public class HomeScene extends Scene {

    public static final int SCENE_WIDTH = 500;
    public static final int SCENE_HEIGHT = 700;


    private HomeScene(Parent parent, double v, double v1) {
        super(parent, v, v1);
    }

    private static TextField getInputField(String promptText, int setLayoutY) {
        TextField inputField = null;

        inputField =  new PasswordField();

        inputField.setPromptText(promptText); //c
        inputField.setFont(new Font("Arial", 15));
        inputField.setFocusTraversable(false);
        inputField.setLayoutX(40);
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

        TextField link_field = getInputField("Paste Link Here", 14);
        Button submit_btn = getButton("Submit", 240,60, 25,"-fx-background-color: #6558f5; -fx-text-fill: #fff;");

        Pane searchPane = new Pane();
        searchPane.setMinWidth(SCENE_WIDTH);
        searchPane.getChildren().add(link_field);
        searchPane.getChildren().add(submit_btn);




        VBox vBox = new VBox();
        vBox.getChildren().add(searchPane);
//        vBox.setMinWidth(SCENE_WIDTH);
        vBox.setPrefWidth(SCENE_WIDTH);
        vBox.setPrefHeight(SCENE_HEIGHT);
        vBox.setSpacing(23);
        vBox.setStyle("-fx-background-color: #fff");

        ScrollPane sp = new ScrollPane();
        sp.setMinWidth(SCENE_WIDTH);
        sp.setContent(vBox);
        sp.setStyle("-fx-background-color: #fff");

        Scene scene = new HomeScene(sp, SCENE_HEIGHT, SCENE_WIDTH);

        System.out.println("creds.toString() = " + creds.toString());

        Runnable getProductsRunnable = new GetProducts(creds, vBox);

//        Threading of java fx
        Platform.runLater(getProductsRunnable);

        return scene;

    }

    public static Scene getScene(Stage stage, HashMap<String, Object> creds){
        return getScrollPane(stage, creds);
    }


}

class GetProducts implements Runnable {
    HashMap<String, Object> creds;
    VBox vBox;
    ArrayList<JSONObject> productObjectsLists = new ArrayList<>();

    GetProducts(HashMap<String, Object> creds, VBox vBox){
        this.creds = creds;
        this.vBox = vBox;
        this.vBox.setSpacing(30);
        this.vBox.setPadding(new Insets(0, 85, 0, 100));
    }

    private static AnchorPane getProductsPane(JSONObject productObject){
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setMinWidth(HomeScene.SCENE_WIDTH);
        anchorPane.setMinHeight(100);
        anchorPane.setLayoutX(150);

        Label name_label = new Label((String) productObject.get("name"));
        name_label.setFont(Font.font("Arial",  FontWeight.SEMI_BOLD,15));
        name_label.setWrapText(true);
        name_label.setMaxWidth(300.0);
        AnchorPane.setLeftAnchor(name_label, 16.0);
        AnchorPane.setTopAnchor(name_label, 10.0);

        Label todaysPrice_label = new Label("Today's Price:");
        todaysPrice_label.setFont(Font.font("Arial",  FontWeight.SEMI_BOLD,15));
        AnchorPane.setLeftAnchor(todaysPrice_label, 15.0);
        AnchorPane.setBottomAnchor(todaysPrice_label, 15.0);


        Double todaysPriceValue = (Double) productObject.get("price");
        Label todaysPriceValue_label = new Label(todaysPriceValue.toString());
        todaysPriceValue_label.setFont(Font.font("Arial", FontWeight.BOLD ,14));
        todaysPriceValue_label.setStyle("-fx-text-fill: #3ebaae;");
        AnchorPane.setLeftAnchor(todaysPriceValue_label, 120.0);
        AnchorPane.setBottomAnchor(todaysPriceValue_label, 15.0);

        Button button = new Button("View daily prices");
        button.setStyle("-fx-background-color: #6558f5; -fx-text-fill: #fff;");
        button.setFont(Font.font("Arial",  FontWeight.SEMI_BOLD,15.0));

        AnchorPane.setRightAnchor(button, 15.0);
        AnchorPane.setBottomAnchor(button, 15.0);

        anchorPane.getChildren().add(name_label);
        anchorPane.getChildren().add(todaysPrice_label);
        anchorPane.getChildren().add(todaysPriceValue_label);
        anchorPane.getChildren().add(button);
        anchorPane.setStyle("-fx-border-color: #c3cfd9;");

        return anchorPane;

    }

    @Override
    public void run() {
        
         try {
             HttpResponse <String> response = null;

             response = getUsersProduct(creds);

//             Getting user's products array
             JSONParser parser = new JSONParser();
             JSONObject body = (JSONObject) parser.parse(response.body());
             JSONArray products = (JSONArray) body.get("products");


             ArrayList<Thread> threadArrayList = new ArrayList<>();
             
             for (int i = 0; i < products.size(); i++) {
                 int finalI = i;

//                 getting today's Price
                 Runnable getProductsPriceRunnable = new Runnable() {
                     @Override
                     public void run() {
                         try {
                             JSONObject product = (JSONObject) products.get(finalI);
                             getTodaysPrice(product);
                         } catch (IOException | InterruptedException e) {
                             System.out.println(e.getMessage());
                             e.printStackTrace();
                         }
                     }
                 };
                 Thread thread = new Thread(getProductsPriceRunnable);
                 thread.start();
                 threadArrayList.add(thread);
             }

             for (Thread thread : threadArrayList) {
                 thread.join();
             }

             for (JSONObject j : productObjectsLists) {
                 System.out.println("j = " + j);
                    vBox.getChildren().add(getProductsPane(j));
             }

         }
         catch (IOException | InterruptedException | ParseException e) {
         e.printStackTrace();
         }

    }

    /**
     *
     * @returns: Json String
     * e.g :
     * {
     *   "message": "Products Found",
     *   "products": [
     *     {
     *       "asin": "B099S1MMJL",
     *       "name": "Master Labs Diamond Office Revolving Desk Chair with Umbrella Base with XW Handle (Black)"
     *     },
     *     {
     *       "asin": "B09GFM8CGS",
     *       "name": "Redmi 9A Sport (Carbon Black, 2GB RAM, 32GB Storage)"
     *     }
     *   ]
     * */
    private HttpResponse<String> getUsersProduct(HashMap<String, Object> creds) throws IOException, InterruptedException {
        final String _URL = "https://amazonpricetracker3.herokuapp.com/usersproduct";
        final String QUERY_STRING = "?username=" + creds.get("username") + "&pwd=" + creds.get("password");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(_URL + QUERY_STRING))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());

    }

    private void getTodaysPrice(JSONObject product) throws IOException, InterruptedException {
        final String _URL = "https://amazonpricetracker3.herokuapp.com/getprice?url=https://www.amazon.in/dp/" + product.get("asin");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(_URL))
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONParser parser = new JSONParser();
        JSONObject responseObject = null;
        try {
            responseObject = (JSONObject) parser.parse(response.body().toString());
            product.put("price", responseObject.get("price"));
        } catch (ParseException e) {
            product.put("price", -1);
            e.printStackTrace();
        }
        finally {
            productObjectsLists.add(product);
        }
    }


}