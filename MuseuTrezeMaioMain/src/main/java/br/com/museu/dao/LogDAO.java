package br.com.museu.dao;

import br.com.museu.model.LogOperacao;
import br.com.museu.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogDAO {

    public List<LogOperacao> listarTodos() {
        List<LogOperacao> lista = new ArrayList<>();
        String sql = "SELECT l.id_log, l.operacao, l.data_operacao, l.id_item_acervo, u.login_usuario " +
                "FROM LOG_OPERACAO l " +
                "JOIN USUARIO u ON l.id_usuario = u.id_usuario " +
                "ORDER BY l.data_operacao DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LogOperacao log = new LogOperacao();
                log.setIdLog(rs.getInt("id_log"));
                log.setOperacao(rs.getString("operacao"));
                Timestamp ts = rs.getTimestamp("data_operacao");
                if (ts != null) log.setDataOperacao(ts.toLocalDateTime());

                log.setIdItemAcervo(rs.getInt("id_item_acervo"));
                log.setNomeUsuario(rs.getString("login_usuario"));

                lista.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}