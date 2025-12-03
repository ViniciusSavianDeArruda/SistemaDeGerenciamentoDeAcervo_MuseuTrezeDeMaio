package br.com.museu.controller;

import br.com.museu.dao.AutorDAO;
import br.com.museu.model.Autor;
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

public class AutorController {

    @FXML private TextField txtNome;
    @FXML private TableView<Autor> tabelaAutores;
    @FXML private TableColumn<Autor, Integer> colId;
    @FXML private TableColumn<Autor, String> colNome;

    @FXML private Button btnSalvar;
    @FXML private Button btnExcluir;


    private int idEmEdicao = 0;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idAutor"));
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
        AutorDAO dao = new AutorDAO();
        ObservableList<Autor> lista = FXCollections.observableArrayList(dao.listarTodos());
        tabelaAutores.setItems(lista);
    }

    private void configurarSelecaoTabela() {
        tabelaAutores.setRowFactory(tv -> {
            TableRow<Autor> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    preencherFormulario(row.getItem());
                }
            });
            return row;
        });
    }

    private void preencherFormulario(Autor autor) {
        idEmEdicao = autor.getIdAutor();
        txtNome.setText(autor.getNome());
        btnSalvar.setText("Atualizar");
    }

    @FXML
    public void onBtnSalvarClick() {
        if (txtNome.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Nome obrigatório");
            return;
        }
        try {
            Autor autor = new Autor();
            autor.setNome(txtNome.getText());

            AutorDAO dao = new AutorDAO();

            if (idEmEdicao == 0) {

                dao.cadastrar(autor);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Autor salvo!");
            } else {

                autor.setIdAutor(idEmEdicao);
                dao.atualizar(autor);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Autor atualizado!");
            }

            limparCampos();
            carregarDados();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao salvar: " + e.getMessage());
        }
    }

    @FXML
    public void onBtnExcluirClick() {
        Autor selecionado = tabelaAutores.getSelectionModel().getSelectedItem();
        if (selecionado == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Excluir " + selecionado.getNome() + "?");
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                new AutorDAO().excluir(selecionado.getIdAutor());
                carregarDados();
            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro ao excluir (Pode estar vinculado a um livro).");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String msg) {
        Alert alert = new Alert(tipo);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}