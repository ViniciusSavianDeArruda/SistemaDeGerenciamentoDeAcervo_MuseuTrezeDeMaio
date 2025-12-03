package br.com.museu.controller;

import br.com.museu.dao.SerieDAO;
import br.com.museu.model.Serie;
import br.com.museu.model.Usuario;
import br.com.museu.util.SessaoUsuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class SerieController {

    @FXML private TextField txtNome;
    @FXML private TableView<Serie> tabelaSeries;
    @FXML private TableColumn<Serie, Integer> colId;
    @FXML private TableColumn<Serie, String> colNome;

    @FXML private Button btnSalvar;
    @FXML private Button btnExcluir;

    private int idEmEdicao = 0;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idSerie"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        carregarDados();
        configurarSelecaoTabela();

        Usuario u = SessaoUsuario.getInstancia().getUsuarioLogado();
        if (u != null && "Visitante".equalsIgnoreCase(u.getTipoUsuario())) {
            btnSalvar.setVisible(false);
            btnExcluir.setVisible(false);
            txtNome.setDisable(true);
        }
    }

    private void carregarDados() {
        SerieDAO dao = new SerieDAO();
        ObservableList<Serie> lista = FXCollections.observableArrayList(dao.listarTodas());
        tabelaSeries.setItems(lista);
    }

    private void configurarSelecaoTabela() {
        tabelaSeries.setRowFactory(tv -> {
            TableRow<Serie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    preencherFormulario(row.getItem());
                }
            });
            return row;
        });
    }

    private void preencherFormulario(Serie serie) {
        idEmEdicao = serie.getIdSerie();
        txtNome.setText(serie.getNome());
        btnSalvar.setText("Atualizar");
    }

    @FXML
    public void onBtnSalvarClick() {
        if (txtNome.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Nome da Série é obrigatório");
            return;
        }
        try {
            Serie serie = new Serie();
            serie.setNome(txtNome.getText());

            SerieDAO dao = new SerieDAO();

            if (idEmEdicao == 0) {
                dao.cadastrar(serie);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Série cadastrada!");
            } else {
                serie.setIdSerie(idEmEdicao);
                dao.atualizar(serie);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Série atualizada!");
            }

            limparCampos();
            carregarDados();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro: " + e.getMessage());
        }
    }

    @FXML
    public void onBtnExcluirClick() {
        Serie selecionada = tabelaSeries.getSelectionModel().getSelectedItem();
        if (selecionada == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Excluir '" + selecionada.getNome() + "'?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                new SerieDAO().excluir(selecionada.getIdSerie());
                carregarDados();
            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro ao excluir (Pode estar vinculada a um livro).");
            }
        }
    }

    private void limparCampos() {
        idEmEdicao = 0;
        btnSalvar.setText("Adicionar");
        txtNome.clear();
    }

    @FXML
    public void onBtnVoltarClick(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/br/com/museu/museutrezemaio/menu-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String msg) {
        new Alert(tipo, msg).showAndWait();
    }
}