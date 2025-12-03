package br.com.museu.controller;

import br.com.museu.dao.UsuarioDAO;
import br.com.museu.model.Usuario;
import br.com.museu.util.SessaoUsuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtSenha;
    @FXML private Label lblMensagem;

    @FXML
    public void onBtnEntrarClick() {
        String login = txtUsuario.getText();
        String senha = txtSenha.getText();

        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuarioLogado = dao.validarLogin(login, senha);

        if (usuarioLogado != null) {
            SessaoUsuario.getInstancia().setUsuarioLogado(usuarioLogado);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/com/museu/museutrezemaio/menu-view.fxml"));
                Parent root = loader.load();

                Scene scene = new Scene(root);
                Stage stage = (Stage) txtUsuario.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Museu Treze de Maio - Menu Principal");
                stage.centerOnScreen();
                stage.show();

            } catch (Exception e) {
                mostrarAlerta("Erro", "Erro ao abrir o menu: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }

        } else {
            mostrarAlerta("Erro", "Usuário ou senha inválidos!", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Sistema Museu");
        alert.setHeaderText(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}