package br.com.museu.controller;

import br.com.museu.dao.AutorDAO;
import br.com.museu.dao.AssuntoDAO;
import br.com.museu.dao.DoadorDAO;
import br.com.museu.dao.SerieDAO;
import br.com.museu.dao.LivroDAO;
import br.com.museu.model.Autor;
import br.com.museu.model.Assunto;
import br.com.museu.model.Doador;
import br.com.museu.model.Serie;
import br.com.museu.model.Livro;
import br.com.museu.model.Usuario;
import br.com.museu.util.SessaoUsuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;


public class BibliotecaController {

    @FXML private Button btnSalvar;
    @FXML private Button btnExcluir;
    @FXML private Button btnSelecionar;
    @FXML private TextField txtTitulo;
    @FXML private TextField txtTituloOriginal;
    @FXML private TextField txtISBN;
    @FXML private TextField txtLocal;
    @FXML private TextField txtEdicao;
    @FXML private TextField txtDescFisica;
    @FXML private DatePicker dtAquisicao;

    @FXML private ComboBox<Autor> cbAutor;
    @FXML private ComboBox<Assunto> cbAssunto;
    @FXML private ComboBox<Serie> cbSerie;
    @FXML private ComboBox<Doador> cbDoador;

    @FXML private ImageView imgCapa;
    @FXML private Label lblCaminhoImagem;
    private String caminhoImagemAtual = "";

    @FXML private TextField txtPesquisa;
    @FXML private TableView<Livro> tabelaLivros;

    @FXML private TableColumn<Livro, Integer> colId;
    @FXML private TableColumn<Livro, String> colTitulo;
    @FXML private TableColumn<Livro, String> colAutor;
    @FXML private TableColumn<Livro, String> colAssunto;
    @FXML private TableColumn<Livro, String> colSerie;
    @FXML private TableColumn<Livro, String> colDoador;
    @FXML private TableColumn<Livro, String> colISBN;
    @FXML private TableColumn<Livro, String> colLocal;
    @FXML private TableColumn<Livro, String> colEdicao;
    @FXML private TableColumn<Livro, LocalDate> colAno;

    private ObservableList<Livro> listaMestra = FXCollections.observableArrayList();
    private int idEmEdicao = 0;

    @FXML
    public void initialize() {
        configurarColunas();
        carregarCombos();
        carregarDados();
        configurarPesquisa();
        configurarSelecaoTabela();
        dtAquisicao.setValue(LocalDate.now());

        Usuario u = SessaoUsuario.getInstancia().getUsuarioLogado();
        if (u != null && "Visitante".equalsIgnoreCase(u.getTipoUsuario())) {
            btnSalvar.setVisible(false);
            btnExcluir.setVisible(false);
            txtTitulo.setDisable(true);
            cbAutor.setDisable(true);
            cbAssunto.setDisable(true);
            cbSerie.setDisable(true);
            cbDoador.setDisable(true);
            txtTituloOriginal.setDisable(true);
            txtISBN.setDisable(true);
            txtLocal.setDisable(true);
            txtEdicao.setDisable(true);
            txtDescFisica.setDisable(true);
            dtAquisicao.setDisable(true);
            imgCapa.setDisable(true);
            btnSelecionar.setDisable(true);

        }
    }

    private void carregarCombos() {
        cbAutor.setItems(FXCollections.observableArrayList(new AutorDAO().listarTodos()));
        cbAssunto.setItems(FXCollections.observableArrayList(new AssuntoDAO().listarTodos()));
        cbSerie.setItems(FXCollections.observableArrayList(new SerieDAO().listarTodas()));
        cbDoador.setItems(FXCollections.observableArrayList(new DoadorDAO().listarTodos()));
    }

    private void configuringColunas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idItemAcervo"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("nomeAutor"));
        colAssunto.setCellValueFactory(new PropertyValueFactory<>("nomeAssunto"));
        colSerie.setCellValueFactory(new PropertyValueFactory<>("nomeSerie"));
        colDoador.setCellValueFactory(new PropertyValueFactory<>("nomeDoador"));
        colISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colLocal.setCellValueFactory(new PropertyValueFactory<>("localChamada"));
        colEdicao.setCellValueFactory(new PropertyValueFactory<>("edicao"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("dataAquisicao"));
    }

