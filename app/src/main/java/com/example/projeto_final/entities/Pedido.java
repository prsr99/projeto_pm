package com.example.projeto_final.entities;

public class Pedido {
    public int id;
    public String assunto;
    public String mensagem;
    public String localizacao;
    public int if_aceite;
    public int if_terminado;
    public String nome_mecanico;
    public String apelido_mecanico;

    public Pedido(int id,String assunto,String mensagem, String localizacao, int if_aceite, int if_terminado, String nome_mecanico, String apelido_mecanico) {
        this.id = id;
        this.assunto=assunto;
        this.mensagem=mensagem;
        this.localizacao=localizacao;
        this.if_aceite=if_aceite;
        this.if_terminado=if_terminado;
        this.nome_mecanico=nome_mecanico;
        this.apelido_mecanico=apelido_mecanico;

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

    public int getIf_aceite() {
        return if_aceite;
    }

    public void setIf_aceite(int if_aceite) {
        this.if_aceite = if_aceite;
    }

    public int getIf_terminado() {
        return if_terminado;
    }

    public void setIf_terminado(int if_terminado) {
        this.if_terminado = if_terminado;
    }

    public String getNome_mecanico() {
        return nome_mecanico;
    }

    public void setNome_mecanico(String nome_mecanico) {
        this.nome_mecanico = nome_mecanico;
    }

    public String getApelido_mecanico() {
        return apelido_mecanico;
    }

    public void setApelido_mecanico(String apelido_mecanico) {
        this.apelido_mecanico = apelido_mecanico;
    }
}
