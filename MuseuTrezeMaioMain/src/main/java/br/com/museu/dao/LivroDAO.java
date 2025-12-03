package br.com.museu.dao;

import br.com.museu.model.Livro;
import br.com.museu.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    public int cadastrar(Livro livro) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmtItem = null;
        PreparedStatement stmtLivro = null;
        int idGerado = 0;

        String sqlItem = "INSERT INTO ITEM_ACERVO (titulo, data_aquisicao, tipo_item, link_midia, id_doador) VALUES (?, ?, ?, ?, ?)";
        String sqlLivro = "INSERT INTO LIVRO (id_item_acervo, local_chamada, edicao, desc_fisica, isbn, titulo_original, notas, id_serie) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            stmtItem = conn.prepareStatement(sqlItem, Statement.RETURN_GENERATED_KEYS);
            stmtItem.setString(1, livro.getTitulo());
            stmtItem.setDate(2, Date.valueOf(livro.getDataAquisicao()));
            stmtItem.setString(3, "Livro");
            stmtItem.setString(4, livro.getLinkMidia());

            if (livro.getIdDoador() > 0) stmtItem.setInt(5, livro.getIdDoador());
            else stmtItem.setNull(5, Types.INTEGER);

            stmtItem.executeUpdate();

            ResultSet rs = stmtItem.getGeneratedKeys();
            if (rs.next()) idGerado = rs.getInt(1);

            stmtLivro = conn.prepareStatement(sqlLivro);
            stmtLivro.setInt(1, idGerado);
            stmtLivro.setString(2, livro.getLocalChamada());
            stmtLivro.setString(3, livro.getEdicao());
            stmtLivro.setString(4, livro.getDescFisica());
            stmtLivro.setString(5, livro.getIsbn());
            stmtLivro.setString(6, livro.getTituloOriginal());
            stmtLivro.setString(7, livro.getNotas());

            if (livro.getIdSerie() > 0) stmtLivro.setInt(8, livro.getIdSerie());
            else stmtLivro.setNull(8, Types.INTEGER);

            stmtLivro.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally { if (conn != null) conn.close(); }
        return idGerado;
    }

    public void vincularAutor(int idLivro, int idAutor) throws SQLException {
        String sql = "INSERT INTO LIVRO_AUTOR (id_livro, id_autor) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.setInt(2, idAutor);
            stmt.executeUpdate();
        }
    }

    public void desvincularAutores(int idLivro) throws SQLException {
        String sql = "DELETE FROM LIVRO_AUTOR WHERE id_livro = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.executeUpdate();
        }
    }

    public void atualizar(Livro livro) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sqlItem = "UPDATE ITEM_ACERVO SET titulo = ?, data_aquisicao = ?, link_midia = ?, id_doador = ? WHERE id_item_acervo = ?";
        String sqlLivro = "UPDATE LIVRO SET local_chamada = ?, edicao = ?, desc_fisica = ?, isbn = ?, titulo_original = ?, notas = ?, id_serie = ? WHERE id_item_acervo = ?";

        try {
            conn.setAutoCommit(false);
            PreparedStatement stmtItem = conn.prepareStatement(sqlItem);
            stmtItem.setString(1, livro.getTitulo());
            stmtItem.setDate(2, Date.valueOf(livro.getDataAquisicao()));
            stmtItem.setString(3, livro.getLinkMidia());

            if (livro.getIdDoador() > 0) stmtItem.setInt(4, livro.getIdDoador());
            else stmtItem.setNull(4, Types.INTEGER);

            stmtItem.setInt(5, livro.getIdItemAcervo());
            stmtItem.executeUpdate();

            PreparedStatement stmtLivro = conn.prepareStatement(sqlLivro);
            stmtLivro.setString(1, livro.getLocalChamada());
            stmtLivro.setString(2, livro.getEdicao());
            stmtLivro.setString(3, livro.getDescFisica());
            stmtLivro.setString(4, livro.getIsbn());
            stmtLivro.setString(5, livro.getTituloOriginal());
            stmtLivro.setString(6, livro.getNotas());

            if (livro.getIdSerie() > 0) stmtLivro.setInt(7, livro.getIdSerie());
            else stmtLivro.setNull(7, Types.INTEGER);

            stmtLivro.setInt(8, livro.getIdItemAcervo());
            stmtLivro.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            conn.rollback(); throw e;
        } finally { conn.close(); }
    }

    public List<Livro> listarTodos() {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT " +
                "  i.id_item_acervo, i.titulo, i.data_aquisicao, i.link_midia, i.id_doador, " +
                "  l.local_chamada, l.isbn, l.edicao, l.desc_fisica, l.titulo_original, l.notas, " +
                "  a.nome, a.id_autor, " +
                "  s.descricao, s.id_assunto, " +
                "  ser.nome AS nome_serie, ser.id_serie, " +
                "  d.nome AS nome_doador " +
                "FROM ITEM_ACERVO i " +
                "JOIN LIVRO l ON i.id_item_acervo = l.id_item_acervo " +
                "LEFT JOIN LIVRO_AUTOR la ON l.id_item_acervo = la.id_livro " +
                "LEFT JOIN AUTOR a ON la.id_autor = a.id_autor " +
                "LEFT JOIN LIVRO_ASSUNTO las ON l.id_item_acervo = las.id_livro " +
                "LEFT JOIN ASSUNTO s ON las.id_assunto = s.id_assunto " +
                "LEFT JOIN SERIE ser ON l.id_serie = ser.id_serie " +
                "LEFT JOIN DOADOR d ON i.id_doador = d.id_doador";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livro livro = new Livro();
                livro.setIdItemAcervo(rs.getInt("id_item_acervo"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setDataAquisicao(rs.getDate("data_aquisicao").toLocalDate());
                livro.setLinkMidia(rs.getString("link_midia"));
                livro.setLocalChamada(rs.getString("local_chamada"));
                livro.setIsbn(rs.getString("isbn"));
                livro.setEdicao(rs.getString("edicao"));
                livro.setDescFisica(rs.getString("desc_fisica"));
                livro.setTituloOriginal(rs.getString("titulo_original"));
                livro.setNotas(rs.getString("notas"));

                livro.setNomeAutor(rs.getString("nome"));
                livro.setIdAutor(rs.getInt("id_autor"));
                livro.setNomeAssunto(rs.getString("descricao"));
                livro.setIdAssunto(rs.getInt("id_assunto"));
                livro.setNomeSerie(rs.getString("nome_serie"));
                livro.setIdSerie(rs.getInt("id_serie"));

                livro.setIdDoador(rs.getInt("id_doador"));
                livro.setNomeDoador(rs.getString("nome_doador"));

                livros.add(livro);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return livros;
    }

    public void excluir(int id) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            // 1. Remove os vínculos com Autores
            PreparedStatement p0 = conn.prepareStatement("DELETE FROM LIVRO_AUTOR WHERE id_livro = ?");
            p0.setInt(1, id);
            p0.executeUpdate();

            // 2. Remove os vínculos com Assuntos
            PreparedStatement p0b = conn.prepareStatement("DELETE FROM LIVRO_ASSUNTO WHERE id_livro = ?");
            p0b.setInt(1, id);
            p0b.executeUpdate();

            // 3. (REMOVIDO) A linha de Emprestimo não deve estar aqui se a tabela não existe.
            // Se você tiver uma tabela de empréstimo com outro nome, coloque aqui.
            // Caso contrário, pule esta etapa.

            // 4. Remove o registro da tabela filha (LIVRO)
            PreparedStatement p1 = conn.prepareStatement("DELETE FROM LIVRO WHERE id_item_acervo = ?");
            p1.setInt(1, id);
            p1.executeUpdate();

            // 5. Remove o registro da tabela pai (ITEM_ACERVO)
            PreparedStatement p2 = conn.prepareStatement("DELETE FROM ITEM_ACERVO WHERE id_item_acervo = ?");
            p2.setInt(1, id);
            p2.executeUpdate();

            conn.commit();
        } catch(SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }

    public void vincularAssunto(int idLivro, int idAssunto) throws SQLException {
        String sql = "INSERT INTO LIVRO_ASSUNTO (id_livro, id_assunto) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.setInt(2, idAssunto);
            stmt.executeUpdate();
        }
    }

    public void desvincularAssuntos(int idLivro) throws SQLException {
        String sql = "DELETE FROM LIVRO_ASSUNTO WHERE id_livro = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            stmt.executeUpdate();
        }
    }
}