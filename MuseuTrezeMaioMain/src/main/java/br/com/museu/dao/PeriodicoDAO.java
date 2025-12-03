package br.com.museu.dao;

import br.com.museu.model.Periodico;
import br.com.museu.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeriodicoDAO {

    public void cadastrar(Periodico p) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();

        String sqlItem = "INSERT INTO ITEM_ACERVO (titulo, data_aquisicao, tipo_item, link_midia, id_doador) VALUES (?, ?, ?, ?, ?)";
        String sqlPer = "INSERT INTO PERIODICO (id_item_acervo, issn, periodicidade, data_inicio_publicacao, data_fim_publicacao) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement stmtItem = null;
        PreparedStatement stmtPer = null;

        try {
            conn.setAutoCommit(false);

            stmtItem = conn.prepareStatement(sqlItem, Statement.RETURN_GENERATED_KEYS);
            stmtItem.setString(1, p.getTitulo());
            stmtItem.setDate(2, Date.valueOf(p.getDataAquisicao()));
            stmtItem.setString(3, "Periodico");
            stmtItem.setString(4, "");

            if (p.getIdDoador() > 0) stmtItem.setInt(5, p.getIdDoador());
            else stmtItem.setNull(5, Types.INTEGER);

            stmtItem.executeUpdate();

            ResultSet rs = stmtItem.getGeneratedKeys();
            int idGerado = 0;
            if (rs.next()) idGerado = rs.getInt(1);

            stmtPer = conn.prepareStatement(sqlPer);
            stmtPer.setInt(1, idGerado);
            stmtPer.setString(2, p.getIssn());
            stmtPer.setString(3, p.getPeriodicidade());

            if (p.getDataInicio() != null) stmtPer.setDate(4, Date.valueOf(p.getDataInicio()));
            else stmtPer.setNull(4, Types.DATE);

            if (p.getDataFim() != null) stmtPer.setDate(5, Date.valueOf(p.getDataFim()));
            else stmtPer.setNull(5, Types.DATE);

            stmtPer.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally { if (conn != null) conn.close(); }
    }

    public void atualizar(Periodico p) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sqlItem = "UPDATE ITEM_ACERVO SET titulo = ?, data_aquisicao = ?, id_doador = ? WHERE id_item_acervo = ?";
        String sqlPer = "UPDATE PERIODICO SET issn = ?, periodicidade = ?, data_inicio_publicacao = ?, data_fim_publicacao = ? WHERE id_item_acervo = ?";

        try {
            conn.setAutoCommit(false);

            PreparedStatement stmtItem = conn.prepareStatement(sqlItem);
            stmtItem.setString(1, p.getTitulo());
            stmtItem.setDate(2, Date.valueOf(p.getDataAquisicao()));

            if (p.getIdDoador() > 0) stmtItem.setInt(3, p.getIdDoador());
            else stmtItem.setNull(3, Types.INTEGER);

            stmtItem.setInt(4, p.getIdItemAcervo());
            stmtItem.executeUpdate();

            PreparedStatement stmtPer = conn.prepareStatement(sqlPer);
            stmtPer.setString(1, p.getIssn());
            stmtPer.setString(2, p.getPeriodicidade());

            if (p.getDataInicio() != null) stmtPer.setDate(3, Date.valueOf(p.getDataInicio()));
            else stmtPer.setNull(3, Types.DATE);

            if (p.getDataFim() != null) stmtPer.setDate(4, Date.valueOf(p.getDataFim()));
            else stmtPer.setNull(4, Types.DATE);

            stmtPer.setInt(5, p.getIdItemAcervo());
            stmtPer.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            conn.rollback(); throw e;
        } finally { conn.close(); }
    }

    public List<Periodico> listarTodos() {
        List<Periodico> lista = new ArrayList<>();
        String sql = "SELECT i.*, p.*, d.nome AS nome_doador, d.id_doador " +
                "FROM ITEM_ACERVO i " +
                "JOIN PERIODICO p ON i.id_item_acervo = p.id_item_acervo " +
                "LEFT JOIN DOADOR d ON i.id_doador = d.id_doador";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Periodico p = new Periodico();
                p.setIdItemAcervo(rs.getInt("id_item_acervo"));
                p.setTitulo(rs.getString("titulo"));
                p.setDataAquisicao(rs.getDate("data_aquisicao").toLocalDate());

                p.setIssn(rs.getString("issn"));
                p.setPeriodicidade(rs.getString("periodicidade"));

                Date dInicio = rs.getDate("data_inicio_publicacao");
                if (dInicio != null) p.setDataInicio(dInicio.toLocalDate());

                p.setIdDoador(rs.getInt("id_doador"));
                p.setNomeDoador(rs.getString("nome_doador"));

                lista.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public void excluir(int id) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement p1 = conn.prepareStatement("DELETE FROM PERIODICO WHERE id_item_acervo = ?");
            p1.setInt(1, id); p1.executeUpdate();

            PreparedStatement p2 = conn.prepareStatement("DELETE FROM ITEM_ACERVO WHERE id_item_acervo = ?");
            p2.setInt(1, id); p2.executeUpdate();
            conn.commit();
        } catch(SQLException e) { conn.rollback(); throw e; } finally { conn.close(); }
    }
}