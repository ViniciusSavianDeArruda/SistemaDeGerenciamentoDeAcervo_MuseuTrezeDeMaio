package br.com.museu.dao;

import br.com.museu.model.Doador;
import br.com.museu.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoadorDAO {

    public void cadastrar(Doador d) throws SQLException {
        String sql = "INSERT INTO DOADOR (nome) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, d.getNome());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Doador d) throws SQLException {
        String sql = "UPDATE DOADOR SET nome = ? WHERE id_doador = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, d.getNome());
            stmt.setInt(2, d.getIdDoador());
            stmt.executeUpdate();
        }
    }

    public List<Doador> listarTodos() {
        List<Doador> lista = new ArrayList<>();
        String sql = "SELECT * FROM DOADOR";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Doador(rs.getInt("id_doador"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM DOADOR WHERE id_doador = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}