package br.com.museu.dao;

import br.com.museu.model.Autor;
import br.com.museu.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {

    public void cadastrar(Autor autor) throws SQLException {
        String sql = "INSERT INTO AUTOR (nome) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, autor.getNome());
            stmt.executeUpdate();
        }
    }

    public List<Autor> listarTodos() {
        List<Autor> lista = new ArrayList<>();
        String sql = "SELECT * FROM AUTOR";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Autor(rs.getInt("id_autor"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM AUTOR WHERE id_autor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void atualizar(Autor autor) throws SQLException {
        String sql = "UPDATE AUTOR SET nome = ? WHERE id_autor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, autor.getNome());
            stmt.setInt(2, autor.getIdAutor());
            stmt.executeUpdate();
        }
    }
}