package br.com.museu.model;

public class Doador {
    private int idDoador;
    private String nome;

    public Doador() {}

    public Doador(int idDoador, String nome) {
        this.idDoador = idDoador;
        this.nome = nome;
    }

    public int getIdDoador() { return idDoador; }
    public void setIdDoador(int idDoador) { this.idDoador = idDoador; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    @Override
    public String toString() {
        return nome;
    }
}