package utils;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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
import java.time.Duration;
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

        inputField =  new TextField();

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
        Label warning_label = new Label("");
        warning_label.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 18));

        VBox vBox = new VBox();

        submit_btn.setOnAction((event) -> {
            final String _URL = "https://amazonpricetracker3.herokuapp.com/usersproduct";
//            final String _URL = "http://127.0.0.1:8000/usersproduct";

            JSONObject body = new JSONObject();
            body.put("username", creds.get("username"));
            body.put("pwd", creds.get("password"));
            body.put("link", link_field.getText());

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body.toJSONString()))
                    .uri(URI.create(_URL))
                    .build();

            try {
                HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200){
                    JSONParser parser = new JSONParser();
                    JSONObject responseObject = (JSONObject) parser.parse(response.body().toString());
                    JSONObject product = (JSONObject) responseObject.get("product");

//                    Getting AnchorPane for Product
                    AnchorPane productPane = GetProducts.getProductsPane(product);
                    vBox.getChildren().add(productPane);
                    warning_label.setText("Product Registered Successfully");
                    warning_label.setStyle("-fx-text-fill: green;");

                }
                else if(response.statusCode() == 201){
                    warning_label.setText("User already registered for this product");
                    warning_label.setStyle("-fx-text-fill: green");
                }
                else {
                    warning_label.setText("Something Went Wrong");
                    warning_label.setStyle("-fx-text-fill: red");
                }
            } catch (IOException | InterruptedException | ParseException e) {
                e.printStackTrace();
            }
        });



        vBox.setPrefWidth(SCENE_WIDTH);
        vBox.setPrefHeight(SCENE_HEIGHT);
        vBox.setSpacing(23);
        vBox.setStyle("-fx-background-color: #fff");
        vBox.getChildren().add(link_field);
        vBox.getChildren().add(submit_btn);
        vBox.getChildren().add(warning_label);


        ScrollPane sp = new ScrollPane();
        sp.setMinWidth(SCENE_WIDTH);
        sp.setContent(vBox);
        sp.setStyle("-fx-background-color: #fff");

        Scene scene = new HomeScene(sp, SCENE_HEIGHT, SCENE_WIDTH);


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

    /**
     * @params JSONobject
     * e.g: {
     *       "asin": "B099S1MMJL",
     *       "name": "Master Labs Diamond Office Revolving Desk Chair with Umbrella Base with XW Handle (Black)",
     *       "price": 1700
     *  },
     * */
    public static AnchorPane getProductsPane(JSONObject productObject){
        /******* Anchor Pane will be appended to this.vbox *********/
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setMinWidth(HomeScene.SCENE_WIDTH);
        anchorPane.setMinHeight(100);
        anchorPane.setLayoutX(150);

        /******* Name of the product *********/
        String nameOfProduct = (String) productObject.get("name");
        nameOfProduct = nameOfProduct.toUpperCase().substring(0, Math.min(nameOfProduct.length(), 40));

        Label name_label = new Label(nameOfProduct);
        name_label.setFont(Font.font("Arial",  FontWeight.SEMI_BOLD,15));
        name_label.setWrapText(true);
        name_label.setMaxWidth(300.0);
        AnchorPane.setLeftAnchor(name_label, 16.0);
        AnchorPane.setTopAnchor(name_label, 10.0);

        /******* Label with Hard Coded "Today's Price" String *********/
        Label todaysPrice_label = new Label("Today's Price:");
        todaysPrice_label.setFont(Font.font("Arial",  FontWeight.SEMI_BOLD,15));
        AnchorPane.setLeftAnchor(todaysPrice_label, 15.0);
        AnchorPane.setBottomAnchor(todaysPrice_label, 15.0);

        /******* Today's price Value of Product will be added tp this label *********/
        Double todaysPriceValue = (Double) productObject.get("price");
        Label todaysPriceValue_label = new Label(todaysPriceValue.toString());
        todaysPriceValue_label.setFont(Font.font("Arial", FontWeight.BOLD ,14));
        todaysPriceValue_label.setStyle("-fx-text-fill: #3ebaae;");
        AnchorPane.setLeftAnchor(todaysPriceValue_label, 120.0);
        AnchorPane.setBottomAnchor(todaysPriceValue_label, 15.0);

        /******* "Views Daily prices" Button *********/
        Button button = new Button("View daily prices");
        button.setStyle("-fx-background-color: #6558f5; -fx-text-fill: #fff;");
        button.setFont(Font.font("Arial",  FontWeight.SEMI_BOLD,15.0));
        button.setCursor(Cursor.HAND);
        button.setId((String) productObject.get("asin"));
        button.setOnAction((event)->{
            final String ASIN =  (String) productObject.get("asin");

            Stage stage = new Stage();

            FluctuationStage fluctuationStage = new FluctuationStage(ASIN);
            try {
                fluctuationStage.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }


        });

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
                 vBox.getChildren().add(getProductsPane(j));
             }

         }
         catch (IOException | InterruptedException | ParseException e) {
         e.printStackTrace();
         }

    }

    /**
     *
     * returns: Json String
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

    /**
     * params JSONobject
     * e.g: {
     *       "asin": "B099S1MMJL",
     *       "name": "Master Labs Diamond Office Revolving Desk Chair with Umbrella Base with XW Handle (Black)",
     *  }
     *
     * returns JSONObject
     * e.g: {
     *      "asin": "B099S1MMJL",
     *      "name": "Master Labs Diamond Office Revolving Desk Chair with Umbrella Base with XW Handle (Black)",
     *      "price": 1700
     *      }
     * */
    private void getTodaysPrice(JSONObject product) throws IOException, InterruptedException {
        final String _URL = "https://amazonpricetracker3.herokuapp.com/getprice?url=https://www.amazon.in/dp/" + product.get("asin");
//        final String _URL = "http://127.0.0.1:8000/getprice?url=https://www.amazon.in/dp/" + product.get("asin");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(_URL))
                .timeout(Duration.ofMinutes(4))
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONParser parser = new JSONParser();
        JSONObject responseObject = null;
        try {
            System.out.println("response = " + response.body().toString());
            responseObject = (JSONObject) parser.parse(response.body().toString());
            product.put("price", responseObject.get("price"));
        } catch (ParseException e) {
            System.out.println("product = " + product.get("asin"));
            product.put("price", -1.0);
        }
        finally {
            productObjectsLists.add(product);
        }
    }


}