    private void configurarColunas() { configuringColunas(); }

    @FXML
    public void onBtnSalvarClick() {
        if (txtTitulo.getText().isEmpty() || cbAutor.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Preencha Título e Autor.");
            return;
        }
        try {
            Livro livro = new Livro();
            livro.setTitulo(txtTitulo.getText());
            livro.setTituloOriginal(txtTituloOriginal.getText());
            livro.setIsbn(txtISBN.getText());
            livro.setLocalChamada(txtLocal.getText());
            livro.setEdicao(txtEdicao.getText());
            livro.setDescFisica(txtDescFisica.getText());
            livro.setDataAquisicao(dtAquisicao.getValue());
            livro.setLinkMidia(caminhoImagemAtual);

            if (cbSerie.getValue() != null) livro.setIdSerie(cbSerie.getValue().getIdSerie());
            if (cbDoador.getValue() != null) livro.setIdDoador(cbDoador.getValue().getIdDoador());

            LivroDAO dao = new LivroDAO();

            if (idEmEdicao == 0) {
                int idNovo = dao.cadastrar(livro);
                dao.vincularAutor(idNovo, cbAutor.getValue().getIdAutor());
                if (cbAssunto.getValue() != null) dao.vincularAssunto(idNovo, cbAssunto.getValue().getIdAssunto());
                mostrarAlerta(Alert.AlertType.INFORMATION, "Livro salvo!");
            } else {
                livro.setIdItemAcervo(idEmEdicao);
                dao.atualizar(livro);
                dao.desvincularAutores(idEmEdicao);
                dao.vincularAutor(idEmEdicao, cbAutor.getValue().getIdAutor());
                dao.desvincularAssuntos(idEmEdicao);
                if (cbAssunto.getValue() != null) dao.vincularAssunto(idEmEdicao, cbAssunto.getValue().getIdAssunto());
                mostrarAlerta(Alert.AlertType.INFORMATION, "Livro atualizado!");
            }

            limparCampos();
            carregarDados();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro: " + e.getMessage());
        }
    }

    private void preencherFormulario(Livro livro) {
        idEmEdicao = livro.getIdItemAcervo();
        btnSalvar.setText("Atualizar");

        txtTitulo.setText(livro.getTitulo());
        txtTituloOriginal.setText(livro.getTituloOriginal());
        txtISBN.setText(livro.getIsbn());
        txtLocal.setText(livro.getLocalChamada());
        txtEdicao.setText(livro.getEdicao());
        txtDescFisica.setText(livro.getDescFisica());
        dtAquisicao.setValue(livro.getDataAquisicao());

        selecionarNoCombo(cbAutor, livro.getIdAutor());
        selecionarNoCombo(cbAssunto, livro.getIdAssunto());
        selecionarNoCombo(cbSerie, livro.getIdSerie());
        selecionarNoCombo(cbDoador, livro.getIdDoador());

        caminhoImagemAtual = livro.getLinkMidia();
        if (caminhoImagemAtual != null && !caminhoImagemAtual.isEmpty()) {
            try { imgCapa.setImage(new Image(caminhoImagemAtual)); lblCaminhoImagem.setText("Carregada"); } catch(Exception e){ imgCapa.setImage(null); }
        } else { imgCapa.setImage(null); lblCaminhoImagem.setText("..."); }
    }


    private <T> void selecionarNoCombo(ComboBox<T> combo, int idAlvo) {
        if (idAlvo <= 0) { combo.getSelectionModel().clearSelection(); return; }


        for (T item : combo.getItems()) {
            if (item instanceof Autor && ((Autor)item).getIdAutor() == idAlvo) { combo.getSelectionModel().select(item); break; }
            if (item instanceof Assunto && ((Assunto)item).getIdAssunto() == idAlvo) { combo.getSelectionModel().select(item); break; }
            if (item instanceof Serie && ((Serie)item).getIdSerie() == idAlvo) { combo.getSelectionModel().select(item); break; }
            if (item instanceof Doador && ((Doador)item).getIdDoador() == idAlvo) { combo.getSelectionModel().select(item); break; }
        }
    }


