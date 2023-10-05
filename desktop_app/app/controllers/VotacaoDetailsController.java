package app.controllers;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import pt.ul.fc.css.example.demo.dtos.VotacaoDTO;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import pt.ul.fc.css.example.demo.enums.VOTO_DELEGADO;
import app.model.*;

import java.net.URL;
import java.net.HttpURLConnection;

public class VotacaoDetailsController {

    @FXML
    private Label idLabel;

    @FXML
    private Label tituloLabel;

    @FXML
    private Label descricaoLabel;

    @FXML
    private Label dataHoraInicioLabel;

    @FXML
    private Label dataHoraFimLabel;

    @FXML
    private Label votosFavoraveisLabel;

    @FXML
    private Label votosDesfavoraveisLabel;

    @FXML
    private Button backBtn;

    private DataModel model;

    private Stage primaryStage;

    private long userID;

    private VotacaoDTO votacao;

    public void initModel(DataModel model, Stage primaryStage, VotacaoDTO votacao, long userID) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }

        this.model = model;
        this.primaryStage = primaryStage;
        this.votacao = votacao;
        this.userID = userID;

        // Set the values of the labels based on the VotacaoDTO
        idLabel.setText(String.valueOf(votacao.getId()));
        tituloLabel.setText(votacao.getProjetoLeiVotacao().getTitulo());
        descricaoLabel.setText(votacao.getProjetoLeiVotacao().getDescricao());

        Timestamp dataHoraInicio = votacao.getDataInicio();
        Timestamp dataHoraFim = votacao.getDataEncerramento();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDataHoraInicio = dateFormat.format(dataHoraInicio);
        String formattedDataHoraFim = dateFormat.format(dataHoraFim);
        dataHoraInicioLabel.setText(formattedDataHoraInicio);
        dataHoraFimLabel.setText(formattedDataHoraFim);

        votosFavoraveisLabel.setText(String.valueOf(votacao.getVotosFavoraveis()));
        votosDesfavoraveisLabel.setText(String.valueOf(votacao.getVotosDesfavoraveis()));
    }

    @FXML
    public void votarFav() throws Exception {
    	long votacao_id = votacao.getId();
    	
        VOTO_DELEGADO voto_delegado = VOTO_DELEGADO.FAVORAVEL;
        
        try {
            // Create the URL for the API endpoint
            URL url = new URL("http://localhost:8080/api/votarProposta/" + votacao_id + "/" + userID + "/" + voto_delegado);
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
    public void votarDesfav() throws Exception {
    	long votacao_id = votacao.getId();
    	
        VOTO_DELEGADO voto_delegado = VOTO_DELEGADO.DESFAVORAVEL;
        
        try {
            // Create the URL for the API endpoint
            URL url = new URL("http://localhost:8080/api/votarProposta/" + votacao_id + "/" + userID + "/" + voto_delegado);
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/votacoes_list.fxml"));
        root.setCenter(loader.load());
        VotacoesController votacoesController = loader.getController();
        votacoesController.initModel(model, primaryStage, userID);
        primaryStage.getScene().setRoot(root);
    }
}
