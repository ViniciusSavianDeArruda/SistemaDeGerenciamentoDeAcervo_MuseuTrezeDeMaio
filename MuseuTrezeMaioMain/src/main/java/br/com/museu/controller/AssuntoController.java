package br.com.museu.controller;

import br.com.museu.dao.AssuntoDAO;
import br.com.museu.model.Assunto;
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

public class AssuntoController {

    @FXML private TextField txtDescricao;
    @FXML private TableView<Assunto> tabelaAssuntos;
    @FXML private TableColumn<Assunto, Integer> colId;
    @FXML private TableColumn<Assunto, String> colDescricao;

    @FXML private Button btnSalvar;
    @FXML private Button btnExcluir;

    private int idEmEdicao = 0;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idAssunto"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        carregarDados();
        configurarSelecaoTabela();

        Usuario u = SessaoUsuario.getInstancia().getUsuarioLogado();
        if (u != null && "Visitante".equalsIgnoreCase(u.getTipoUsuario())) {
            btnSalvar.setVisible(false);
            btnExcluir.setVisible(false);
            txtDescricao.setDisable(true);
        }
    }

    private void carregarDados() {
        AssuntoDAO dao = new AssuntoDAO();
        ObservableList<Assunto> lista = FXCollections.observableArrayList(dao.listarTodos());
        tabelaAssuntos.setItems(lista);
    }

    private void configuringSelecaoTabela() {
        tabelaAssuntos.setRowFactory(tv -> {
            TableRow<Assunto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    preencherFormulario(row.getItem());
                }
            });
            return row;
        });
    }

    private void configurarSelecaoTabela() {
        configuringSelecaoTabela();
    }

    private void preencherFormulario(Assunto assunto) {
        idEmEdicao = assunto.getIdAssunto();
        txtDescricao.setText(assunto.getDescricao());
        btnSalvar.setText("Atualizar");
    }

    @FXML
    public void onBtnSalvarClick() {
        if (txtDescricao.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Descrição obrigatória");
            return;
        }
        try {
            Assunto assunto = new Assunto();
            assunto.setDescricao(txtDescricao.getText());

            AssuntoDAO dao = new AssuntoDAO();

            if (idEmEdicao == 0) {
                dao.cadastrar(assunto);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Assunto salvo!");
            } else {
                assunto.setIdAssunto(idEmEdicao);
                dao.atualizar(assunto);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Assunto atualizado!");
            }

            limparCampos();
            carregarDados();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro: " + e.getMessage());
        }
    }

    @FXML
    public void onBtnExcluirClick() {
        Assunto selecionado = tabelaAssuntos.getSelectionModel().getSelectedItem();
        if (selecionado == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Excluir " + selecionado.getDescricao() + "?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                new AssuntoDAO().excluir(selecionado.getIdAssunto());
                carregarDados();
            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro ao excluir (Pode estar em uso).");
            }
        }
    }

    private void limparCampos() {
        idEmEdicao = 0;
        btnSalvar.setText("Adicionar");
        txtDescricao.clear();
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