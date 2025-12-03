package br.com.museu.dao;

import br.com.museu.model.Assunto;
import br.com.museu.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssuntoDAO {

    public void cadastrar(Assunto assunto) throws SQLException {
        String sql = "INSERT INTO ASSUNTO (descricao) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, assunto.getDescricao());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Assunto assunto) throws SQLException {
        String sql = "UPDATE ASSUNTO SET descricao = ? WHERE id_assunto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, assunto.getDescricao());
            stmt.setInt(2, assunto.getIdAssunto());
            stmt.executeUpdate();
        }
    }

    public List<Assunto> listarTodos() {
        List<Assunto> lista = new ArrayList<>();
        String sql = "SELECT * FROM ASSUNTO";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Assunto(rs.getInt("id_assunto"), rs.getString("descricao")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM ASSUNTO WHERE id_assunto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}