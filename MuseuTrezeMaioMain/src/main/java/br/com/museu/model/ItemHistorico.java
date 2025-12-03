package br.com.museu.model;

import java.time.LocalDate;

public class ItemHistorico extends ItemAcervo {
    private LocalDate dataCriacao;
    private String descricao;
    private String dimensoes;
    private String material;
    private String localArmazenado;

    public ItemHistorico() {
        super();
        this.setTipoItem("ItemHistorico");
    }

    public LocalDate getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getDimensoes() { return dimensoes; }
    public void setDimensoes(String dimensoes) { this.dimensoes = dimensoes; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getLocalArmazenado() { return localArmazenado; }
    public void setLocalArmazenado(String localArmazenado) { this.localArmazenado = localArmazenado; }
}