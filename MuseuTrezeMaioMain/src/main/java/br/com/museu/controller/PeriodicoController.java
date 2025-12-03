package br.com.museu.controller;

import br.com.museu.dao.DoadorDAO;
import br.com.museu.dao.PeriodicoDAO;
import br.com.museu.model.Doador;
import br.com.museu.model.Periodico;
import br.com.museu.model.Usuario;
import br.com.museu.util.SessaoUsuario;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Optional;

public class PeriodicoController {

    @FXML private TextField txtTitulo;
    @FXML private TextField txtISSN;
    @FXML private ComboBox<String> cbPeriodicidade;
    @FXML private DatePicker dtAquisicao;
    @FXML private DatePicker dtInicio;

    @FXML private ComboBox<Doador> cbDoador;

    @FXML private TableView<Periodico> tabelaPeriodicos;
    @FXML private TableColumn<Periodico, Integer> colId;
    @FXML private TableColumn<Periodico, String> colTitulo;
    @FXML private TableColumn<Periodico, String> colISSN;
    @FXML private TableColumn<Periodico, String> colPeriodicidade;
    @FXML private TableColumn<Periodico, String> colDoador;

    @FXML private Button btnSalvar;
    @FXML private Button btnExcluir;

    private int idEmEdicao = 0;

    @FXML
    public void initialize() {
        cbPeriodicidade.setItems(FXCollections.observableArrayList("Diário", "Semanal", "Quinzenal", "Mensal", "Bimestral", "Anual"));
        dtAquisicao.setValue(LocalDate.now());

        carregarCombos();
        configurarColunas();
        carregarDados();
        configurarSelecaoTabela();

        Usuario u = SessaoUsuario.getInstancia().getUsuarioLogado();
        if (u != null && "Visitante".equalsIgnoreCase(u.getTipoUsuario())) {
            btnSalvar.setVisible(false);
            btnExcluir.setVisible(false);
            bloquearCampos();
        }
    }

    private void carregarCombos() {
        cbDoador.setItems(FXCollections.observableArrayList(new DoadorDAO().listarTodos()));
    }

    private void configurarColunas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idItemAcervo"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colISSN.setCellValueFactory(new PropertyValueFactory<>("issn"));
        colPeriodicidade.setCellValueFactory(new PropertyValueFactory<>("periodicidade"));
        colDoador.setCellValueFactory(new PropertyValueFactory<>("nomeDoador"));
    }

    private void carregarDados() {
        tabelaPeriodicos.setItems(FXCollections.observableArrayList(new PeriodicoDAO().listarTodos()));
    }

    @FXML
    public void onBtnSalvarClick() {
        if (txtTitulo.getText().isEmpty() || cbPeriodicidade.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Preencha Título e Periodicidade.");
            return;
        }
        try {
            Periodico p = new Periodico();
            p.setTitulo(txtTitulo.getText());
            p.setIssn(txtISSN.getText());
            p.setPeriodicidade(cbPeriodicidade.getValue());
            p.setDataAquisicao(dtAquisicao.getValue());
            p.setDataInicio(dtInicio.getValue());

            if (cbDoador.getValue() != null) {
                p.setIdDoador(cbDoador.getValue().getIdDoador());
            }

            PeriodicoDAO dao = new PeriodicoDAO();

            if (idEmEdicao == 0) {
                dao.cadastrar(p);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Salvo com sucesso!");
            } else {
                p.setIdItemAcervo(idEmEdicao);
                dao.atualizar(p);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Atualizado com sucesso!");
            }

            limparCampos();
            carregarDados();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro: " + e.getMessage());
        }
    }

    private void preencherFormulario(Periodico p) {
        idEmEdicao = p.getIdItemAcervo();
        btnSalvar.setText("Atualizar Periódico");

        txtTitulo.setText(p.getTitulo());
        txtISSN.setText(p.getIssn());
        cbPeriodicidade.setValue(p.getPeriodicidade());
        dtAquisicao.setValue(p.getDataAquisicao());
        dtInicio.setValue(p.getDataInicio());

        if (p.getIdDoador() > 0) {
            for(Doador d : cbDoador.getItems()) {
                if(d.getIdDoador() == p.getIdDoador()) { cbDoador.getSelectionModel().select(d); break; }
            }
        } else { cbDoador.getSelectionModel().clearSelection(); }
    }

    private void configurarSelecaoTabela() {
        tabelaPeriodicos.setRowFactory(tv -> {
            TableRow<Periodico> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) preencherFormulario(row.getItem());
            });
            return row;
        });
    }

    @FXML public void onBtnExcluirClick() { /* Igual ao anterior */
        Periodico p = tabelaPeriodicos.getSelectionModel().getSelectedItem();
        if(p == null) return;
        Optional<ButtonType> res = new Alert(Alert.AlertType.CONFIRMATION, "Excluir " + p.getTitulo() + "?").showAndWait();
        if(res.isPresent() && res.get() == ButtonType.OK) {
            try { new PeriodicoDAO().excluir(p.getIdItemAcervo()); carregarDados(); } catch(Exception e) {}
        }
    }

    @FXML public void onBtnVoltarClick() {
        try {
            Parent r = FXMLLoader.load(getClass().getResource("/br/com/museu/museutrezemaio/menu-view.fxml"));
            ((Stage) txtTitulo.getScene().getWindow()).setScene(new Scene(r));
        } catch (Exception e) {}
    }

    private void limparCampos() {
        idEmEdicao = 0; btnSalvar.setText("Salvar Periódico");
        txtTitulo.clear(); txtISSN.clear(); cbPeriodicidade.getSelectionModel().clearSelection();
        cbDoador.getSelectionModel().clearSelection();
        dtAquisicao.setValue(LocalDate.now()); dtInicio.setValue(null);
    }

    private void bloquearCampos() {
        txtTitulo.setDisable(true); txtISSN.setDisable(true); cbPeriodicidade.setDisable(true);
        cbDoador.setDisable(true); dtAquisicao.setDisable(true); dtInicio.setDisable(true);
    }

    private void mostrarAlerta(Alert.AlertType t, String m) { new Alert(t, m).showAndWait(); }
}