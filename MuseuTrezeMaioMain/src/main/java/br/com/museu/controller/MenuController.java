package br.com.museu.controller;

import br.com.museu.util.SessaoUsuario;
import br.com.museu.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuController {

    @FXML private Button btnLogs;

    @FXML
    public void initialize() {
        Usuario u = SessaoUsuario.getInstancia().getUsuarioLogado();

        if (u != null && "Visitante".equalsIgnoreCase(u.getTipoUsuario())) {
            btnLogs.setVisible(false);
            btnLogs.setManaged(false);
        }
    }

    @FXML
    public void onBtnItensHistoricosClick(ActionEvent event) {
        navegar(event, "/br/com/museu/museutrezemaio/item-historico-view.fxml", "Acervo Histórico");
    }

    @FXML
    public void onBtnBibliograficosClick(ActionEvent event) {
        navegar(event, "/br/com/museu/museutrezemaio/biblioteca-view.fxml", "Biblioteca");
    }

    @FXML
    public void onBtnPeriodicosClick(ActionEvent event) {
        navegar(event, "/br/com/museu/museutrezemaio/periodico-view.fxml", "Periódicos");
    }

    @FXML
    public void onBtnAutoresClick(ActionEvent event) {
        navegar(event, "/br/com/museu/museutrezemaio/autor-view.fxml", "Autores");
    }

    @FXML
    public void onBtnAssuntosClick(ActionEvent event) {
        navegar(event, "/br/com/museu/museutrezemaio/assunto-view.fxml", "Gerenciamento de Assuntos");
    }

    @FXML
    public void onBtnSeriesClick(ActionEvent event) {
        navegar(event, "/br/com/museu/museutrezemaio/serie-view.fxml", "Gerenciamento de Series");
    }

    @FXML
    public void onBtnLogsClick(ActionEvent event) {
        navegar(event, "/br/com/museu/museutrezemaio/log-view.fxml", "Logs de Auditoria");
    }

    @FXML
    public void onBtnDoadoresClick(ActionEvent event) {
        navegar(event, "/br/com/museu/museutrezemaio/doador-view.fxml", "Gerenciamento de Doadores");
    }



    @FXML
    public void onBtnSairClick(ActionEvent event) {
        System.exit(0);
    }

    private void navegar(ActionEvent event, String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Museu Treze de Maio - " + titulo);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAviso("Erro", "Não foi possível abrir a tela: " + e.getMessage());
        }
    }

    private void mostrarAviso(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}