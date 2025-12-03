package br.com.museu.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogOperacao {
    private int idLog;
    private String operacao;
    private LocalDateTime dataOperacao;
    private String nomeUsuario;
    private int idItemAcervo;

    public LogOperacao() {}

    public LogOperacao(int idLog, String operacao, LocalDateTime dataOperacao, String nomeUsuario, int idItemAcervo) {
        this.idLog = idLog;
        this.operacao = operacao;
        this.dataOperacao = dataOperacao;
        this.nomeUsuario = nomeUsuario;
        this.idItemAcervo = idItemAcervo;
    }

    public int getIdLog() { return idLog; }
    public void setIdLog(int idLog) { this.idLog = idLog; }

    public String getOperacao() { return operacao; }
    public void setOperacao(String operacao) { this.operacao = operacao; }

    public LocalDateTime getDataOperacao() { return dataOperacao; }
    public void setDataOperacao(LocalDateTime dataOperacao) { this.dataOperacao = dataOperacao; }

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public int getIdItemAcervo() { return idItemAcervo; }
    public void setIdItemAcervo(int idItemAcervo) { this.idItemAcervo = idItemAcervo; }

    public String getDataFormatada() {
        if (dataOperacao != null) {
            return dataOperacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        }
        return "";
    }
}