package br.com.museu.model;

public class Livro extends ItemAcervo {
    private String localChamada;
    private String edicao;
    private String descFisica;
    private String isbn;
    private String tituloOriginal;
    private String notas;

    public Livro() {
        super();
        this.setTipoItem("Livro");
    }


    public String getLocalChamada() { return localChamada; }
    public void setLocalChamada(String localChamada) { this.localChamada = localChamada; }

    public String getEdicao() { return edicao; }
    public void setEdicao(String edicao) { this.edicao = edicao; }

    public String getDescFisica() { return descFisica; }
    public void setDescFisica(String descFisica) { this.descFisica = descFisica; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTituloOriginal() { return tituloOriginal; }
    public void setTituloOriginal(String tituloOriginal) { this.tituloOriginal = tituloOriginal; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    private String nomeAutor;
    private int idAutor;

    public String getNomeAutor() { return nomeAutor; }
    public void setNomeAutor(String nomeAutor) { this.nomeAutor = nomeAutor; }

    public int getIdAutor() { return idAutor; }
    public void setIdAutor(int idAutor) { this.idAutor = idAutor; }

    private String nomeAssunto;
    private int idAssunto;

    public String getNomeAssunto() { return nomeAssunto; }
    public void setNomeAssunto(String nomeAssunto) { this.nomeAssunto = nomeAssunto; }

    public int getIdAssunto() { return idAssunto; }
    public void setIdAssunto(int idAssunto) { this.idAssunto = idAssunto; }


    private String nomeSerie;
    private int idSerie;

    public String getNomeSerie() { return nomeSerie; }
    public void setNomeSerie(String nomeSerie) { this.nomeSerie = nomeSerie; }

    public int getIdSerie() { return idSerie; }
    public void setIdSerie(int idSerie) { this.idSerie = idSerie; }
}