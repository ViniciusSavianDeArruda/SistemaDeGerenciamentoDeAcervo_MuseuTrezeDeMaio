package br.com.museu.model;

public class Serie {
    private int idSerie;
    private String nome;

    public Serie() {}

    public Serie(int idSerie, String nome) {
        this.idSerie = idSerie;
        this.nome = nome;
    }

    public int getIdSerie() { return idSerie; }
    public void setIdSerie(int idSerie) { this.idSerie = idSerie; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    @Override
    public String toString() {
        return nome;
    }
}