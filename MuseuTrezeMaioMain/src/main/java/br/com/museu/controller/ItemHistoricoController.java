package br.com.museu.controller;

import br.com.museu.dao.DoadorDAO;
import br.com.museu.dao.ItemHistoricoDAO;
import br.com.museu.model.Doador;
import br.com.museu.model.ItemHistorico;
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

public class ItemHistoricoController {

    @FXML private Button btnSalvar;
    @FXML private Button btnExcluir;

    @FXML private TextField txtTitulo;
    @FXML private DatePicker dtAquisicao;
    @FXML private DatePicker dtCriacao;
    @FXML private TextField txtMaterial;
    @FXML private TextField txtDimensoes;
    @FXML private TextField txtLocal;
    @FXML private TextArea txtDescricao;

    @FXML private ComboBox<Doador> cbDoador;

    @FXML private TableView<ItemHistorico> tabelaItens;
    @FXML private TableColumn<ItemHistorico, Integer> colId;
    @FXML private TableColumn<ItemHistorico, String> colTitulo;
    @FXML private TableColumn<ItemHistorico, String> colMaterial;
    @FXML private TableColumn<ItemHistorico, String> colLocal;
    @FXML private TableColumn<ItemHistorico, String> colDoador;

    private int idEmEdicao = 0;

    @FXML
    public void initialize() {
        configurarColunas();
        carregarCombos();
        carregarDados();
        configurarSelecaoTabela();
        dtAquisicao.setValue(LocalDate.now());

        Usuario u = SessaoUsuario.getInstancia().getUsuarioLogado();
        if (u != null && "Visitante".equalsIgnoreCase(u.getTipoUsuario())) {
            btnSalvar.setVisible(false);
            btnExcluir.setVisible(false);
            cbDoador.setDisable(true);
            txtTitulo.setDisable(true);
            txtMaterial.setDisable(true);
            txtDimensoes.setDisable(true);
            txtLocal.setDisable(true);
            txtDescricao.setDisable(true);
            dtAquisicao.setDisable(true);
            dtCriacao.setDisable(true);
            bloquearCampos();
        }
    }

    private void carregarCombos() {
        DoadorDAO dao = new DoadorDAO();
        cbDoador.setItems(FXCollections.observableArrayList(dao.listarTodos()));
    }

    private void configuringColunas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idItemAcervo"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colMaterial.setCellValueFactory(new PropertyValueFactory<>("material"));
        colLocal.setCellValueFactory(new PropertyValueFactory<>("localArmazenado"));
        colDoador.setCellValueFactory(new PropertyValueFactory<>("nomeDoador"));
    }

    private void configurarColunas() { configuringColunas(); }

    private void carregarDados() {
        tabelaItens.setItems(FXCollections.observableArrayList(new ItemHistoricoDAO().listarTodos()));
    }

    @FXML
    public void onBtnSalvarClick() {
        if (txtTitulo.getText().isEmpty() || txtLocal.getText().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Título e Local são obrigatórios.");
            return;
        }
        try {
            ItemHistorico item = new ItemHistorico();
            item.setTitulo(txtTitulo.getText());
            item.setDataAquisicao(dtAquisicao.getValue());
            item.setDataCriacao(dtCriacao.getValue());
            item.setMaterial(txtMaterial.getText());
            item.setDimensoes(txtDimensoes.getText());
            item.setLocalArmazenado(txtLocal.getText());
            item.setDescricao(txtDescricao.getText());

            if (cbDoador.getValue() != null) {
                item.setIdDoador(cbDoador.getValue().getIdDoador());
            }

            ItemHistoricoDAO dao = new ItemHistoricoDAO();

            if (idEmEdicao == 0) {
                dao.cadastrar(item);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Item salvo!");
            } else {
                item.setIdItemAcervo(idEmEdicao);
                dao.atualizar(item);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Item atualizado!");
            }
            limparCampos();
            carregarDados();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro: " + e.getMessage());
        }
    }

    private void preencherFormulario(ItemHistorico item) {
        idEmEdicao = item.getIdItemAcervo();
        btnSalvar.setText("Atualizar");
        txtTitulo.setText(item.getTitulo());
        txtMaterial.setText(item.getMaterial());
        txtDimensoes.setText(item.getDimensoes());
        txtLocal.setText(item.getLocalArmazenado());
        txtDescricao.setText(item.getDescricao());
        dtAquisicao.setValue(item.getDataAquisicao());
        dtCriacao.setValue(item.getDataCriacao());

        if (item.getIdDoador() > 0) {
            for (Doador d : cbDoador.getItems()) {
                if (d.getIdDoador() == item.getIdDoador()) {
                    cbDoador.getSelectionModel().select(d);
                    break;
                }
            }
        } else {
            cbDoador.getSelectionModel().clearSelection();
        }
    }

    private void configurarSelecaoTabela() {
        tabelaItens.setRowFactory(tv -> {
            TableRow<ItemHistorico> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) preencherFormulario(row.getItem());
            });
            return row;
        });
    }

    @FXML
    public void onBtnExcluirClick() {
        ItemHistorico itemSelecionado = tabelaItens.getSelectionModel().getSelectedItem();

        // 1. Validação: Verifica se tem algo selecionado
        if (itemSelecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione um item histórico na tabela para excluir.");
            return;
        }

        // 2. Confirmação
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText(null);
        alert.setContentText("Tem certeza que deseja excluir: " + itemSelecionado.getTitulo() + "?");

        if (alert.showAndWait().get() != ButtonType.OK) {
            return;
        }

        // 3. Execução no Banco
        try {
            new ItemHistoricoDAO().excluir(itemSelecionado.getIdItemAcervo());

            // Atualiza a tela
            carregarDados();
            limparCampos();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Item excluído com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Não foi possível excluir.\nErro: " + e.getMessage());
        }
    }
    @FXML public void onBtnVoltarClick() {
        try {
            Parent r = FXMLLoader.load(getClass().getResource("/br/com/museu/museutrezemaio/menu-view.fxml"));
            ((Stage)txtTitulo.getScene().getWindow()).setScene(new Scene(r));
        } catch(Exception e){}
    }

    private void limparCampos() {
        idEmEdicao = 0; btnSalvar.setText("Salvar Item");
        txtTitulo.clear(); txtMaterial.clear(); txtDimensoes.clear(); txtLocal.clear(); txtDescricao.clear();
        cbDoador.getSelectionModel().clearSelection();
        dtCriacao.setValue(null); dtAquisicao.setValue(LocalDate.now());
    }

    private void bloquearCampos(){}
    private void mostrarAlerta(Alert.AlertType t, String m) { new Alert(t, m).showAndWait(); }
}