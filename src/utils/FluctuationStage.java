package utils;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.simple.JSONObject;


public class FluctuationStage extends Application {
    JSONObject productObject;
    public static final int SCENE_WIDTH = 500;
    public static final int SCENE_HEIGHT = 700;
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

        vBox.getChildren().add(productInfoPane);

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
