package app.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Optional;


import org.springframework.stereotype.Controller;


import com.fasterxml.jackson.databind.ObjectMapper;

import app.model.DataModel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.ul.fc.css.example.demo.dtos.CidadaoDTO;



@Controller
public class LoginController {
	
    @FXML
    private TextField idField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Label errorLabel;
    
    private DataModel model;
    
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initModel(DataModel model, Stage primaryStage) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        
        this.primaryStage = primaryStage;
    }

    @FXML
    private void login() throws Exception {
        String userID = idField.getText();
        long userIdLong=-1;
        try {
            userIdLong = Long.parseLong(userID);
        } catch (NumberFormatException e) {
            showError("Utilizador não encontrado. Tente novamente");
        }
        
        Optional<CidadaoDTO> cidadao = getCidadaoByID(userIdLong);
        
        if (cidadao.isPresent()) {
        	signIn(userIdLong);
        } else {
        	 showError("Utilizador não encontrado. Tente novamente");
        }
    }
    
 
    private Optional<CidadaoDTO> getCidadaoByID(Long userID) {
        try {
            // Make an HTTP GET request to the API endpoint
            URL url = new URL("http://localhost:8080/api/cidadao/" + userID);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // Check the response code
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response from the API
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON response into CidadaoDTO object
                ObjectMapper objectMapper = new ObjectMapper();
                CidadaoDTO cidadaoDTO = objectMapper.readValue(response.toString(), CidadaoDTO.class);

                return Optional.ofNullable(cidadaoDTO);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @FXML
  	public void signIn(long userID) throws Exception {
    	BorderPane root = new BorderPane();
      	FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/mainMenu.fxml"));
      	root.setCenter(loader.load());
      	MainMenuController  mainMenuController = loader.getController();
        primaryStage.getScene().setRoot(root);
        DataModel model = new DataModel();
        mainMenuController.initModel(model, primaryStage, userID);
  		
  	}
    
    
    private void showError(String message) {
    	errorLabel.setText(message);
        errorLabel.setVisible(true);
        idField.clear();
    }


}
