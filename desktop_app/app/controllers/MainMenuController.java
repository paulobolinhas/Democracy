package app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import app.model.*;

public class MainMenuController {
	@FXML
    private Button votacoesBtn;
	
	@FXML
    private Button projetosBtn;

    private DataModel model;
    
    private Stage primaryStage;
    
    private long userID;

    public void initModel(DataModel model, Stage primaryStage, long userID) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        
        this.primaryStage = primaryStage;
        this.userID = userID;
    }
    
    @FXML
  	public void listarProjetos() throws Exception {
    	BorderPane root = new BorderPane();
      	FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/projetos_list.fxml"));
      	root.setCenter(loader.load());
        ProjetosController projetosController = loader.getController();
        primaryStage.getScene().setRoot(root);
        DataModel model = new DataModel();
        projetosController.initModel(model, primaryStage, userID);
  		
  	}
    
    @FXML
  	public void listarVotacoes() throws Exception {
      	BorderPane root = new BorderPane();
      	FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/votacoes_list.fxml"));
      	root.setCenter(loader.load());
      	VotacoesController votacoesController = loader.getController();
        primaryStage.getScene().setRoot(root);
        DataModel model = new DataModel();
        votacoesController.initModel(model, primaryStage, userID);
  		
  	}
    
    @FXML
	public void logout() throws Exception {
		BorderPane root = new BorderPane();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/login.fxml"));
		root.setCenter(loader.load());
		LoginController loginController = loader.getController();
		loginController.initModel(model, primaryStage);
		primaryStage.getScene().setRoot(root);
	}
}