    @FXML public void onBtnSelecionarImagemClick() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagens", "*.jpg", "*.png"));
        File f = fc.showOpenDialog(txtTitulo.getScene().getWindow());
        if (f != null) {
            caminhoImagemAtual = f.toURI().toString();
            lblCaminhoImagem.setText(f.getName());
            imgCapa.setImage(new Image(caminhoImagemAtual));
        }
    }

    private void configurarSelecaoTabela() {
        tabelaLivros.setRowFactory(tv -> {
            TableRow<Livro> row = new TableRow<>();
            row.setOnMouseClicked(ev -> {
                if (ev.getClickCount() == 2 && (!row.isEmpty())) preencherFormulario(row.getItem());
            });
            return row;
        });
    }

    @FXML
    public void onBtnExcluirClick() {
        Livro livroSelecionado = tabelaLivros.getSelectionModel().getSelectedItem();

        // 1. Validação: Verifica se tem um livro selecionado na tabela
        if (livroSelecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selecione um livro na tabela para excluir.");
            return;
        }

        // 2. Confirmação de Segurança
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText(null);
        alert.setContentText("Tem certeza que deseja excluir o livro: " + livroSelecionado.getTitulo() + "?");

        if (alert.showAndWait().get() != ButtonType.OK) {
            return; // Usuário cancelou
        }

        // 3. Tenta excluir no Banco de Dados
        try {
            new LivroDAO().excluir(livroSelecionado.getIdItemAcervo());

            // Se chegou aqui, deu certo:
            carregarDados(); // Atualiza a tabela
            limparCampos();  // Limpa o formulário para não salvar por cima
            mostrarAlerta(Alert.AlertType.INFORMATION, "Livro excluído com sucesso!");

        } catch (Exception e) {
            e.printStackTrace(); // Mantém o log técnico
            // 4. Mostra o erro real na tela para você saber o que houve
            mostrarAlerta(Alert.AlertType.ERROR, "Não foi possível excluir.\nErro: " + e.getMessage());
        }
    }

    @FXML public void onBtnVoltarClick() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/br/com/museu/museutrezemaio/menu-view.fxml"));
            Stage stage = (Stage) txtTitulo.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch(Exception e){ e.printStackTrace(); }
    }

    private void limparCampos() {
        idEmEdicao = 0; btnSalvar.setText("Salvar Livro");
        txtTitulo.clear(); txtTituloOriginal.clear(); txtISBN.clear(); txtLocal.clear();
        txtEdicao.clear(); txtDescFisica.clear();
        cbAutor.getSelectionModel().clearSelection();
        cbAssunto.getSelectionModel().clearSelection();
        cbSerie.getSelectionModel().clearSelection();
        cbDoador.getSelectionModel().clearSelection();
        imgCapa.setImage(null); lblCaminhoImagem.setText("...");
        dtAquisicao.setValue(LocalDate.now());
    }

    private void carregarDados() {
        listaMestra.clear();
        listaMestra.addAll(new LivroDAO().listarTodos());
    }

    private void configurarPesquisa() {
        FilteredList<Livro> dados = new FilteredList<>(listaMestra, p->true);
        txtPesquisa.textProperty().addListener((obs, old, novo) -> {
            dados.setPredicate(l -> {
                if(novo == null || novo.isEmpty()) return true;
                String f = novo.toLowerCase();
                return l.getTitulo().toLowerCase().contains(f) || (l.getIsbn()!=null && l.getIsbn().contains(f));
            });
        });
        SortedList<Livro> ord = new SortedList<>(dados);
        ord.comparatorProperty().bind(tabelaLivros.comparatorProperty());
        tabelaLivros.setItems(ord);
    }

    private void mostrarAlerta(Alert.AlertType t, String m) { new Alert(t, m).showAndWait(); }
}