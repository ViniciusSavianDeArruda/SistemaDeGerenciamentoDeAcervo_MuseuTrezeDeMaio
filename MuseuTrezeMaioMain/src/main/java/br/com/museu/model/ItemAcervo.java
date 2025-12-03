package br.com.museu.model;

import java.time.LocalDate;

public abstract class ItemAcervo {
    private int idItemAcervo;
    private String titulo;
    private LocalDate dataAquisicao;
    private String tipoItem;
    private String linkMidia;
    private int idDoador;

    public ItemAcervo() {}

    public ItemAcervo(int idItemAcervo, String titulo, LocalDate dataAquisicao, String tipoItem, String linkMidia) {
        this.idItemAcervo = idItemAcervo;
        this.titulo = titulo;
        this.dataAquisicao = dataAquisicao;
        this.tipoItem = tipoItem;
        this.linkMidia = linkMidia;
    }

    public int getIdItemAcervo() { return idItemAcervo; }
    public void setIdItemAcervo(int idItemAcervo) { this.idItemAcervo = idItemAcervo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public LocalDate getDataAquisicao() { return dataAquisicao; }
    public void setDataAquisicao(LocalDate dataAquisicao) { this.dataAquisicao = dataAquisicao; }

    public String getTipoItem() { return tipoItem; }
    public void setTipoItem(String tipoItem) { this.tipoItem = tipoItem; }

    public String getLinkMidia() { return linkMidia; }
    public void setLinkMidia(String linkMidia) { this.linkMidia = linkMidia; }

    private String nomeDoador;

    public int getIdDoador() { return idDoador; }
    public void setIdDoador(int idDoador) { this.idDoador = idDoador; }

    public String getNomeDoador() { return nomeDoador; }
    public void setNomeDoador(String nomeDoador) { this.nomeDoador = nomeDoador; }


}