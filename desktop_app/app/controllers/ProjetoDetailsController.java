package app.controllers;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import pt.ul.fc.css.example.demo.dtos.ProjetoLeiDTO;
import app.model.*;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

import java.net.URL;
import java.net.HttpURLConnection;


public class ProjetoDetailsController {

	@FXML
	private Label idLabel;

	@FXML
	private Label tituloLabel;

	@FXML
	private Label descricaoLabel;

	@FXML
	private Label dataHoraValidadeLabel;

	@FXML
	private Label subtemaLabel;

	@FXML
	private Label delegadoProponenteLabel;

	@FXML
	private Label numeroApoiantesLabel;

	@FXML
	private Button backBtn;

	private DataModel model;

	private Stage primaryStage;

	private long userID;

	private ProjetoLeiDTO projetoLei;

	public void initModel(DataModel model, Stage primaryStage, ProjetoLeiDTO projetoLei, long userID) {
		if (this.model != null) {
			throw new IllegalStateException("Model can only be initialized once");
		}

		this.model = model;
		this.primaryStage = primaryStage;
		this.projetoLei = projetoLei;
		this.userID = userID;

		// Set the values of the labels based on the ProjetoLeiDTO
		idLabel.setText(String.valueOf(projetoLei.getId()));
		tituloLabel.setText(projetoLei.getTitulo());
		descricaoLabel.setText(projetoLei.getDescricao());

		Timestamp dataHoraValidade = projetoLei.getDatahoraValidade();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDataHoraValidade = dateFormat.format(dataHoraValidade);
		dataHoraValidadeLabel.setText(formattedDataHoraValidade);

		subtemaLabel.setText(projetoLei.getSubtema().getTitulo());
		delegadoProponenteLabel.setText(projetoLei.getDelegadoProponente().getNome());
		numeroApoiantesLabel.setText(String.valueOf(projetoLei.getNumeroApoiantes()));
	}

	@FXML
	public void apoiarProjeto() {

		long proj_id = projetoLei.getId();

		try {
			// Create the URL for the API endpoint
			URL url = new URL("http://localhost:8080/api/apoiarProjetos/" + userID + "/" + proj_id);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			// Check the response code
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				goBack();
			} else {
				System.out.println("URL: " + url);
			}

			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@FXML
	public void goBack() throws Exception {
		BorderPane root = new BorderPane();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/projetos_list.fxml"));
		root.setCenter(loader.load());
		ProjetosController projetosController = loader.getController();
		projetosController.initModel(model, primaryStage, userID);
		primaryStage.getScene().setRoot(root);
	}
}
