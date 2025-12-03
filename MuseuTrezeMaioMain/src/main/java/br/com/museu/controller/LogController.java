package br.com.museu.controller;

import br.com.museu.dao.LogDAO;
import br.com.museu.model.LogOperacao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class LogController {

    @FXML private TableView<LogOperacao> tabelaLogs;
    @FXML private TableColumn<LogOperacao, String> colData;
    @FXML private TableColumn<LogOperacao, String> colUsuario;
    @FXML private TableColumn<LogOperacao, String> colOperacao;
    @FXML private TableColumn<LogOperacao, Integer> colItem;

    @FXML
    public void initialize() {
        colData.setCellValueFactory(new PropertyValueFactory<>("dataFormatada"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("nomeUsuario"));
        colOperacao.setCellValueFactory(new PropertyValueFactory<>("operacao"));
        colItem.setCellValueFactory(new PropertyValueFactory<>("idItemAcervo"));

        carregarLogs();
    }

    private void carregarLogs() {
        LogDAO dao = new LogDAO();
        ObservableList<LogOperacao> lista = FXCollections.observableArrayList(dao.listarTodos());
        tabelaLogs.setItems(lista);
    }

    @FXML
    public void onBtnVoltarClick(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/br/com/museu/museutrezemaio/menu-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onBtnAtualizarClick() {
        carregarLogs();
    }
}