package app.controllers;
import app.model.*;
import javafx.scene.layout.ColumnConstraints;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.ul.fc.css.example.demo.dtos.ProjetoLeiDTO;
import javafx.scene.control.TableView;

import javafx.scene.control.TableRow;


public class ProjetosController {

	@FXML
	private TableView<ProjetoLeiDTO> tableViewProjetos;

	//	@FXML
	//	private ListView<ProjetoLeiDTO> listaProjetosNaoExpirados;

	@FXML
	private Button backBtn;

	private DataModel model;

	private Stage primaryStage;

	private ObservableList<ProjetoLeiDTO> listaProjetosNaoExpiradosObservable;
	
	private long userID;

	public void initModel(DataModel model, Stage primaryStage, long userID) {	
		if (this.model != null) {
			throw new IllegalStateException("Model can only be initialized once");
		}
		
		this.model = model;
		
		this.userID = userID;

		retrieveProjetosNaoExpirados();

		// Initialize the observable list for projetos
		listaProjetosNaoExpiradosObservable = FXCollections.observableArrayList();

		// Set the table view's items to the observable list
		tableViewProjetos.setItems(listaProjetosNaoExpiradosObservable);

		// Set the column resize policy to adjust column sizes based on content
		tableViewProjetos.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

		// Adjust column sizes based on content
		adjustColumnSizes();

		this.primaryStage = primaryStage;
		
		 // Add row click event handling
        tableViewProjetos.setRowFactory(tv -> {
            TableRow<ProjetoLeiDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                	ProjetoLeiDTO pl = row.getItem();
                    try {
						projectDetails(pl, userID);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            });
            return row;
        });
	}

	private void projectDetails(ProjetoLeiDTO pl, long userID) throws IOException {
		BorderPane root = new BorderPane();
      	FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/projeto_details.fxml"));
      	root.setCenter(loader.load());
      	ProjetoDetailsController projetoDetailsController = loader.getController();
        primaryStage.getScene().setRoot(root);
        DataModel model = new DataModel();
        projetoDetailsController.initModel(model, primaryStage, pl, userID);
	}

	// Method to retrieve the JSON response from the endpoint and update the list view
	private void retrieveProjetosNaoExpirados() {
		try {
			// Make an HTTP GET request to the API endpoint
			URL url = new URL("http://localhost:8080/api/consultarProjetos");
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
				List<ProjetoLeiDTO> votacoesEmCurso = objectMapper.readValue(response.toString(), new TypeReference<List<ProjetoLeiDTO>>() {});

				// Update the observable list with the retrieved data
				Platform.runLater(() -> {
					listaProjetosNaoExpiradosObservable.setAll(votacoesEmCurso);
				});
			} else {
				System.out.println("HTTP GET request failed with response code: " + responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void adjustColumnSizes() {
		tableViewProjetos.widthProperty().addListener((obs, oldWidth, newWidth) -> {
			// Calculate the available width for columns
			double availableWidth = newWidth.doubleValue();

			// Get the number of columns
			int numColumns = tableViewProjetos.getColumns().size();

			// Calculate the width to distribute evenly among columns
			double columnWidth = availableWidth / numColumns;

			// Set the column constraints to adjust column sizes
			for (TableColumn<ProjetoLeiDTO, ?> column : tableViewProjetos.getColumns()) {
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
}
