package br.com.museu.model;

import java.time.LocalDate;

public class Periodico extends ItemAcervo {
    private String issn;
    private String periodicidade;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String tituloOriginal;

    public Periodico() {
        super();
        this.setTipoItem("Periodico");
    }

    public String getIssn() { return issn; }
    public void setIssn(String issn) { this.issn = issn; }

    public String getPeriodicidade() { return periodicidade; }
    public void setPeriodicidade(String periodicidade) { this.periodicidade = periodicidade; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public String getTituloOriginal() { return tituloOriginal; }
    public void setTituloOriginal(String tituloOriginal) { this.tituloOriginal = tituloOriginal; }
}