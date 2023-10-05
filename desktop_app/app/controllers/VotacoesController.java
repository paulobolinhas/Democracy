package app.controllers;

import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.stage.Stage;

import pt.ul.fc.css.example.demo.dtos.VotacaoDTO;
import app.model.*;
import javafx.fxml.FXMLLoader;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.scene.control.TableRow;


public class VotacoesController {

	@FXML
	private TableView<VotacaoDTO> tableViewVotacoes;

	@FXML
	private Button backBtn;

	private DataModel model;

	private Stage primaryStage;
	
	private long userID;

	private ObservableList<VotacaoDTO> votacoesEmCursoList;

	public void initModel(DataModel model, Stage primaryStage, long userID) {	
		if (this.model != null) {
			throw new IllegalStateException("Model can only be initialized once");
		}

		this.model = model;
		
		this.userID = userID;

		retrieveVotacoesEmCurso();

		// Initialize the observable list for votações em curso
		votacoesEmCursoList = FXCollections.observableArrayList();

		// Set the table view's items to the observable list
		tableViewVotacoes.setItems(votacoesEmCursoList);

		// Set the column resize policy to adjust column sizes based on content
		tableViewVotacoes.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

		// Adjust column sizes based on content
		adjustColumnSizes();

		this.primaryStage = primaryStage;
		
		 // Add row double-click event handling
        tableViewVotacoes.setRowFactory(tv -> {
            TableRow<VotacaoDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    VotacaoDTO votacao = row.getItem();
                    try {
                        votacaoDetails(votacao, userID);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
	}

	// Method to retrieve the JSON response from the endpoint and update the list view
	private void retrieveVotacoesEmCurso() {
		try {
			// Make an HTTP GET request to the API endpoint
			URL url = new URL("http://localhost:8080/api/votacoesCurso");
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

				// Parse the JSON response into VotacaoDTO objects
				ObjectMapper objectMapper = new ObjectMapper();
				List<VotacaoDTO> votacoesEmCurso = objectMapper.readValue(response.toString(), new TypeReference<List<VotacaoDTO>>() {});

				// Update the observable list with the retrieved data
				Platform.runLater(() -> {
					votacoesEmCursoList.setAll(votacoesEmCurso);
				});
			} else {
				System.out.println("HTTP GET request failed with response code: " + responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void votacaoDetails(VotacaoDTO votacao, long userID) throws IOException {
        BorderPane root = new BorderPane();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/votacao_details.fxml"));
        root.setCenter(loader.load());
        VotacaoDetailsController votacaoDetailsController = loader.getController();
        primaryStage.getScene().setRoot(root);
        DataModel model = new DataModel();
        votacaoDetailsController.initModel(model, primaryStage, votacao, userID);
    }


	@FXML
	public void goBack() throws Exception {
		BorderPane root = new BorderPane();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/mainMenu.fxml"));
		root.setCenter(loader.load());
		MainMenuController mainMenuController = loader.getController();
		DataModel model = new DataModel();
		mainMenuController.initModel(model, primaryStage, userID);	
		primaryStage.getScene().setRoot(root);

	}


	private void adjustColumnSizes() {
		tableViewVotacoes.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			// Calculate the available width for columns
			double availableWidth = newWidth.doubleValue();

			// Get the number of columns
			int numColumns = tableViewVotacoes.getColumns().size();

			// Calculate the width to distribute evenly among columns
			double columnWidth = availableWidth / numColumns;

			// Set the column constraints to adjust column sizes
			for (TableColumn<VotacaoDTO, ?> column : tableViewVotacoes.getColumns()) {
				ColumnConstraints constraints = new ColumnConstraints(columnWidth);
				column.setPrefWidth(columnWidth);
				column.setMinWidth(0);
				column.setMaxWidth(Double.MAX_VALUE);
				column.setResizable(true);
				column.setStyle("-fx-alignment: CENTER;");
				constraints.setFillWidth(true);
				column.setUserData(constraints);
			}
		});
	}
}
