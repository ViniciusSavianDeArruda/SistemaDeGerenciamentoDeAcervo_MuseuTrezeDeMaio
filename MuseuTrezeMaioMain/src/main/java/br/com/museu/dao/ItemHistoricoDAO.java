package br.com.museu.dao;

import br.com.museu.model.ItemHistorico;
import br.com.museu.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemHistoricoDAO {

    public void cadastrar(ItemHistorico item) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sqlItem = "INSERT INTO ITEM_ACERVO (titulo, data_aquisicao, tipo_item, link_midia, id_doador) VALUES (?, ?, ?, ?, ?)";
        String sqlHist = "INSERT INTO ITEM_HISTORICO (id_item_acervo, data_criacao, descricao, material, dimensoes, local_armazenado) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement stmtItem = null;
        PreparedStatement stmtHist = null;

        try {
            conn.setAutoCommit(false);

            stmtItem = conn.prepareStatement(sqlItem, Statement.RETURN_GENERATED_KEYS);
            stmtItem.setString(1, item.getTitulo());
            stmtItem.setDate(2, Date.valueOf(item.getDataAquisicao()));
            stmtItem.setString(3, "ItemHistorico");
            stmtItem.setString(4, "");

            if (item.getIdDoador() > 0) {
                stmtItem.setInt(5, item.getIdDoador());
            } else {
                stmtItem.setNull(5, Types.INTEGER);
            }

            stmtItem.executeUpdate();

            ResultSet rs = stmtItem.getGeneratedKeys();
            int idGerado = 0;
            if (rs.next()) idGerado = rs.getInt(1);

            stmtHist = conn.prepareStatement(sqlHist);
            stmtHist.setInt(1, idGerado);
            if (item.getDataCriacao() != null) stmtHist.setDate(2, Date.valueOf(item.getDataCriacao()));
            else stmtHist.setNull(2, Types.DATE);

            stmtHist.setString(3, item.getDescricao());
            stmtHist.setString(4, item.getMaterial());
            stmtHist.setString(5, item.getDimensoes());
            stmtHist.setString(6, item.getLocalArmazenado());
            stmtHist.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            conn.rollback(); throw e;
        } finally { conn.close(); }
    }

    public List<ItemHistorico> listarTodos() {
        List<ItemHistorico> lista = new ArrayList<>();
        String sql = "SELECT i.*, h.*, d.nome as nome_doador, d.id_doador " +
                "FROM ITEM_ACERVO i " +
                "JOIN ITEM_HISTORICO h ON i.id_item_acervo = h.id_item_acervo " +
                "LEFT JOIN DOADOR d ON i.id_doador = d.id_doador";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ItemHistorico item = new ItemHistorico();
                item.setIdItemAcervo(rs.getInt("id_item_acervo"));
                item.setTitulo(rs.getString("titulo"));
                item.setDataAquisicao(rs.getDate("data_aquisicao").toLocalDate());

                item.setMaterial(rs.getString("material"));
                item.setLocalArmazenado(rs.getString("local_armazenado"));
                item.setDescricao(rs.getString("descricao"));
                item.setDimensoes(rs.getString("dimensoes"));

                Date dtCriacao = rs.getDate("data_criacao");
                if (dtCriacao != null) item.setDataCriacao(dtCriacao.toLocalDate());

                item.setNomeDoador(rs.getString("nome_doador"));
                item.setIdDoador(rs.getInt("id_doador"));

                lista.add(item);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public void excluir(int id) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            PreparedStatement p1 = conn.prepareStatement("DELETE FROM ITEM_HISTORICO WHERE id_item_acervo = ?");
            p1.setInt(1, id);
            p1.executeUpdate();

            PreparedStatement p2 = conn.prepareStatement("DELETE FROM ITEM_ACERVO WHERE id_item_acervo = ?");
            p2.setInt(1, id);
            p2.executeUpdate();

            conn.commit();
        } catch(SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    public void atualizar(ItemHistorico item) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String sqlItem = "UPDATE ITEM_ACERVO SET titulo = ?, data_aquisicao = ?, id_doador = ? WHERE id_item_acervo = ?";
        String sqlHist = "UPDATE ITEM_HISTORICO SET data_criacao = ?, descricao = ?, material = ?, dimensoes = ?, local_armazenado = ? WHERE id_item_acervo = ?";

        try {
            conn.setAutoCommit(false);

            PreparedStatement stmtItem = conn.prepareStatement(sqlItem);
            stmtItem.setString(1, item.getTitulo());
            stmtItem.setDate(2, Date.valueOf(item.getDataAquisicao()));

            if (item.getIdDoador() > 0) stmtItem.setInt(3, item.getIdDoador());
            else stmtItem.setNull(3, Types.INTEGER);

            stmtItem.setInt(4, item.getIdItemAcervo());
            stmtItem.executeUpdate();

            PreparedStatement stmtHist = conn.prepareStatement(sqlHist);
            if (item.getDataCriacao() != null) stmtHist.setDate(1, Date.valueOf(item.getDataCriacao()));
            else stmtHist.setNull(1, Types.DATE);

            stmtHist.setString(2, item.getDescricao());
            stmtHist.setString(3, item.getMaterial());
            stmtHist.setString(4, item.getDimensoes());
            stmtHist.setString(5, item.getLocalArmazenado());
            stmtHist.setInt(6, item.getIdItemAcervo());
            stmtHist.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            conn.rollback(); throw e;
        } finally { conn.close(); }
    }
}