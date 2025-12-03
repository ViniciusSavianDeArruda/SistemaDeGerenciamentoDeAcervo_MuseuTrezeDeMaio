package br.com.museu.controller;

import br.com.museu.dao.DoadorDAO;
import br.com.museu.model.Doador;
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

public class DoadorController {

    @FXML private TextField txtNome;
    @FXML private TableView<Doador> tabelaDoadores;
    @FXML private TableColumn<Doador, Integer> colId;
    @FXML private TableColumn<Doador, String> colNome;

    @FXML private Button btnSalvar;
    @FXML private Button btnExcluir;

    private int idEmEdicao = 0;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idDoador"));
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
        DoadorDAO dao = new DoadorDAO();
        ObservableList<Doador> lista = FXCollections.observableArrayList(dao.listarTodos());
        tabelaDoadores.setItems(lista);
    }

    private void configurarSelecaoTabela() {
        tabelaDoadores.setRowFactory(tv -> {
            TableRow<Doador> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    preencherFormulario(row.getItem());
                }
            });
            return row;
        });
    }

    private void preencherFormulario(Doador d) {
        idEmEdicao = d.getIdDoador();
        txtNome.setText(d.getNome());
        btnSalvar.setText("Atualizar");
    }

    @FXML
    public void onBtnSalvarClick() {
        if (txtNome.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Nome do Doador é obrigatório");
            return;
        }
        try {
            Doador d = new Doador();
            d.setNome(txtNome.getText());
            DoadorDAO dao = new DoadorDAO();

            if (idEmEdicao == 0) {
                dao.cadastrar(d);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Doador cadastrado!");
            } else {
                d.setIdDoador(idEmEdicao);
                dao.atualizar(d);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Doador atualizado!");
            }
            limparCampos();
            carregarDados();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro: " + e.getMessage());
        }
    }

    @FXML
    public void onBtnExcluirClick() {
        Doador selecionado = tabelaDoadores.getSelectionModel().getSelectedItem();
        if (selecionado == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Excluir '" + selecionado.getNome() + "'?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                new DoadorDAO().excluir(selecionado.getIdDoador());
                carregarDados();
            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro ao excluir (Pode ter doado itens).");
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