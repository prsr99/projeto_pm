package com.example.projeto_final.entities;

public class Pedido {
    public int id;
    public String assunto;
    public String mensagem;
    public String localizacao;


    public Pedido(int id,String assunto,String mensagem, String localizacao) {
        this.id = id;
        this.assunto=assunto;
        this.mensagem=mensagem;
        this.localizacao=localizacao;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
}
