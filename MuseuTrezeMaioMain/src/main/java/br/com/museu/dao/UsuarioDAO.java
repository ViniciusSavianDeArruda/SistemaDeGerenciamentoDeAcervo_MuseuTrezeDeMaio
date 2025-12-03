package br.com.museu.dao;

import br.com.museu.model.Usuario;
import br.com.museu.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    /**
     * Tenta fazer login no sistema.
     * @return Retorna o objeto Usuario se der certo, ou null se errar a senha.
     */
    public Usuario validarLogin(String login, String senha) {
        String sql = "SELECT * FROM USUARIO WHERE login_usuario = ? AND senha = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setLogin(rs.getString("login_usuario"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setTipoUsuario(rs.getString("tipo_usuario"));
                return usuario;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao validar login: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}