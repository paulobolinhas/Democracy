package app;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import app.controllers.LoginController;
import app.model.*;


public class Democracia2 extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
    	
    	primaryStage.setTitle("Democracia2");

        BorderPane root = new BorderPane();
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("fxml/login.fxml"));
        root.setCenter(loginLoader.load());
        LoginController loginController = loginLoader.getController();

        DataModel model = new DataModel();
        loginController.initModel(model, primaryStage);	

        Scene scene = new Scene(root, 1600, 800);
		
		primaryStage.setMinHeight(600);
		primaryStage.setMinWidth(1200);
        
		primaryStage.setScene(scene);
		primaryStage.show();
    }
    
    public static void main(String[] args) { launch(args); }
}

