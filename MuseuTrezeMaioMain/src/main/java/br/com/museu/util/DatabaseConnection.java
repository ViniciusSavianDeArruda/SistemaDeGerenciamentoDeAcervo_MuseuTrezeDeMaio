package br.com.museu.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // ⚠ ATENÇÃO: Confirmar se o banco é 'db_museu_treze_maio' mesmo
    private static final String URL =
            "jdbc:sqlserver://localhost:1433;databaseName=db_museu_treze_maio;encrypt=false;trustServerCertificate=true;";


    private static final String USER = "sa";
    private static final String PASSWORD = "1203";


    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados!");
            System.err.println("Erro: " + e.getMessage());
            return null;
        }
    }



    public static void main(String[] args) {
     System.out.println("testando Login...");


     br.com.museu.dao.UsuarioDAO dao = new br.com.museu.dao.UsuarioDAO();
     br.com.museu.model.Usuario user = dao.validarLogin("admin", "admin123");


      Connection conn = getConnection();
      if (conn != null) {
           System.out.println("SUCESSO! Conexão realizada para: " + user.getLogin());
           System.out.println("TIPO: " + user.getTipoUsuario());
     } else {
          System.out.println("FALHA. USUARIO N ENCONTRADOD OU SENHA ERRADA");
      }
     }


    //public static void main(String[] args) {
    // System.out.println("Tentando conectar com usuário 'app_museu'...");
    //  Connection conn = getConnection();
    //  if (conn != null) {
    //       System.out.println("SUCESSO! Conexão realizada com db_museu_treze_maio.");
    //  }
    // }
}