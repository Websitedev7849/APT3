package utils;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class FluctuationStage extends Application {
    JSONObject productObject;
    public static final int SCENE_WIDTH = 500;
    public static final int SCENE_HEIGHT = 700;
    public static final String _URL = "https://amazonpricetracker3.herokuapp.com/fluctuations";


    /**
     * @params JSONobject
     * e.g: {
     *       "asin": "B099S1MMJL",
     *       "name": "Master Labs Diamond Office Revolving Desk Chair with Umbrella Base with XW Handle (Black)",
     *       "price": 1700
     *  },
     * */
    FluctuationStage(JSONObject productObject){
        this.productObject = productObject;
    }

    @Override
    public void start(Stage stage) throws Exception {


        ScrollPane scrollPane = new ScrollPane();
        VBox vBox = new VBox();
        vBox.setPrefWidth(SCENE_WIDTH);
        vBox.setPrefHeight(SCENE_HEIGHT);
        vBox.setSpacing(30);
        vBox.setPadding(new Insets(30, 0, 30, 80));
        vBox.setMinWidth(SCENE_WIDTH);


        AnchorPane productInfoPane = new AnchorPane();
        productInfoPane.setMinWidth(SCENE_WIDTH);
        productInfoPane.setMinHeight(100);
        productInfoPane.setMinWidth(SCENE_WIDTH);
//        productInfoPane.setStyle("-fx-background-color: red");

        /********** Product Name Label *************/
        String nameOfProduct = (String) this.productObject.get("name");
        Label productName_label = new Label(nameOfProduct.substring(0, Math.min(nameOfProduct.length(), 40)).toUpperCase());
        productName_label.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 19));
        productName_label.setWrapText(true);
        productName_label.setMaxWidth(300);
        AnchorPane.setLeftAnchor(productName_label, 10.0);

        /********** TODAY'S PRICE LABEL *************/
        Label todaysPrice_label = new Label("Today's Price:");
        todaysPrice_label.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 17));
        AnchorPane.setLeftAnchor(todaysPrice_label, 10.0);
        AnchorPane.setBottomAnchor(todaysPrice_label, 14.0);

        /********** TODAY'S PRICE VALUE LABEL *************/
        Double price = (Double) productObject.get("price");
        Label todaysPriceValue_label = new Label(price.toString());
        todaysPriceValue_label.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 17));
        todaysPriceValue_label.setStyle("-fx-text-fill: green");
        AnchorPane.setLeftAnchor(todaysPriceValue_label, 140.0);
        AnchorPane.setBottomAnchor(todaysPriceValue_label, 14.0);

        productInfoPane.getChildren().add(productName_label);
        productInfoPane.getChildren().add(todaysPrice_label);
        productInfoPane.getChildren().add(todaysPriceValue_label);

        /********** FLUCTUATIONS PANE *************/
        ScrollPane fluctuationsPane = new ScrollPane();
        Pane datePane = new Pane();
        Label dateLabel = new Label("Date");
        dateLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        dateLabel.setAlignment(Pos.CENTER);
        dateLabel.setPadding(new Insets(8));
        datePane.getChildren().add(dateLabel);
        datePane.setStyle("-fx-background-color: #96c3ec; -fx-border-color: black");
        datePane.setMinWidth(200);

        Pane pricePane = new Pane();
        Label priceLabel = new Label("Price");
        priceLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        priceLabel.setPadding(new Insets(8));
        priceLabel.setAlignment(Pos.CENTER);
        pricePane.getChildren().add(priceLabel);
        pricePane.setStyle("-fx-background-color: #96c3ec; -fx-border-color: black");
        pricePane.setLayoutX(200);
        pricePane.setMinWidth(200);

        VBox vBoxForFluctuationPane = new VBox();

        HBox hBox = new HBox();
        hBox.getChildren().addAll(datePane, pricePane);
        vBoxForFluctuationPane.getChildren().add(hBox);
//        fluctuationsPane.setStyle("-fx-background-color: red");


        /********** GETTING AND APPENDING FLUCTUATIONS *************/
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(_URL + "?asin=" + this.productObject.get("asin")))
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200){
            JSONParser parser = new JSONParser();
            JSONObject responseObject = (JSONObject) parser.parse(response.body());

            JSONArray fluctuationsArray = (JSONArray) responseObject.get("fluctuations");


            for (int i = 0; i < fluctuationsArray.size(); i++) {
                JSONObject fluctuation = (JSONObject) fluctuationsArray.get(i);
                System.out.println("fluctuation = " + fluctuation);
                Pane dateValuePane = new Pane();
                Label dateValueLabel = new Label((String) fluctuation.get("date"));
                dateValueLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
                dateValueLabel.setPadding(new Insets(8));
                dateValueLabel.setAlignment(Pos.CENTER);
                dateValuePane.getChildren().add(dateValueLabel);
                dateValuePane.setStyle("-fx-border-color: black");
                dateValuePane.setMinWidth(200);

                Pane priceValuePane = new Pane();
                Label priceValueLabel = new Label( ((Double) fluctuation.get("price")).toString() );
                priceValueLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
                priceValueLabel.setPadding(new Insets(8));
                priceValueLabel.setAlignment(Pos.CENTER);
                priceValuePane.getChildren().add(priceValueLabel);
                priceValuePane.setStyle("-fx-border-color: black");
                priceValuePane.setLayoutX(200);
                priceValuePane.setMinWidth(200);

                HBox hBox1 = new HBox();
                hBox1.getChildren().addAll(dateValuePane, priceValuePane);
                vBoxForFluctuationPane.getChildren().add(hBox1);
            }

            fluctuationsPane.setContent(vBoxForFluctuationPane);


        }
        else if (response.statusCode() == 404){
            System.out.println("Not Found");
        }


        vBox.getChildren().add(productInfoPane);
        vBox.getChildren().add(fluctuationsPane);


        scrollPane.setContent(vBox);
        Scene scene = new Scene(scrollPane, SCENE_HEIGHT, SCENE_WIDTH);
        stage.setScene(scene);
        stage.setTitle("First JavaFX Application");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
