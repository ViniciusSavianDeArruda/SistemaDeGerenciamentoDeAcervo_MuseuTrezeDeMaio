package br.com.museu.dao;

import br.com.museu.model.Serie;
import br.com.museu.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SerieDAO {

    public void cadastrar(Serie serie) throws SQLException {
        String sql = "INSERT INTO SERIE (nome) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, serie.getNome());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Serie serie) throws SQLException {
        String sql = "UPDATE SERIE SET nome = ? WHERE id_serie = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, serie.getNome());
            stmt.setInt(2, serie.getIdSerie());
            stmt.executeUpdate();
        }
    }

    public List<Serie> listarTodas() {
        List<Serie> lista = new ArrayList<>();
        String sql = "SELECT * FROM SERIE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Serie(rs.getInt("id_serie"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM SERIE WHERE id_serie = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}