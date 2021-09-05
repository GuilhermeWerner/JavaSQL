package com.ti2cc;

import java.sql.*;

public class DAO {
    private Connection conexao;

    public DAO() {
        conexao = null;
    }

    public boolean conectar() {
        String driverName = "org.postgresql.Driver";

        String server = "localhost";
        int porta = 5432;

        String username = "postgres";
        String password = "1234";

        String mydatabase = "postgres";

        boolean status = false;

        String url = "jdbc:postgresql://" + server + ":" + porta + "/" + mydatabase;

        try {
            Class.forName(driverName);
            conexao = DriverManager.getConnection(url, username, password);
            status = (conexao == null);
            System.out.println("Conexão efetuada com o postgres!");
        } catch (ClassNotFoundException e) {
            System.err.println("Conexão NÃO efetuada com o postgres -- Driver não encontrado -- " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
        }

        return status;
    }

    public boolean close() {
        boolean status = false;

        try {
            conexao.close();
            status = true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return status;
    }

    public boolean inserirUsuario(Usuario usuario) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("INSERT INTO usuario (codigo, login, senha, sexo) " + "VALUES (" + usuario.getCodigo()
                    + ", '" + usuario.getLogin() + "', '" + usuario.getSenha() + "', '" + usuario.getSexo() + "');");
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean atualizarUsuario(Usuario usuario) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            String sql = "UPDATE usuario SET login = '" + usuario.getLogin() + "', senha = '" + usuario.getSenha()
                    + "', sexo = '" + usuario.getSexo() + "'" + " WHERE codigo = " + usuario.getCodigo();
            st.executeUpdate(sql);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean excluirUsuario(int codigo) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM usuario WHERE codigo = " + codigo);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public Usuario[] getUsuarios() {
        Usuario[] usuarios = null;

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("SELECT * FROM usuario");
            if (rs.next()) {
                rs.last();
                usuarios = new Usuario[rs.getRow()];
                rs.beforeFirst();

                for (int i = 0; rs.next(); i++) {
                    usuarios[i] = new Usuario(rs.getInt("codigo"), rs.getString("login"), rs.getString("senha"),
                            rs.getString("sexo").charAt(0));
                }
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return usuarios;
    }

    public Usuario getUsuario(int codigo) {
        Usuario usuario = null;

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("SELECT * FROM usuario WHERE usuario.codigo = " + codigo);
            if (rs.first()) {
                usuario = new Usuario(rs.getInt("codigo"), rs.getString("login"), rs.getString("senha"),
                        rs.getString("sexo").charAt(0));
            }
            st.close();
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }

        return usuario;
    }

    public Usuario[] getUsuariosMasculinos() {
        Usuario[] usuarios = null;

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = st.executeQuery("SELECT * FROM usuario WHERE usuario.sexo LIKE 'M'");
            if (rs.next()) {
                rs.last();
                usuarios = new Usuario[rs.getRow()];
                rs.beforeFirst();

                for (int i = 0; rs.next(); i++) {
                    usuarios[i] = new Usuario(rs.getInt("codigo"), rs.getString("login"), rs.getString("senha"),
                            rs.getString("sexo").charAt(0));
                }
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return usuarios;
    }
}
